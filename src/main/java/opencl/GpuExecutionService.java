package opencl;

import static org.bridj.Pointer.*;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.util.HashMap;

import org.bridj.Pointer;

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

/**
 * @author frederik
 *
 */
public class GpuExecutionService {

	private HashMap<String, CLKernel> kernels;

	ByteOrder byteOrder;
	CLQueue queue;

	CLContext context;

	public GpuExecutionService() throws Exception {
		context = JavaCL.createBestContext();

		queue = context.createDefaultQueue();
		byteOrder = context.getByteOrder();

		// Read the program sources and compile them :
		String kernelCode = IOUtils.readText(new File(
				"src/main/resources/opencl/bigint.cl"));

		// Get and call the kernel
		CLProgram program = context.createProgram(kernelCode);
		kernels = new HashMap<String, CLKernel>();
		
		kernels.put("mult_krnl", program.createKernel("mult_krnl"));
		kernels.put("sub_krnl", program.createKernel("sub_krnl"));
		kernels.put("div_krnl", program.createKernel("div_krnl"));
		kernels.put("wmexp_l_krnl", program.createKernel("wmexp_l_krnl"));
		
	}

	public BigInteger multiply(BigInteger aBigInt, BigInteger bBigInt)
			throws IOException {

		Clint aClint = new Clint(aBigInt);
		Clint bClint = new Clint(bBigInt);

		int n = Clint.NUMBER_OF_DIGITS;
		Pointer<Short> aPtr = allocateShorts(n).order(byteOrder);
		Pointer<Short> bPtr = allocateShorts(n).order(byteOrder);
		Pointer<Short> outPtr = allocateShorts(n).order(byteOrder);

		short[] aIntRep = aClint.getShorts();
		short[] bIntRep = bClint.getShorts();
		for (int i = 0; i < n; i++) {
			aPtr.set(i, aIntRep[i]);
			bPtr.set(i, bIntRep[i]);
			outPtr.set(i, (short) 0);
		}

		// Create OpenCL input buffers (using the native memory pointers aPtr
		// and bPtr) :
		CLBuffer<Short> a = context.createBuffer(Usage.Input, aPtr);
		CLBuffer<Short> b = context.createBuffer(Usage.Input, bPtr);

		// Create an OpenCL output buffer :
		CLBuffer<Short> out = context.createBuffer(Usage.Output, outPtr);

		CLKernel kernel = kernels.get("mult_krnl");
		kernel.setArg(0, a);
		kernel.setArg(1, b);
		kernel.setArg(2, out);
		CLEvent addEvt = kernel.enqueueNDRange(queue, new int[] { 1 });

		outPtr = out.read(queue, addEvt);

		short[] outShorts = outPtr.getShorts();
		Clint result = new Clint(outShorts);
		
		a.release();
		b.release();
		out.release();

		return result.asBigInteger();
	}

	public BigInteger sub(BigInteger aBigInt, BigInteger bBigInt)
			throws IOException {

		Clint aClint = new Clint(aBigInt);
		Clint bClint = new Clint(bBigInt);

		int n = Clint.NUMBER_OF_DIGITS;
		Pointer<Short> aPtr = allocateShorts(n).order(byteOrder);
		Pointer<Short> bPtr = allocateShorts(n).order(byteOrder);
		Pointer<Short> outPtr = allocateShorts(n).order(byteOrder);

		short[] aIntRep = aClint.getShorts();
		short[] bIntRep = bClint.getShorts();
		for (int i = 0; i < n; i++) {
			aPtr.set(i, aIntRep[i]);
			bPtr.set(i, bIntRep[i]);
			outPtr.set(i, (short) 0);
		}

		// Create OpenCL input buffers (using the native memory pointers aPtr
		// and bPtr) :
		CLBuffer<Short> a = context.createBuffer(Usage.Input, aPtr);
		CLBuffer<Short> b = context.createBuffer(Usage.Input, bPtr);

		// Create an OpenCL output buffer :
		CLBuffer<Short> out = context.createBuffer(Usage.Output, outPtr);

		CLKernel kernel = kernels.get("sub_krnl");
		kernel.setArg(0, a);
		kernel.setArg(1, b);
		kernel.setArg(2, out);
		CLEvent addEvt = kernel.enqueueNDRange(queue, new int[] { 1 });

		outPtr = out.read(queue, addEvt);

		short[] outShorts = outPtr.getShorts();
		Clint result = new Clint(outShorts);
		
		a.release();
		b.release();
		out.release();

		return result.asBigInteger();
	}
	
