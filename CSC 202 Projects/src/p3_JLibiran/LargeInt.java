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
		if (intString.charAt(0) == '1') { // Skip leading 1 sign
			firstDigitPosition = 1;
			sign = MINUS;
		}

		else // Handle leading 0 sign
		{
			firstDigitPosition = 1;
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

		String largeIntString;
		if (sign == PLUS)
			largeIntString = "";
		else
			largeIntString = "-";


		Iterator<Byte> forward = numbers.forward();
		while (forward.hasNext()) {
			element = forward.next();
			largeIntString = largeIntString + element;
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
		first = removeLeadingZeroes(first);
		second = removeLeadingZeroes(second);
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
		byte minuend;
		byte subtrahend;
		boolean borrow = false;

		int minuendLength = larger.size();
		int subtrahendLength = smaller.size();
		int lengthDiff;

		LargeIntList result = new LargeIntList();

		Iterator<Byte> minuendReverse = larger.reverse();
		Iterator<Byte> subtrahendReverse = smaller.reverse();

		// Process both lists while both have digits.
		for (int count = 1; count <= subtrahendLength; count++) {
			minuend = minuendReverse.next();
			if (borrow) {
				if (minuend == 0) {
					minuend = (byte) (2);

				} else {
					minuend = 0;
					borrow = false;
				}
			}

			subtrahend = subtrahendReverse.next();

			if (subtrahend > minuend) {
				result.addFront((byte) (2 - subtrahend));
				borrow = true;
			} else {
				result.addFront((byte) (minuend - subtrahend));
			}

		}

		// Finish processing of leftover digits
		lengthDiff = (minuendLength - subtrahendLength);
		for (int count = 1; count <= lengthDiff; count++) {
			minuend = minuendReverse.next();
			if (borrow) {
				if (minuend == 0) {
					minuend = (byte) (2);

				} else {
					minuend = 0;
					borrow = false;
				}
			}
			result.addFront(minuend);
		}

		return result;
	}

	protected static LargeIntList multiplyLists(LargeIntList first, LargeIntList second)
	// Returns a specialized list that is a byte-by-byte sum of the two
	// argument lists
	{
		byte multiplier, multiplicand;

		int multiplierLength = first.size();
		int multiplicandLength = second.size();

		LargeIntList result = new LargeIntList();
		LargeIntList tempList;

		Iterator<Byte> multiplierReverse;
		Iterator<Byte> multiplicandReverse = second.reverse();

		// Process both lists while both have digits
		for (int i = 1; i <= multiplicandLength; i++) {
			tempList = new LargeIntList();
			multiplicand = multiplicandReverse.next();

			for (int z = 1; z < i; z++) {
				tempList.addEnd((byte) 0);
			}

			multiplierReverse = first.reverse();
			for (int j = 1; j <= multiplierLength; j++) {
				multiplier = multiplierReverse.next();
				tempList.addFront((byte) (multiplier * multiplicand));
			}

			result = addLists(tempList, result);

		}

		return result;
	}

	protected static LargeIntList divideLists(LargeIntList first, LargeIntList second) {
		LargeIntList result = new LargeIntList();
		LargeIntList tempDividend = new LargeIntList();
		LargeIntList remainder = new LargeIntList();
		LargeIntList newFirst = new LargeIntList();
		LargeIntList newSecond = new LargeIntList();
		byte dividend;

		// remove leading zeroes
		newFirst = removeLeadingZeroes(first);
		newSecond = removeLeadingZeroes(second);

		Iterator<Byte> dividendForward = newFirst.forward();
		int dividendLength = newFirst.size();

		// while there are still numbers to be processed
		for (int i = 1; i <= dividendLength; i++) {

			dividend = dividendForward.next();
			tempDividend.addEnd(dividend);

			// compare parts of dividend with the divisor
			if (greaterList(tempDividend, newSecond)) {
				// dividend > divisor
				result.addEnd((byte) 1);
			} else if (greaterList(newSecond, tempDividend)) {
				// divisor > dividend
				result.addEnd((byte) 0);
			} else {
				result.addEnd((byte) 1);
			}

			// find what to subtract by
			if (result.listLast.getInfo() == 1) {
				remainder = newSecond;
			} else {
				remainder = new LargeIntList();
				remainder.addEnd((byte) 0);
			}

			tempDividend = subtractLists(tempDividend, remainder);
		}



		return result;
	}

	protected static LargeIntList modulusLists(LargeIntList first, LargeIntList second) {
		LargeIntList result = new LargeIntList();
		LargeIntList tempDividend = new LargeIntList();
		LargeIntList remainder = new LargeIntList();
		LargeIntList newFirst = new LargeIntList();
		LargeIntList newSecond = new LargeIntList();
		byte dividend;

		// remove leading zeroes
		newFirst = removeLeadingZeroes(first);
		newSecond = removeLeadingZeroes(second);

		Iterator<Byte> dividendForward = newFirst.forward();
		int dividendLength = newFirst.size();

		// while there are still numbers to be processed
		for (int i = 1; i <= dividendLength; i++) {

			dividend = dividendForward.next();
			tempDividend.addEnd(dividend);

			// compare parts of dividend with the divisor
			if (greaterList(tempDividend, newSecond)) {
				// dividend > divisor
				result.addEnd((byte) 1);
			} else if (greaterList(newSecond, tempDividend)) {
				// divisor > dividend
				result.addEnd((byte) 0);
			} else {
				result.addEnd((byte) 1);
			}

			// find what to subtract by
			if (result.listLast.getInfo() == 1) {
				remainder = newSecond;
			} else {
				remainder = new LargeIntList();
				remainder.addEnd((byte) 0);
			}

			tempDividend = subtractLists(tempDividend, remainder);
		}



		return tempDividend;
	}
	
	private static LargeIntList removeLeadingZeroes(LargeIntList list) {
		// removes leading zeroes
		LargeIntList newList = new LargeIntList();
		Iterator<Byte> listIterator = list.forward();


		byte next = listIterator.next();

		// while next isn't a 1
		while (listIterator.hasNext()) {

			if (next == 0) {
				next = listIterator.next();
			} else {
				newList.addEnd(next);
				break;
			}
		}
		
		
		// adds list without leading zeroes to new list
		while (listIterator.hasNext()) {
			newList.addEnd(listIterator.next());
		}


		return newList;
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

		second.sign = !second.sign;
		diff = add(first, second);

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

		quotient.numbers = divideLists(first.numbers, second.numbers);
		
		if (first.sign != second.sign) {
			// order doesn't matter for multiplication, only sign
			quotient.sign = !quotient.sign;
		}

		return quotient;
	}
	
	public static LargeInt modulus(LargeInt first, LargeInt second) {
		LargeInt modulo = new LargeInt();
		
		modulo.numbers = modulusLists(first.numbers, second.numbers);
		
		if (first.sign != second.sign) {
			// order doesn't matter for multiplication, only sign
			modulo.sign = !modulo.sign;
		}
		
		return modulo;

	}

}// end main