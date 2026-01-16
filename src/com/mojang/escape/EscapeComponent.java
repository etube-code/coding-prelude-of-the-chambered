package com.mojang.escape;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.mojang.escape.gui.Screen;

public class EscapeComponent extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH=160;
	private static final int HEIGHT=120;
	private static final int SCALE=4;
	
	private boolean running;
	private Thread thread;
	
	private Game game;
	private Screen screen;
	private BufferedImage img;
	private int[] pixels;
	
	public EscapeComponent() {		
		Dimension size = new Dimension(WIDTH*SCALE, HEIGHT*SCALE);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		
		game = new Game();
		screen = new Screen(WIDTH, HEIGHT);
		
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	}
	
	public synchronized void start() {
		if (running) 
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		if (!running) 
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		while (running) {
			tick();
			render();
		}
	}
	
	private void tick() {
		game.tick();
		
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
			
		}
			
		screen.render(game);
		
		for (int i = 0; i < WIDTH * HEIGHT; i++) {
			pixels[i] = screen.pixels[i];			
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(img,0,0, WIDTH*SCALE, HEIGHT*SCALE, null);
		g.dispose();
		bs.show();
		
	}

	public static void main(String[] args) {		
		EscapeComponent game = new EscapeComponent();
		
		JFrame frame = new JFrame("Escape!");
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(game, BorderLayout.CENTER);
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		game.start();
		
	}

}
