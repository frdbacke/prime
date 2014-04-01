package com.dbfconsult.bigint.baseint;

import java.math.BigInteger;

import org.apache.commons.lang.StringUtils;

public class Algebra {

	public static final long LONG_MASK = 0xFFFFFFFFL;

	public static final int BITS_PER_DIGIT = 32;

	public static final int BASE = (int) Math.pow(2, BITS_PER_DIGIT);

	public static final int LOG2BITS_PER_DIGIT = log(BITS_PER_DIGIT, 2);

	public static final int TOTAL_DIGITS = 16;

	private static final int MAX_BITS = TOTAL_DIGITS * BITS_PER_DIGIT;

	// representation of large digits,
	// LSD is first

	public static int[] asIntArray(BigInteger bigInteger) {
		String binaryRepresentation = bigInteger.toString(2);
		if (binaryRepresentation.length() > MAX_BITS) {
			throw new IllegalArgumentException("Number too big, max "
					+ MAX_BITS + " allowed.");
		}
		int[] internalRepresentation = new int[TOTAL_DIGITS];
		String input = binaryRepresentation;
		// prepad until length 16*32=512 bits
		while (input.length() != MAX_BITS) {
			input = '0' + input;
		}

		for (int i = TOTAL_DIGITS; i >= 1; i--) {
			int start = (i - 1) << LOG2BITS_PER_DIGIT;
			int end = i << LOG2BITS_PER_DIGIT;
			internalRepresentation[TOTAL_DIGITS - i] = (int) (Long.valueOf(
					input.substring(start, end), 2) & 0x0ffffFFFf);
		}
		return internalRepresentation;
	}

	public static BigInteger asBigInteger(int[] bigintRepresentation) {
		return new BigInteger(getBinaryRepresentation(bigintRepresentation), 2);
	}

	public static String getBinaryRepresentation(int[] bigintRepresentation) {
		String s = "";
		for (int i = TOTAL_DIGITS - 1; i >= 0; i--) {
			s += StringUtils.leftPad(
					Integer.toBinaryString(bigintRepresentation[i]),
					BITS_PER_DIGIT, '0');
		}
		return s;
	}
	
	public static boolean eq(int[] a, int[] b) {
		int diffIndex = -1;
		for (int i = 0; i < TOTAL_DIGITS; i++) {
			diffIndex = ((a[i] & LONG_MASK) != (b[i] & LONG_MASK)) ? i
					: diffIndex;
		}

		return diffIndex == -1;
	}

	public static boolean lt(int[] a, int[] b) {
		int diffIndex = -1;
		for (int i = 0; i < TOTAL_DIGITS; i++) {
			diffIndex = ((a[i] & LONG_MASK) != (b[i] & LONG_MASK)) ? i
					: diffIndex;
		}

		return (diffIndex >= 0 && a[diffIndex] < b[diffIndex]) ? true : false;
	}

	public static boolean gt(int[] a, int[] b) {
		int diffIndex = -1;
		for (int i = 0; i < TOTAL_DIGITS; i++) {
			diffIndex = ((a[i] & LONG_MASK) != (b[i] & LONG_MASK)) ? i
					: diffIndex;
		}

		return (diffIndex >= 0 && a[diffIndex] > b[diffIndex]) ? true : false;
	}	

	public static String toString(int[] bigintRepresentation) {
		StringBuffer out = new StringBuffer();
		for (int a : bigintRepresentation) {
			out.append(
					StringUtils.leftPad(Integer.toBinaryString(a),
							BITS_PER_DIGIT, '0')).append("/");
		}
		return out.toString();
	}

	public static int log(int x, int base) {
		return (int) (Math.log(x) / Math.log(base));
	}
}
