package com.mojang.escape;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.mojang.escape.gui.Bitmap;

public class Art{
	
	public static Bitmap floors = loadBitmap("/tex/floors.png");

	public static Bitmap loadBitmap(String fileName) {
		
		try {
			BufferedImage img = ImageIO.read(Art.class.getResource(fileName));
			
			int w = img.getWidth();
			int h = img.getHeight();
			
			Bitmap result = new Bitmap(img.getWidth(), img.getHeight());
			img.getRGB(0, 0, w, h, result.pixels, 0, w);
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);

		}
				
	}
}	