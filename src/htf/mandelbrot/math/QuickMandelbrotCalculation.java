package htf.mandelbrot.math;

public class QuickMandelbrotCalculation implements Runnable {

    private final Model model;

    public QuickMandelbrotCalculation(Model model) {
        this.model = model;
    }

    @Override
    public void run() {
        calculate(0, 0);
        calculate(0, 1);
        calculate(1, 0);
        calculate(1, 1);
        model.calculationFinished();
    }

    public void calculate(int offsetX, int offsetY) {
        int threshold = 4;
        if(model.usesSmoothColoring()) {
            threshold = 256;
        }
        for(int x = offsetX; x < model.getWidth(); x += 2) {
            double reel = (((x * 1.0) / model.getWidth()) * 4.0 - 2.0) * (1.0 / model.getZoomFactor()) + model.getPosition().getReel();
            nextNumber:
            for(int y = offsetY; y < model.getHeight(); y += 2) {
                if(model.getComplexSet()[x][y].getCalculationState() == Pixel.CalculationState.CALCULATED) {
                    continue nextNumber;
                }
                double imaginary = - (((y * 1.0) / model.getHeight()) * 4.0 - 2.0) * (1.0 / model.getZoomFactor()) + model.getPosition().getImaginary();
                ComplexNumber c = new ComplexNumber(reel, imaginary);
                ComplexNumber z = new ComplexNumber(reel, imaginary);
                double r2 = z.getReel() * z.getReel();
                double i2 = z.getImaginary() * z.getImaginary();
                for(int k = 0; k < model.getIterations(); k++) {
                    if(r2 + i2 >= threshold) {
                        if(model.usesSmoothColoring()) {
                            model.getComplexSet()[x][y].setIterationValue(k - Math.log(Math.log(r2+i2) / Math.log(2)) / Math.log(2) - 256);
                        } else {
                            model.getComplexSet()[x][y].setIterationValue(k);
                        }
                        model.getComplexSet()[x][y].setCalculationState(Pixel.CalculationState.CALCULATED);
                        continue nextNumber;
                    }
                    z = z.square(r2, i2).add(c);
                    r2 = z.getReel() * z.getReel();
                    i2 = z.getImaginary() * z.getImaginary();
                }
                model.getComplexSet()[x][y].setIterationValue(-1);
                model.getComplexSet()[x][y].setCalculationState(Pixel.CalculationState.CALCULATED);
                addToSetBySurroundings(x + 1, y);
                addToSetBySurroundings(x, y + 1);
            }
        }
    }

    private void addToSetBySurroundings(int x, int y) {
        boolean surroundingsBelongToSet;
        try {
            surroundingsBelongToSet =
                    model.getComplexSet()[x][y - 1].getIterationValue() == -1 &&
                    model.getComplexSet()[x - 1][y].getIterationValue() == -1 &&
                    model.getComplexSet()[x + 1][y].getIterationValue() == -1 &&
                    model.getComplexSet()[x][y + 1].getIterationValue() == -1;
        } catch(IndexOutOfBoundsException e) {
            return;
        }
        if(surroundingsBelongToSet) {
            model.getComplexSet()[x][y].setIterationValue(-1);
        }
    }
}
