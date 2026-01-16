package com.mojang.escape.gui;

import java.util.Random;

import com.mojang.escape.Game;

public class Screen extends Bitmap{
	private Bitmap testBitmap;
	
	public Screen(int width, int height) {
		super(width, height);
		
		Random random = new Random();
		testBitmap = new Bitmap(64, 64);
		for (int i = 0; i < 64 * 64; i++) {
			testBitmap.pixels[i] = random.nextInt() * (random.nextInt(5) / 4);
						
		}
	}

	public void render(Game game) {
		for( int i = 0; i < width * height; i++){
			pixels[i] = 0;
		}
		
		for (int i = 0; i < 100; i++) {
			int xo = (int) (Math.sin((System.currentTimeMillis() - i * 16) % 2000 / 2000.0*Math.PI*2) * 100);
			int yo = (int) (Math.cos((System.currentTimeMillis() - i * 16) % 2000 / 2000.0*Math.PI*2) * 60);
			draw(testBitmap, (width - 64) / 2 + xo, (height - 64) / 2 + yo);
		}
	}
}
