package com.dbfconsult.bigint.base10;

import org.junit.Assert;
import org.junit.Test;

import com.dbfconsult.util.BigintUtil;

public class GtTestCase
{

	@Test
	public void testGt2()
	{
		int[] a = new int[] {9, 2, 3, 9, 0};
		System.out.println("a: " + BigintUtil.print(a));
		boolean gt = BigintBase10.gt(a, a);
		System.out.println(gt);
		Assert.assertFalse(gt);

	}

	@Test
	public void testGt3()
	{
		for (int i = 0; i < 1000; i++)
		{
			int[] a = BigintUtil.randomBigint();
			int[] b = BigintUtil.randomBigint();
//			System.out.println("a: " + BigintUtil.print(a));
//			System.out.println("b: " + BigintUtil.print(b));
			int aAsInteger = BigintUtil.asInteger(a);
			int bAsInteger = BigintUtil.asInteger(b);
//			System.out.println("a: " + aAsInteger);
//			System.out.println("b: " + bAsInteger);
			boolean expected = (aAsInteger > bAsInteger);
			boolean actual = BigintBase10.gt(a, b);
			Assert.assertEquals(expected, actual);

		}

	}
}
