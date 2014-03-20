package com.dbfconsult.opencl;

import java.io.IOException;
import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.dbfconsult.opencl.GpuExecutionService;
import com.dbfconsult.opencl.NumberUtil;

public class MExpTest {

	private GpuExecutionService gpuExecutionService;

	@Before
	public void beforeTest() throws Exception {
		this.gpuExecutionService = new GpuExecutionService();
	}

	@Test
	public void testCase_Random() throws IOException {

		int numberoftests = 1000;
		for (int m = 0; m < numberoftests; m++) {

			System.out.println("============================== BEGIN TEST " + m
					+ " ==============================");
			BigInteger primeNumber = NumberUtil.generateRandomBigInteger(256, 512);
			BigInteger expBigInt = primeNumber.subtract(BigInteger.ONE);
			BigInteger mBigInt = primeNumber;


			BigInteger resultBigInt = new BigInteger("2").modPow(expBigInt, mBigInt);

			// modpow via biginteger class
			System.out.println("exp: " + expBigInt);
			System.out.println("m: " + mBigInt);
			System.out.println("modpow result: " + resultBigInt);
			System.out.println("modpow result binary: " + resultBigInt.toString(2));

			// modpow via GPU
			BigInteger resultGpu = gpuExecutionService.modExp((short)2, expBigInt, mBigInt);

			System.out.println("result: " + resultGpu);
			Assert.assertEquals(resultBigInt, resultGpu);
		}
	}

//	@Test
	public void testCase() throws IOException {

		BigInteger primeNumber = new BigInteger("906881370602081575899685051777545274174230614396911166247573690849237121633126610432065");

		BigInteger expBigInt = primeNumber.subtract(BigInteger.ONE);
		BigInteger mBigInt = primeNumber;


		BigInteger resultBigInt = new BigInteger("2").modPow(expBigInt, mBigInt);

		// modpow via biginteger class
		System.out.println("exp: " + expBigInt);
		System.out.println("m: " + mBigInt);
		System.out.println("modpow result: " + resultBigInt);
		System.out.println("modpow result binary: " + resultBigInt.toString(2));

		// modpow via GPU
		BigInteger resultGpu = gpuExecutionService.modExp((short)2, expBigInt, mBigInt);

		System.out.println("result: " + resultGpu);
		Assert.assertEquals(resultBigInt, resultGpu);
	}

}
