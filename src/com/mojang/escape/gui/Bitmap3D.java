package com.mojang.escape.gui;

import com.mojang.escape.Art;
import com.mojang.escape.Game;

public class Bitmap3D extends Bitmap {

	public Bitmap3D(int width, int height) {
		super(width, height);
	}

	public void render(Game game) {

		for (int y = 0; y < height; y++) {
			double yd = ((y + 0.5) - height / 2.0) / height;

			double z = 2 / yd;

			if (yd < 0) {
				z = 4 / -yd;
			}

			for (int x = 0; x < width; x++) {
				double xd = (x - width / 2.0) / height;
				xd *= z;
				int xx = (int) (xd + game.time * 0.1) & 7;
				int yy = (int) (z + game.time * 0.1) & 7;

				pixels[x + y * width] = Art.floors.pixels[xx + yy * 64];

			}
		}
	}
}