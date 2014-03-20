package com.dbfconsult.bigint.montgomery;
public class Montgomery {

	private static final long LONG_MASK = 0xFFFFFFFFL;

	private static final int BITS = 32;
	
	private static final long BIT_MASK = 0x80000000L;
	
	private Montgomery() {
		// no instance
	}

	private static final int inverse(int value) {
		int temp = (((value + 2) & 4) << 1) + value;

		temp *= 2 - value * temp;
		temp *= 2 - value * temp;
		temp *= 2 - value * temp;

		return -temp;
	}

	// [1] 14.36 Algorithm Montgomery multiplication
	// INPUT: integers
	// m = (m[n-1] ... m[1] m[0]){b},
	// x = (x[n-1] ... x[1] x[0]){b},
	// y = (y[n-1] ... y[1] y[0]){b}
	// with 0 <= x, y < m,
	// R = b^n with gcd(m, b) = 1,
	// and mQ = -m^1 mod b.
	// OUTPUT: x * y * R^-1 mod m.
	public static final int[] montgomeryMultiplication(int[] x, int[] y,
			int[] m, int mQ) {
		int n = Algebra.significance(m);

		// 1. A = 0. (Notation: A = (a[n] a[n-1] ... a[1] a[0]){b})
		int[] a = new int[n + 1];

		// 2. For i from 0 to (n - 1) do the following:
		for (int i = 0; i < n; i++) {
			// 2.1 u_i = (a[0] + x[i] * y[0]) * mQ mod b.
			long u = ((a[0] + (((x[i] & LONG_MASK) * (y[0] & LONG_MASK)) & LONG_MASK)) * mQ)
					& LONG_MASK;

			// 2.2 A = (A + x[i] * y + u_i * m) / b.
			long xy = (x[i] & LONG_MASK) * (y[0] & LONG_MASK);
			long um = u * (m[0] & LONG_MASK);

			long temp = (a[0] & LONG_MASK) + (xy & LONG_MASK)
					+ (um & LONG_MASK);
			long carry = (xy >>> BITS) + (um >>> BITS) + (temp >>> BITS);

			for (int pos = 1; pos < n; pos++) {
				xy = (x[i] & LONG_MASK) * (y[pos] & LONG_MASK);
				um = u * (m[pos] & LONG_MASK);

				temp = (a[pos] & LONG_MASK) + (xy & LONG_MASK)
						+ (um & LONG_MASK) + (carry & LONG_MASK);
				carry = (carry >>> 32) + (xy >>> BITS) + (um >>> BITS)
						+ (temp >>> BITS);

				a[pos - 1] = (int) temp;
			}

			carry += (a[n] & LONG_MASK);

			a[n - 1] = (int) carry;
			a[n] = (int) (carry >>> BITS);
		}

		// 3. If A >= m then A = A - m
		if (Algebra.compare(a, m) >= 0) {
			Algebra.sub(a, m);
		}

		// 4. Return (A).
		return a;
	}

	// [1] 14.94 Algorithm Montgomery exponentiation
	// INPUT:
	// m = (m[l-1] ... m[0]){b},
	// R = b^l,
	// mQ = -m^1 mod b,
	// e = (e[t] ... e[0]){2}
	// with e[t] = 1,
	// and an integer x, 1 <= x < m.
	// OUTPUT: x^e mod m.
	public static final int[] montgomeryExponentiation(int[] xValue, int[] e,
			int[] m) {
		int[] x = xValue.clone();

		x = Algebra.resize(x, m.length);

		// mQ = -m^1 mod b
		int mQ = inverse(m[0]);

		// 1. temp = Mont(x, R^2 mod m), A = R mod m.
		int[] r2 = new int[m.length * 2 + 1];
		r2[r2.length - 1] = 1;

		Algebra.remainder(r2, m);

		int[] temp = montgomeryMultiplication(x, r2, m, mQ);

		int[] a = new int[m.length + 1];
		a[a.length - 1] = 1;

		Algebra.remainder(a, m);

		int expLen = Algebra.significance(e); // значащая длина exponent
		int expBits = expLen * BITS; // количество битов степени
		int expPos = expLen - 1; // позиция
		long bitMask = BIT_MASK; // битовая маска

		// Узнаем количество значащих битов степени
		while (0 == (e[expPos] & bitMask)) {
			bitMask >>>= 1;
			expBits--;
		}

		// 2. For i from t down to 0 do the following:
		while (expBits != 0) {
			// 2.1 A = Mont(A, A).
			a = montgomeryMultiplication(a, a, m, mQ);

			// 2.2 If e[i] = 1 then A = Mont(A, temp).
			if (0 != (e[expPos] & bitMask)) // если бит равен 1
				a = montgomeryMultiplication(a, temp, m, mQ);

			expBits--;
			bitMask >>>= 1;

			if (0 == bitMask) {
				bitMask = BIT_MASK;
				expPos--;
			}
		}

		// 3. A Mont(A, 1).
		int[] one = new int[m.length];
		one[0] = 1;

		a = montgomeryMultiplication(a, one, m, mQ);

		Algebra.normalize(a);

		// 4. Return (A).
		return a;
	}

}
