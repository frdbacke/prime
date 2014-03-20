package com.dbfconsult.unsigned;

import org.junit.Assert;
import org.junit.Test;

import com.dbfconsult.unsigned.UnsignedShort;


public class UnsignedShortTest {

	@Test
	public void testName() throws Exception {
		UnsignedShort unsignedShort = new UnsignedShort(1);
		Assert.assertEquals("0000000000000001",
				unsignedShort.getBinaryRepresentation(true));
		Assert.assertEquals(1, unsignedShort.getValue());

		int biggestUnsignedShort = (int) Math.pow(2, 16) - 1;
		unsignedShort = new UnsignedShort(biggestUnsignedShort);
		Assert.assertEquals("1111111111111111",
				unsignedShort.getBinaryRepresentation(true));
		Assert.assertEquals(65535, unsignedShort.getValue());

	}
}
