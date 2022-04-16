//---------------------------------------------------------------------------
// LargeInt.java            by Dale/Joyce/Weems                     Chapter 6
// 
// Provides a Large Integer ADT. Large integers can consist of any number
// of digits, plus a sign. Supports an add and a subtract operation.
//---------------------------------------------------------------------------
package p3_JLibiran;

import java.util.Iterator;

public class LargeInt {
	protected LargeIntList numbers; // Holds digits

	// Constants for sign variable
	protected static final boolean PLUS = true;
	protected static final boolean MINUS = false;

	protected boolean sign;

	public LargeInt()
	// Instantiates an "empty" large integer.
	{
		numbers = new LargeIntList();
		sign = PLUS;
	}

	public LargeInt(String intString)
	// Precondition: intString contains a well-formatted integer
	//
	// Instantiates a large integer as indicated by intString
	{
		numbers = new LargeIntList();
		sign = PLUS;

		int firstDigitPosition; // Position of first digit in intString
		int lastDigitPosition; // Position of last digit in intString

		// Used to translate character to byte
		char digitChar;
		int digitInt;
		byte digitByte;

		firstDigitPosition = 0;
		if (intString.charAt(0) == '+') // Skip leading plus sign
			firstDigitPosition = 1;
		else if (intString.charAt(0) == '-') // Handle leading minus sign
		{
			firstDigitPosition = 1;
			sign = MINUS;
		}

		lastDigitPosition = intString.length() - 1;

		for (int count = firstDigitPosition; count <= lastDigitPosition; count++) {
			digitChar = intString.charAt(count);
			digitInt = Character.digit(digitChar, 2);
			digitByte = (byte) digitInt;
			numbers.addEnd(digitByte);
		}
	}

	public void setNegative() {
		sign = MINUS;
	}

	public String toString() {
		Byte element;

		String largeIntString = "0";
//		if (sign == PLUS)
//			largeIntString = "0";
//		else
//			largeIntString = "1";

		int count = numbers.size();
		Iterator<Byte> forward = numbers.forward();
		while (forward.hasNext()) {
			element = forward.next();
			largeIntString = largeIntString + element;
//			if ((((count - 1) % 3) == 0) && (count != 1))
//				largeIntString = largeIntString + ",";
			count--;
		}
		return (largeIntString);
	}

	protected static boolean greaterList(LargeIntList first, LargeIntList second)
	// Precondition: first and second have no leading zeros
	//
	// Returns true if first represents a larger number than second;
	// otherwise, returns false

	{
		boolean greater = false;
		if (first.size() > second.size())
			greater = true;
		else if (first.size() < second.size())
			greater = false;
		else {
			byte digitFirst;
			byte digitSecond;
			Iterator<Byte> firstForward = first.forward();
			Iterator<Byte> secondForward = second.forward();

			// Set up loop
			int length = first.size();
			boolean keepChecking = true;
			int count = 1;

			while ((count <= length) && (keepChecking)) {
				digitFirst = firstForward.next();
				digitSecond = secondForward.next();
				if (digitFirst > digitSecond) {
					greater = true;
					keepChecking = false;
				} else if (digitFirst < digitSecond) {
					greater = false;
					keepChecking = false;
				}
				count++;
			}
		}
		return greater;
	}

	protected static LargeIntList addLists(LargeIntList larger, LargeIntList smaller)
	// Precondition: larger > smaller
	//
	// Returns a specialized list that is a byte-by-byte sum of the two
	// argument lists
	{
		byte digit1;
		byte digit2;
		byte temp;
		byte carry = 0;

		int largerLength = larger.size();
		int smallerLength = smaller.size();
		int lengthDiff;

		LargeIntList result = new LargeIntList();

		Iterator<Byte> largerReverse = larger.reverse();
		Iterator<Byte> smallerReverse = smaller.reverse();
		// Process both lists while both have digits
		for (int count = 1; count <= smallerLength; count++) {
			digit1 = largerReverse.next();
			digit2 = smallerReverse.next();
			temp = (byte) (digit1 + digit2 + carry);
			carry = (byte) (temp / 2);
			result.addFront((byte) (temp % 2));
		}

		// Finish processing of leftover digits
		lengthDiff = (largerLength - smallerLength);
		for (int count = 1; count <= lengthDiff; count++) {
			digit1 = largerReverse.next();
			temp = (byte) (digit1 + carry);
			carry = (byte) (temp / 2);
			result.addFront((byte) (temp % 2));
		}
		if (carry != 0)
			result.addFront((byte) carry);

		return result;
	}

