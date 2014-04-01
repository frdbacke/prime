package com.dbfconsult.unsigned;

import java.math.BigInteger;



public class Main {

	private static final long LONG_MASK = 0x00000000FFFFFFFF;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		BigInteger maxInt = new BigInteger(StringUtils.leftPad("", 32, "1"), 2);
//		System.out.println(maxInt.toString(10));
		int maxInt = (int) (Long.valueOf("4294967295", 10) & LONG_MASK);
		String maxIntBinaryString = Integer.toBinaryString(maxInt);
		System.out.println(maxInt + ", binary string: " + maxIntBinaryString);
		System.out.println((maxInt & LONG_MASK) + ", binary string: " + Long.toBinaryString((maxInt & LONG_MASK)));
		
//		long result = new BigInteger(maxIntBinaryString, 2).multiply(new BigInteger(maxIntBinaryString, 2)).toString(10) ;
		System.out.println(new BigInteger(maxIntBinaryString, 2).multiply(new BigInteger(maxIntBinaryString, 2)).toString(2) );
		System.out.println(new BigInteger("4294967295", 10).multiply(new BigInteger("4294967295", 10)));
		
//		String resultBinaryString = Long.toBinaryString(result);
//		System.out.println("resultBinaryString: " + resultBinaryString);
//		System.out.println(new BigInteger(resultBinaryString, 2));
//		
//		System.out.println(new BigInteger("18446744065119617025", 10).toString(2));
		
	}

}
