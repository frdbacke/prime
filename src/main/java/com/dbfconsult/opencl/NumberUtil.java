package com.dbfconsult.opencl;

import java.math.BigInteger;
import java.util.Random;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import com.dbfconsult.unsigned.UnsignedInteger;


public class NumberUtil {

	public static BigInteger toBigInteger(int[] lo, int[] hi) {
		String loStr = toString(lo, true, false, true);
		String hiStr = toString(hi, true, false, true);
		return new BigInteger(hiStr+ loStr, 2);
		
	}

	public static String toString(int[] input, boolean pad, boolean separator,
			boolean inverse) {
		String s = "";
		int[] toPrint = input;
		if (inverse) {
			ArrayUtils.reverse(toPrint);
		}
		for (int i : toPrint) {
			String binaryString = new UnsignedInteger(i).getBinaryRepresentation();
			if (pad) {
				binaryString = StringUtils.leftPad(binaryString, 16, '0');
			}
			s += binaryString;
			if (separator) {
				s += '/';
			}
		}
		return s;
	}
	
	public static BigInteger generateRandomBigInteger(int minLenght, int maxLength) {
		Random rnd = new Random();
		int count = minLenght + rnd.nextInt(maxLength-minLenght);
		char[] chars = {'0', '1'};
		String string = RandomStringUtils.random(count, chars);
		return new BigInteger(string, 2);
	}

}