	protected static LargeIntList subtractLists(LargeIntList larger, LargeIntList smaller)
	// Precondition: larger >= smaller
	//
	// Returns a specialized list that is the difference of the two argument lists
	{
		byte digit1;
		byte digit2;
		byte temp;
		boolean borrow = false;

		int largerLength = larger.size();
		int smallerLength = smaller.size();
		int lengthDiff;

		LargeIntList result = new LargeIntList();

		Iterator<Byte> largerReverse = larger.reverse();
		Iterator<Byte> smallerReverse = smaller.reverse();

		// Process both lists while both have digits.
		for (int count = 1; count <= smallerLength; count++) {
			digit1 = largerReverse.next();
			if (borrow) {
				if (digit1 != 0) {
					digit1 = (byte) (digit1 - 1);
					borrow = false;
				} else {
					digit1 = 0;
					borrow = true;
				}
			}

			digit2 = smallerReverse.next();

			if (digit2 <= digit1)
				result.addFront((byte) (digit1 - digit2));
			else {
				borrow = true;
				result.addFront((byte) (digit1 + 1 - digit2));
			}
		}

		// Finish processing of leftover digits
		lengthDiff = (largerLength - smallerLength);
		for (int count = 1; count <= lengthDiff; count++) {
			digit1 = largerReverse.next();
			if (borrow) {
				if (digit1 != 0) {
					digit1 = (byte) (digit1 - 1);
					borrow = false;
				} else {
					digit1 = 0;
					borrow = true;
				}
			}
			result.addFront(digit1);
		}

		return result;
	}

	protected static LargeIntList multiplyLists(LargeIntList first, LargeIntList second)
	// Returns a specialized list that is a byte-by-byte sum of the two
	// argument lists
	{
		byte multiplier;
		byte multiplicand;
		byte temp;
		byte tempDigit;
		byte carry = 0;

		int multiplierLength = first.size();
		int multiplicandLength = second.size();
		int lengthDiff;

		LargeIntList result = new LargeIntList();
		LargeIntList tempList;


		Iterator<Byte> multiplierReverse;
		Iterator<Byte> multiplicandReverse = second.reverse();
		Iterator<Byte> tempListReverse;
		// Process both lists while both have digits
		for (int i = 1; i <= multiplicandLength; i++) {
			tempList = new LargeIntList();
			multiplicand = multiplicandReverse.next();
			
			for(int z = 1; z < i; z++) {
				tempList.addEnd((byte) 0);
			}
			
			multiplierReverse  = first.reverse();
			for(int j = 1; j <= multiplierLength; j++) {
				multiplier = multiplierReverse.next();
				tempList.addFront((byte) (multiplier * multiplicand));
			}
			
			tempListReverse = tempList.reverse();
			tempDigit = tempListReverse.next();
			
			result = addLists(tempList, result);
			
		}

//		// Finish processing of leftover digits
//		lengthDiff = (largerLength - smallerLength);
//		for (int count = 1; count <= lengthDiff; count++) {
//			digit1 = largerReverse.next();
//			temp = (byte) (digit1 + carry);
//			carry = (byte) (temp / 2);
//			result.addFront((byte) (temp % 2));
//		}
//		
//		if (carry != 0)
//			result.addFront((byte) carry);

		return result;
	}

	public static LargeInt add(LargeInt first, LargeInt second)
	// Returns a LargeInt that is the sum of the two argument LargeInts
	{
		LargeInt sum = new LargeInt();

		if (first.sign == second.sign) {
			if (greaterList(first.numbers, second.numbers))
				sum.numbers = addLists(first.numbers, second.numbers);
			else
				sum.numbers = addLists(second.numbers, first.numbers);
			sum.sign = first.sign;
		} else // Signs are different
		{
			if (greaterList(first.numbers, second.numbers)) {
				sum.numbers = subtractLists(first.numbers, second.numbers);
				sum.sign = first.sign;
			} else {
				sum.numbers = subtractLists(second.numbers, first.numbers);
				sum.sign = second.sign;
			}
		}

		return sum;
	}

	public static LargeInt subtract(LargeInt first, LargeInt second)
	// Returns a LargeInt that is the difference of the two argument LargeInts
	{
		LargeInt diff = new LargeInt();
		StringBuffer str = new StringBuffer();

		// Create an inverse of second
//		LargeInt negSecond = new LargeInt();
		LargeInt negSecond = new LargeInt(twosComplement(str.append(second.toString())));
		
		
		negSecond.sign = !second.sign;
		Iterator<Byte> secondForward = second.numbers.forward();
		
		int length = second.numbers.size();
		for (int count = 1; count <= length; count++)
			negSecond.numbers.addEnd(secondForward.next());

		// Add first to inverse of second
		diff = add(first, negSecond);

		return diff;
	}

	public static LargeInt multiply(LargeInt first, LargeInt second) {
		LargeInt product = new LargeInt();

		product.numbers = multiplyLists(first.numbers, second.numbers);
		
		if (first.sign != second.sign) {
			// order doesn't matter for multiplication, only sign
			product.sign = !product.sign;
		} 

		return product;

	}


	public static LargeInt divide(LargeInt first, LargeInt second) {
		LargeInt quotient = new LargeInt();
		
		
		
		return quotient;
	}
	
	protected static String twosComplement(StringBuffer input) {
		// Outputs the twos complement of Input
		
	    int n = input.length();
	      
        // Traverse the string to get first '1' from
        // the last of string
        int i;
        for (i = n-1 ; i >= 0 ; i--)
            if (input.charAt(i) == '1')
                break;
      
        // If there exists no '1' concat 1 at the
        // starting of string
        if (i == -1)
            return "1" + input;
      
        // Continue traversal after the position of
        // first '1'
        for (int k = i-1 ; k >= 0; k--)
        {
            //Just flip the values
            if (input.charAt(k) == '1')
                input.replace(k, k+1, "0");
            else
                input.replace(k, k+1, "1");
        }
      
        // return the modified string
        return input.toString();
    
	}
	
}// end main