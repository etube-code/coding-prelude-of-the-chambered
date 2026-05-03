package com.mojang.escape.gui;

import com.mojang.escape.Art;
import com.mojang.escape.Game;

public class Bitmap3D extends Bitmap {

	private double[] zBuffer;

	public Bitmap3D(int width, int height) {
		super(width, height);

		zBuffer = new double[width * height];
	}

	public void render(Game game) {

		double xCam = game.time / 10.0;
		double yCam = 0;
		double zCam = 0;

		double rot = game.time / 100.0;

		double rCos = Math.cos(rot);
		double rSin = Math.sin(rot);

		for (int y = 0; y < height; y++) {
			double yd = ((y + 0.5) - height / 2.0) / height;

			double zd = (4 + zCam) / yd;

			if (yd < 0) {
				zd = (4 - zCam) / -yd;
			}

			for (int x = 0; x < width; x++) {
				double xd = (x - width / 2.0) / height;
				xd *= zd;

				double xx = (xd * rCos + zd * rSin + xCam);
				double yy = (zd * rCos - xd * rSin + yCam);

				int xPix = (int) (xx);
				int yPix = (int) (yy);
				if (xx < 0)
					xPix--;
				if (yy < 0)
					yPix--;

				zBuffer[x + y * width] = zd;
				pixels[x + y * width] = Art.floors.pixels[(xPix & 7) + (yPix & 7) * 64];

			}
		}
	}

	public void postProcess() {

		for (int i = 0; i < width * height; i++) {
			int col = pixels[i];
			int brightness = (int) (20000 / (zBuffer[i] * zBuffer[i]));
			if (brightness < 0)
				brightness = 0;
			if (brightness > 255)
				brightness = 255;

			int r = (col >> 16) & 0xff;
			int g = (col >> 8) & 0xff;
			int b = (col) & 0xff;

			r = r * brightness / 255;
			g = g * brightness / 255;
			b = b * brightness / 255;

			pixels[i] = r << 16 | g << 8 | b;

		}

	}

}