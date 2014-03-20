package com.dbfconsult.bigint.base10;

import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

import com.dbfconsult.util.BigintUtil;

public class MulSingleDigitTestCase
{

	@Test
	public void testMul1()
	{
		int[] a = new int[] {1, 2, 3, 4, 0};
		int b = 2;
		int[] result = BigintBase10.mulwithsingledigit(a, b);
		System.out.println(BigintUtil.print(result));
	}
	
	@Test
	public void testMul2()
	{
		int[] a = new int[] {8, 9, 7, 4, 0};
		int b = 3;
		int[] result = BigintBase10.mulwithsingledigit(a, b);
		int resultAsInt = BigintUtil.asInteger(result);
		int expected = BigintUtil.asInteger(a) * b;
		System.out.println("expected: " + expected);
		
		System.out.println("result: " + resultAsInt);
		Assert.assertEquals(expected, resultAsInt);
	}
	
	@Test
	public void testMul3()
	{
		for (int i = 0; i < 1000; i++)
		{
			System.out.println("================================================");
			int[] a = BigintUtil.randomBigint();
			int b = new Random().nextInt(10);
			System.out.println("a: " + BigintUtil.print(a));
			System.out.println("b: " + b);
			
			int expected = (b * BigintUtil.asInteger(a));
			int[] actual = BigintBase10.mulwithsingledigit(a, b);
			System.out.println("result: " + BigintUtil.print(actual));
			int actualAsInt = BigintUtil.asInteger(actual);
			Assert.assertEquals(expected, actualAsInt);

		}

	}
	
}
