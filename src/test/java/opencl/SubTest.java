package opencl;

import java.io.IOException;
import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class SubTest {
	
	private GpuExecutionService gpuExecutionService;
	
	@Before
	public void beforeTest() throws Exception {
		this.gpuExecutionService = new GpuExecutionService();
	}
	
	@Test
	public void testCaseRandom() throws IOException {


		int numberoftests = 1000;
		for (int m = 0; m < numberoftests; m++) {

			System.out.println("============================== BEGIN TEST " + m
					+ " ==============================");
			BigInteger aBigInt = NumberUtil.generateRandomBigInteger(256, 512);
			BigInteger bBigInt = NumberUtil.generateRandomBigInteger(1, 230);

			BigInteger resultBigInt = aBigInt.subtract(bBigInt);

			// multiply via biginteger class
			System.out.println("a: " + aBigInt);
			System.out.println("b: " + bBigInt);
			System.out.println("substract result: " + resultBigInt);
			System.out.println("substract result binary: " + resultBigInt.toString(2));

			// multiply via GPU
			BigInteger resultGpu = gpuExecutionService.sub(aBigInt, bBigInt);
			
			System.out.println("result: " + resultGpu);
			Assert.assertEquals(resultBigInt, resultGpu);
		}
	}
	
//	@Test
	public void testCase() throws IOException {


		int numberoftests = 1;
		for (int m = 0; m < numberoftests; m++) {

			System.out.println("============================== BEGIN TEST " + m
					+ " ==============================");
			BigInteger aBigInt = new BigInteger("5000");
			BigInteger bBigInt = new BigInteger("400");

			BigInteger resultBigInt = aBigInt.subtract(bBigInt);

			// multiply via biginteger class
			System.out.println("a: " + aBigInt);
			System.out.println("b: " + bBigInt);
			System.out.println("substract result: " + resultBigInt);
			System.out.println("substract result binary: " + resultBigInt.toString(2));

			// multiply via GPU
			BigInteger resultGpu = gpuExecutionService.sub(aBigInt, bBigInt);
			
			System.out.println("result: " + resultGpu);
			Assert.assertEquals(resultBigInt, resultGpu);
		}
	}
	
}
