package flappybirds;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

class Background {

	private BufferedImage backgroundImage;
	private BufferedImage backgroundImage1;
	private BufferedImage backgroundImage2;

	private Graphics2D g2;

	private int x1, y1, x2, y2;

	public Background() {
		try {
			backgroundImage = ImageIO.read(new File("Assets/background0copy2.png"));
			backgroundImage1 = ImageIO.read(new File("Assets/background1.jpg"));
			backgroundImage2 = ImageIO.read(new File("Assets/background2.jpg"));
		} catch (IOException ex) {
		}

		x1 = 0;
		y1 = 0;
		x2 = x1 + 800;
		y2 = 0;
	}

	public void Update(float speed) {
		x1 -= speed;
		x2 -= speed;

		if (x2 < 0)
			x1 = x2 + 800;
		if (x1 < 0)
			x2 = x1 + 800;
	}

	public void Paint(Graphics2D g2) {
		g2.drawImage(backgroundImage, x1, y1 - 100, null);
		g2.drawImage(backgroundImage, x2, y2 - 100, null);

	}

	public void Paint(Graphics2D g2, int value) {
		if (value == 0) {
			g2.drawImage(backgroundImage, x1, y1 - 100, null);
			g2.drawImage(backgroundImage, x2, y2 - 100, null);
		} else if (value == 1) {
			g2.drawImage(backgroundImage1, x1, y1, null);
			g2.drawImage(backgroundImage1, x2, y2, null);
		} else if (value == 2) {
			g2.drawImage(backgroundImage2, x1, y1, null);
			g2.drawImage(backgroundImage2, x2, y2, null);
		}
	}
}