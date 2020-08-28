package htf.mandelbrot.colors;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Henning Thomas Flath
 */
public class ColorMap {

	//Farbwerte
	private Color[] colors;

	//Konstruktor
	public ColorMap(Color[] colors) {
		this.colors = colors;
	}

	//Farbkarte mit dem angebenen Namen aus dem Speicher laden.
	public ColorMap(String name) {
		colors = new Color[256];
		File file = new File(""+name);
		try {
			int counter = 0;
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine() && counter < colors.length) {
				String colorString = scanner.nextLine();
				String[] colorValues = colorString.split(" ");
				colors[counter] = new Color(parseColorValue(colorValues[0]), parseColorValue(colorValues[1]), parseColorValue(colorValues[2]));
				counter++;
		 	}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	//String in Farbwert umwandeln.
	private float parseColorValue(String colorValue) {
		return Integer.parseInt(colorValue) / 255.0f;
	}

	//Farbe an einem Index zurückgeben.
	public Color getColor(double index) {
		try {
			final double value = ((Math.sin(index) + 1) / 2) * (colors.length - 1);
			return colors[(int) value];
		} catch(Exception e) {
			return colors[0];
		}
	}
}