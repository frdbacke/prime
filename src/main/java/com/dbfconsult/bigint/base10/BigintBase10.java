package com.dbfconsult.bigint.base10;

public class BigintBase10
{

	public static final int MAX_DIGITS = 2;

	public static final int BASE = 10;

	public static int[] add(int[] a, int[] b)
	{
		int[] result = new int[MAX_DIGITS];
		int carry = 0;
		int sum = 0;

		sum = a[0] + b[0] + carry;
		carry = sum >= 10 ? 1 : 0;
		sum %= BASE;
		result[0] = sum;

		sum = a[1] + b[1] + carry;
		carry = sum >= BASE ? 1 : 0;
		sum %= BASE;
		result[1] = sum;

		sum = a[2] + b[2] + carry;
		carry = sum >= BASE ? 1 : 0;
		sum %= BASE;
		result[2] = sum;

		sum = a[3] + b[3] + carry;
		carry = sum >= BASE ? 1 : 0;
		sum %= BASE;
		result[3] = sum;

		sum = a[4] + b[4] + carry;
		carry = sum >= BASE ? 1 : 0;
		sum %= BASE;
		result[4] = sum;

		if (carry == 1)
		{
			System.out.println("Panic: overflow");
			System.exit(0);
		}
		return result;
	}

	public static int[] sub(int[] a, int[] b)
	{
		int[] result = new int[MAX_DIGITS];
		int carry = 0;
		int diff = 0;
		for (int i = 0; i < MAX_DIGITS; i++)
		{
			diff = a[i] - b[i] - carry;
			carry = diff < 0 ? 1 : 0;
			diff = diff < 0 ? diff += BASE : diff;

			result[i] = diff;
		}
		return result;
	}

	public static boolean eq(int[] a, int[] b)
	{
		int diffIndex = -1;
		for (int i = 0; i < MAX_DIGITS; i++)
		{
			diffIndex = (a[i] != b[i]) ? i : diffIndex;
		}

		return diffIndex == -1;
	}

	public static boolean lt(int[] a, int[] b)
	{
		int diffIndex = -1;
		for (int i = 0; i < MAX_DIGITS; i++)
		{
			diffIndex = (a[i] != b[i]) ? i : diffIndex;
		}

		return (diffIndex >= 0 && a[diffIndex] < b[diffIndex]) ? true : false;
	}

	public static boolean gt(int[] a, int[] b)
	{
		int diffIndex = -1;
		for (int i = 0; i < MAX_DIGITS; i++)
		{
			diffIndex = (a[i] != b[i]) ? i : diffIndex;
		}

		return (diffIndex >= 0 && a[diffIndex] > b[diffIndex]) ? true : false;
	}

	public static int[] mulwithsingledigit(int[] a, int b)
	{
		int[] result = new int[MAX_DIGITS + 1];
		int carry = 0;
		int mul = 0;
		for (int i = 0; i < result.length - 1; i++)
		{
			mul = a[i] * b + carry;
			carry = mul / BASE;
			mul = mul % BASE;
			result[i] = mul;
		}
		result[MAX_DIGITS] = carry;

		return result;
	}

	public static int[] mul(int[] a, int[] b)
	{
		int[] result = new int[2 * MAX_DIGITS];
		int carry = 0;
		int resptr = 0;

		
		for (int aptr = 0; aptr < a.length; aptr++)
		{
			resptr = aptr;
			for (int bptr = 0; bptr < b.length; bptr++)
			{
				int product = a[aptr] * b[bptr] + result[resptr] + carry;
				carry = product / BASE;
				result[resptr] = product % BASE;
				resptr++;
			}
			result[resptr] = carry;
			carry = 0;
		}

		return result;
	}

	public static int[] mul_2x2(int[] a, int[] b)
	{
		int[] result = new int[2 * MAX_DIGITS];
		int carry = 0, product = 0;

		// unroll 0;
		product = a[0] * b[0] + result[0] + carry;
		carry = product / BASE;
		result[0] = product % BASE;

		product = a[0] * b[1] + result[1] + carry;
		carry = product / BASE;
		result[1] = product % BASE;
		
		result[2] = carry;
		carry = 0;

		// unroll 1;
		
		product = a[1] * b[0] + result[1] + carry;
		carry = product / BASE;
		result[1] = product % BASE;

		product = a[1] * b[1] + result[2] + carry;
		carry = product / BASE;
		result[2] = product % BASE;
		
		result[3] = carry;

		return result;
	}
	

	
}
