package opencl;

import java.math.BigInteger;


public class MExpTestMain {


	public static void main(String[] args) throws Exception {
		GpuExecutionService gpuExecutionService = new GpuExecutionService();
//		BigInteger primeNumber = new BigInteger("25");
		BigInteger primeNumber = NumberUtil.generateRandomBigInteger(256, 300);

		BigInteger expBigInt = primeNumber.subtract(BigInteger.ONE);
		BigInteger mBigInt = primeNumber;

		
		// 2^10 mod 7
		BigInteger resultBigInt = new BigInteger("2").modPow(expBigInt, mBigInt);

		// modpow via biginteger class
		System.out.println("exp: " + expBigInt);
		System.out.println("m: " + mBigInt);
		System.out.println("modpow result: " + resultBigInt);
		System.out.println("modpow result binary: " + resultBigInt.toString(2));

		// modpow via GPU
		BigInteger resultGpu = gpuExecutionService.modExp((short)2, expBigInt, mBigInt);

		System.out.println("result: " + resultGpu);
		
	}


}
