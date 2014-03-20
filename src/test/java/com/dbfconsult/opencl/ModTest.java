package com.dbfconsult.opencl;

import java.io.IOException;
import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.dbfconsult.opencl.GpuExecutionService;
import com.dbfconsult.opencl.NumberUtil;

public class ModTest {

	private GpuExecutionService gpuExecutionService;

	@Before
	public void beforeTest() throws Exception {
		this.gpuExecutionService = new GpuExecutionService();
	}

	@Test
	public void testCaseRandom() throws IOException {

		int numberoftests = 10000;
		for (int m = 0; m < numberoftests; m++) {

			System.out.println("============================== BEGIN TEST " + m
					+ " ==============================");
			BigInteger aBigInt = NumberUtil.generateRandomBigInteger(256, 512);
			BigInteger bBigInt = NumberUtil.generateRandomBigInteger(50, 256);

			BigInteger resultBigInt = aBigInt.mod(bBigInt);

			// mod via biginteger class
			System.out.println("a: " + aBigInt);
			System.out.println("b: " + bBigInt);
			System.out.println("mod result: " + resultBigInt);
			System.out.println("mod result binary: "
					+ resultBigInt.toString(2));

			// mod via GPU
			BigInteger resultGpu = gpuExecutionService.mod(aBigInt, bBigInt);

			System.out.println("result: " + resultGpu);
			Assert.assertEquals(resultBigInt, resultGpu);
		}
	}

	//@Test
	public void testCase() throws IOException {

		BigInteger d1BigInt = new BigInteger("50");
		BigInteger d2BigInt = new BigInteger("400");

		BigInteger resultBigInt = d1BigInt.mod(d2BigInt);

		// mod via biginteger class
		System.out.println("a: " + d1BigInt);
		System.out.println("b: " + d2BigInt);
		System.out.println("mod result: " + resultBigInt);
		System.out.println("mod result binary: "
				+ resultBigInt.toString(2));

		// mod via GPU
		BigInteger resultGpu = gpuExecutionService.mod(d1BigInt, d2BigInt);

		System.out.println("result: " + resultGpu);
		Assert.assertEquals(resultBigInt, resultGpu);
	}

}
