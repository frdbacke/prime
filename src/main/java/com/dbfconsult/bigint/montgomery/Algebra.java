package com.dbfconsult.bigint.montgomery;
public final class Algebra {

	private static final int BITS = 32;
	private static final long LONG_MASK = 0xFFFFFFFFL;

	private Algebra() {
		// not instance
	}

	public static final int significance(int[] value) {
		int k = value.length;
		while (k > 1 && value[k - 1] == 0) {
			k--;
		}
		return k;
	}

	public static final int[] resize(int[] value, int length) {
		int[] array = new int[length];
		System.arraycopy(value, 0, array, 0,
				Math.min(value.length, array.length));
		return array;
	}

	public static final int[] normalize(int[] value) {
		int length = significance(value);

		if (value.length != length) {
			return resize(value, length);
		}
		return value;
	}

	public static final int getBitsCount(int value) {
		return BITS - Integer.numberOfLeadingZeros(value);
	}

	public static final int getBitsCount(int[] value) {
		int len = significance(value);
		return (len - 1) * BITS + getBitsCount(value[len - 1]);
	}

	public static final int compare(int[] lhs, int[] rhs) {
		int lhsLen = significance(lhs);
		int rhsLen = significance(rhs);

		if (lhsLen > rhsLen)
			return 1;
		else if (lhsLen < rhsLen)
			return -1;
		else {
			for (int pos = lhsLen - 1; pos >= 0; pos--) {
				if ((lhs[pos] & LONG_MASK) > (rhs[pos] & LONG_MASK))
					return 1;
				else if ((lhs[pos] & LONG_MASK) < (rhs[pos] & LONG_MASK))
					return -1;
			}
			return 0;
		}
	}

	public static final void sub(int[] lhs, int[] rhs) {
		int pos = 0;
		int borrow = 0;

		for (; pos < rhs.length; pos++) {
			long temp = (lhs[pos] & LONG_MASK) - (rhs[pos] & LONG_MASK)
					- borrow;

			lhs[pos] = (int) temp;
			borrow = (((temp & (1L << BITS)) != 0) ? 1 : 0);
		}

		if (rhs.length < lhs.length)
			for (; pos < lhs.length; pos++) {
				long temp = (lhs[pos] & LONG_MASK) - borrow;
				lhs[pos] = (int) temp;
				borrow = (((temp & (1L << BITS)) != 0) ? 1 : 0);
			}
	}

	public static final int[] shift(int[] lhs, int rhs) {
		int shiftBits = (rhs > 0 ? rhs : -rhs) % BITS;
		int shiftWords = (rhs > 0 ? rhs : -rhs) / BITS;

		int inBitsCount = getBitsCount(lhs);
		int inWordsCount = inBitsCount / BITS
				+ (inBitsCount % BITS > 0 ? 1 : 0);

		int outBitsCount = inBitsCount + rhs;
		int outWordsCount = outBitsCount / BITS
				+ (outBitsCount % BITS > 0 ? 1 : 0);

		if (outWordsCount <= 0)
			return new int[] { 0 };

		int[] result = new int[inWordsCount > outWordsCount ? inWordsCount
				: outWordsCount];

		if (rhs > 0) {
			if (0 == shiftBits) {
				System.arraycopy(lhs, 0, result, shiftWords, outWordsCount
						- shiftWords);
			} else {
				int pos = 0;
				int carry = 0;

				for (; pos < inWordsCount; pos++) {
					int temp = lhs[pos];
					result[pos + shiftWords] = (temp << shiftBits) | carry;
					carry = temp >>> (BITS - shiftBits);
				}

				if (pos + shiftWords < outWordsCount) {
					result[pos + shiftWords] |= carry;
				}
			}
		} else {
			if (0 == shiftBits) {
				System.arraycopy(lhs, shiftWords, result, 0, outWordsCount);
			} else {
				int carry = 0;
				int pos = outWordsCount;

				if (pos + shiftWords < inWordsCount) {
					carry = lhs[pos + shiftWords] << (BITS - shiftBits);
				}
				pos--;

				for (; pos >= 0; pos--) {
					int temp = lhs[pos + shiftWords];
					result[pos] = (temp >>> shiftBits) | carry;
					carry = temp << (BITS - shiftBits);
				}
			}
		}

		return result;
	}

	public static final void shiftRight(int[] value) {
		int len = significance(value);
		long carry = 0;

		for (int pos = len - 1; pos >= 0; pos--) {
			long temp = (value[pos] & LONG_MASK);

			long nextCarry = ((temp & 1) << (BITS - 1) & LONG_MASK);
			value[pos] = (int) (((temp >>> 1) | carry) & LONG_MASK);
			carry = nextCarry;
		}
	}

	public static final void remainder(int[] lhs, int[] rhs) {
		int lhsBitsCount = getBitsCount(lhs);
		int rhsBitsCount = getBitsCount(rhs);

		int shift = lhsBitsCount - rhsBitsCount;

		int[] temp = shift(rhs, shift);

		if (compare(lhs, temp) < 0) {
			shiftRight(temp);
		}

		while (compare(lhs, rhs) > 0) {
			while (compare(lhs, temp) > 0) {
				sub(lhs, temp);
			}
			lhsBitsCount = getBitsCount(lhs);
			shift = lhsBitsCount - rhsBitsCount;

			temp = shift(rhs, shift);

			if (compare(lhs, temp) < 0) {
				shiftRight(temp);
			}
		}
	}

}
