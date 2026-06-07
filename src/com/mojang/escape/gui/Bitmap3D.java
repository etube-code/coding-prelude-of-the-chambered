package com.mojang.escape.gui;

import com.mojang.escape.Art;
import com.mojang.escape.Game;

public class Bitmap3D extends Bitmap {

	private double[] zBuffer;
	private double xCam, yCam, zCam, rCos, rSin, fov;

	public Bitmap3D(int width, int height) {
		super(width, height);

		zBuffer = new double[width * height];
	}

	public void render(Game game) {

		xCam = 0;
		yCam = -1 + game.time % 100 * 0.01;
		zCam = 0;

		double rot = Math.sin(game.time / 40.0) * 0.5;

		rCos = Math.cos(rot);
		rSin = Math.sin(rot);

		fov = height;

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

		renderWall(-1, 2, 2, 2);

	}

	private void renderWall(double x0, double y0, double x1, double y1) {

		double xc0 = ((x0 - 0.5) - xCam) * 2;
		double yc0 = ((y0 - 0.5) - yCam) * 2;

		double xx0 = xc0 * rCos - yc0 * rSin;
		double u0 = ((-0.5) - zCam) * 2;
		double l0 = ((+0.5) - zCam) * 2;
		double zz0 = yc0 * rCos + xc0 * rSin;

		double xc1 = ((x1 - 0.5) - xCam) * 2;
		double yc1 = ((y1 - 0.5) - yCam) * 2;

		double xx1 = xc1 * rCos - yc1 * rSin;
		double u1 = ((-0.5) - zCam) * 2;
		double l1 = ((+0.5) - zCam) * 2;
		double zz1 = yc1 * rCos + xc1 * rSin;

		double xPixel0 = (xx0 / zz0 * fov + width / 2);
		double xPixel1 = (xx1 / zz1 * fov + width / 2);

		if (xPixel0 >= xPixel1)
			return;

		int xp0 = (int) Math.floor(xPixel0);
		int xp1 = (int) Math.floor(xPixel1);

		if (xp0 < 0)
			xp0 = 0;
		if (xp1 > width)
			xp1 = width;

		for (int x = xp0; x < xp1; x++) {
			double pr = (x - xPixel0) / (xPixel1 - xPixel0);

			double u = u0 + (u1 - u0) * pr;
			double l = l0 + (l1 - l0) * pr;
			double zz = zz0 + (zz1 - zz0) * pr;

			int yPixel = (int) (u / zz * fov + height / 2);

			if (yPixel > 0 && yPixel < height) {
				pixels[x + yPixel * width] = 0xff00ff;
				zBuffer[x + yPixel * width] = 0;

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