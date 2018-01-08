package com.mathhead200.math;

import com.mathhead200.*;


/**
 * functions: sin(x), cos(x), tan(x), sec(x), csc(x), cot(x),
 * 	sign(x), sqrt(x), log(x), ln(x)<br />
 * grouping: <br />
 * &emsp; parentheses (x) [x]<br />
 * &emsp; absolute value |x| <br />
 * operations: a^b (exponentiation), a*b (multiplication), a/b (division),
 * 	a+b (addition), a-b (subtraction) <br />
 * boolean operations: a=b (equals), a~b (not equals),
 * 	a&lt;b (less then), a&gt;b (greater then),
 * 	a_&lt;b (less then or equal to), a_&gt;b (greater then or equal to),
 * 	a&b (and), a\b (or), a_\b (xor)
 * @author Christopher D'Angelo
 * @author JBD Computers &trade;
 */
public class StringEquation
{
	private String equationStr;

	private StringEquation(String equStr, boolean fixSignAndSubs) {
		//remove whitespace
		for( int i = 0; i < equStr.length(); i++ ) {
			String s = "" + equStr.charAt(i);
			if( s.matches("\\s") ) {
				equStr = equStr.substring(0, i) + equStr.substring(i + 1);
				i--;
			}
		}
		//add * signs between a number and a character (2x becomes 2*x)
		for( int i = 0; i < equStr.length() - 1; i++ ) {
			String s = "" + equStr.charAt(i),
				s1 = "" + equStr.charAt(i + 1);
			if( ( s.matches("\\d") && s1.matches("[a-zA-Z]") ) ||
				( s.matches("\\)|\\]") && s1.matches("\\w|\\(||\\[") )
			) {
				equStr = equStr.substring(0, i + 1) + "*" + equStr.substring(i + 1);
				i++;
			}
		}

		if( fixSignAndSubs ) {
			{ //fix sign
			Character[] opps = new Character[] {'=','~','>', '<', '+', '-', '*', '/', '^'};
			for( int i = 1; i < equStr.length(); i++ ) {
				if( equStr.charAt(i) == '-' && !MH.isIn(opps, equStr.charAt(i-1)) ) {
					equStr = equStr.substring(0, i) + "+" + equStr.substring(i);
					i++;
				}
			}
			equStr.replace("(--)+", "+");
			equStr.replace("\\++", "+");
			}
			//fix negation, -x becomes -1*x
			for( int i = 0; i < equStr.length(); i++ ) {
				if( equStr.charAt(i) == '-' && MH.getNumBefore(equStr, i) == i ) {
					equStr = equStr.substring(0, i + 1) + "1*" + equStr.substring(i + 1);
					i += 2;
				}
			}
			{ //fix subs
			String[] eqs = equStr.split(",");
			equStr = eqs[0];
			this.equationStr = equStr;
			for( int i = 1; i < eqs.length; i++ ) {
				int s = eqs[i].indexOf('=');
				try {
					substitute( eqs[i].substring(0, s), eqs[i].substring(s + 1) );
				} catch(IndexOutOfBoundsException e) {
					continue;
				}
			}
			substitute("pi", Math.PI);
			substitute("e", Math.E);
			}
		} else
			this.equationStr = equStr;
	}

	public StringEquation(String equStr) {
		this(equStr, true);
	}


	public static double parseEquation(String equStr) {
		return new StringEquation(equStr).solve();
	}

	public String toString() {
		return equationStr;
	}

