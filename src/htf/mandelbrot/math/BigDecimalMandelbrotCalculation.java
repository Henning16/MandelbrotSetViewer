package htf.mandelbrot.math;

import java.math.BigDecimal;

public class BigDecimalMandelbrotCalculation implements Runnable {

    private Model model;

    public BigDecimalMandelbrotCalculation(Model model) {
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
            BigDecimal reel = (BigDecimal.valueOf(x).divide(BigDecimal.valueOf(model.getWidth()), model.getMathContext())).multiply(new BigDecimal("4")).subtract(new BigDecimal("2"))
                    .multiply(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(model.getZoomFactor()), model.getMathContext()))
                    .add(BigDecimal.valueOf(model.getPosition().getReel()));
            nextNumber:
            for(int y = offsetY; y < model.getHeight(); y += 2) {
                BigDecimal imaginary = (BigDecimal.valueOf(y).divide(BigDecimal.valueOf(model.getHeight()), model.getMathContext())).multiply(new BigDecimal("4")).subtract(new BigDecimal("2"))
                        .multiply(new BigDecimal("1").divide(BigDecimal.valueOf(model.getZoomFactor()), model.getMathContext())).multiply(new BigDecimal("-1"))
                        .add(BigDecimal.valueOf(model.getPosition().getImaginary()));
                BigDecimalComplexNumber c = new BigDecimalComplexNumber(reel, imaginary);
                BigDecimalComplexNumber z = new BigDecimalComplexNumber(reel, imaginary);
                BigDecimal r2 = z.getReel().pow(2, model.getMathContext());
                BigDecimal i2 = z.getImaginary().pow(2, model.getMathContext());
                for(int k = 0; k < model.getIterations(); k++) {
                    if(r2.add(i2, model.getMathContext()).compareTo(BigDecimal.valueOf(threshold)) >= 0) {
                        if(model.usesSmoothColoring()) {
                            model.getComplexSet()[x][y].setIterationValue(k - Math.log(Math.log(r2.doubleValue() + i2.doubleValue()) / Math.log(2)) / Math.log(2) - 256);
                        } else {
                            model.getComplexSet()[x][y].setIterationValue(k);
                        }
                        continue nextNumber;
                    }
                    z = z.square(model.getMathContext(), r2, i2).add(c, model.getMathContext());
                    r2 = z.getReel().pow(2, model.getMathContext());
                    i2 = z.getImaginary().pow(2, model.getMathContext());
                }
                model.getComplexSet()[x][y].setIterationValue(-1);
            }
        }
    }
}
