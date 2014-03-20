package com.dbfconsult.bigint.baseshort;

import java.math.BigInteger;

import org.apache.commons.lang.StringUtils;

public class BigintBaseUint {

	private static final long LONG_MASK = 0xFFFFFFFFL;

	public static final int BITS_PER_DIGIT = 32;

	public static final int BASE = (int) Math.pow(2, BITS_PER_DIGIT);

	public static final int LOG2BITS_PER_DIGIT = log(BITS_PER_DIGIT, 2);

	public static final int TOTAL_DIGITS = 16;

	int[] internalRepresentation = new int[TOTAL_DIGITS];

	public int[] getInternalRepresentation() {
		return internalRepresentation;
	}

	public BigintBaseUint(String binaryRepresentation) {
		String input = binaryRepresentation;
		// prepad until length
		while (input.length() != TOTAL_DIGITS * BITS_PER_DIGIT) {
			input = '0' + input;
		}

		for (int i = TOTAL_DIGITS; i > 0; i--) {
			int start = (i - 1) << LOG2BITS_PER_DIGIT;
			int end = i << LOG2BITS_PER_DIGIT;
			internalRepresentation[TOTAL_DIGITS - i - 1] = (int) (Long.valueOf(
					input.substring(start, end), 2) & LONG_MASK);
		}
	}

	public BigintBaseUint(BigInteger bigInteger) {
		this(bigInteger.toString(2));
	}

	@Override
	public String toString() {
		StringBuffer out = new StringBuffer();
		for (int s : internalRepresentation) {
			String binaryString = getBinaryRepresentation(s);
			out.append(binaryString).append("/");
		}
		return out.toString();
	}

	public String getNumberAsBinaryString() {
		StringBuffer out = new StringBuffer();
		for (int i = internalRepresentation.length - 1; i > 0; i--) {
			out.append(getBinaryRepresentation(internalRepresentation[i]));
		}
		return out.toString();

	}

	public BigInteger asBigInteger() {
		return new BigInteger(getNumberAsBinaryString(), 2);
	}

	private String getBinaryRepresentation(int s) {
		String binaryString = Long.toBinaryString(s & LONG_MASK);
		binaryString = StringUtils.leftPad(binaryString, BITS_PER_DIGIT, '0');
		return binaryString;
	}

	private static int log(int x, int base) {
		return (int) (Math.log(x) / Math.log(base));
	}
}