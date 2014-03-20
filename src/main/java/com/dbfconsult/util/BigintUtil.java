package com.dbfconsult.util;

import java.util.Random;

import com.dbfconsult.bigint.base10.BigintBase10;

public class BigintUtil
{

	public static String print(int[] a)
	{
		StringBuffer sb = new StringBuffer();
		for (int i : a)
		{
			sb.append(i + ",");
		}
		return sb.toString();
	}

	public static int[] randomBigint()
	{
		Random rnd = new Random();
		int[] number = new int[BigintBase10.MAX_DIGITS];
		for (int i = 0; i < BigintBase10.MAX_DIGITS; i++)
		{
			number[i] = rnd.nextInt(BigintBase10.BASE);
		}
		return number;
	}
	
	public static int asInteger(int[] number) {
		StringBuffer sb = new StringBuffer();
		
		for (int i = number.length-1; i >= 0 ; i--)
		{
			sb.append(number[i]);
		}
		return Integer.parseInt(sb.toString());
	}

}
