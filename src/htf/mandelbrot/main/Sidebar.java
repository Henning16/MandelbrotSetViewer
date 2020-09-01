package htf.mandelbrot.main;

import htf.mandelbrot.colors.ColorMap;
import htf.mandelbrot.math.Model;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Henning Thomas Flath
 */
public class Sidebar extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private boolean updatingValues = false;

	//Fenster
	private MandelbrotFrame frame;
	//Schriftart der Titel
	private Font titleFont = new Font("Arial", Font.BOLD, 16);

	//Info-Titel
	private JLabel infoTitle;

	private JLabel calculationDuration;
	private JLabel mousePosition;

	//Optionen-Titel
	private JLabel optionsTitle;

	//Über-Button
	private JButton about;
	//Neu-Laden-Button
	private JButton reload;

	//Zoom-ÜBerschrift
	private JLabel zoomHeader;
	//Zoom-Feld
	private JSpinner zoomSpinner;

	//Realteil-Überschrift
	private JLabel reel;
	//Imaginärteil-Überschrift
	private JLabel imaginary;
	//Realteil-Feld
	private JTextField reelInput;
	//Imaginärteil-Feld
	private JTextField imaginaryInput;
	//Information, dass die Pfeiltasten auch benutzt werden können.
	private JLabel movingInfo;

	//Iterationen-Überschirft
	private JLabel iterations;
	//Iterationen-Feld
	private JSpinner iterationSpinner;

	//Farbkarten-Überschrift
	private JLabel colorMap;
	//Farbkarten-Auswahl
	private JComboBox<String> colorMapDropDown;
	//Farbausbreitungs-Überschrift
	private JLabel colorFactor;
	//Farbausbreitungs-Feld
	private JSpinner colorSpinner;
	//Weiche-Farben-Überschrift
	private JLabel useSmoothColoring;
	//Weiche-Farben-Haken
	private JCheckBox smoothColoringCheckbox;

	//Julia
	private JButton switchBetweenMandelbrotAndJuliaSetButton;

	//Bild-ausgeben-Button
	private JButton output;

	//Konstruktor
	public Sidebar(MandelbrotFrame frame) {
		this.frame = frame;
		constructUI();
	}

	private void constructUI() {
		//Setup
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
		
		//Info
		infoTitle = new JLabel("Info");
		infoTitle.setFont(titleFont);
		add(infoTitle);
		add(Box.createRigidArea(new Dimension(0, 5)));

		//Über-Button mit Anzeige.
		about = new JButton("About");
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Sidebar.this.frame, "This program was developed by Henning Flath for the math paper \"Visualisierung der Mandelbrot-Menge\".\n\nThe color maps were generated using the website https://jdherman.github.io/colormap/.", "About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		add(about);
		add(Box.createRigidArea(new Dimension(0, 10)));

		//Zeit
		calculationDuration = new JLabel("Calculation duration: 0 ms");
		add(calculationDuration);
		//Maus-Position
		mousePosition = new JLabel("Cursor position: 0, 0");
		add(mousePosition);
		add(Box.createRigidArea(new Dimension(0, 25)));
		
		//Optionen
		optionsTitle = new JLabel("Options");
		optionsTitle.setFont(titleFont);
		add(optionsTitle);
		add(Box.createRigidArea(new Dimension(0, 5)));
		//Neu laden
		reload = new JButton("Reload");
		reload.setPreferredSize(new Dimension(reload.getPreferredSize().width, 26));
		reload.setMaximumSize(new Dimension(reload.getPreferredSize().width, 26));
		reload.setMinimumSize(new Dimension(reload.getPreferredSize().width, 26));
		reload.addActionListener((ActionEvent e) -> {
			frame.getModel().calculate();
		});
		add(reload);
		add(Box.createRigidArea(new Dimension(0, 10)));

		//Zoom-Eingabe
		zoomHeader = new JLabel("Zoom");
		add(zoomHeader);
		SpinnerModel zoomModel = new SpinnerHalfDoubleNumberModel(0.5, 0.001, 1e25);
		zoomSpinner = new JSpinner(zoomModel);
		zoomSpinner.setMaximumSize(new Dimension(240, 25));
		zoomSpinner.setAlignmentX(JSpinner.LEFT_ALIGNMENT);
		zoomSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(updatingValues) return;
				frame.getModel().setZoomFactor((double) zoomSpinner.getValue());
				frame.getModel().calculate();
			}
		});
		add(zoomSpinner);
		add(Box.createRigidArea(new Dimension(0, 25)));

		//Positions-Eingabe
		reel = new JLabel("Reel part of c");
		add(reel);
		reelInput = new JTextField("0.0");
		reelInput.setMinimumSize(new Dimension(240, 25));
		reelInput.setMaximumSize(new Dimension(240, 25));
		reelInput.setAlignmentX(JTextField.LEFT_ALIGNMENT);
		reelInput.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(updatingValues) return;
				parse();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if(updatingValues) return;
				parse();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if(updatingValues) return;
				parse();
			}

			private void parse() {
				double reel;
				try {
					reel = Double.parseDouble(reelInput.getText());
				} catch(NumberFormatException e) {
					return;
				}
				frame.getModel().getPosition().setReel(reel);
				frame.getModel().calculate();
			}
		});
		add(reelInput);
		add(Box.createRigidArea(new Dimension(0, 5)));
		imaginary = new JLabel("Imaginary part of c");
		add(imaginary);
		imaginaryInput = new JTextField("0.0");
		imaginaryInput.setMinimumSize(new Dimension(240, 25));
		imaginaryInput.setMaximumSize(new Dimension(240, 25));
		imaginaryInput.setAlignmentX(JTextField.LEFT_ALIGNMENT);
		imaginaryInput.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(updatingValues) return;
				parse();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if(updatingValues) return;
				parse();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if(updatingValues) return;
				parse();
			}

			private void parse() {
				double imaginary;
				try {
					imaginary = Double.parseDouble(imaginaryInput.getText());
				} catch(NumberFormatException e) {
					return;
				}
				frame.getModel().getPosition().setImaginary(imaginary);
				frame.getModel().calculate();
			}
		});
		add(imaginaryInput);
		add(Box.createRigidArea(new Dimension(0, 5)));
		movingInfo = new JLabel("You can move using the arrow keys.");
		add(movingInfo);
		add(Box.createRigidArea(new Dimension(0, 25)));
		
		//Iterationen-Eingabe
		iterations = new JLabel("Iterations");
		add(iterations);
		SpinnerModel iterationsModel = new SpinnerHalfDoubleNumberModel(1, 1, 1e6);
		iterationSpinner = new JSpinner(iterationsModel);
		iterationSpinner.setMaximumSize(new Dimension(240, 25));
		iterationSpinner.setMinimumSize(new Dimension(240, 25));
		iterationSpinner.setAlignmentX(JSpinner.LEFT_ALIGNMENT);
		iterationSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				frame.getModel().setIterations((int) (double) iterationSpinner.getValue());
				frame.getModel().calculate();
			}
		});
		add(iterationSpinner);
		add(Box.createRigidArea(new Dimension(0, 25)));

		//Farbkarten-Auswahl
		colorMap = new JLabel("Color map");
		add(colorMap);
		String[] items = {"blueorange", "bluegreen", "blueyellow", "redblue", "teals", "seashore", "dawn", "none", "blues", "deepocean", "reds", "lava"};
		colorMapDropDown = new JComboBox<String>(items);
		colorMapDropDown.setMaximumSize(new Dimension(240, 25));
		colorMapDropDown.setMinimumSize(new Dimension(240, 25));
		colorMapDropDown.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
		colorMapDropDown.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				frame.getModel().setColorMap(new ColorMap(colorMapDropDown.getItemAt(colorMapDropDown.getSelectedIndex())));
				frame.getMinimapModel().setColorMap(new ColorMap(colorMapDropDown.getItemAt(colorMapDropDown.getSelectedIndex())));
			}
		});
		add(colorMapDropDown);
		add(Box.createRigidArea(new Dimension(0, 5)));
		//Farbausbreitungs-Feld
		colorFactor = new JLabel("Color factor");
		add(colorFactor);
		SpinnerModel colorModel = new SpinnerNumberModel(0.1, 0.000001f, 1e5, 0.001);
		colorSpinner = new JSpinner(colorModel);
		colorSpinner.setMaximumSize(new Dimension(240, 25));
		colorSpinner.setMinimumSize(new Dimension(240, 25));
		colorSpinner.setAlignmentX(JSpinner.LEFT_ALIGNMENT);
		colorSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				frame.getModel().setColorSpread((float) (double) colorSpinner.getValue());
			}
		});
		add(colorSpinner);
		add(Box.createRigidArea(new Dimension(0, 5)));
		//Farbglättungs-Haken
		useSmoothColoring = new JLabel("Smooth coloring");
		add(useSmoothColoring);
		smoothColoringCheckbox = new JCheckBox();
		smoothColoringCheckbox.setSelected(false);
		ToolTipManager.sharedInstance().setInitialDelay(100);
		smoothColoringCheckbox.setToolTipText("If you are using this function you might have\nto increse the iterations for correct results.");
		smoothColoringCheckbox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				frame.getModel().setSmoothColoring(smoothColoringCheckbox.isSelected());
				frame.getModel().calculate();
			}
		});
		add(smoothColoringCheckbox);
		add(Box.createRigidArea(new Dimension(0, 20)));

		//Julia
		switchBetweenMandelbrotAndJuliaSetButton = new JButton("Mandelbrot set ↔ Julia set");
		switchBetweenMandelbrotAndJuliaSetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(frame.getModel().getType()) {
					case MANDELBROT:
						frame.getModel().setType(Model.Type.JULIA);
						frame.getModel().calculate();
						break;
					case JULIA:
						frame.getModel().setType(Model.Type.MANDELBROT);
						break;
				}
				frame.getModel().calculate();
			}
		});
		add(switchBetweenMandelbrotAndJuliaSetButton);
		add(Box.createRigidArea(new Dimension(0, 20)));

		//Output generieren.
		output = new JButton("Save as output.png");
		output.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					frame.outputFile();
					output.setText("Save as output.png");
				} catch(IOException e) {
					output.setText("Save as output.png (An error ocurred)");
				}
			}
		});
		add(output);
	}

	public void updateCalculationDuration() {
		calculationDuration.setText("Calculation duration: "+frame.getModel().getCalculationDuration()+" ms");
	}

	public void updateMousePosition(double reel, double imaginary) {
		mousePosition.setText("Cursor position: "+(float) reel+", "+(float) imaginary);
	}

	//Werte anpassen, wenn sie über die Tastatur verändert wurden.
	public void updateValues() {
		updatingValues = true;
		zoomSpinner.setValue(frame.getModel().getZoomFactor());
		reelInput.setText(String.valueOf(frame.getModel().getPosition().getReel()));
		imaginaryInput.setText(String.valueOf(frame.getModel().getPosition().getImaginary()));
		updatingValues = false;
	}

}
