package com.dbfconsult.bigint.montgomery;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

public class MontgomeryTestCase {

	@Test
	public void testMontgomeryMultiplication() {
		int[] m = {7,2,6,3,9};
		int[] y = {0,1,2,2,9};
		int[] x = {0,5,7,9,2};
		int mQ = 1;
		int[] montgomeryMultiplication = Montgomery.montgomeryMultiplication(x, y, m, mQ);
		System.out.println(ArrayUtils.toString(montgomeryMultiplication));
	}

}
