package com.mathhead200;

import java.io.*;


/**
 * Stores Strings to a text file
 * @author Christopher D'Angelo
 * @author JBD Computers &trade;
 * @since 2/22/2010
 */
public class FileVar
{
	private String[][] myVars;
	private String myAddr;

	public FileVar(String addr) throws IOException
	{
		myAddr = addr;
		try {
			FileReader reader = new FileReader(addr);
			String fileStr = "";
			for( int c = reader.read(); c != -1; c = reader.read() ) {
				fileStr += (char)c;
			}
			String[] tempVars = fileStr.split("};;");
			myVars = new String[tempVars.length][2];
			for( int i = 0; i < tempVars.length; i++ ) {
				String str = tempVars[i];
				int index = str.indexOf("=={");
				if( index == -1 ) {
					myVars[i][0] = "";
					myVars[i][1] = str;
				}
				else {
					myVars[i][0] = str.substring( 0, index );
					if( (index + 3) < str.length() ) {
						myVars[i][1] = str.substring( index + 3 ); }
					else {
						myVars[i][1] = ""; }
					if( myVars[i][0].charAt(0) == ';'
						&& myVars[i][0].charAt( myVars[i][0].length() - 1 ) == '=')
					{
						myVars[i][0] = myVars[i][0].substring( 1, myVars[i][0].length() - 1 );
						myVars[i][0] = myVars[i][0].trim();
					}
				}
			}
		}
		catch(FileNotFoundException e) {
			myVars = new String[0][2]; }
	}


	public String[][] getVars()
	{
		return this.myVars;
	}

	public int[] findIndexesAt(String key)
	{
		int[] indexes = new int[this.myVars.length];
		int pos = 0;
		for( int i = 0; i < this.myVars.length; i++ ) {
			if( key.equals(this.myVars[i][0]) ) {
				indexes[pos] = i;
				pos++;
			}
		}
		int[] indexesAfterTrim = new int[pos];
		System.arraycopy(indexes, 0, indexesAfterTrim, 0, pos);
		return indexesAfterTrim;
	}

	public String[] findKeysAt(int... indexes)
	{
		String[] keys = new String[indexes.length];
		for( int i = 0; i < indexes.length; i++ ) {
			keys[i] = this.myVars[ indexes[i] ][0];
		}
		return keys;
	}

	public String[] findValuesAt(int... indexes)
	{
		String[] values = new String[indexes.length];
		for( int i = 0; i < indexes.length; i++ ) {
			values[i] = this.myVars[ indexes[i] ][1];
		}
		return values;
	}

	public String[] findValuesAt(String key)
	{
		String[] values = new String[this.myVars.length];
		int index = 0;
		for( int i = 0; i < this.myVars.length; i++ ) {
			if( key.equals(this.myVars[i][0]) ) {
				values[index] = this.myVars[i][1];
				index++;
			}
		}
		String[] valuesAfterTrim = new String[index];
		System.arraycopy(values, 0, valuesAfterTrim, 0, index);
		return valuesAfterTrim;
	}

	public void add(String[]... newVars) throws IOException
	{
		this.myVars = (String[][])MH.increaseLengthOfMatrix( this.myVars, newVars.length, 0 );
		for( int i = 0; i < newVars.length; i++ ) {
			int index = this.myVars.length - newVars.length + i;
			this.myVars[index][0] = newVars[i][0];
			String str = "";
			for( int j = 1; j < newVars[0].length; j++) {
				if( newVars[i][j] != null ) {
					str += newVars[i][j]; }
			}
			this.myVars[index][1] = str;
		}
		this.writeToFile();
	}

	public String[][] remove(int n) throws IOException
	{
		String[][] removed = new String[n][2];
		for( int i = 0; i < removed.length; i++ ) {
			removed[i] = this.myVars[this.myVars.length - 1 - i];
		}
		this.myVars = (String[][])MH.increaseLengthOfMatrix(this.myVars, -n, 0);
		return removed;
	}

	public String[][] removeByIndex(Integer... indexes) throws IOException
	{
		indexes = MH.sort(indexes, new MH.Sort<Integer>() {
			public boolean isBetter(Integer a, Integer b) {
				return a.compareTo(b) > 0;
			}
		} );
		String[][] removed = new String[indexes.length][2];
		for( int i = 0; i < removed.length; i++ ) {
			removed[i] = this.myVars[indexes[i]];
		}
		for( int n : indexes ) {
			String[][] tempVars = this.myVars;
			this.myVars = new String[this.myVars.length - 1][2];
			System.arraycopy(tempVars, 0, this.myVars, 0, n);
			System.arraycopy(tempVars, n + 1, this.myVars, n, this.myVars.length - n);
		}
		this.writeToFile();
		return removed;
	}

	public void writeToFile() throws IOException
	{
		FileWriter writer = new FileWriter(this.myAddr);
		for( int i = 0; i < myVars.length; i++ ) {
			if( myVars[i][0] == null ) {
				writer.write(""); }
			else {
				writer.write( ";\n\n\t" + myVars[i][0] + "\n\t==={" ); }
			writer.write( myVars[i][1] + "};;" );
		}
		writer.close();
	}


	public static void main(String[] args) throws IOException
	{
		FileVar vars = new FileVar("misc/test_file.var");
		String var1Name = "aNewVar_Chris";
		String var1Value = "This is a new var being added though the add() method";
		vars.add( new String[][]{{var1Name,var1Value}} );
		vars.removeByIndex(1, vars.findIndexesAt("")[0]);
		String[][] varMat = vars.getVars();
		for(int i=0; i<varMat.length; i++) {
			System.out.print(i+"-> ");
			for(int j=0; j<varMat[0].length; j++) {
				System.out.print(j+"["+varMat[i][j]+"] ");
			}
			System.out.print("\n");
		}
	}
}
