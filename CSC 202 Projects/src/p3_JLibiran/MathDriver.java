package p3_JLibiran;

import java.util.Scanner;

public class MathDriver {
	
	public static void main(String[] args) {	
		Scanner sc = new Scanner(System.in);
		String M, N, answer;
		int intM, intN, intAnswer;

		LargeInt product = new LargeInt();
		LargeInt quotient = new LargeInt();
		LargeInt mInput;
		LargeInt nInput;
		
	
		System.out.println("I will perform M * N and M / N. All input and output will be in hex.\r\n");
		System.out.print("Value of M?\r\n> ");
//		M = sc.next();
		
		M = "16"; // DEBUG PURPOSES
		
		System.out.println();
		
		System.out.print("Value of N?\r\n> ");
//		N = sc.next();
		
		N = "D"; // DEBUG PURPOSES
		
		// convert Hex input to Decimal, then convert from Decimal to binary
		
		// convert M
		intM = Integer.parseInt(M, 16);
		M  = Integer.toBinaryString(intM);
		
		// convertN
		intN = Integer.parseInt(N, 16);
		N  = Integer.toBinaryString(intN);
		
		// create M and N as LargeInt Objects
		mInput = new LargeInt(M);
		nInput = new LargeInt(N);
		
		
		
		
		
		// converts product from Binary to Hex and print
		product = LargeInt.subtract(mInput, nInput); // CHANGE THIS TO MUTLIPLY WHEN METHOD IS DONE
		answer = product.toString();
		intAnswer = Integer.parseInt(answer, 2);
		answer = Integer.toHexString(intAnswer);
		
		
		// 22 * 13
		// should print out 11E
		
		System.out.println(); // DEBUG
		System.out.println(answer);
		
		
		
	} //end main
}
