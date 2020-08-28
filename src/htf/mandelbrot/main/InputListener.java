package htf.mandelbrot.main;

import javax.swing.event.MouseInputListener;
import java.awt.event.*;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @author Henning Thomas Flath
 */
public class InputListener implements KeyListener, MouseInputListener, MouseWheelListener {
	
	private MandelbrotFrame frame;
	
	public InputListener(MandelbrotFrame frame) {
		this.frame = frame;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
	}

	//Beim Loslassen einer Taste verschiedene Aktionen ausführen.
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			frame.getModel().getPosition().setReel(frame.getModel().getPosition().getReel() - 1 / frame.getModel().getZoomFactor());
			frame.getModel().calculate();
		} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			frame.getModel().getPosition().setReel(frame.getModel().getPosition().getReel() + 1 / frame.getModel().getZoomFactor());
			frame.getModel().calculate();
		} else if(e.getKeyCode() == KeyEvent.VK_UP) {
			frame.getModel().getPosition().setImaginary(frame.getModel().getPosition().getImaginary() + 1 / frame.getModel().getZoomFactor());
			frame.getModel().calculate();
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			frame.getModel().getPosition().setImaginary(frame.getModel().getPosition().getImaginary() - 1 / frame.getModel().getZoomFactor());
			frame.getModel().calculate();
		} else if(e.getKeyCode() == KeyEvent.VK_PLUS) {
			frame.getModel().setZoomFactor(frame.getModel().getZoomFactor() * 2);
			frame.getModel().calculate();
		} else if(e.getKeyCode() == KeyEvent.VK_MINUS) {
			frame.getModel().setZoomFactor(frame.getModel().getZoomFactor() / 2);
			frame.getModel().calculate();
		} else if(e.getKeyCode() == KeyEvent.VK_P) {
			frame.getModel().setMathContext(new MathContext(frame.getModel().getMathContext().getPrecision() + 1, RoundingMode.HALF_EVEN));
			frame.getModel().calculate();
		} else if(e.getKeyCode() == KeyEvent.VK_L) {
			frame.getModel().setMathContext(new MathContext(frame.getModel().getMathContext().getPrecision() - 1, RoundingMode.HALF_EVEN));
			frame.getModel().calculate();
		} else if(e.getKeyCode() == KeyEvent.VK_0) {
			frame.getModel().setResolutionScale(0.125f);
			frame.getModel().calculate();
		} else if(e.getKeyCode() == KeyEvent.VK_1) {
			frame.getModel().setResolutionScale(0.25f);
			frame.getModel().calculate();
		} else if(e.getKeyCode() == KeyEvent.VK_2) {
			frame.getModel().setResolutionScale(0.5f);
			frame.getModel().calculate();
		} else if(e.getKeyCode() == KeyEvent.VK_3) {
			frame.getModel().setResolutionScale(1);
			frame.getModel().calculate();
		} else if(e.getKeyCode() == KeyEvent.VK_4) {
			frame.getModel().setResolutionScale(2);
			frame.getModel().calculate();
		}  else if(e.getKeyCode() == KeyEvent.VK_5) {
			frame.getModel().setResolutionScale(3);
			frame.getModel().calculate();
		} else if(e.getKeyCode() == KeyEvent.VK_D) {
			frame.getModel().setDrawing(!frame.getModel().isDrawing());
		}
		frame.getSidebar().updateValues();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		frame.getModel().getPosition().setReel((((e.getX() * 1.0) / frame.getModel().getWidth() * frame.getModel().getResolutionScale()) * 4.0 - 2.0) * (1.0 / frame.getModel().getZoomFactor()) + frame.getModel().getPosition().getReel());
		frame.getModel().getPosition().setImaginary(-(((e.getY() * 1.0) / frame.getModel().getHeight() * frame.getModel().getResolutionScale()) * 4.0 - 2.0) * (1.0 / frame.getModel().getZoomFactor()) + frame.getModel().getPosition().getImaginary());
		frame.getModel().calculate();
		frame.getSidebar().updateValues();
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		frame.getSidebar().updateMousePosition(
				((((e.getX() * 1.0) / frame.getModel().getWidth()) * 4.0 - 2.0) * (1.0 / frame.getModel().getZoomFactor()) + frame.getModel().getPosition().getReel()),
				-(((e.getY() * 1.0) / frame.getModel().getHeight()) * 4.0 - 2.0) * (1.0 / frame.getModel().getZoomFactor()) + frame.getModel().getPosition().getImaginary());
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int rotation = e.getWheelRotation();
		if(rotation < 0) {
			frame.getModel().setZoomFactor(frame.getModel().getZoomFactor() * 2);
		} else if(rotation > 0) {
			frame.getModel().setZoomFactor(frame.getModel().getZoomFactor() / 2);
		}
		frame.getModel().getPosition().setReel((((e.getX() * 1.0) / frame.getModel().getWidth() * frame.getModel().getResolutionScale()) * 4.0 - 2.0) * (1.0 / frame.getModel().getZoomFactor()) + frame.getModel().getPosition().getReel());
		frame.getModel().getPosition().setImaginary(-(((e.getY() * 1.0) / frame.getModel().getHeight() * frame.getModel().getResolutionScale()) * 4.0 - 2.0) * (1.0 / frame.getModel().getZoomFactor()) + frame.getModel().getPosition().getImaginary());
		frame.getModel().calculate();
		frame.getSidebar().updateValues();
	}
}