	public double solve() {
		try {

		String equStr = "";
		for( char c : equationStr.toCharArray() ) {
			equStr += c;
		}
		//parentheses: () []
		//functions: sin cos tan sec csc cot sign sqrt
		for( int i = equStr.length() - 1; i >= 0; i-- ) {
			if( equStr.substring(i, i + 1).equals("(") ) {
				if( i - 1 >= 0 && equStr.substring(i - 1, i).matches("\\d") ) {
					equStr = equStr.substring(0, i) + "*" + equStr.substring(i);
					i = equStr.length() - 1;
				} else if( i - 1 < 0 || !equStr.substring(i - 1, i).matches("\\w") ) {
					int s = i + 1;
					int f = equStr.indexOf(')', s);
					StringEquation sub = new StringEquation( equStr.substring(s, f), false );
					equStr = equStr.substring(0, s - 1) + sub.solve() + equStr.substring(f + 1);
					i = equStr.length() - 1;
				}
			} else if( equStr.substring(i, i + 1).equals("[") ) {
				if( i - 1 >= 0 && equStr.substring(i - 1, i).matches("\\d") ) {
					equStr = equStr.substring(0, i) + "*" + equStr.substring(i);
					i = equStr.length() - 1;
				} else if( i - 1 < 0 || !equStr.substring(i - 1, i).matches("\\w") ) {
					int s = i + 1;
					int f = equStr.indexOf(']', s);
					StringEquation sub = new StringEquation( equStr.substring(s, f), false );
					equStr = equStr.substring(0, s - 1) + sub.solve() + equStr.substring(f + 1);
					i = equStr.length() - 1;
				}
			} else if( i + 4 <= equStr.length() && equStr.substring(i, i + 4).equals("sin(") ) {
				int s = i + 4;
				int f = equStr.indexOf(')', s);
				StringEquation sub = new StringEquation( equStr.substring(s, f), false );
				equStr = equStr.substring(0, i) + Math.sin( sub.solve() ) + equStr.substring(f + 1);
				i = equStr.length() - 1;
			} else if( i + 4 <= equStr.length() && equStr.substring(i, i + 4).equals("cos(") ) {
				int s = i + 4;
				int f = equStr.indexOf(')', s);
				StringEquation sub = new StringEquation( equStr.substring(s, f), false );
				equStr = equStr.substring(0, i) + Math.cos( sub.solve() ) + equStr.substring(f + 1);
				i = equStr.length() - 1;
			} else if( i + 4 <= equStr.length() && equStr.substring(i, i + 4).equals("tan(") ) {
				int s = i + 4;
				int f = equStr.indexOf(')', s);
				StringEquation sub = new StringEquation( equStr.substring(s, f), false );
				equStr = equStr.substring(0, i) + Math.tan( sub.solve() ) + equStr.substring(f + 1);
				i = equStr.length() - 1;
			} else if( i + 4 <= equStr.length() && equStr.substring(i, i + 4).equals("sec(") ) {
				int s = i + 4;
				int f = equStr.indexOf(')', s);
				StringEquation sub = new StringEquation( equStr.substring(s, f), false );
				equStr = equStr.substring(0, i) + (1 / Math.cos( sub.solve() )) + equStr.substring(f + 1);
				i = equStr.length() - 1;
			} else if( i + 4 <= equStr.length() && equStr.substring(i, i + 4).equals("csc(") ) {
				int s = i + 4;
				int f = equStr.indexOf(')', s);
				StringEquation sub = new StringEquation( equStr.substring(s, f), false );
				equStr = equStr.substring(0, i) + (1 / Math.sin( sub.solve() )) + equStr.substring(f + 1);
				i = equStr.length() - 1;
			} else if( i + 4 <= equStr.length() && equStr.substring(i, i + 4).equals("cot(") ) {
				int s = i + 4;
				int f = equStr.indexOf(')', s);
				StringEquation sub = new StringEquation( equStr.substring(s, f), false );
				equStr = equStr.substring(0, i) + (1 / Math.tan( sub.solve() )) + equStr.substring(f + 1);
				i = equStr.length() - 1;
			} else if( i + 5 <= equStr.length() && equStr.substring(i, i + 5).equals("sign(") ) {
				int s = i + 5;
				int f = equStr.indexOf(')', s);
				StringEquation sub = new StringEquation( equStr.substring(s, f), false );
				double a = sub.solve();
				equStr = equStr.substring(0, i) + (  a>0 ? 1 : a<0 ? -1 : Double.NaN ) + equStr.substring(f + 1);
				i = equStr.length() - 1;
			} else if( i + 5 <= equStr.length() && equStr.substring(i, i + 5).equals("sqrt(") ) {
				int s = i + 5;
				int f = equStr.indexOf(')', s);
				StringEquation sub = new StringEquation( equStr.substring(s, f), false );
				equStr = equStr.substring(0, i) + Math.sqrt( sub.solve() ) + equStr.substring(f + 1);
				i = equStr.length() - 1;
			} else if( i + 4 <= equStr.length() && equStr.substring(i, i + 4).equals("log(") ) {
				int s = i + 4;
				int f = equStr.indexOf(')', s);
				StringEquation sub = new StringEquation( equStr.substring(s, f), false );
				equStr = equStr.substring(0, i) + Math.log10( sub.solve() ) + equStr.substring(f + 1);
				i = equStr.length() - 1;
			} else if( i + 3 <= equStr.length() && equStr.substring(i, i + 3).equals("ln(") ) {
				int s = i + 3;
				int f = equStr.indexOf(')', s);
				StringEquation sub = new StringEquation( equStr.substring(s, f), false );
				equStr = equStr.substring(0, i) + Math.log( sub.solve() ) + equStr.substring(f + 1);
				i = equStr.length() - 1;
			}
		}
		//absolute values: ||
		for( int s = equStr.indexOf('|') + 1; s > 0; s = equStr.indexOf('|', s) ) {
			int f = equStr.indexOf('|', s);
			StringEquation sub = new StringEquation( equStr.substring(s, f), false );
			equStr = equStr.substring(0, s - 1) + Math.abs( sub.solve() ) + equStr.substring(f + 1);
		}
		//boolean operators _\ \ &
		for( int i = 0; i < equStr.length(); i++ ) {
			if( i+2 < equStr.length() && equStr.substring(i, i+2).equals("_\\") ) {
				int b = MH.getNumBefore(equStr, i),
					a = MH.getNumAfter(equStr, i+1);
				equStr = equStr.substring(0, b) + MH.is(
						(Double.parseDouble( equStr.substring(b, i) ) != 0) !=
						(Double.parseDouble( equStr.substring(i+2, a) ) != 0)
					) + equStr.substring(a);
				i = b;
			}
			else if( equStr.charAt(i) == '\\' ) {
				int b = MH.getNumBefore(equStr, i),
					a = MH.getNumAfter(equStr, i);
				equStr = equStr.substring(0, b) + MH.is(
						Double.parseDouble( equStr.substring(b, i) ) != 0 ||
						Double.parseDouble( equStr.substring(i+1, a) ) != 0
					) + equStr.substring(a);
				i = b;
			}
			else if( equStr.charAt(i) == '&' ) {
				int b = MH.getNumBefore(equStr, i),
					a = MH.getNumAfter(equStr, i);
				equStr = equStr.substring(0, b) + MH.is(
						Double.parseDouble( equStr.substring(b, i) ) != 0 &&
						Double.parseDouble( equStr.substring(i+1, a) ) != 0
					) + equStr.substring(a);
				i = b;
			}
		}
		//boolean operators: _> _< = ~ > <
		for( int i = 0; i < equStr.length(); i++ ) {
			if( i+2 < equStr.length() && equStr.substring(i, i+2).equals("_>") ) {
				int b = MH.getNumBefore(equStr, i),
					a = MH.getNumAfter(equStr, i+1);
				equStr = equStr.substring(0, b) + MH.is(
						Double.parseDouble( equStr.substring(b, i) ) >=
						Double.parseDouble( equStr.substring(i+2, a) )
					) + equStr.substring(a);
				i = b;
			}
			else if( i+2 < equStr.length() && equStr.substring(i, i+2).equals("_<") ) {
				int b = MH.getNumBefore(equStr, i),
					a = MH.getNumAfter(equStr, i+1);
				equStr = equStr.substring(0, b) + MH.is(
						Double.parseDouble( equStr.substring(b, i) ) <=
						Double.parseDouble( equStr.substring(i+2, a) )
					) + equStr.substring(a);
				i = b;
			}
			else if( equStr.charAt(i) == '=' ) {
				int b = MH.getNumBefore(equStr, i),
					a = MH.getNumAfter(equStr, i);
				equStr = equStr.substring(0, b) + MH.is(
						Double.parseDouble( equStr.substring(b, i) ) ==
						Double.parseDouble( equStr.substring(i+1, a) )
					) + equStr.substring(a);
				i = b;
			}
			else if( equStr.charAt(i) == '~' ) {
				int b = MH.getNumBefore(equStr, i),
					a = MH.getNumAfter(equStr, i);
				equStr = equStr.substring(0, b) + MH.is(
						Double.parseDouble( equStr.substring(b, i) ) !=
						Double.parseDouble( equStr.substring(i+1, a) )
					) + equStr.substring(a);
				i = b;
			}
			else if( equStr.charAt(i) == '>' ) {
				int b = MH.getNumBefore(equStr, i),
					a = MH.getNumAfter(equStr, i);
				equStr = equStr.substring(0, b) + MH.is(
						Double.parseDouble( equStr.substring(b, i) ) >
						Double.parseDouble( equStr.substring(i+1, a) )
					) + equStr.substring(a);
				i = b;
			}
			else if( equStr.charAt(i) == '<' ) {
				int b = MH.getNumBefore(equStr, i),
					a = MH.getNumAfter(equStr, i);
				equStr = equStr.substring(0, b) + MH.is(
						Double.parseDouble( equStr.substring(b, i) ) <
						Double.parseDouble( equStr.substring(i+1, a) )
					) + equStr.substring(a);
				i = b;
			}
		}
		//operations: ^ !
		for( int i = 0; i < equStr.length(); i++) {
			if( equStr.charAt(i) == '^' ) {
				int b = MH.getNumBefore(equStr, i),
					a = MH.getNumAfter(equStr, i);
				equStr = equStr.substring(0, b) + Math.pow(
						Double.parseDouble( equStr.substring(b, i) ),
						Double.parseDouble( equStr.substring(i+1, a) )
					) + equStr.substring(a);
				i = b;
			}
			else if( equStr.charAt(i) == '!' ) {
				int b = MH.getNumBefore(equStr, i);
				double f = Double.parseDouble( equStr.substring(b, i) );
				if( (int)f != f )
					throw new NumberFormatException("factorial of a non-integer");
				equStr = equStr.substring(0, b) + MH.factorial( (int)f ) + equStr.substring(i+1);
				i = b;
			}
		}

		//operations: * /
		for( int i = 0; i < equStr.length(); i++ ) {
			if( equStr.charAt(i) == '*' ) {
				int b = MH.getNumBefore(equStr, i),
					a = MH.getNumAfter(equStr, i);
				equStr = equStr.substring(0, b) + (
						Double.parseDouble( equStr.substring(b, i) ) *
						Double.parseDouble( equStr.substring(i+1, a) )
					) + equStr.substring(a);
				i = b;
			}
			else if( equStr.charAt(i) == '/' ) {
				int b = MH.getNumBefore(equStr, i),
					a = MH.getNumAfter(equStr, i);
				equStr = equStr.substring(0, b) + (
					Double.parseDouble( equStr.substring(b, i) ) /
					Double.parseDouble( equStr.substring(i+1, a) )
				) + equStr.substring(a);
				i = b;
			}
		}

		//operations: +
		for( int i = 0; i < equStr.length(); i++ ) {
			if( equStr.charAt(i) == '+' ) {
				int b = MH.getNumBefore(equStr, i),
					a = MH.getNumAfter(equStr, i);
				if( b == i )
					break;
				equStr = equStr.substring(0, b) + (
						Double.parseDouble( equStr.substring(b, i) ) +
						Double.parseDouble( equStr.substring(i+1, a) )
					) + equStr.substring(a);
				i = b;
			}
		}

		return Double.parseDouble(equStr);

		} catch(NumberFormatException e) {
			return Double.NaN;
		}
	}

