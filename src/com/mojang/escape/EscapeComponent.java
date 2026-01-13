package com.mojang.escape;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

import com.mojang.escape.gui.Screen;

public class EscapeComponent extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH=160;
	private static final int HEIGHT=120;
	private static final int SCALE=4;
	
	private boolean running;
	private Thread thread;

	private Screen screen;
	
	public EscapeComponent() {		
		Dimension size = new Dimension(WIDTH*SCALE, HEIGHT*SCALE);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		
		screen = new Screen(WIDTH, HEIGHT);
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
		
	}

	private void render() {
		
	}

	public static void main(String[] args) {		
		EscapeComponent game = new EscapeComponent();
		
		JFrame frame = new JFrame("Escape!");
		frame.add(game);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		game.start();
		
	}

}
