package com.dbfconsult.bigint.base10;
import org.junit.Test;

import com.dbfconsult.util.BigintUtil;

public class AddSubTestCase
{

	 @Test
	 public void testAdd()
	 {
	 int[] a = new int[] {1, 2, 3, 0, 0};
	 int[] b = new int[] {9, 9, 9, 9, 0};
	 int[] result = BigintBase10.add(a, b);
	 System.out.println(BigintUtil.print(result));
	 }
	
	 @Test
	 public void testSub()
	 {
	 int[] a = new int[] {1, 2, 3, 9, 0};
	 int[] b = new int[] {2, 1, 5, 0, 0};
	 int[] result = BigintBase10.sub(a, b);
	
	 //3039
	 System.out.println(BigintUtil.print(result));
	 }
	//
	// @Test
	// public void testEqual()
	// {
	// int[] a = new int[] {1, 2, 3, 9, 0};
	// int[] b = new int[] {2, 1, 5, 0, 0};
	// boolean result = Bigint.equal(a, b);
	// //false
	// Assert.assertFalse(result);
	//
	// result = Bigint.equal(a, a);
	// //false
	// Assert.assertTrue(result);
	//
	// result = Bigint.equal(b, a);
	// //false
	// Assert.assertFalse(result);
	// }

}
