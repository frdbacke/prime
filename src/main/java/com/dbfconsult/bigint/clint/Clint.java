package com.dbfconsult.bigint.clint;

import java.math.BigInteger;

import com.dbfconsult.unsigned.UnsignedShort;

public class Clint {

	public static final int NUMBER_OF_DIGITS = 65;

	UnsignedShort number[] = new UnsignedShort[NUMBER_OF_DIGITS];

	public Clint(String binaryRepresentation) {
		init(binaryRepresentation);
	}

	private void init(String binaryRepresentation) {
		if (binaryRepresentation.length() > 512) {
			throw new IllegalArgumentException(
					"Self constructed Clint object can be max 512 bits long to allow the multiplication of 2 Clint numbers.");
		}
		String input = binaryRepresentation;
		// prepad until length 1024
		while (input.length() != 1024) {
			input = '0' + input;
		}

		double numberOfBits = (((double) input.length() - (double) input
				.indexOf('1')));
		short numberOfDigits = (short) (Math.ceil(numberOfBits / 16));

		for (int i = NUMBER_OF_DIGITS - 1; i >= 1; i--) {
			int start = (i - 1) << 4;
			int end = i << 4;
			number[NUMBER_OF_DIGITS - i] = new UnsignedShort(input.substring(
					start, end));
		}
		number[0] = new UnsignedShort(numberOfDigits);
	}

	public Clint(BigInteger bigInteger) {
		String binaryRepresentation = bigInteger.toString(2);
		init(binaryRepresentation);
	}

	public void setZero() {
		for (int i = 0; i < NUMBER_OF_DIGITS; i++) {
			number[i] = new UnsignedShort();
		}
	}

	public Clint(short[] shorts) {
		for (int i = 0; i < shorts.length; i++) {
			number[i] = new UnsignedShort(shorts[i]);
		}
	}

	public UnsignedShort[] getInternalRepresentation() {
		return number;
	}

	public short[] getShorts() {
		short[] shorts = new short[NUMBER_OF_DIGITS];
		for (int i = 0; i < number.length; i++) {
			shorts[i] = number[i].getInternalRepresentation();
		}
		return shorts;
	}

	@Override
	public String toString() {
		StringBuffer out = new StringBuffer();
		for (UnsignedShort a : number) {
			out.append(a.getBinaryRepresentation()).append("/");
		}
		return out.toString();
	}

	public String getNumberAsBinaryString() {
		StringBuffer out = new StringBuffer();
		for (int i = number.length - 1; i >= 1; i--) {
			out.append(number[i].getBinaryRepresentation());
		}
		return out.toString();

	}

	public BigInteger asBigInteger() {
		return new BigInteger(getNumberAsBinaryString(), 2);
	}
}
