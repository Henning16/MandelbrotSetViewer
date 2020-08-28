package htf.mandelbrot.math;

public class Pixel {

    public enum CalculationState {
        NOT_CALCULATED, CALCULATED;
    }

    private double iterationValue = 0;
    private CalculationState calculationState = CalculationState.NOT_CALCULATED;

    public Pixel() {

    }

    public double getIterationValue() {
        return iterationValue;
    }

    public void setIterationValue(double iterationValue) {
        this.iterationValue = iterationValue;
    }

    public CalculationState getCalculationState() {
        return calculationState;
    }

    public void setCalculationState(CalculationState calculationState) {
        this.calculationState = calculationState;
    }
}
