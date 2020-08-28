package htf.mandelbrot.main;

import javax.swing.*;

/**
 * @author Henning Thomas Flath
 */
public class SpinnerHalfDoubleNumberModel extends SpinnerNumberModel {

    //Dieses Spinner-Modell halbiert bzw. verdoppelt die Werte immer.

    private double minValue, maxValue;

    public SpinnerHalfDoubleNumberModel(double startValue, double minValue, double maxValue) {
        super();
        this.minValue = minValue;
        this.maxValue = maxValue;
        setValue(startValue);
    }

    @Override
    public Object getNextValue() {
        double newValue = (double) getValue() * 2;
        if(newValue > maxValue) {
            return maxValue;
        }
        return newValue;
    }

    @Override
    public Object getPreviousValue() {
        double newValue = (double) getValue() / 2;
        if(newValue < minValue) {
            return minValue;
        }
        return newValue;
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);
        if((double) value > maxValue) {
            setValue(maxValue);
        } else if((double) value < minValue) {
            setValue(minValue);
        }
    }
}
