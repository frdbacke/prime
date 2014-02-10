package opencl;

public class TestUnsignedInteger
{
	public static void main(String[] args)
	{
		// 2.147.483.653
		// 10000000000000000000000000000101
		UnsignedInteger unsignedInteger = new UnsignedInteger((long)Math.pow(2,31)+5);
		System.out.println(unsignedInteger.getBinaryRepresentation());
		System.out.println(unsignedInteger.getValue());
		
		unsignedInteger = new UnsignedInteger("10000000000000000000000000000101");
		System.out.println(unsignedInteger.getValue());

		// 65535
		unsignedInteger = new UnsignedInteger("1111111111111111");
		System.out.println(unsignedInteger.getValue());

//		unsignedInteger = new unsignedInteger("1011110111101111");
//		System.out.println(unsignedInteger.getValue());
//		unsignedInteger = new unsignedInteger(48623);
//		System.out.println(unsignedInteger.getBinaryRepresentation());
//		
//		unsignedInteger = new unsignedInteger("0000000000000010");
//		System.out.println(unsignedInteger.getValue());
	}
}
