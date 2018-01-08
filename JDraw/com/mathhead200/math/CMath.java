package com.mathhead200.math;


public class CMath
{
	public static int add(int a, int b)
	{
		int m = a ^ b,
		n = a & b;
		if( n != 0 )
			return add(m, n << 1);
		else
			return m;
	}

	public static int subtract(int a, int b)
	{
		int m = a ^ b,
		n = ~a & b;
		if( n != 0 )
			return subtract(m, n << 1);
		else
			return m;
	}

	public static int multiply(int a, int b)
	{
		int sum = 0;
		for( int i = 0; i < 32; i++ ) {
			int p = 1 << i;
			if( (b & p) == p )
				sum = add(sum, a << i);
		}
		return sum;
	}


	public static long add(long a, long b)
	{
		long m = a ^ b,
		n = a & b;
		if( n != 0 )
			return add(m, n << 1);
		else
			return m;
	}

	public static long subtract(long a, long b)
	{
		long m = a ^ b,
		n = ~a & b;
		if( n != 0 )
			return subtract(m, n << 1);
		else
			return m;
	}

	public static long multiply(long a, long b)
	{
		long sum = 0;
		for( int i = 0; i < 64; i++ ) {
			long p = 1 << i;
			if( (b & p) == p )
				sum = add(sum, a << i);
		}
		return sum;
	}
}
