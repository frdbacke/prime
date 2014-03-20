package com.dbfconsult.unsigned;

public class UnsignedShort {

	private short number;

	public UnsignedShort(int input) {
		number = (short) (input & 0x0ffff);
	}

	public UnsignedShort(short input) {
		number = input;
	}

	public UnsignedShort(Short input) {
		number = input;
	}

	public UnsignedShort() {
		number = 0;
	}

	public UnsignedShort(String binaryRepresentation) {
		if (binaryRepresentation.length() != 16) {
			throw new IllegalArgumentException(
					"Input String should be 16 chars long");
		} else {
			int val = Integer.valueOf(binaryRepresentation, 2);
			number = (short) (val & 0x0ffff);
		}
	}

	public int getValue() {
		return Integer.valueOf(getBinaryRepresentation(false), 2);
	}

	public String getBinaryRepresentation(boolean pad) {
		String binaryString = Integer.toBinaryString(0xFFFF & number);
		if (pad) {
			while (binaryString.length() < 16) {
				binaryString = '0' + binaryString;
			}
		}
		return binaryString;
	}

	public String getBinaryRepresentation() {
		return getBinaryRepresentation(true);
	}

	public short getInternalRepresentation() {
		return number;
	}
}
