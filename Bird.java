package flappybirds;

import java.awt.Rectangle;
import java.io.File;
import pkg2dgamesframework.Objects;
import pkg2dgamesframework.SoundPlayer;

public class Bird extends Objects {

	private float vt = 0;

	private boolean isFlying = false;

	private Rectangle rect;

	private boolean isLive = true;

	SoundPlayer fapSound;
	SoundPlayer themeSound;
	SoundPlayer themeSound2;
	SoundPlayer themeSound3;
	SoundPlayer themeSound4;
	SoundPlayer getPointSound;
	SoundPlayer fallSound;
	SoundPlayer loseSound;

	public Bird(int x, int y, int w, int h) {
		super(x, y, w, h);
		rect = new Rectangle(x, y, w, h);
		fapSound = new SoundPlayer(new File("Assets/fap.wav"));
		getPointSound = new SoundPlayer(new File("Assets/getpoint.wav"));
		fallSound = new SoundPlayer(new File("Assets/fall.wav"));
		themeSound = new SoundPlayer(new File("Assets/theme.wav"));
		themeSound2 = new SoundPlayer(new File("Assets/theme5.wav"));
		themeSound3 = new SoundPlayer(new File("Assets/theme6.wav"));
		themeSound4 = new SoundPlayer(new File("Assets/theme4.wav"));

		loseSound = new SoundPlayer(new File("Assets/lose.wav"));
	}

	public void setLive(boolean b, int themeType) {

		isLive = b;
		if (!isLive) {
			loseSound.play();
			if (themeType == 1)
				themeSound.stop();
			else if (themeType == 2)
				themeSound2.stop();
			else if (themeType == 3)
				themeSound3.stop();
		} else {
			// Play the theme sound when the bird is created
			if (themeType == 1)
				themeSound.playLoop();
			else if (themeType == 2)
				themeSound2.playLoop();
			else if (themeType == 3)
				themeSound3.playLoop();

		}

	}


	public boolean getLive() {
		return isLive;
	}

	public Rectangle getRect() {
		return rect;
	}

	public void setVt(float vt) {
		this.vt = vt;
	}

	public void update(long deltaTime) {

		vt += FlappyBirds.g;
		this.setPosY(this.getPosY() + vt);
		this.rect.setLocation((int) this.getPosX(), (int) this.getPosY());

		if (vt < 0)
			isFlying = true;
		else if (vt > 0)
			isFlying = false;

	}

	public void fly() {
		vt = -5;
		fapSound.setVolume(0.5f);
	}

	public boolean getIsFlying() {
		return isFlying;
	}

	public void stopThemeCurent(int themeType) {
		if (themeType == 1)
			themeSound.stop();
		else if (themeType == 2)
			themeSound2.stop();
		else if (themeType == 3)
			themeSound3.stop();
	}
}
