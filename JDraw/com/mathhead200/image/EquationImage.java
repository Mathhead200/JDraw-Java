package com.mathhead200.image;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class EquationImage extends BufferedImage
{
	private static final int STD_XMIN = -100, STD_XMAX = 100, STD_YMIN = -100, STD_YMAX = 100;

	private static int fix(double x) {
		int i = (int)Math.round( 0xff / 100.0 * x );
		return inRange(i);
	}

	private static int inRange(int n) {
		if( n < 0 )
			return 0;
		if( n > 0xff )
			return 0xff;
		return n;
	}


	/**
	 * creates an Image object that is of the dimensions <code>width</code> by <code>height</code>
	 * @param width
	 * @param height
	 */
	public EquationImage(int width, int height) {
		super(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	public EquationImage(BufferedImage img) {
		this( img.getWidth(), img.getHeight() );
		for( int y = 0; y < this.getHeight(); y++ )
			for( int x = 0; x < this.getWidth(); x++ )
				this.setRGB( x, y, img.getRGB(x, y) );
	}

	/**
	 * creates an Image object that is of the dimensions <code>width</code> by <code>height</code>
	 * @param width
	 * @param height
	 * @param xMin - the lowest x-value passed to the ColorEquations (inclusive)
	 * @param xMax - the high limit for x-values passed to the ColorEquations (exclusive)
	 * @param yMin - the lowest y-value passed to the ColorEquations (inclusive)
	 * @param yMax - the high limit for x-values passed to the ColorEquations (exclusive)
	 * @param colors - the functions that define the colors
	 */
	public EquationImage(int width, int height,
		double xMin, double xMax, double yMin, double yMax, ColorEquations colors)
	{
		super(width, height, BufferedImage.TYPE_INT_ARGB);
		this.draw(xMin, xMax, yMin, yMax, colors);
	}

	/**
	 * creates an Image object that is of the dimensions <code>width</code> by <code>height</code>
	 * @param width
	 * @param height
	 * @param colors - the functions that define the colors
	 */
	public EquationImage(int width, int height, ColorEquations colors) {
		this(width, height, STD_XMIN, STD_XMAX, STD_YMIN, STD_YMAX, colors);
	}

	public static EquationImage load(File file) throws IOException {
		BufferedImage img = ImageIO.read(file);
		if( img == null )
			throw new NullImageException();
		return new EquationImage(img);
	}

	public static EquationImage load(String filename) throws IOException {
		return load( new File(filename) );
	}


	public void draw(double xMin, double xMax, double yMin, double yMax, ColorEquations colors) {
		for( int y = 0; y < this.getHeight(); y++ ) {
			for( int x = 0; x < this.getWidth(); x++ ) {
				double _x = (xMax - xMin) * x / this.getWidth() + xMin,
					_y = (yMin - yMax) * (y + 1) / this.getHeight() + yMax;
				colors.initCalc(_x, _y);
				int a = fix( colors.alpha(_x, _y) ),
					r = fix( colors.red(_x, _y) ),
					g = fix( colors.green(_x, _y) ),
					b = fix( colors.blue(_x, _y) );
				this.setRGB( x, y, 0x1000000 * a + 0x10000 * r + 0x100 * g + b );
			}
		}
	}


	public void draw(ColorEquations colors) {
		this.draw(STD_XMIN, STD_XMAX, STD_YMIN, STD_YMAX, colors);
	}

	public EquationImage copy() {
		EquationImage r = new EquationImage( this.getWidth(), this.getHeight() );
		for( int y = 0; y < this.getHeight(); y++ )
			for( int x = 0; x < this.getWidth(); x++ )
				r.setRGB( x, y, this.getRGB(x, y) );
		return r;
	}

	public EquationImage tint(int red, int green, int blue, int alpha) {
		EquationImage ri = new EquationImage( this.getWidth(), this.getHeight() );
		for( int y = 0; y < this.getHeight(); y++ ) {
			for( int x = 0; x < this.getWidth(); x++ ) {
				int color = this.getRGB(x, y),
				a = inRange( ((color >> 24) & 0xff) + alpha ) << 24,
				r = inRange( ((color >> 16) & 0xff) + red ) << 16,
				g = inRange( ((color >> 8) & 0xff) + green ) << 8,
				b = inRange( (color & 0xff) + blue );
				ri.setRGB( x, y, a + r + g +  b );
			}
		}
		return ri;
	}

	public EquationImage tint(int red, int green, int blue) {
		return tint(red, green, blue, 0);
	}

	public EquationImage invert() {
		EquationImage r = new EquationImage( this.getWidth(), this.getHeight() );
		for( int y = 0; y < this.getHeight(); y++ ) {
			for( int x = 0; x < this.getWidth(); x++ ) {
				int alpha = 0xff000000 & this.getRGB(x, y),
					color = 0xffffff & this.getRGB(x, y);
				r.setRGB(x, y, alpha + 0xffffff - color);
			}
		}
		return r;
	}

	public EquationImage invertAlpha() {
		EquationImage r = new EquationImage( this.getWidth(), this.getHeight() );
		for( int y = 0; y < this.getHeight(); y++ ) {
			for( int x = 0; x < this.getWidth(); x++ ) {
				int alpha = 0xff000000 & this.getRGB(x, y),
					color = 0xffffff & this.getRGB(x, y);
				r.setRGB(x, y, 0xff000000 - alpha + color);
			}
		}
		return r;
	}

	public EquationImage flipHorizontal() {
		EquationImage r = new EquationImage( this.getWidth(), this.getHeight() );
		for( int y = 0; y < this.getHeight(); y++ ) {
			for( int x = 0; x < this.getWidth(); x++ ) {
				r.setRGB( x, y, this.getRGB( this.getWidth() - 1 - x, y ) );
			}
		}
		return r;
	}

	public EquationImage flipVertical() {
		EquationImage r = new EquationImage( this.getWidth(), this.getHeight() );
		for( int y = 0; y < this.getHeight(); y++ ) {
			for( int x = 0; x < this.getWidth(); x++ ) {
				r.setRGB( x, y, this.getRGB( x, this.getHeight() - 1 - y ) );
			}
		}
		return r;
	}

	/**
	 * Stretches the image to have dimensions: <code>width</code> by <code>height</code>
	 * @param width - new width
	 * @param height - new height
	 * @return a stretched image
	 */
	public EquationImage stretch(int width, int height) {
		EquationImage r = new EquationImage(width, height);
		for( int y = 0; y < r.getHeight(); y++ )
			for( int x = 0; x < r.getWidth(); x++ )
				r.setRGB( x, y,
					this.getRGB( this.getWidth() * x / r.getWidth(),this.getHeight() * y / r.getHeight() )
				);
		return r;
	}

	/**
	 * Stretches the image to have dimensions:
	 * (current width * <code>horizontal</code>) by
	 * (current height * <code>vertical</code>)
	 * @param horizontal
	 * @param vertical
	 * @return
	 */
	public EquationImage stretchByRatio(double horizontal, double vertical) {
		return stretch(
			(int)Math.round( horizontal * this.getWidth() ),
			(int)Math.round( vertical * this.getHeight() )
		);
	}

	public EquationImage shift(int right, int down) {
		right %= this.getWidth();
		down %= this.getHeight();
		EquationImage r = new EquationImage( this.getWidth(), this.getHeight() );
		for( int x = 0; x < r.getWidth(); x++ ) {
			for( int y = 0; y < r.getHeight(); y++ ) {
				int _x = ((x - right) + this.getHeight()) % this.getWidth(),
					_y = ((y - down) + this.getHeight()) % this.getHeight();
				r.setRGB( x, y, this.getRGB(_x , _y) );
			}
		}
		return r;
	}

	public EquationImage transpose() {
		EquationImage r = new EquationImage( this.getHeight(), this.getWidth() );
		for( int y = 0; y < r.getHeight(); y++  )
			for( int x = 0; x < r.getWidth(); x++ )
				r.setRGB( x, y, this.getRGB(y, x) );
		return r;
	}

	/**
	 * @see #rotateC()
	 * @return an image rotated 90 degrees counter-clockwise
	 */
	public EquationImage rotate() {
		return this.transpose().flipVertical();
	}

	/**
	 * @see #rotate()
	 * @return an image rotated 90 degrees clockwise
	 */
	public EquationImage rotateC() {
		return this.transpose().flipHorizontal();
	}

	/**
	 * for rotations about a multiple of 90 degrees use {@link #rotate()} or {@link #rotateCC()}
	 * for faster performance
	 * @see #rotate()
	 * @see #rotateCC()
	 * @param angle - (in degrees)
	 * @param fill - an equation to use to fill the default pixels
	 * @return an image rotated <code>angle</code> degrees
	 */
	public EquationImage rotate(double angle, ColorEquations fill) {
		int maxX = Integer.MIN_VALUE, minX = Integer.MAX_VALUE,
			maxY = Integer.MIN_VALUE, minY = Integer.MAX_VALUE;
		int[][] nX = new int[this.getWidth()][this.getHeight()],
			nY = new int[this.getWidth()][this.getHeight()];
		for( int y = 0; y < this.getHeight(); y++ ) {
			for( int x = 0; x < this.getWidth(); x++ ) {
				double theta = Math.atan2(y, x),
					r = Math.sqrt( x*x + y*y ),
					_angle = -Math.PI * angle / 180;
				nX[x][y] = (int)Math.round( r * Math.cos(theta + _angle) );
				nY[x][y] = (int)Math.round( r * Math.sin(theta + _angle) );
				if( nX[x][y] > maxX )
					maxX = nX[x][y];
				if( nX[x][y] < minX )
					minX = nX[x][y];
				if( nY[x][y] > maxY )
					maxY = nY[x][y];
				if( nY[x][y] < minY )
					minY = nY[x][y];
			}
		}
		EquationImage ri = new EquationImage( maxX - minX + 1, maxY - minY + 1, fill );
		for( int x = 0; x < nX.length; x++ ) {
			for( int y = 0; y < nY[0].length; y++ ) {
				nX[x][y] -= minX;
				nY[x][y] -= minY;
				ri.setRGB( nX[x][y], nY[x][y], getRGB(x, y) );
			}
		}
		return ri;
	}

	/**
	 * For rotations about a multiple of 90 degrees use {@link #rotate()} or {@link #rotateCC()} <br />
	 * Calls {@link #rotate(double, ColorEquations)} filling the default pixels with "see-though" black (ARGB=0)
	 * @see #rotate()
	 * @see #rotateCC()
	 * @see #rotate(double, ColorEquations)
	 * @param angle - (in degrees)
	 * @return an image rotated <code>angle</code> degrees
	 */
	public EquationImage rotate(double angle) {
		return rotate( angle, new ColorEquations() {
			public void initCalc(double x, double y) {}
			public double alpha(double x, double y) { return 0; }
			public double blue(double x, double y) { return 0; }
			public double green(double x, double y) { return 0; }
			public double red(double x, double y) { return 0; }
		} );
	}

	/**
	 * @param h
	 * @param v
	 * @return
	 */
	public EquationImage blur(int h, int v) {
		EquationImage ri = new EquationImage( this.getWidth() - h, this.getHeight() - v );
		for( int y = v; y < ri.getHeight() - v; y++ ) {
			for( int x = h; x < ri.getWidth() - h; x++ ) {
				double a = 0, r = 0, g = 0, b = 0;
				for( int _x = -h; _x <= h; _x++ ) {
					for( int _y = -v; _y <= v; _y++ ) {
						int c = this.getRGB(x + _x, y + _y);
						a += ((c & 0xff000000) >> 24) / (double)((2*h + 1)*(2*v + 1));
						r += ((c & 0xff0000) >> 16) / (double)((2*h + 1)*(2*v + 1));
						g += ((c & 0xff00) >> 8) / (double)((2*v + 1)*(2*h + 1));
						b += (c & 0xff) / (double)((2*h + 1)*(2*v + 1));
					}
				}
				ri.setRGB( x, y,
					( (int)Math.round(a) * 0x1000000 )
					+ ( (int)Math.round(r) * 0x10000 )
					+ ( (int)Math.round(g) * 0x100 )
					+ ( (int)Math.round(b) )
				);
			}
		}
		return ri;
	}

	public EquationImage blur(int d) {
		return blur(d, d);
	}

	/**
	 * Saves the image as a PNG
	 * @param filename
	 * @throws IOException
	 */
	public void overwrite(File file) throws IOException {
		ImageIO.write( this, "png", file );
	}

	public void overwrite(String filename) throws IOException {
		overwrite( new File(filename) );
	}

	public boolean save(File saveFile) throws IOException {
		if( saveFile.isFile() )
			return false;
		overwrite( saveFile );
		return true;
	}

	public boolean save(String filename) throws IOException {
		return save( new File(filename) );
	}
}
