package com.mojang.escape.gui;

import java.util.Random;

import com.mojang.escape.Game;

public class Screen extends Bitmap {
	private static final int PANEL_HEIGHT = 8 * 3;
	private Bitmap testBitmap;
	private Bitmap gamePanel;
	private Bitmap3D viewport;

	public Screen(int width, int height) {
		super(width, height);

		gamePanel = new Bitmap(width, PANEL_HEIGHT);

		viewport = new Bitmap3D(width, height - PANEL_HEIGHT);

		Random random = new Random();
		testBitmap = new Bitmap(64, 64);
		for (int i = 0; i < 64 * 64; i++) {
			testBitmap.pixels[i] = random.nextInt() * (random.nextInt(5) / 4);

		}
	}

	int time = 0;

	public void render(Game game) {
		for (int i = 0; i < width * height; i++) {
			pixels[i] = 0;
		}

		for (int i = 0; i < 100; i++) {
			int xo = (game.time + i * 8) % 400 - 200;
			int yo = 0;
			gamePanel.draw(testBitmap, (gamePanel.width - 64) / 2 + xo, (gamePanel.height - 64) / 2 + yo);
		}
		
		viewport.render(game);
		draw(viewport, 0, 0);
		draw(gamePanel, 0, height - PANEL_HEIGHT);
	}
}