	public BigInteger mod(BigInteger d1BigInt, BigInteger d2BigInt) {
		Clint d1Clint = new Clint(d1BigInt);
		System.out.println("d1 clint: " + d1Clint.toString());
		Clint d2Clint = new Clint(d2BigInt);
		System.out.println("d2 clint: " + d2Clint.toString());

		int n = Clint.NUMBER_OF_DIGITS;
		Pointer<Short> d1Ptr = allocateShorts(n).order(byteOrder);
		Pointer<Short> d2Ptr = allocateShorts(n).order(byteOrder);
		Pointer<Short> quotPtr = allocateShorts(n).order(byteOrder);
		Pointer<Short> remPtr = allocateShorts(n).order(byteOrder);

		short[] d1IntRep = d1Clint.getShorts();
		short[] d2IntRep = d2Clint.getShorts();
		for (int i = 0; i < n; i++) {
			d1Ptr.set(i, d1IntRep[i]);
			d2Ptr.set(i, d2IntRep[i]);
			quotPtr.set(i, (short) 0);
			remPtr.set(i, (short) 0);
		}

		// Create OpenCL input buffers (using the native memory pointers aPtr
		// and bPtr) :
		CLBuffer<Short> d1 = context.createBuffer(Usage.Input, d1Ptr);
		CLBuffer<Short> d2 = context.createBuffer(Usage.Input, d2Ptr);

		// Create an OpenCL output buffer :
		CLBuffer<Short> quot= context.createBuffer(Usage.Output, quotPtr);
		CLBuffer<Short> rem= context.createBuffer(Usage.Output, remPtr);

		CLKernel kernel = kernels.get("div_krnl");
		kernel.setArg(0, d1);
		kernel.setArg(1, d2);
		kernel.setArg(2, quot);
		kernel.setArg(3, rem);
		CLEvent addEvt = kernel.enqueueNDRange(queue, new int[] { 1 });

//		quotPtr = quot.read(queue, addEvt);
		remPtr = rem.read(queue, addEvt);

		short[] remShorts = remPtr.getShorts();
		Clint remClint = new Clint(remShorts);
		System.out.println(remClint.toString());
		
		d1.release();
		d2.release();
		rem.release();
		quot.release();

		return remClint.asBigInteger();
		
	}	

	
	/**
	 * 
	 * void wmexp_l (ushort bas, ushort*  e_l, ushort* rem_l, ushort* m_l)
	 * 
	 * @return
	 */
	public BigInteger modExp(Short bas, BigInteger expBigInt, BigInteger mBigInt) {
		System.out.println("base: " + bas);
		Clint expClint = new Clint(expBigInt);
		System.out.println("exp clint: " + expClint.toString());
		Clint mClint = new Clint(mBigInt);
		System.out.println("m clint: " + mClint.toString());

		int n = Clint.NUMBER_OF_DIGITS;
		Pointer<Short> expPtr = allocateShorts(n).order(byteOrder);
		Pointer<Short> mPtr = allocateShorts(n).order(byteOrder);
		Pointer<Short> remPtr = allocateShorts(n).order(byteOrder);

		short[] expIntRep = expClint.getShorts();
		short[] mIntRep = mClint.getShorts();
		for (int i = 0; i < n; i++) {
			expPtr.set(i, expIntRep[i]);
			mPtr.set(i, mIntRep[i]);
			remPtr.set(i, (short) 0);
		}

		// Create OpenCL input buffers (using the native memory pointers aPtr
		// and bPtr) :
		CLBuffer<Short> exp = context.createBuffer(Usage.Input, expPtr);
		CLBuffer<Short> m = context.createBuffer(Usage.Input, mPtr);

		// Create an OpenCL output buffer :
		CLBuffer<Short> rem= context.createBuffer(Usage.Output, remPtr);

		CLKernel kernel = kernels.get("wmexp_l_krnl");
		kernel.setArg(0, (short)2);
		kernel.setArg(1, exp);
		kernel.setArg(2, rem);
		kernel.setArg(3, m);
		CLEvent addEvt = kernel.enqueueNDRange(queue, new int[] { 1 });

		remPtr = rem.read(queue, addEvt);

		short[] remShorts = remPtr.getShorts();
		Clint remClint = new Clint(remShorts);
		System.out.println(remClint.toString());
		
		exp.release();
		m.release();
		rem.release();

		return remClint.asBigInteger();
		
	}	

	public void printDeviceInfo() {
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

	}
}
