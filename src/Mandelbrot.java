import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Mandelbrot extends JFrame{
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final int MAX_ITERATIONS = 1000;
	
	private double zoom = 1.0;
	private double centerX = -0.5;
	private double centerY = 0.0;
	
	public Mandelbrot() {
		setTitle("Infinite Mandelbrot Set Zoom");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MandelbrotPanel panel = new MandelbrotPanel();
		add(panel);
		
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				double clickX = (e.getX() - WIDTH / 2.0) / (WIDTH * zoom / 4.0) + centerX;
				double clickY = (e.getY() - HEIGHT / 2.0) / (HEIGHT * zoom / 4.0) + centerY;
				
				centerX = clickX;
				centerY = clickY;
				
				zoom *= 1.5;
				
				panel.repaint();
			}
		});
	}
	
	private class MandelbrotPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			
			for (int x = 0; x < WIDTH; x++) {
				for (int y = 0; y < HEIGHT; y++) {
					double real = (x - WIDTH / 2.0) / (WIDTH * zoom / 4.0) + centerX;
					double imag = (y - HEIGHT / 2.0) / (HEIGHT * zoom / 4.0) + centerY;
					
					int iterations = calculateMandelbrot(real, imag);
					image.setRGB(x, y, getColor(iterations));
				}
			}
			
			g.drawImage(image, 0,  0, this);
		}
		
		private int calculateMandelbrot(double real, double imag) {
			double zReal = 0;
			double zImag = 0;
			
			for (int i = 0; i < MAX_ITERATIONS; i++) {
				double newZReal = zReal * zReal - zImag * zImag + real;
				double newZImag = 2 * zReal * zImag + imag;
				
				zReal = newZReal;
				zImag = newZImag;
				
				if (zReal * zReal + zImag * zImag > 4.0) {
					return i;
				}
			}
			
			return MAX_ITERATIONS;
		}
		
		private int getColor(int iterations) {
			if (iterations == MAX_ITERATIONS) {
				return Color.BLACK.getRGB();
			}
			
			float hue = iterations / (float) MAX_ITERATIONS;
			return Color.HSBtoRGB(hue, 0.8f, 0.8f);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Mandelbrot mandelbrot = new Mandelbrot();
			mandelbrot.setVisible(true);
		});

	}

}
