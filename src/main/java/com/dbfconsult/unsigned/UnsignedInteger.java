package com.dbfconsult.unsigned;

public class UnsignedInteger
{

	private int number;

	public UnsignedInteger(long input)
	{
//		if (input >= Math.pow(2, 32) || input < 0)
//		{
//			throw new IllegalArgumentException("Input not in suitable range for unsigned int");
//		}
//		else
//		{
			number = (int)(input & 0x0FFFFFFFF);
//		}
	}

	public UnsignedInteger(Long input)
	{
		number = (int)(input & 0x0FFFFffff);
	}

	public UnsignedInteger(Integer input)
	{
		number = (int)(input & 0x0FFFFffff);
	}

	public UnsignedInteger(String binaryRepresentation)
	{
		if (binaryRepresentation.length() > 32)
		{
			throw new IllegalArgumentException("Input String should not be > 32 chars long");
		}
		else
		{
			long val = Long.valueOf(binaryRepresentation, 2);
			number = (int)(val & 0x0ffffFFFf);
		}
	}

	public long getValue()
	{
		return Long.valueOf(getBinaryRepresentation(), 2);
	}

	public String getBinaryRepresentation()
	{
		String binaryString = Long.toBinaryString(0x00000000FFFFFFFF & number);
		while (binaryString.length() < 32)
		{
			binaryString = '0' + binaryString;
		}
		if (binaryString.length() > 32) {
			binaryString = binaryString.substring(binaryString.length()-32, binaryString.length());
		}
		return binaryString;
	}

	public int getNumber()
	{
		return number;
	}
}
