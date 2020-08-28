package htf.mandelbrot.math;

/**
 * @author Henning Thomas Flath
 */
public class ComplexNumber {

	//Implementation von komplexen Zahlen. Ich habe nur die nötigen Methoden implementiert.

	private double reel, imaginary;

	public ComplexNumber(double reel, double imaginary) {
		this.reel = reel;
		this.imaginary = imaginary;
	}

	public double getAbsoluteValue() {
		return Math.sqrt(reel * reel + imaginary * imaginary);
	}
	
	public double distanceTo(ComplexNumber complexNumber) {
		return Math.sqrt((complexNumber.reel - this.reel)*(complexNumber.reel - this.reel) + (complexNumber.imaginary - this.imaginary)*(complexNumber.imaginary - this.imaginary));
	}
	
	public ComplexNumber add(ComplexNumber complexNumber) {
		return new ComplexNumber(this.reel + complexNumber.reel, this.imaginary + complexNumber.imaginary);
	}

	public ComplexNumber divideBy(ComplexNumber complexNumber) {
		return new ComplexNumber(
				(this.getReel() * complexNumber.getReel() + this.getImaginary() * complexNumber.getImaginary()) / (complexNumber.getReel() * complexNumber.getReel() + complexNumber.getImaginary() * complexNumber.getImaginary()),
				(this.getImaginary() * complexNumber.getReel() - (this.getReel() * complexNumber.getImaginary())) / (complexNumber.getReel() * complexNumber.getReel() + complexNumber.getImaginary() * complexNumber.getImaginary()));
	}

	public ComplexNumber square() {
		return new ComplexNumber(this.reel * this.reel - this.imaginary * this.imaginary, 2.0 * this.reel * this.imaginary);
	}

	//Beschleunigte Methode, falls das Quadrat des Realteils / Imaginärteils schon bekannt ist.
	public ComplexNumber square(double reelSquare, double imaginarySquare) {
		return new ComplexNumber(reelSquare - imaginarySquare, 2.0 * this.reel * this.imaginary);
	}

	//Beschleunigte Methode, falls das Quadrat des Realteils / Imaginärteils und das Produkt aus Real- und Imaginärteil schon bekannt sind.
	public ComplexNumber square(double reelSquare, double imaginarySquare, double reelTimesImaginary) {
		return new ComplexNumber(reelSquare - imaginarySquare, 2.0 * reelTimesImaginary);
	}

	//Get- und Set-Methoden

	public double getReel() {
		return reel;
	}

	public void setReel(double reel) {
		this.reel = reel;
	}

	public double getImaginary() {
		return imaginary;
	}

	public void setImaginary(double imaginary) {
		this.imaginary = imaginary;
	}
	
}