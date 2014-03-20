package com.dbfconsult.bigint.base10;

import junit.framework.Assert;

import org.junit.Test;

import com.dbfconsult.util.BigintUtil;

public class MulTestCase
{

//	@Test
	public void testMul1()
	{
		int[] a = new int[] {1, 9, 9};
		int[] b = new int[] {9, 4, 2};
		System.out.println("mul expected:  " + BigintUtil.asInteger(a) * BigintUtil.asInteger(b));
		int[] result = BigintBase10.mul(a, b);
		System.out.println(BigintUtil.print(result));
	}

	// @Test
	// public void testMul2()
	// {
	// int[] a = new int[] {8, 9, 7, 4, 0};
	// int b = 3;
	// int[] result = Bigint.mulwithsingledigit(a, b);
	// int resultAsInt = BigintUtil.asInteger(result);
	// int expected = BigintUtil.asInteger(a) * b;
	// System.out.println("expected: " + expected);
	//
	// System.out.println("result: " + resultAsInt);
	// Assert.assertEquals(expected, resultAsInt);
	// }
	//
	@Test
	public void testMul3()
	{
		for (int i = 0; i < 10000; i++)
		{
			System.out.println("================================================");
			int[] a = BigintUtil.randomBigint();
			int[] b = BigintUtil.randomBigint();
			System.out.println("a: " + BigintUtil.print(a));
			System.out.println("b: " + BigintUtil.print(b));

			int expected = (BigintUtil.asInteger(b) * BigintUtil.asInteger(a));
			System.out.println("mul expected:  " + expected);

			int[] actual = BigintBase10.mul_2x2(a, b);
			System.out.println("result: " + BigintUtil.print(actual));
			int actualAsInt = BigintUtil.asInteger(actual);
			Assert.assertEquals(expected, actualAsInt);

		}

	}

}