	public int substitute(String var, StringEquation val) {
		int count = 0;
		for( int i = equationStr.indexOf(var); i >= 0; i = equationStr.indexOf(var, i + 1) ) {
			if( ( i - 1 >= 0 && equationStr.substring(i - 1, i).matches("[a-zA-Z]") ) ||
				( i + var.length() < equationStr.length() && equationStr.substring(i + var.length(), i + var.length() + 1).matches("[a-zA-Z]") )
			) continue;
			equationStr = equationStr.substring(0, i) + "[" + val + "]" + equationStr.substring( i + var.length() );
			count++;
			i += val.toString().length() + 2;
		}
		return count;
	}

	public int substitute(String var, String val) {
		return substitute( var, new StringEquation(val) );
	}

	public int substitute(String var, double val) {
		int count = 0;
		for( int i = equationStr.indexOf(var); i >= 0; i = equationStr.indexOf(var, i + 1) ) {
			if( ( i - 1 >= 0 && equationStr.substring(i - 1, i).matches("[a-zA-Z]") ) ||
				( i + var.length() < equationStr.length() && equationStr.substring(i + var.length(), i + var.length() + 1).matches("[a-zA-Z]") )
			) continue;
			equationStr = equationStr.substring(0, i) + "[" + val + "]" + equationStr.substring( i + var.length() );
			count++;
			i = i += (""+val).length() + 2;
		}
		return count;
	}
}
