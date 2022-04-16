package p2_JLibiran;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class NodeGrid {

	// Instantiate LinkedGrids and nodeList
	LinkedGridNode<TrainStation> currentNode, linkNode, targetNode, prevNode;
	ArrayList<LinkedGridNode<TrainStation>> nodeList = new ArrayList<LinkedGridNode<TrainStation>>();
	ArrayList<ArrayList<LinkedGridNode<TrainStation>>> listOfPaths = new ArrayList<ArrayList<LinkedGridNode<TrainStation>>>();
	StackInterface<LinkedGridNode<TrainStation>> pathStack = new LinkedStack<LinkedGridNode<TrainStation>>();
	ArrayList<LinkedGridNode<TrainStation>> stackCurrentPath = new ArrayList<LinkedGridNode<TrainStation>>();
	ArrayList<LinkedGridNode<TrainStation>> stackBlacklist = new ArrayList<LinkedGridNode<TrainStation>>();

	public void nodeBuilder() throws Exception {
		File file = new File("p2.txt");
		Scanner sc = new Scanner(file);
		String nodeName, nodeEast, nodeSouth, nodeWest, nodeNorth;

		while (sc.hasNextLine()) { // while there are still inputs to be read

			sc.useDelimiter("[ :\r\n]");
			nodeName = sc.next();
			nodeEast = sc.next();
			nodeSouth = sc.next();
			nodeWest = sc.next();
			nodeNorth = sc.next();

			if (!nodeExists(nodeName)) {
				// if node does not exist, make new node with name sc.next
				currentNode = new LinkedGridNode<TrainStation>(new TrainStation(nodeName));
				nodeList.add(currentNode);
			} else {
				currentNode = nodeList.get(findNodeIndex(nodeName));
			}

			for (int i = 0; i < 5; i++) {

				switch (i) {

				// East Node
				case 1:
					if (!nodeEast.isEmpty()) { // if node is not a blank space
						setLinkNode(nodeEast);
						currentNode.setEast(linkNode);
					}
					break;

				// South Node
				case 2:
					if (!nodeSouth.isEmpty()) { // if node is not a blank space
						setLinkNode(nodeSouth);
						currentNode.setSouth(linkNode);

					}
					break;

				// West Node
				case 3:
					if (!nodeWest.isEmpty()) { // if node is not a blank space
						setLinkNode(nodeWest);
						currentNode.setWest(linkNode);

					}
					break;

				// North Node
				case 4:
					if (!nodeNorth.isEmpty()) { // if node is not a blank space
						setLinkNode(nodeNorth);
						currentNode.setNorth(linkNode);

					}
					break;

				}
			}

			sc.nextLine();
		}
		evaluateNodeLinks();
		// close scanner
		sc.close();
	}

	public void findRecursively(LinkedGridNode<TrainStation> currNode, LinkedGridNode<TrainStation> target,
			ArrayList<LinkedGridNode<TrainStation>> currPath) {
		boolean atDestination = false;
		boolean eastNotAvailable = false, westNotAvailable = false, southNotAvailable = false,
				northNotAvailable = false;

		currPath.add(currNode);

		// Make logic statements easier to read
		if (currNode.getInfo().getName().matches(target.getInfo().getName())) {
			atDestination = true;
		}

		if (currNode.getEast() == null || existsOnPath(currNode.getEast().getInfo().getName(), currPath)) {
			eastNotAvailable = true;
		}

		if (currNode.getSouth() == null || existsOnPath(currNode.getSouth().getInfo().getName(), currPath)) {
			southNotAvailable = true;
		}

		if (currNode.getWest() == null || existsOnPath(currNode.getWest().getInfo().getName(), currPath)) {
			westNotAvailable = true;
		}

		if (currNode.getNorth() == null || existsOnPath(currNode.getNorth().getInfo().getName(), currPath)) {
			northNotAvailable = true;
		}

		// Base case
		// If at the target destination
		if (atDestination) {
			listOfPaths.add(currPath);
			printPath(currPath);
			currPath.remove(currNode);
			return;
		}

		// If at a dead end or remaining paths already exist
		else if (eastNotAvailable && southNotAvailable && westNotAvailable && northNotAvailable) {
			currPath.remove(currNode);
			return;
		}

		// Recursive calls
		else {

			// EAST NODE
			if (currNode.getEast() != null && !existsOnPath(currNode.getEast().getInfo().getName(),
					new ArrayList<LinkedGridNode<TrainStation>>(currPath))) {
				findRecursively(currNode.getEast(), target, currPath);
			}

			// SOUTH NODE
			if (currNode.getSouth() != null && !existsOnPath(currNode.getSouth().getInfo().getName(),
					new ArrayList<LinkedGridNode<TrainStation>>(currPath))) {
				findRecursively(currNode.getSouth(), target, currPath);
			}

			// WEST NODE
			if (currNode.getWest() != null && !existsOnPath(currNode.getWest().getInfo().getName(),
					new ArrayList<LinkedGridNode<TrainStation>>(currPath))) {
				findRecursively(currNode.getWest(), target, currPath);
			}

			// NORTH NODE
			if (currNode.getNorth() != null && !existsOnPath(currNode.getNorth().getInfo().getName(),
					new ArrayList<LinkedGridNode<TrainStation>>(currPath))) {
				findRecursively(currNode.getNorth(), target, currPath);
			}

			// No more possible nodes
			currPath.remove(currNode);

		}
	}

	public void findUsingStacks(LinkedGridNode<TrainStation> currNode, LinkedGridNode<TrainStation> target) {

		boolean atDestination = false;
		boolean eastNotAvailable = false, westNotAvailable = false, southNotAvailable = false,
				northNotAvailable = false;

		pathStack.push(currNode);
		stackCurrentPath.add(currNode);

		while (currNode.stillHasPossiblePaths()) {
			// reset variables
			eastNotAvailable = false;
			southNotAvailable = false;
			westNotAvailable = false;
			northNotAvailable = false;

			// Make logic statements easier to read
			if (pathStack.top().getInfo().getName().matches(target.getInfo().getName())) {
				// if at destination
				atDestination = true;
			}

			// Checks top of stack if adjacent nodes are:
			// null, exist already on path, have already been visited, or is blacklisted
			if (pathStack.top().getEast() == null
					|| existsOnPath(pathStack.top().getEast().getInfo().getName(), stackCurrentPath)
					|| (pathStack.top().getEast() != null && pathStack.top().getEast().beenVisited)
					|| inBlacklist(pathStack.top().getEast())) {
				
				eastNotAvailable = true;
			}

			if (pathStack.top().getSouth() == null
					|| existsOnPath(pathStack.top().getSouth().getInfo().getName(), stackCurrentPath)
					|| (pathStack.top().getEast() != null && pathStack.top().getSouth().beenVisited)
					|| inBlacklist(pathStack.top().getSouth())) {
				
				southNotAvailable = true;
			}

			if (pathStack.top().getWest() == null
					|| existsOnPath(pathStack.top().getWest().getInfo().getName(), stackCurrentPath)
					|| (pathStack.top().getEast() != null && pathStack.top().getWest().beenVisited)
					|| inBlacklist(pathStack.top().getWest())) {
				
				westNotAvailable = true;
			}

			if (pathStack.top().getNorth() == null
					|| existsOnPath(pathStack.top().getNorth().getInfo().getName(), stackCurrentPath)
					|| (pathStack.top().getEast() != null && pathStack.top().getNorth().beenVisited)
					|| inBlacklist(pathStack.top().getNorth())) {
				
				northNotAvailable = true;
			}

			if (atDestination) {
				// if at destination, add current path to total list of paths and print current
				// path
				// remove target node from stack and current path

				listOfPaths.add(stackCurrentPath);
				printPath(stackCurrentPath);
				stackCurrentPath.remove(pathStack.top());
				pathStack.pop();
				atDestination = false;
			}

			else if (eastNotAvailable && southNotAvailable && westNotAvailable && northNotAvailable) {
				// if no other possible move, set current node (top) to visited, and remove last
				// pushed node from list and stack
				stackBlacklist.add(pathStack.top());
				stackCurrentPath.remove(pathStack.top());
				pathStack.pop();

			}

			else {
				// Go to each adjacent node of current node (top)
				// check to see if adjacent node is not null and doesn't already exist on path
				// push current node to stack and add it to the list

				// EAST NODE
				if (pathStack.top().getEast() != null
						&& !existsOnPath(pathStack.top().getEast().getInfo().getName(), stackCurrentPath)
						&& !inBlacklist(pathStack.top().getEast()) && !pathStack.top().goneEast) {

					pathStack.top().setGoneEast();
					stackCurrentPath.add(pathStack.top().getEast());
					pathStack.push(pathStack.top().getEast());
				}

				// SOUTH NODE
				else if (pathStack.top().getSouth() != null
						&& !existsOnPath(pathStack.top().getSouth().getInfo().getName(), stackCurrentPath)
						&& !inBlacklist(pathStack.top().getSouth()) && !pathStack.top().goneSouth) {
					
					pathStack.top().setGoneSouth();
					stackCurrentPath.add(pathStack.top().getSouth());
					pathStack.push(pathStack.top().getSouth());

				}

				// WEST NODE
				else if (pathStack.top().getWest() != null
						&& !existsOnPath(pathStack.top().getWest().getInfo().getName(), stackCurrentPath)
						&& !inBlacklist(pathStack.top().getWest()) && !pathStack.top().goneWest) {
					
					pathStack.top().setGoneWest();
					stackCurrentPath.add(pathStack.top().getWest());
					pathStack.push(pathStack.top().getWest());

				}

				// NORTH NODE
				else if (pathStack.top().getNorth() != null
						&& !existsOnPath(pathStack.top().getNorth().getInfo().getName(), stackCurrentPath)
						&& !inBlacklist(pathStack.top().getNorth()) && !pathStack.top().goneNorth) {
					
					pathStack.top().setGoneNorth();
					stackCurrentPath.add(pathStack.top().getNorth());
					pathStack.push(pathStack.top().getNorth());
				}

				// No more possible nodes
				else {
					stackBlacklist.add(pathStack.top());
					stackCurrentPath.remove(pathStack.top());
					pathStack.pop();
				}

			}
		} // end while loop

		// if only 2 nodes in list
		if (pathStack.top().getInfo().getName().matches(target.getInfo().getName())) {
			printPath(stackCurrentPath);
		}

		// reset all nodes to be unvisited
		evaluateNodeLinks();

	}

	private boolean inBlacklist(LinkedGridNode<TrainStation> nodeToCheck) {
		// iterate through blacklist to see if node to check is blacklisted
		// return true if nodeToCheck is blacklisted
		// return false if not
		for (int index = 0; index < stackBlacklist.size(); index++) {
			if (stackBlacklist.contains(nodeToCheck)) {
				return true;
			}

		}
		return false;
	}

	private void evaluateNodeLinks() {
		// iterate through node repository and set all nodes to have proper link
		// booleans
		for (int index = 0; index < nodeList.size(); index++) {

			if (nodeList.get(index).getEast() == null)
				nodeList.get(index).setGoneEast();

			if (nodeList.get(index).getSouth() == null)
				nodeList.get(index).setGoneSouth();

			if (nodeList.get(index).getWest() == null)
				nodeList.get(index).setGoneWest();

			if (nodeList.get(index).getNorth() == null)
				nodeList.get(index).setGoneNorth();
		}
	}

	private void setLinkNode(String nodeInfo) {
		if (!nodeExists(nodeInfo)) {
			linkNode = new LinkedGridNode<TrainStation>(new TrainStation(nodeInfo));
			nodeList.add(linkNode);
		} else {
			linkNode = nodeList.get(findNodeIndex(nodeInfo));
		}
	}

	private boolean nodeExists(String nodeName) {
		// iterate through nodeList to see if node already exists
		// true if node is found
		// false if no node is found

		for (int index = 0; index < nodeList.size(); index++) {
			if (nodeList.get(index).getInfo().getName().equals(nodeName)) {
				return true;
			}
		}

		return false;
	}

	private static boolean existsOnPath(String nodeName, ArrayList<LinkedGridNode<TrainStation>> nodePath) {

		for (int index = 0; index < nodePath.size(); index++) {
			if (nodePath.get(index).getInfo().getName().matches(nodeName)) {
				return true;
			}
		}
		return false;
	}

	public LinkedGridNode<TrainStation> findNode(String nodeName) {
		for (int index = 0; index < nodeList.size(); index++) {
			if (nodeList.get(index).getInfo().getName().equals(nodeName)) {
				return nodeList.get(index);
			}
		}

		return null;
	}

	private int findNodeIndex(String nodeName) {
		// find index of node with same name as nodeName
		// returns the index of the node you're looking for
		// returns -1 if no node is found

		for (int index = 0; index < nodeList.size(); index++) {
			if (nodeList.get(index).getInfo().getName().equals(nodeName)) {
				return index;
			}
		}

		return -1;
	}

	public void sortAndPrintTrains() {
		// sorts nodeList by total connections from high to low
		// prints name of node, incoming, and outgoing

		Collections.sort(nodeList); // sorts the list
		Collections.reverse(nodeList); // re-orders from highest to lowest

		for (int i = 0; i < nodeList.size(); i++) {
			System.out.println(nodeList.get(i).getInfo().getName() + "(" + nodeList.get(i).getIncoming() + ", "
					+ nodeList.get(i).getOutgoing() + ")");
		}

	}

	private static void printPath(ArrayList<LinkedGridNode<TrainStation>> currPath) {

		for (int i = 0; i < currPath.size(); i++) {
			System.out.print(currPath.get(i).getInfo().getName() + " ");
		}
		System.out.println();

	}

} // end
