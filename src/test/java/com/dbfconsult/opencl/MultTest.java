package com.dbfconsult.opencl;

import java.io.IOException;
import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.dbfconsult.opencl.GpuExecutionService;
import com.dbfconsult.opencl.NumberUtil;

public class MultTest {
	
	private GpuExecutionService gpuExecutionService;
	
	@Before
	public void beforeTest() throws Exception {
		this.gpuExecutionService = new GpuExecutionService();
	}
	
	@Test
	public void testCase() throws IOException {


		int numberoftests = 10000;
		for (int m = 0; m < numberoftests; m++) {

			System.out.println("============================== BEGIN TEST " + m
					+ " ==============================");
			BigInteger aBigInt = NumberUtil.generateRandomBigInteger(256, 512);
			BigInteger bBigInt = NumberUtil.generateRandomBigInteger(256, 512);

			BigInteger resultBigInt = aBigInt.multiply(bBigInt);

			// multiply via biginteger class
			System.out.println("a: " + aBigInt);
			System.out.println("b: " + bBigInt);
			System.out.println("mult result: " + resultBigInt);
			System.out.println("mult result binary: " + resultBigInt.toString(2));

			// multiply via GPU
			BigInteger resultGpu = gpuExecutionService.multiply(aBigInt, bBigInt);
			
			System.out.println("result: " + resultGpu);
			Assert.assertEquals(resultBigInt, resultGpu);
		}
	}
	
}
