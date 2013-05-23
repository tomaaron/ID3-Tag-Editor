package model;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import view.Constants;

/**
 * Handles image conversions and resizing
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
public class ImageHandler {

	public static byte[] createByteArray(File picture) throws IOException {
		BufferedImage imageFile = getScaledInstance(ImageIO.read(picture),
				Constants.INITIAL_EDITPANEL_WIDTH - 4
						* Constants.DEFAULT_HORIZONTAL_SPACE,
				Constants.MAX_FRONT_COVER_HEIGHT);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (picture.getName().toLowerCase().endsWith("jpg")) {
			ImageIO.write(imageFile, "jpg", baos);
		} else if (picture.getName().toLowerCase().endsWith("png")) {
			ImageIO.write(imageFile, "png", baos);
		}
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		return imageInByte;
	}

	/**
	 * creates a ImageIcon from the given byte array
	 * 
	 * @param array
	 * @return
	 */
	public static ImageIcon createPicture(byte[] array) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new ByteArrayInputStream(array));
		} catch (IOException e) {
			System.err.println("Fehler beim Lesen des Bildes: "
					+ e.getMessage());
		}
		// is null, when byte array was empty
		if (image != null) {
			image = getScaledInstance(image, Constants.INITIAL_EDITPANEL_WIDTH
					- 4 * Constants.DEFAULT_HORIZONTAL_SPACE,
					Constants.MAX_FRONT_COVER_HEIGHT);
			return new ImageIcon(image);
		} else
			return new ImageIcon();
	}

	/**
	 * scales the image to fit in the GUI
	 * 
	 * @param img
	 * @param targetWidth
	 * @return
	 */
	public static BufferedImage getScaledInstance(BufferedImage img,
			int targetWidth, int targetHeight) {
		int w, h, newW, newH;
		w = img.getWidth();
		h = img.getHeight();
		newW = targetWidth;
		newH = targetHeight;

		if (w > targetWidth || h > targetHeight) {
			if (w >= h) {
				newH = (int) ((float) h * ((float) targetWidth / (float) w));
				if (h > targetHeight) {
					newW = (int) ((float) w * ((float) targetHeight / (float) h));
					newH = targetHeight;
				}
			} else {
				newW = (int) ((float) w * ((float) targetHeight / (float) h));
				if (w > targetWidth) {
					newH = (int) ((float) h * ((float) targetWidth / (float) w));
					newW = targetWidth;
				}
			}
		}

		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = new BufferedImage(newW, newH, type);
		Graphics2D g2 = ret.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(img, 0, 0, newW, newH, null);
		g2.dispose();

		return ret;
	}
}
