package com.dbfconsult.bigint.baseint;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;

public class CompareTestCase {

	@Test
	public void testEq() {
		int[] a = Algebra.asIntArray(new BigInteger("8948146515161648946546"))	;
		int[] b = Algebra.asIntArray(new BigInteger("894814651516164894974567923426546"));
		
		Assert.assertTrue(Algebra.eq(a, a));
		Assert.assertFalse(Algebra.eq(a, b));
	}

	@Test
	public void testGt() {
		int[] a = Algebra.asIntArray(new BigInteger("8948146515161648946546"))	;
		int[] b = Algebra.asIntArray(new BigInteger("894814651516164894974567923426546"));
		
		Assert.assertTrue(Algebra.gt(b, a));
		Assert.assertFalse(Algebra.gt(a, a));
		Assert.assertFalse(Algebra.gt(a, b));
	}

	@Test
	public void testLt() {
		int[] a = Algebra.asIntArray(new BigInteger("8948146515161648946546"))	;
		int[] b = Algebra.asIntArray(new BigInteger("894814651516164894974567923426546"));
		
		Assert.assertFalse(Algebra.lt(b, a));
		Assert.assertFalse(Algebra.lt(a, a));
		Assert.assertTrue(Algebra.lt(a, b));	}

}
