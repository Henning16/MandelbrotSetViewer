package htf.mandelbrot.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import htf.mandelbrot.colors.ColorMap;

/**
 * @author Henning Thomas Flath
 */
public class Model {

	public enum Type  {
		JULIA, MANDELBROT;
	}

	//Array zum Speichern der Werte
	private Pixel[][] complexSet;

	//Berechnungsdauer
	private long calculationDuration = 0;
	//Momentaner Thread
	private Thread thread;
	//Start-Zeitpunkt
	private long start = 0;
	//Größe
	private int width = 10000, height = 10000;
	//Auflösungsfaktor
	private double resolutionScale = 1;
	//Zoom
	private double zoomFactor = 1;
	//Iterationen
	private int iterations = 1;
	//MathContext (Rundungsverhalten) für das Rechnen mit BigDecimal.
	private MathContext mathContext = new MathContext(8, RoundingMode.HALF_EVEN);
	//Position in der komplexen Zahlenebene
	private ComplexNumber position = new ComplexNumber(0,0);
	//Farbausbreitung
	private float colorSpread = 0.1f;
	//Farbglättung
	private boolean smoothColoring = false;
	//Farbkarte
	private ColorMap colorMap = new ColorMap("blueorange");
	//Typ
	private Type type = Type.MANDELBROT;
	//Zeichnen
	private boolean isDrawing = true;

	//Konstruktor
	public Model() {
		complexSet =  new Pixel[getWidth()][getHeight()];
	}

	//Mandelbrot-Menge mit den entsprechenden Algorithmen berechnen.
	public void calculate() {
		start = System.currentTimeMillis();
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				complexSet[x][y] = new Pixel();
			}
		}
		switch(type) {
			case JULIA:
				thread = new Thread(new QuickJuliaCalculation(this));
				thread.start();
				break;
			case MANDELBROT:
				if(getZoomFactor() < 1e13) {
					thread = new Thread(new QuickMandelbrotCalculation(this));
					thread.start();
				} else {
					thread = new Thread(new BigDecimalMandelbrotCalculation(this));
					thread.start();
				}
				break;
		}
	}

	public void calculationFinished() {
		calculationDuration = System.currentTimeMillis() - start;
	}


	
	//Der langsamere, aber genauere Algorithmus.
	/**public void calculateMandelbrotSetUsingBigDecimal() {
		int threshold = 4;
		if(smoothColoring) {
			threshold = 256;
		}
		for(int x = 0; x < getWidth(); x++) {
			BigDecimal reel = (BigDecimal.valueOf(x).divide(BigDecimal.valueOf(getWidth()), getMathContext())).multiply(new BigDecimal("4")).subtract(new BigDecimal("2"))
			.multiply(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(getZoomFactor()), getMathContext()))
			.add(BigDecimal.valueOf(getPosition().getReel()));
			nextNumber:
			for(int y = 0; y < getHeight(); y++) {
				BigDecimal imaginary = (BigDecimal.valueOf(y).divide(BigDecimal.valueOf(getHeight()), getMathContext())).multiply(new BigDecimal("4")).subtract(new BigDecimal("2"))
				.multiply(new BigDecimal("1").divide(BigDecimal.valueOf(getZoomFactor()), getMathContext())).multiply(new BigDecimal("-1"))
				.add(BigDecimal.valueOf(getPosition().getImaginary()));
				BigDecimalComplexNumber c = new BigDecimalComplexNumber(reel, imaginary);
				BigDecimalComplexNumber z = new BigDecimalComplexNumber(reel, imaginary);
				BigDecimal r2 = z.getReel().pow(2, getMathContext());
				BigDecimal i2 = z.getImaginary().pow(2, getMathContext());
				for(int k = 0; k < getIterations(); k++) {
					if(r2.add(i2, getMathContext()).compareTo(BigDecimal.valueOf(threshold)) >= 0) {
						if(smoothColoring) {
							complexSet[x][y] = k - Math.log(Math.log(r2.doubleValue() + i2.doubleValue()) / Math.log(2)) / Math.log(2) - 256;
						} else {
							complexSet[x][y] = k;
						}
						continue nextNumber;
					}
					z = z.square(getMathContext(), r2, i2).add(c, getMathContext());
					r2 = z.getReel().pow(2, getMathContext());
					i2 = z.getImaginary().pow(2, getMathContext());
				}
				complexSet[x][y] = -1;
			}
		}
	}**/

	//Get- und Set-Methoden


	public void setComplexSet(Pixel[][] complexSet) {
		this.complexSet = complexSet;
	}

	public Pixel[][] getComplexSet() {
		return complexSet;
	}

	public boolean isPartOfMandelbrotSet(int x, int y) {
		try {
			return complexSet[x][y].getIterationValue() == -1;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	public double getValue(int x, int y) {
		try {
			return complexSet[x][y].getIterationValue();
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
			return 1;
		}
	}

	public long getCalculationDuration() {
		return calculationDuration;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		complexSet = new Pixel[getWidth()][getHeight()];
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		complexSet = new Pixel[getWidth()][getHeight()];
	}

	public double getResolutionScale() {
		return resolutionScale;
	}

	public void setResolutionScale(double resolutionScale) {
		setWidth((int) ((resolutionScale / this.resolutionScale) * getWidth()));
		setHeight((int) ((resolutionScale / this.resolutionScale) * getHeight()));
		this.resolutionScale = resolutionScale;
	}

	public double getZoomFactor() {
		return zoomFactor;
	}

	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public float getColorSpread() {
		return colorSpread;
	}

	public void setColorSpread(float colorSpread) {
		this.colorSpread = colorSpread;
	}

	public MathContext getMathContext() {
		return mathContext;
	}

	public void setMathContext(MathContext mathContext) {
		this.mathContext = mathContext;
	}

	public ColorMap getColorMap() {
		return colorMap;
	}

	public void setColorMap(ColorMap colorMap) {
		this.colorMap = colorMap;
	}

	public ComplexNumber getPosition() {
		return position;
	}

	public void setPosition(ComplexNumber position) {
		this.position = position;
	}

	public boolean usesSmoothColoring() {
		return smoothColoring;
	}

	public void setSmoothColoring(boolean smoothColoring) {
		this.smoothColoring = smoothColoring;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Thread getThread() {
		return thread;
	}

	public boolean isDrawing() {
		return isDrawing;
	}

	public void setDrawing(boolean drawing) {
		isDrawing = drawing;
	}
}
