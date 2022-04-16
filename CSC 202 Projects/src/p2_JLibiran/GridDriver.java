package p2_JLibiran;

import java.util.ArrayList;
import java.util.Scanner;

public class GridDriver {

	public static void main(String[] args) throws Exception {	
		Scanner sc = new Scanner(System.in);
		String choice;
		String originInput, destinationInput;
		LinkedGridNode<TrainStation> origin, destination;
		NodeGrid train = new NodeGrid();
		ArrayList<LinkedGridNode<TrainStation>> nodePath = new ArrayList<LinkedGridNode<TrainStation>>();
			
		
		train.nodeBuilder();

		
			System.out.print("Which method, (s)tack, (r)cursion, or (q)uit?\r\n> ");
			choice = sc.next().toLowerCase();
			switch(choice) {
			case "r":
				System.out.print("Origin?\r\n> ");
				originInput = sc.next();
				origin = train.findNode(originInput);
				
				System.out.print("Destination?\r\n> ");
				destinationInput = sc.next();
				destination = train.findNode(destinationInput);
				
				train.findRecursively(origin, destination, nodePath);
				break;
				
			case "s":
				System.out.print("Origin?\r\n> ");
				originInput = sc.next();
				origin = train.findNode(originInput);
				
				System.out.print("Destination?\r\n> ");
				destinationInput = sc.next();
				destination = train.findNode(destinationInput);
				
				train.findUsingStacks(origin, destination);
				break;
				
			default:
				System.out.println("Goodbye.");
				break;
			}

		sc.close();


	}

}
