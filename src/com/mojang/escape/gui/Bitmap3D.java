package com.mojang.escape.gui;

import java.util.Random;

import com.mojang.escape.Art;
import com.mojang.escape.Game;

public class Bitmap3D extends Bitmap {

	private double[] zBuffer;

	public Bitmap3D(int width, int height) {
		super(width, height);

		zBuffer = new double[width * height];
	}

	public void render(Game game) {

		double xCam = 0;
		double yCam = game.time % 100 / 20.0 - 2;
		double zCam = 0;

		double rot = Math.sin(game.time / 40.0) * 0.5;

		double rCos = Math.cos(rot);
		double rSin = Math.sin(rot);

		for (int y = 0; y < height; y++) {
			double yd = ((y + 0.5) - height / 2.0) / height;

			double zd = (8 + zCam) / yd;

			if (yd < 0) {
				zd = (8 - zCam) / -yd;
			}

			for (int x = 0; x < width; x++) {
				double xd = (x - width / 2.0) / height;
				xd *= zd;

				double xx = xd * rCos + zd * rSin + (xCam + 0.5) * 8;
				double yy = zd * rCos - xd * rSin + (yCam) * 8;

				int xPix = (int) (xx / 2 + 2);
				int yPix = (int) (yy / 2 + 2);
				if (xx < 0)
					xPix--;
				if (yy < 0)
					yPix--;

				zBuffer[x + y * width] = zd;
				pixels[x + y * width] = Art.floors.pixels[(xPix & 7) + (yPix & 7) * 64];

			}
		}

		Random random = new Random(100);
		for (int i = 0; i < 1000; i++) {
			double x = random.nextDouble() * 2 - 1 - xCam;
			double z = random.nextDouble() * 2 - 1 - zCam;
			double y = 2 - yCam;

			double xx = x * rCos - y * rSin;
			double yy = z;
			double zz = y * rCos + x * rSin;

			if (zz > 0) {

				int xPixel = (int) (xx / zz * height + width / 2);
				int yPixel = (int) (yy / zz * height + height / 2);
				if (xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height) {
					zBuffer[xPixel + yPixel * width] = zz * 5;
					pixels[xPixel + yPixel * width] = 0xffffff;
				}
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