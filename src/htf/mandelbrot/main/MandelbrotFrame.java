package htf.mandelbrot.main;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatHighContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;
import htf.mandelbrot.colors.ColorMap;
import htf.mandelbrot.math.Model;

/**
 * @author Henning Thomas Flath
 */
public class MandelbrotFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	//Variablen für die fortlaufende Ausführung des Programms.
	private Thread thread;
	public boolean running = false;

	//Die Zeichenfläche.
	private Canvas canvas;
	//Das Nutzen einer BufferStrategy, verhindert, dass der Bildschirminhalt flickert.
	private BufferStrategy bufferStrategy;
	//Das Graphics-Objekt zum Zeichnen auf der Zeichenfläche.
	private Graphics graphics;

	private final double[][] currentDisplay = new double[1000][1000];

	//Die Seitenleiste für Einstellungen.
	private Sidebar sidebar;
	private JScrollPane sidebarScrollPane;

	//Die Breite und Größe des Fensters.
	private int frameWidth = 1300, frameHeight = 1000;
	//Das in dieser Variable abgespeicherte Model-Objekt enthält die mathematischen Konzepte.
	private Model minimapModel;
	//Das in dieser Variable abgespeicherte Model-Objekt enthält die mathematischen Konzepte.
	private Model model;

	//Der InputListener verwaltet Tastatur-Eingaben.
	private InputListener inputListener;

	//Startet den Thread.
	public synchronized void start() {
		if(running) {
			return;
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	//Stoppt den Thread.
	public synchronized void stop() {
		if(!running) {
			return;
		}
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//Initialisiert alles, was notwendig ist.
	private void init() {
		FlatArcIJTheme.install();
		try {
			UIManager.setLookAndFeel(new FlatArcIJTheme());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		//Initialisert die model-Variable.
		model = new Model();

		//Initialisiert die minimap-model-Variable.
		minimapModel = new Model();
		minimapModel.setWidth(100);
		minimapModel.setHeight(100);
		minimapModel.setIterations(1024);
		minimapModel.setSmoothColoring(true);
		minimapModel.setColorSpread(0.09f);
		minimapModel.setResolutionScale(4);
		minimapModel.setZoomFactor(1);
		minimapModel.calculate();

		//In insets werden die Position und Größe des Fensters gespeichert.
		Insets insets = getContentPane().getInsets();
		getContentPane().setLayout(null);

		//Initialisert die Zeichenebene und positioniert sie.
		canvas = new Canvas();
		canvas.setFocusable(true);
		canvas.setBounds(insets.left, insets.top, model.getWidth(), model.getHeight());
		add(canvas);

		//Initialisert die Seitenleiste und positioniert sie.
		sidebar = new Sidebar(this);
		sidebarScrollPane = new JScrollPane(sidebar, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sidebarScrollPane.setBounds(insets.left + model.getWidth(), insets.top, frameWidth - model.getWidth(), frameHeight);
		sidebarScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		add(sidebarScrollPane);

		//Initialisert den Key-Listener.
		inputListener = new InputListener(this);
		canvas.addKeyListener(inputListener);
		canvas.addMouseListener(inputListener);
		canvas.addMouseMotionListener(inputListener);
		canvas.addMouseWheelListener(inputListener);

		pack();

		setSize(frameWidth, frameHeight);
		//Titel des Fensters einstellen.
		setTitle("Mandelbrot-Facharbeit");
		//Beim Schließen des Fensters auch das Programm beenden.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Das Fenster mittig platzieren.
		setLocationRelativeTo(null);
		//Minimale Fenstergröße.
		setMinimumSize(new Dimension(800, 500));

		//Kontrolliert die richtige Größe.
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				if(getWidth() < 600) {
					setSize(600, 300);
					return;
				}
				int canvasSize = Math.max(1, Math.min(getWidth() - 300, getHeight()));
				canvas.setSize(canvasSize, canvasSize);
				model.setWidth((int) (canvasSize * model.getResolutionScale()));
				model.setHeight((int) (canvasSize * model.getResolutionScale()));
				sidebarScrollPane.setBounds((int) (insets.left + model.getWidth() / model.getResolutionScale()), insets.top, (int) (getWidth() - model.getWidth() / model.getResolutionScale()), getHeight());
				model.calculate();
			}
		});
		//Fenster sichtbar machen.
		setVisible(true);

		//Mandelbrot-Menge berechnen.
		getModel().calculate();
	}

	//Datei ausgeben und als output.png abspeichern.
	public void outputFile() throws IOException {
		BufferedImage img = new BufferedImage(model.getWidth() * 4, model.getHeight() * 4, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.createGraphics();
		int oldWidth = model.getWidth();
		int oldHeight = model.getHeight();
		model.setWidth(4000);
		model.setHeight(4000);
		model.calculate();
		try {
			if(model.getThread() != null) {
				model.getThread().join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		draw(g);
		g.dispose();
		ImageIO.write(img, "png", new File("output.png"));
		model.setWidth(oldWidth);
		model.setHeight(oldHeight);
		model.calculate();
	}

	//Die berechneten Daten anzeigen.
	private void draw(Graphics g) {
		if(!model.isDrawing()) {
			return;
		}
		BufferedImage img = new BufferedImage(model.getWidth(), model.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = img.createGraphics();
		for(int x = 0; x < model.getWidth(); x++) {
			for (int y = 0; y < model.getHeight(); y++) {
				if(model.getValue(x, y) == -1) {
					g2.setColor(Color.BLACK);
				} else if(model.getValue(x, y) == -2) {
					g2.setColor(new Color(0, 0, 0, 0));
				} else {
					g2.setColor(model.getColorMap().getColor(model.getValue(x, y) * model.getColorSpread()));
				}
				g2.fillRect(x, y, 1, 1);
			}
		}
		g.drawImage(img.getScaledInstance((int) (model.getWidth() / model.getResolutionScale()), (int) (model.getHeight() / model.getResolutionScale()), Image.SCALE_SMOOTH), 0, 0, null);
	}

	//Die berechneten Daten anzeigen.
	private void drawMinimap(Graphics g) {
		if(!model.isDrawing()) {
			return;
		}
		BufferedImage img = new BufferedImage(minimapModel.getWidth(), minimapModel.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = img.createGraphics();
		for(int x = 0; x < minimapModel.getWidth(); x++) {
			for (int y = 0; y < minimapModel.getHeight(); y++) {
				if(minimapModel.getValue(x, y) == -1) {
					g2.setColor(Color.BLACK);
				} else if(minimapModel.getValue(x, y) == -2) {
					g2.setColor(new Color(0, 0, 0, 0));
				} else {
					g2.setColor(minimapModel.getColorMap().getColor(minimapModel.getValue(x, y) * minimapModel.getColorSpread()));
				}
				g2.fillRect(x, y, 1, 1);
			}
		}
		g.drawImage(img.getScaledInstance((int) (minimapModel.getWidth() / minimapModel.getResolutionScale()), (int) (minimapModel.getHeight() / minimapModel.getResolutionScale()), Image.SCALE_SMOOTH), 0, 0, null);
		g.setColor(Color.WHITE);
		int x = (int) (((model.getPosition().getReel() + 2) / 4) * minimapModel.getWidth() * (1 / minimapModel.getResolutionScale()));
		int y = (int) (((-model.getPosition().getImaginary() + 2) / 4) * minimapModel.getHeight() * (1 / minimapModel.getResolutionScale()));
		g.fillRect(x - 2, y - 2, 4, 4);
	}

	//Fortlaufend zeichnen.
	@Override
	public void run() {
		init();
		while(running) {
			bufferStrategy = canvas.getBufferStrategy();
			if(bufferStrategy == null) {
				canvas.createBufferStrategy(4);
				continue;
			}
			graphics = bufferStrategy.getDrawGraphics();

			draw(graphics);
			drawMinimap(graphics);
			graphics.setColor(Color.RED);
			graphics.fillRect((model.getWidth() / 2) - 2, (model.getHeight() / 2) - 2,4,4);
			graphics.setColor(new Color(1f, 1f, 1f, 0.1f));
			if(!canvas.hasFocus()) {
				graphics.fillRect(0, 0, (int) (model.getWidth() / model.getResolutionScale()), (int) (model.getHeight() / model.getResolutionScale()));
			}
			bufferStrategy.show();
			graphics.dispose();
			sidebar.updateCalculationDuration();
		}
	}
	
	//Get-Methoden.

	public Model getMinimapModel() {
		return minimapModel;
	}

	public Model getModel() {
		return model;
	}

	public Sidebar getSidebar() {
		return sidebar;
	}

	public InputListener getInputListener() {
		return inputListener;
	}
}
