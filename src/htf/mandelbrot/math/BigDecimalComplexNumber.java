package htf.mandelbrot.math;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Henning Thomas Flath
 */
public class BigDecimalComplexNumber {

	//Implementation von komplexen Zahlen mithilfe, wenn BigDecimal genutzt wird. Ich habe nur die nötigen Methoden implementiert.

	private BigDecimal reel, imaginary;

	public BigDecimalComplexNumber(BigDecimal reel, BigDecimal imaginary) {
		this.reel = reel;
		this.imaginary = imaginary;
	}
	
	public BigDecimal getAbsoluteValue(MathContext mathContext) {
		return reel.pow(2).add(imaginary.pow(2)).sqrt(mathContext);
	}
	
	public BigDecimal distanceTo(BigDecimalComplexNumber complexNumber, MathContext mathContext) {
		return complexNumber.reel.subtract(this.reel, mathContext).pow(2, mathContext)
		.add(complexNumber.imaginary.subtract(this.imaginary, mathContext).pow(2, mathContext), mathContext).sqrt(mathContext);
	}
	
	public BigDecimalComplexNumber add(BigDecimalComplexNumber complexNumber, MathContext mathContext) {
		return new BigDecimalComplexNumber(reel.add(complexNumber.reel, mathContext), imaginary.add(complexNumber.imaginary, mathContext));
	}
	
	public BigDecimalComplexNumber square(MathContext mathContext) {
		return new BigDecimalComplexNumber(reel.pow(2, mathContext).subtract(imaginary.pow(2, mathContext), mathContext),
		reel.multiply(imaginary, mathContext).multiply(new BigDecimal("2"), mathContext));
	}

	//Beschleunigte Methode, falls das Quadrat des Realteils / Imaginärteils schon bekannt ist.
	public BigDecimalComplexNumber square(MathContext mathContext, BigDecimal reelSquare, BigDecimal imaginarySquare) {
		return new BigDecimalComplexNumber(reelSquare.subtract(imaginarySquare, mathContext),
		reel.multiply(imaginary, mathContext).multiply(new BigDecimal("2"), mathContext));
	}

	//Get- und Set-Methoden

	public BigDecimal getReel() {
		return reel;
	}

	public void setReel(BigDecimal reel) {
		this.reel = reel;
	}

	public BigDecimal getImaginary() {
		return imaginary;
	}

	public void setImaginary(BigDecimal imaginary) {
		this.imaginary = imaginary;
	}

}
