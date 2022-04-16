package p2_JLibiran;

public class LinkedGridNode<T> implements Comparable<LinkedGridNode<T>> {

	// Instantiate links
	private LinkedGridNode<T> east;
	private LinkedGridNode<T> south;
	private LinkedGridNode<T> west;
	private LinkedGridNode<T> north;
	protected int incoming, outgoing, total;
	protected boolean beenVisited = false;
	protected boolean goneEast = false, goneSouth = false, goneWest = false, goneNorth = false;

	T info;

	public LinkedGridNode(T info) { // constructor

		// counts number of incoming and outgoing nodes
		incoming = 0;
		outgoing = 0;

		this.info = info;

		east = null;
		south = null;
		west = null;
		north = null;
	}

	public T getInfo() {
		return this.info;
	}

	public boolean equals(LinkedGridNode<T> otherNode) {
		if (this.getInfo() == otherNode.getInfo())
			return true;
		return false;
	}
	
	public int compareTo(LinkedGridNode<T> otherNode) {
		// if THIS > otherNode, return positive integer
		// if THIS < otherNode, return negative integer
		// if THIS = otherNode, return 0

		return (int) (this.getTotal() - otherNode.getTotal());

	}

	// getters for Incoming , Outgoing, and Total
	public int getIncoming() {
		return incoming;
	}

	public int getOutgoing() {
		return outgoing;
	}

	public int getTotal() {
		total = incoming + outgoing;
		return total;
	}

	public boolean stillHasPossiblePaths() {
		// still has possible paths if any this.adjacentdirection is false
		if(this.goneEast == false || this.goneSouth == false || this.goneWest == false || this.goneNorth == false) {
			return true;
		}
		
		return false;
			
	}
	
	public void unvisit() {
		// "unvisits" all node links
		this.goneEast = false;
		this.goneSouth = false;
		this.goneWest = false;
		this.goneNorth = false;
	}

	// set paths if they've been visited
	public void setGoneEast() {
		this.goneEast = true;
	}
	
	public void setGoneSouth() {
		this.goneSouth = true;
	}
	
	public void setGoneWest() {
		this.goneWest = true;
	}
	
	public void setGoneNorth() {
		this.goneNorth = true;
	}



	
	// East Link Getters and Setters
	public LinkedGridNode<T> getEast() {
		return east;
	}

	public void setEast(LinkedGridNode<T> east) {
		this.east = east;
		outgoing += 1;
		east.incoming += 1;
	}

	// South Link Getters and Setters
	public LinkedGridNode<T> getSouth() {
		return south;
	}

	public void setSouth(LinkedGridNode<T> south) {
		this.south = south;
		outgoing += 1;
		south.incoming += 1;
	}

	// West Link Getters and Setters
	public LinkedGridNode<T> getWest() {
		return west;

	}

	public void setWest(LinkedGridNode<T> west) {
		this.west = west;
		outgoing += 1;
		west.incoming += 1;
	}

	// North Link Getters and Setters
	public LinkedGridNode<T> getNorth() {
		return north;

	}

	public void setNorth(LinkedGridNode<T> north) {
		this.north = north;
		outgoing += 1;
		north.incoming += 1;
	}

}