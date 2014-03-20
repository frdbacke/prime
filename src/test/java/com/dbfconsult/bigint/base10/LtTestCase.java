package com.dbfconsult.bigint.base10;

import org.junit.Assert;
import org.junit.Test;

import com.dbfconsult.util.BigintUtil;

public class LtTestCase
{

	@Test
	public void testLt1()
	{
		int[] a = new int[] {1, 2, 3, 9, 0};
		// false
		System.out.println("a: " + BigintUtil.print(a));
		boolean lt = BigintBase10.lt(a, a);
		System.out.println(lt);
		Assert.assertFalse(lt);

	}

	@Test
	public void testLt3()
	{
		long total = 0;
		for (int i = 0; i < 1000; i++)
		{
			int[] a = BigintUtil.randomBigint();
			int[] b = BigintUtil.randomBigint();
			int aAsInteger = BigintUtil.asInteger(a);
			int bAsInteger = BigintUtil.asInteger(b);
			boolean expected = (aAsInteger < bAsInteger);
			long begin = System.currentTimeMillis();
			boolean actual = BigintBase10.lt(a, b);
			total += (System.currentTimeMillis() - begin);
			Assert.assertEquals(expected, actual);
		}
		System.out.println("Time: " + total);
	}
}
