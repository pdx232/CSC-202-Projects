package p3_JLibiran;

import java.util.HashMap;

public class Converter {
	// Repurposed code from GeeksForGeeks to convert form Hex to binary and binary
	// to hex

	public static String hexToBinary(String hex) {

		// variable to store the converted
		// Binary Sequence
		String binary = "";

		// converting the accepted Hexadecimal
		// string to upper case
		hex = hex.toUpperCase();

		// initializing the HashMap class
		HashMap<Character, String> hashMap = new HashMap<Character, String>();

		// storing the key value pairs
		hashMap.put('0', "0000");
		hashMap.put('1', "0001");
		hashMap.put('2', "0010");
		hashMap.put('3', "0011");
		hashMap.put('4', "0100");
		hashMap.put('5', "0101");
		hashMap.put('6', "0110");
		hashMap.put('7', "0111");
		hashMap.put('8', "1000");
		hashMap.put('9', "1001");
		hashMap.put('A', "1010");
		hashMap.put('B', "1011");
		hashMap.put('C', "1100");
		hashMap.put('D', "1101");
		hashMap.put('E', "1110");
		hashMap.put('F', "1111");

		int i;
		char ch;
		int startingIndex = 0;

		if (hex.charAt(0) == '-') {
			binary = "1" + binary;
			startingIndex = 1;
		} else {
			binary = "0" + binary;
		}

		// loop to iterate through the length
		// of the Hexadecimal String
		for (i = startingIndex; i < hex.length(); i++) {
			// extracting each character
			ch = hex.charAt(i);

			// checking if the character is
			// present in the keys
			if (hashMap.containsKey(ch))

				// adding to the Binary Sequence
				// the corresponding value of
				// the key
				binary += hashMap.get(ch);

			// returning Invalid Hexadecimal
			// String if the character is
			// not present in the keys
			else {
				binary = "Invalid Hexadecimal String";
				return binary;
			}
		}

		// returning the converted Binary
		return binary;
	}

	public static String binaryToHex(String binary) {

		// variable to store the converted
		// Hex Sequence
		String hex = "";

		// initializing the HashMap class
		HashMap<String, String> hashMap = new HashMap<String, String>();

		// storing the key value pairs
		hashMap.put("0000", "0");
		hashMap.put("0001", "1");
		hashMap.put("0010", "2");
		hashMap.put("0011", "3");
		hashMap.put("0100", "4");
		hashMap.put("0101", "5");
		hashMap.put("0110", "6");
		hashMap.put("0111", "7");
		hashMap.put("1000", "8");
		hashMap.put("1001", "9");
		hashMap.put("1010", "A");
		hashMap.put("1011", "B");
		hashMap.put("1100", "C");
		hashMap.put("1101", "D");
		hashMap.put("1110", "E");
		hashMap.put("1111", "F");

		// Handles sign
		if (binary.charAt(0) == '-') {
			hex = "-";
			binary = binary.substring(1);
		} else {
			binary = binary.substring(1);
		}

		// makes string divisible by 4
		while (binary.length() % 4 != 0) {
			binary = "0" + binary;
		}

		String tempString;
		// loop to iterate through the length
		// of the Binary String
		for (int i = 0; i < binary.length();) {
			tempString = binary.substring(i, i + 4);
			i += 4;

			// checking if the substring is
			// present in the keys
			if (hashMap.containsKey(tempString))

				// adding to the Hex Sequence
				// the corresponding value of
				// the key
				hex += hashMap.get(tempString);

			// returning Invalid Binary
			// String if the character is
			// not present in the keys
			else {
				hex = "Invalid Binary String";
				return hex;
			}

		}

		// returning the converted Binary
		return hex;
	}
}
