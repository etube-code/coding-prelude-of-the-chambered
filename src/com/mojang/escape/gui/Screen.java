package com.mojang.escape.gui;

import java.util.Random;

import com.mojang.escape.Game;

public class Screen extends Bitmap{
	private Bitmap testBitmap;
	
	public Screen(int width, int height) {
		super(width, height);
		
		Random random = new Random();
		testBitmap = new Bitmap(64,64);
		for (int i = 0; i < 64 * 64; i++) {
			testBitmap.pixels[i] = random.nextInt();
			
			
		}
	}

	public void render(Game game) {
		draw(testBitmap, 0, 0);
	}
}
	
