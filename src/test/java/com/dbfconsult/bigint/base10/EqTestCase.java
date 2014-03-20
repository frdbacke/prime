package com.dbfconsult.bigint.base10;

import org.junit.Assert;
import org.junit.Test;

import com.dbfconsult.util.BigintUtil;

public class EqTestCase
{

	@Test
	public void testEqual()
	{
		int[] a = new int[] {1, 2, 3, 9, 0};
		int[] b = new int[] {2, 1, 5, 0, 0};
		boolean result = BigintBase10.eq(a, b);
		// false
		Assert.assertFalse(result);

		result = BigintBase10.eq(a, a);
		// false
		Assert.assertTrue(result);

		result = BigintBase10.eq(b, a);
		// false
		Assert.assertFalse(result);
	}
	
	@Test
	public void testEq2()
	{
		for (int i = 0; i < 1000; i++)
		{
			int[] a = BigintUtil.randomBigint();
			int[] b = BigintUtil.randomBigint();
			int aAsInteger = BigintUtil.asInteger(a);
			int bAsInteger = BigintUtil.asInteger(b);
			boolean expected = (aAsInteger == aAsInteger);
			boolean actual = BigintBase10.eq(a, a);
			Assert.assertEquals(expected, actual);

			expected = (aAsInteger == bAsInteger);
			actual = BigintBase10.eq(a, b);
			Assert.assertEquals(expected, actual);
		}

	}
	
}
