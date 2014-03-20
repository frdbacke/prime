package com.dbfconsult.opencl;
//package opencl;
//
//import static java.lang.Math.*;
//import static org.bridj.Pointer.*;
//
//import java.io.File;
//import java.io.IOException;
//import java.math.BigInteger;
//import java.nio.ByteOrder;
//import java.nio.DoubleBuffer;
//
//import junit.framework.Assert;
//
//import org.bridj.Pointer;
//
//import com.nativelibs4java.opencl.CLBuffer;
//import com.nativelibs4java.opencl.CLContext;
//import com.nativelibs4java.opencl.CLDevice;
//import com.nativelibs4java.opencl.CLEvent;
//import com.nativelibs4java.opencl.CLKernel;
//import com.nativelibs4java.opencl.CLMem.Usage;
//import com.nativelibs4java.opencl.CLProgram;
//import com.nativelibs4java.opencl.CLQueue;
//import com.nativelibs4java.opencl.JavaCL;
//import com.nativelibs4java.util.IOUtils;
//
//public class Mult32bitTest {
//	public static void main(String[] args) throws IOException {
//		JavaCL javaCL = new JavaCL();
//
//		CLContext context = JavaCL.createBestContext();
//
//		CLDevice[] devices = context.getDevices();
//		showInfo(devices);
//
//		CLQueue queue = context.createDefaultQueue();
//		ByteOrder byteOrder = context.getByteOrder();
//
//		int numberoftests = 100;
//		for (int m = 0; m < numberoftests; m++) {
//			
////			BigInteger aBigInt = new BigInteger(
////					"1557777754545545555555555555555555555848484889478948955555888888888888888888888888888888888888888484",
////					10);
////			BigInteger bBigInt = new BigInteger(
////					"1498494848465431321311231222222222222222222222222222222222222222222222222222222",
////					10);
//
//			System.out.println("============================== BEGIN TEST " + m + " ==============================");
//			BigInteger aBigInt = NumberUtil.generateRandomBigInteger();
//			BigInteger bBigInt = NumberUtil.generateRandomBigInteger();
//
//			BigInteger resultBigInt = aBigInt.multiply(bBigInt);
//			System.out.println("a: " + aBigInt);
//			System.out.println("b: " + bBigInt);
//			System.out.println("mult result: " + resultBigInt);
//			System.out.println("mult result binary : "
//					+ resultBigInt.toString(2));
//			System.out.println("mult result binary length: "
//					+ resultBigInt.toString(2).length());
//
//			Uint16 aUInt16 = new Uint16(aBigInt);
//			Uint16 bUInt16 = new Uint16(bBigInt);
//
//			int n = 16;
//			// Pointer<Integer> aPtr = allocateInts(n).order(byteOrder);
//			// Pointer<Integer> bPtr = allocateInts(n).order(byteOrder);
//			Pointer<Integer> hiresPtr = allocateInts(n).order(byteOrder);
//			Pointer<Integer> loresPtr = allocateInts(n).order(byteOrder);
//
//			int[] aUInt16IntRep = aUInt16.getInternalRepresentation();
//			int[] bUInt16IntRep = bUInt16.getInternalRepresentation();
//			for (int i = 0; i < n; i++) {
//				// aPtr.set(i, aUInt16IntRep[i]);
//				// bPtr.set(i, bUInt16IntRep[i]);
//				hiresPtr.set(i, (int) 0);
//				loresPtr.set(i, (int) 0);
//			}
//
//			// Create OpenCL input buffers (using the native memory pointers
//			// aPtr
//			// and bPtr) :
//			// CLBuffer<Integer> a = context.createBuffer(Usage.Input, aPtr);
//			// CLBuffer<Integer> b = context.createBuffer(Usage.Input, bPtr);
//
//			// Create an OpenCL output buffer :
//			CLBuffer<Integer> hires = context.createBuffer(Usage.Output,
//					hiresPtr);
//			CLBuffer<Integer> lores = context.createBuffer(Usage.Output,
//					loresPtr);
//
//			// Read the program sources and compile them :
//			String kernelCode = IOUtils.readText(new File(
//					"src/main/resources/opencl/primecoin.cl"));
//			CLProgram program = context.createProgram(kernelCode);// Get and
//																	// call
//																	// the
//																	// kernel :
//			CLKernel kernel = program.createKernel("Mul_kernel");
//			kernel.setArg(0, aUInt16IntRep);
//			kernel.setArg(1, bUInt16IntRep);
//			kernel.setArg(2, hires);
//			kernel.setArg(3, lores);
//			CLEvent addEvt = kernel.enqueueNDRange(queue, new int[] { 1 });
//
//			hiresPtr = hires.read(queue, addEvt);
//			loresPtr = lores.read(queue, addEvt);
//
//			int[] loInts = loresPtr.getInts();
//			int[] hiInts = hiresPtr.getInts();
//
//			String loIntsAsString = NumberUtil.toString(loInts, true, true,
//					false);
//			System.out.println("lo: " + loIntsAsString);
//
//			String hiIntsAsString = NumberUtil.toString(hiInts, true, true,
//					false);
//			System.out.println("hi: " + hiIntsAsString);
//
//			BigInteger bigIntegerViaOpencl = NumberUtil.toBigInteger(loInts, hiInts);
//			System.out.println(bigIntegerViaOpencl);
//			Assert.assertEquals(resultBigInt, bigIntegerViaOpencl);
//			System.out.println("============================== END TEST " + m + " ==============================");
//		}
//	}
//
//	private static void showInfo(CLDevice[] devices) {
//		for (CLDevice clDevice : devices) {
//			System.out.println("Global mem size: "
//					+ clDevice.getGlobalMemSize() + " bytes..");
//			System.out.println("Address bits: " + clDevice.getAddressBits());
//			System.out.println("Preferred vector width chars: "
//					+ clDevice.getPreferredVectorWidthChar());
//			System.out.println("Max clock frequency: "
//					+ clDevice.getMaxClockFrequency());
//			System.out.println("Name: " + clDevice.getName());
//			String[] extensions = clDevice.getExtensions();
//
//			System.out.print("Extensions: ");
//			for (String extension : extensions) {
//				System.out.print(extension + ", ");
//			}
//			System.out.println();
//		}
//	}
//}
