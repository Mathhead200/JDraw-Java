package com.mathhead200;

import java.io.*;


/**
 * A collection of static methods
 * @author Christopher D'Angelo
 * @author JBD Computers &trade;
 */
public final class MH
{
	public static Object[][] increaseLengthOfMatrix(Object[][] mat, int nI, int nJ)
	{
		Object[][] newMatrix = new String[mat.length + nI][mat[0].length + nJ];
		System.arraycopy( mat, 0, newMatrix, 0, Math.min(mat.length, newMatrix.length) );
		System.arraycopy( mat[0], 0, newMatrix[0], 0, Math.min(mat[0].length, newMatrix[0].length) );
		return newMatrix;
	}


	public interface Sort<T> {
		public boolean isBetter(T a, T b);
	}

	//I call this the "CSort Algorithm"
	public static <T> T[] sort(T[] arr, Sort<T> sort)
	{
		for( int pos = 1; pos < arr.length; pos++ ) { //marker #1: moving right across array
			T n = arr[pos];
			for( int i = 0; i < pos; i++ ) { //marker #2: moving right from start of array to marker #1
				if( sort.isBetter(n, arr[i]) ) {
					for( int $_ = pos - 1; $_ >= i; $_--  ) { //moving left from marker #1 to marker #2
						arr[$_ + 1] = arr[$_];
					}
					arr[i] = n;
					break;
				}
			}
		}
		return arr;
	}

	/**
	 * sorts by {@link Comparable#compareTo(Object)} if a.compareTo(b) > 0,
	 * if a or b cannot be compared in this way will not sort
	 * @param arr - array to sort
	 * @return
	 */
	public static <T> T[] sort(T[] arr) {
		return sort( arr, new Sort<T>() {
			public boolean isBetter(T a, T b) {
				try{
					return ((Comparable<T>)a).compareTo(b) > 0;
				} catch(ClassCastException e) {
					return false;
				}
			}
		});
	}


	/**
	 * Gets a line of input from {@link System#in}
	 * @param message - a message to print before prompting for input
	 * @return a line as a String
	 */
	public static String ask(String message) {
		BufferedReader r = new BufferedReader( new InputStreamReader(System.in) );
		System.out.print(message);
		String rStr = null;
		try {
			rStr = r.readLine();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return rStr;
	}

	public static String ask() {
		return ask("");
	}


	/**
	 * @return start position of number,
	 * or <code>pos</code> if no number can be found
	 */
	public static int getNumBefore(String str, int pos) {
		int len;
		for( len = 1; len <= pos; len++ ) {
			String num = str.substring(pos - len, pos);
			if( num.matches("\\s{"+len+"}") || num.charAt(0) == '.' )
				continue;
			try {
				Double.parseDouble(num);
			} catch(NumberFormatException e) { break; }
		}
		len--;
		return pos - len;
	}

	/**
	 * @return end position of number,
	 * or <code>pos</code> if no number can be found
	 */
	public static int getNumAfter(String str, int pos) {
		int len, ws = 0;
		for( len = 2; len + pos <= str.length(); len++ ) {
			String num = str.substring(pos + 1, pos + len);
			if( num.matches("\\s{"+(len-1)+"}") ) {
				ws = len - 1;
				continue;
			} else if( ws == num.length() - 1 && (num.charAt(ws) == '.' || num.charAt(ws) == '-' || num.charAt(ws) == '+') )
				continue;
			try {
				Double.parseDouble(num);
			} catch(NumberFormatException e) { break; }
		}
		len--;
		return pos + len;
	}

	/**
	 * @param b
	 * @return if true 1 (one), else 0 (zero)
	 */
	public static int is(boolean b) {
		return b ? 1 : 0;
	}

	/**
	 * @param n
	 * @return n!
	 */
	public static int factorial(int n) {
		if( n < 0 )
			throw new IllegalArgumentException(n + " < 0");
		else if( n > 1 )
			return n * factorial(n - 1);
		return 1;
	}

	/**
	 * check against all elements in <code>arr</code> with {@link Object#equals(Object)}
	 * @param <T>
	 * @param arr
	 * @param o
	 * @return true if any iteration return true, else false
	 */
	public static <T> boolean isIn(T[] arr, T o) {
		for( T ele : arr ) {
			if( ele.equals(o) )
				return true;
		}
		return false;
	}
}
