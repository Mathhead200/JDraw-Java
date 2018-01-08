package com.mathhead200.image;


public interface ColorEquations
{
	/**
	 * Initial Calculations always be called before {@link #red(double, double)}, {@link #green(double, double)},
	 * {@link #blue(double, double)}, or {@link #alpha(double, double)}.
	 * @param x - The same <code>x</code> passed to the other four methods
	 * @param y - The same <code>y</code> passed to the other four methods
	 */
	public void initCalc(double x, double y);

	/**
	 * @param x on [xMin = -100, xMax = 100)
	 * @param y on [yMin = -100, yMax = 100)
	 * @see ImageTools#makeImage(int, int, double xMin, double xMax, double yMin, double yMax, ColorEquations)
	 * @return the percentage of red on [0, 100]
	 */
	public double red(double x, double y);

	/**
	 * @param x on [xMin = -100, xMax = 100)
	 * @param y on [yMin = -100, yMax = 100)
	 * @see ImageTools#makeImage(int, int, double xMin, double xMax, double yMin, double yMax, ColorEquations)
	 * @return the percentage of green on [0, 100]
	 */
	public double green(double x, double y);

	/**
	 * @param x on [xMin = -100, xMax = 100)
	 * @param y on [yMin = -100, yMax = 100)
	 * @see ImageTools#makeImage(int, int, double xMin, double xMax, double yMin, double yMax, ColorEquations)
	 * @return the percentage of blue on [0, 100]
	 */
	public double blue(double x, double y);

	/**
	 * @param x on [xMin = -100, xMax = 100)
	 * @param y on [yMin = -100, yMax = 100)
	 * @see ImageTools#makeImage(int, int, double xMin, double xMax, double yMin, double yMax, ColorEquations)
	 * @return the percentage of opacity on [0, 100]
	 * 	0 is see through, 100 is solid
	 */
	public double alpha(double x, double y);
}
