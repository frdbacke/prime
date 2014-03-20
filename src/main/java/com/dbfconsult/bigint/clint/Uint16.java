package com.dbfconsult.bigint.clint;

import java.math.BigInteger;

import org.apache.commons.lang.StringUtils;

import com.dbfconsult.unsigned.UnsignedInteger;

public class Uint16 {

	// representation of large digits,
	// LSD is first
	private int[] ints;

	public Uint16(String binaryRepresentation) {
		init(binaryRepresentation);

	}

	private void init(String binaryRepresentation) {
		ints = new int[16];
		String input = binaryRepresentation;
		// prepad until length 16*32=512 bits
		while (input.length() != 512) {
			input = '0' + input;
		}

		for (int i = 16; i >= 1; i--) {
			int start = (i - 1) << 5;
			int end = i << 5;
			ints[16 - i] = new UnsignedInteger(input.substring(start, end))
					.getNumber();
		}
	}

	public Uint16(BigInteger bigInteger) {
		String binaryRepresentation = bigInteger.toString(2);
		if (binaryRepresentation.length() > 11 * 32) {
			throw new IllegalArgumentException(
					"too big number for uint16, bin rep is "
							+ binaryRepresentation.length() + " long.");
		}
		init(binaryRepresentation);
	}

	public Uint16(int[] numbers) {
		this.ints = numbers;
	}

	public int[] getInternalRepresentation() {
		return ints;
	}

	public BigInteger getAsBigInteger() {
		return new BigInteger(getBinaryRepresentation(), 2);
	}

	public String getBinaryRepresentation() {
		String s = "";
		for (int i = 15; i >= 0; i--) {
			s += StringUtils.leftPad(Integer.toBinaryString(ints[i]), 16, '0');
		}
		return s;
	}

	// public String toString() {
	// StringBuffer out = new StringBuffer();
	// for (int a : ints) {
	// out.append(new unsi).append("/");
	// }
	// return out.toString();
	// }
}
