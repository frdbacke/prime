package com.dbfconsult.bigint.baseint;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;

public class InitialisationTestCase {

	@Test
	public void testConversion() {
		BigInteger bigInteger = new BigInteger("1895646347357418");
		int[] intArray = Algebra.asIntArray(bigInteger);
		System.out.println(Algebra.toString(intArray));
		Assert.assertEquals(bigInteger, Algebra.asBigInteger(intArray));
	}

	@Test
	public void testConversion2() {
		BigInteger bigInteger = new BigInteger("1895671895671895671895671895671895671895671895671895671895671895671895671895671895671895671895671895679567189567189567189567189567189567189567189567189567");
		int[] intArray = Algebra.asIntArray(bigInteger);
		System.out.println(Algebra.toString(intArray));
		Assert.assertEquals(bigInteger, Algebra.asBigInteger(intArray));
	}
}
