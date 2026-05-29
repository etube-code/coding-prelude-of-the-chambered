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
		double yCam = -1 + game.time % 100 * 0.01;
		double zCam = 0;

		double rot = Math.sin(game.time / 40.0) * 0.5;

		double rCos = Math.cos(rot);
		double rSin = Math.sin(rot);
		
		double fov = height;

		for (int y = 0; y < height; y++) {
			double yd = ((y + 0.5) - height / 2.0) / fov;

			double zd = (4 + zCam) / yd;

			if (yd < 0) {
				zd = (4 - zCam) / -yd;
			}

			for (int x = 0; x < width; x++) {
				double xd = (x - width / 2.0) / fov;
				xd *= zd;

				double xx = xd * rCos + zd * rSin + (xCam + 0.5) * 8;
				double yy = zd * rCos - xd * rSin + (yCam + 0.5) * 8;

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

		Random random = new Random(100);
		for (int i = 0; i < 1000; i++) {
			double x = random.nextDouble() * 2 - 1 - xCam * 2;
			double z = random.nextDouble() * 2 - 1 - zCam * 2;
			double y = (1 + 0.5 - yCam) * 2;

			double xx = x * rCos - y * rSin;
			double yy = z;
			double zz = y * rCos + x * rSin;

			if (zz > 0) {

				int xPixel = (int) (xx / zz * fov + width / 2);
				int yPixel = (int) (yy / zz * fov + height / 2);
				if (xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height) {
					zBuffer[xPixel + yPixel * width] = zz * 4;
					pixels[xPixel + yPixel * width] = 0xffffff;
				}
			}
		}
	}

	public void postProcess() {

		for (int i = 0; i < width * height; i++) {
			int col = pixels[i];
			int brightness = (int) (15000 / (zBuffer[i] * zBuffer[i]));
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