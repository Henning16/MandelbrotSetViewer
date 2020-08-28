package htf.mandelbrot.main;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;

import javax.swing.*;

/**
 * @author Henning Thomas Flath
 */
public class Launcher {

	//Startet das Programm.
	public static void main(String[] args) {
		MandelbrotFrame frame = new MandelbrotFrame();
		frame.start();
	}

}