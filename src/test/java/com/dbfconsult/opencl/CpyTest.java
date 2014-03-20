package com.dbfconsult.opencl;

import static java.lang.Math.*;
import static org.bridj.Pointer.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;

import org.bridj.Pointer;


import com.dbfconsult.bigint.clint.Clint;
import com.dbfconsult.unsigned.UnsignedShort;
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

public class CpyTest {
	public static void main(String[] args) throws IOException {
		JavaCL javaCL = new JavaCL();

		CLContext context = JavaCL.createBestContext();

		CLDevice[] devices = context.getDevices();
		for (CLDevice clDevice : devices) {
			System.out.println("Global mem size: " + clDevice.getGlobalMemSize() + " bytes..");
			System.out.println("Address bits: " + clDevice.getAddressBits());
			System.out.println("Preferred vector width chars: " + clDevice.getPreferredVectorWidthChar());
			System.out.println("Max clock frequency: " + clDevice.getMaxClockFrequency());
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
		
		Clint clint = new Clint("0011111111111111111111111111111100011111");

		int n = 65;
//		Pointer<Float> aPtr = allocateFloats(n).order(byteOrder);
		Pointer<Short> srcPtr = allocateShorts(n).order(byteOrder);
		Pointer<Short> destPtr = allocateShorts(n).order(byteOrder);
		

		short[] shorts = clint.getShorts();
		for (int i = 0; i < n; i++) {
			srcPtr.set(i, shorts[i]);
			destPtr.set(i, (short)0);
		}

		// Create OpenCL input buffers (using the native memory pointers aPtr
		// and bPtr) :
		CLBuffer<Short> src = context.createBuffer(Usage.Input, srcPtr);

		// Create an OpenCL output buffer :
		CLBuffer<Short> dest = context.createBuffer(Usage.Output, destPtr);

		// Read the program sources and compile them :
		String kernelCode = IOUtils.readText(new File("src/main/resources/opencl/primecoin.cl"));
		CLProgram program = context.createProgram(kernelCode);// Get and call the kernel :
		CLKernel kernel = program.createKernel("cpy_l");
		kernel.setArgs(src, dest);
		CLEvent addEvt = kernel.enqueueNDRange(queue, new int[] { 1 });

		destPtr = dest.read(queue, addEvt); // blocks until
															// add_floats
															// finished

		System.out.println("Source: " + clint.toString());
		// Print the first 10 output values :
		for (int i = 0; i < 10; i++) {
			UnsignedShort unsignedShort = new UnsignedShort(destPtr.get(i));
			System.out.println("dest[" + i + "] = " + unsignedShort.getValue() + ", " + unsignedShort.getBinaryRepresentation());
		}

		// Print the first 10 output values :
//		for (int i = 0; i < n; i++)
//			System.out.println("out[" + i + "] = " + outPtr.get(i));

	}
}
