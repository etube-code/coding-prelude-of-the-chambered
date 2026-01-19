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

	int time = 0;
	
	public void render(Game game) {
		for( int i = 0; i < width * height; i++){
			pixels[i] = 0;
		}
		
		time++;
		
		for (int i = 0; i < 100; i++) {
			int xo = (int) (Math.sin((time + i * i)/100.0) * 100);
			int yo = (int) (Math.cos((time + i * i)/110.0) * 60);
			draw(testBitmap, (width - 64) / 2 + xo, (height - 64) / 2 + yo);
		}
	}
}
