package p3_JLibiran;

import java.util.Scanner;

public class MathDriver {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String M, N;

		LargeInt product = new LargeInt();
		LargeInt quotient = new LargeInt();
		LargeInt modulus = new LargeInt();
		LargeInt mInput;
		LargeInt nInput;

		System.out.println("I will perform M * N and M / N. All input and output will be in hex.\r\n");
		System.out.print("Value of M?\r\n> ");
		M = sc.next();
		M = M.toUpperCase();

		System.out.println();

		System.out.print("Value of N?\r\n> ");
		N = sc.next();
		N = N.toUpperCase();

		// convert Hex input to Decimal, then convert from Decimal to binary

		// convert M
		M = Converter.hexToBinary(M);

		// convertN
		N = Converter.hexToBinary(N);

		// create M and N as LargeInt Objects
		mInput = new LargeInt(M);
		nInput = new LargeInt(N);

		// converts product from Binary to Hex and print
		product = LargeInt.multiply(mInput, nInput);
		quotient = LargeInt.divide(mInput, nInput);
		modulus = LargeInt.modulus(mInput, nInput);

		System.out.println("\r\n");
		System.out.println("M * N = " + Converter.binaryToHex(product.toString()));
		System.out.println("Quotient of M/N = " + Converter.binaryToHex(quotient.toString()));
		System.out.println("Remainder of M/N = " + Converter.binaryToHex(modulus.toString()));

		sc.close();
	} // end main
}
