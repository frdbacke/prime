package com.dbfconsult.opencl;

import static java.lang.Math.*;
import static org.bridj.Pointer.*;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;

import org.bridj.Pointer;


import com.dbfconsult.bigint.clint.Uint16;
import com.dbfconsult.unsigned.UnsignedInteger;
import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLDevice;
import com.nativelibs4java.opencl.CLEvent;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLMem.Usage;
import com.nativelibs4java.opencl.CLProgram;
import com.nativelibs4java.opencl.CLQueue;
import com.nativelibs4java.opencl.JavaCL;
import com.nativelibs4java.util.IOUtils;

public class FermatTest {
	public static void main(String[] args) throws IOException {
		JavaCL javaCL = new JavaCL();

		CLContext context = JavaCL.createBestContext();

		CLDevice[] devices = context.getDevices();
		for (CLDevice clDevice : devices) {
			System.out.println("Global mem size: "
					+ clDevice.getGlobalMemSize() + " bytes..");
			System.out.println("Address bits: " + clDevice.getAddressBits());
			System.out.println("Preferred vector width chars: "
					+ clDevice.getPreferredVectorWidthChar());
			System.out.println("Max clock frequency: "
					+ clDevice.getMaxClockFrequency());
			System.out.println("Name: " + clDevice.getName());
			String[] extensions = clDevice.getExtensions();

			System.out.print("Extensions: ");
			for (String extension : extensions) {
				System.out.print(extension + ", ");
			}
			System.out.println();
		}

		CLQueue queue = context.createDefaultQueue();
		ByteOrder byteOrder = context.getByteOrder();

		String aBits = "111111111111101011111010111110100100100101111111111111010111110101111101001001001011111111111110101111101011111010010010010111111111111101011111010111110100100100101111111111111010111110101111101001001001011111111111110101111101011111010010010010111111111111101011111010111110100100100101111111111111010111110101111101001001001011111111101";
		Uint16 aNumber = new Uint16(aBits);

		BigInteger aBigInt = new BigInteger(aBits, 2);
		System.out.println("a:" + aBigInt.toString(10));

		int n = 16;
		// Pointer<Float> aPtr = allocateFloats(n).order(byteOrder);
		Pointer<Integer> aPtr = allocateInts(16).order(byteOrder);
		Pointer<Integer> resPtr = allocateInts(1).order(byteOrder);
		resPtr.set(0, (int) 0);

		//int[] aInts = aNumber.getInts();
		 int[] aInts = new int[16];
		 aInts[0] = 11;

		for (int i = 0; i < n; i++) {
			aPtr.set(i, aInts[i]);
		}

		// Create OpenCL input buffers (using the native memory pointers aPtr
		// and bPtr) :
		CLBuffer<Integer> a = context.createBuffer(Usage.Input, aPtr);

		// Create an OpenCL output buffer :
		CLBuffer<Integer> res = context.createBuffer(Usage.Output, resPtr);

		// Read the program sources and compile them :
		String kernelCode = IOUtils.readText(new File(
				"src/main/resources/opencl/primecoin.cl"));
		CLProgram program = context.createProgram(kernelCode);// Get and call
																// the kernel :
		CLKernel kernel = program.createKernel("FermatTest");
		// kernel.setArgs(aInts,b, resInts);
		kernel.setArg(0, aInts);
//		kernel.setArg(1, b);
		kernel.setArg(1, res);
		CLEvent addEvt = kernel.enqueueNDRange(queue, new int[] { 1 });

		resPtr = res.read(queue, addEvt); // blocks until
											// add_floats
											// finished
											//
		// System.out.println("Source: " + clint.toString());
		// // Print the first 10 output values :
		// for (int i = 0; i < 10; i++) {
		// UnsignedShort unsignedShort = new UnsignedShort(destPtr.get(i));
		// System.out.println("dest[" + i + "] = " + unsignedShort.getValue() +
		// ", " + unsignedShort.getBinaryRepresentation());
		// }

		System.out.println(new UnsignedInteger(resPtr.get(0)).getBinaryRepresentation());
		// Print the first 10 output values :
//		int[] resAsInts = new int[16];
//		String resBinRep = "";
//		for (int i = 15; i >= 0; i--) {
//			System.out.println("out["
//					+ i
//					+ "] = "
//					+ new UnsignedInteger(resPtr.get(i))
//							.getBinaryRepresentation());
//			resBinRep += new UnsignedInteger(resPtr.get(i))
//					.getBinaryRepresentation();
//		}
//		System.out.println(resBinRep);
//		System.out.println(new BigInteger(resBinRep, 2));
//		Uint16 c = new Uint16(resBinRep);
//		System.out.println(c.getAsBigInteger());
	}
}
