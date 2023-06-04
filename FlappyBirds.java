package flappybirds;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import pkg2dgamesframework.AFrameOnImage;
import pkg2dgamesframework.Animation;
import pkg2dgamesframework.GameScreen;
import pkg2dgamesframework.SoundPlayer;

public class FlappyBirds extends GameScreen {
	private BufferedImage birds;
	private Animation bird_anim;
	private BufferedImage obj;
	private float speed;
	public static float g;
	private SoundPlayer getPointSound, fallSound;
	private flappybirds.Bird bird;
	private Ground ground;
	private Background Background;

	private ChimneyGroup chimneyGroup;

	private int Point;

	private int BEGIN_SCREEN = 0;
	private int GAMEPLAY_SCREEN = 1;
	private int GAMEOVER_SCREEN = 2;

	private int CurrentScreen = BEGIN_SCREEN;

	private int bestScore;

	private enum Mode {
		NORMAL, HARD, SUPER
	}

	private int backgroundType = 0;
	private int themeType = 1;
	private int chimneyType=0;

	private Mode currentMode = Mode.NORMAL;
	
	private JComboBox<String> bgComboBox;

	private void resetGame() {
		g = 0.2f;
		Point = 0;
		chimneyGroup.resetChimneys();
		chimneyGroup.resetChimneys();

		switch (currentMode) {
		case NORMAL:
			speed = 1;
			break;
		case HARD:
			speed = 2;
			break;
		case SUPER:
			speed = 1;
			break;
		}

		bird.setPos(350, 250);
		bird.setVt(0);
		bird.setLive(true, themeType);
	}

	public FlappyBirds() {
		super(800, 600);

		bestScore = loadBestScore();

		try {
			birds = ImageIO.read(new File("Assets/bird_sprite.png"));

		} catch (IOException ex) {
		}

		bird_anim = new Animation(70);
		AFrameOnImage f;
		f = new AFrameOnImage(0, 0, 60, 60);
		bird_anim.AddFrame(f);
		f = new AFrameOnImage(60, 0, 60, 60);
		bird_anim.AddFrame(f);
		f = new AFrameOnImage(120, 0, 60, 60);
		bird_anim.AddFrame(f);
		f = new AFrameOnImage(60, 0, 60, 60);
		bird_anim.AddFrame(f);

		Background = new Background();// khởi tạo bg
		bird = new Bird(350, 250, 50, 50);
		ground = new Ground();

		chimneyGroup = new ChimneyGroup();

		BeginGame();
	}

	private int loadBestScore() {
		int score = 0;
		try {
			File file = new File("bestscore.txt");
			if (file.exists()) {
				Scanner scanner = new Scanner(file);
				score = scanner.nextInt();
				scanner.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return score;
	}

	private void updateBestScore() {
		if (Point > bestScore) {
			bestScore = Point;
			try {
				FileWriter writer = new FileWriter("bestscore.txt");
				writer.write(Integer.toString(bestScore));
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void resetBestScore() {
		bestScore = 0;
	}

	public static void main(String[] args) {
		new FlappyBirds();
	}

	@Override
	public void GAME_UPDATE(long deltaTime) {

		if (CurrentScreen == BEGIN_SCREEN) {
			resetGame();
		} else if (CurrentScreen == GAMEPLAY_SCREEN) {

			if (bird.getLive())
				bird_anim.Update_Me(deltaTime);

			Background.Update(speed);
			bird.update(deltaTime);
			ground.Update(speed);

			chimneyGroup.update((int) speed);

			for (int i = 0; i < ChimneyGroup.SIZE; i++) {
				if (bird.getRect().intersects(chimneyGroup.getChimney(i).getRect())) {
					if (bird.getLive())
						bird.fallSound.play();
					bird.setLive(false, themeType);
					CurrentScreen = GAMEOVER_SCREEN;
					break; // Exit the loop since the bird has collided with a chimney
				}
			}

			for (int i = 0; i < ChimneyGroup.SIZE; i++) {
				if (bird.getPosX() > chimneyGroup.getChimney(i).getPosX()
						&& !chimneyGroup.getChimney(i).getIsBehindBird() && i % 2 == 0) {
					Point++;
					bird.getPointSound.setVolume(0.5f);
					chimneyGroup.getChimney(i).setIsBehindBird(true);

					// Speed up the game when the player earns certain points
					
					if (Point % 5 == 0) {
						speed += 0.125;
						g += 0.01;
					}
				}

			}
			for (int i = 0; i < ChimneyGroup.SIZE; i++) {
				if (bird.getPosY() < 0 && bird.getPosX() > chimneyGroup.getChimney(i).getPosX()
						&& !chimneyGroup.getChimney(i).getIsBehindBird()) {
					CurrentScreen = GAMEOVER_SCREEN;
					bird.fallSound.setVolume(0.5f);
					bird.loseSound.play();
					// Stop the theme sound when the bird dies
					bird.stopThemeCurent(themeType);
				}
			}

			if (bird.getPosY() + bird.getH() > ground.getYGround()) {
				CurrentScreen = GAMEOVER_SCREEN;
				bird.fallSound.setVolume(0.5f);
				bird.loseSound.play();
				// Stop the theme sound when the bird dies
				bird.stopThemeCurent(themeType);
			}

			updateBestScore();

		} else {

		}

	}

	public void setMode(String mode) {
		switch (mode.toUpperCase()) {
		case "NORMAL":
			currentMode = Mode.NORMAL;
			break;
		case "HARD":
			currentMode = Mode.HARD;
			break;
		case "SUPER":
			currentMode = Mode.SUPER;
			break;
		default:
			currentMode = Mode.NORMAL;
			break;
		}
	}

	@Override
	public void GAME_PAINT(Graphics2D g2) {

		Background.Paint(g2, backgroundType);// vẽ bg

		chimneyGroup.Paint(g2,chimneyType);

		ground.Paint(g2);

		if (bird.getIsFlying())
			bird_anim.PaintAnims((int) bird.getPosX(), (int) bird.getPosY(), birds, g2, 0, -1);
		else
			bird_anim.PaintAnims((int) bird.getPosX(), (int) bird.getPosY(), birds, g2, 0, 0.4f);

		if (CurrentScreen == BEGIN_SCREEN) {
			g2.setColor(Color.white);
			Font titleFont = new Font("Arial", Font.BOLD, 60);
			g2.setFont(titleFont);
			String title = "Welcome to My Game";
			FontMetrics fontMetrics = g2.getFontMetrics();
			int titleWidth = fontMetrics.stringWidth(title);
			int x = (800 - titleWidth) / 2; // center the text horizontally
			int y = 200; // set the y-coordinate
			g2.drawString(title, x, y);

			Font messageFont = new Font("Arial", Font.PLAIN, 30);
			g2.setFont(messageFont);
			String message = "Press space to play";
			int messageWidth = fontMetrics.stringWidth(message);
			int messageX = (800 - messageWidth) / 2; // center the text horizontally
			int messageY = 350; // set the y-coordinate
			g2.drawString(message, messageX, messageY);
			
		}

		if (CurrentScreen == GAMEOVER_SCREEN) {
			g2.setColor(Color.white);
			Font titleFont = new Font("Arial", Font.BOLD, 60);
			g2.setFont(titleFont);
			String title = "Game Over";
			FontMetrics fontMetrics = g2.getFontMetrics();
			int titleWidth = fontMetrics.stringWidth(title);
			int x = (800 - titleWidth) / 2; // center the text horizontally
			int y = 200; // set the y-coordinate
			g2.drawString(title, x, y);

			Font messageFont = new Font("Arial", Font.PLAIN, 30);
			g2.setFont(messageFont);
			String message = "Press space to return";
			int messageWidth = fontMetrics.stringWidth(message);
			int messageX = (800 - messageWidth) / 2; // center the text horizontally
			int messageY = 350; // set the y-coordinate
			g2.drawString(message, messageX, messageY);
		}
		
		// Move the Point to the middle of the top of the screen
		Font font = new Font("Arial", Font.BOLD, 30);
		g2.setFont(font);
		FontMetrics fontMetrics = g2.getFontMetrics();
		String scoreText = "Point: " + Point;
		int x = (MASTER_WIDTH - fontMetrics.stringWidth(scoreText)) / 2;
		int y = fontMetrics.getHeight();
		g2.setColor(Color.white);
		g2.drawString(scoreText, x, y);

		Font fontBest = new Font("Arial", Font.BOLD, 20);
		g2.setFont(fontBest);
		g2.setColor(Color.white);
		String bestScoreText = "Best Score: " + bestScore;
		g2.drawString(bestScoreText, x - 10, y + 20);

		// show tốc độ bay
		Font fontSpeed = new Font("Arial", Font.BOLD, 20);
		g2.setFont(fontSpeed);
		g2.setColor(Color.white);
		g2.drawString("Speed: " + speed, 10, 30);

		// show tốc độ bay
		Font fontMode = new Font("Arial", Font.BOLD, 20);
		g2.setFont(fontMode);
		g2.setColor(Color.white);
		String ModeText = "" + currentMode;
		int ModeX = MASTER_WIDTH - fontMetrics.stringWidth(ModeText) - 10;
		int ModeY = 30;
		g2.drawString(ModeText, ModeX, ModeY);
	}

	@Override
	public void KEY_ACTION(KeyEvent e, int Event) {
		if (Event == KEY_PRESSED) {
			if (CurrentScreen == BEGIN_SCREEN) {
				if (bird.getLive() && (e.getKeyCode() == KeyEvent.VK_SPACE  ))
					CurrentScreen = GAMEPLAY_SCREEN; // bấm space để bát đầu
				else if (bird.getLive() && e.getKeyCode() == KeyEvent.VK_0)
					resetBestScore(); // bấm 0 để reset bestScore

				if (e.getKeyCode() == KeyEvent.VK_N) { // Kiểm tra nếu phím "N" được nhấn
					currentMode = Mode.NORMAL; // Cập nhật độ khó thành "normal"

				} else if (e.getKeyCode() == KeyEvent.VK_H) { // Kiểm tra nếu phím "H" được nhấn
					currentMode = Mode.HARD; // Cập nhật độ khó thành "hard"
				} else if (e.getKeyCode() == KeyEvent.VK_S) { // Kiểm tra nếu phím "S" được nhấn
					currentMode = Mode.SUPER; // Cập nhật độ khó thành "super"
				} else if (e.getKeyCode() == KeyEvent.VK_1) { // doi nen 1
					backgroundType = 0;
				} else if (e.getKeyCode() == KeyEvent.VK_2) { // doi nen 2
					backgroundType = 1;//
				} else if (e.getKeyCode() == KeyEvent.VK_3) { // doi nen 3
					backgroundType = 2; //
				} else if (e.getKeyCode() == KeyEvent.VK_F1) { // doi nhac nen 1					
					bird.stopThemeCurent(themeType);
					if (themeType != 1) {
						themeType = 1;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_F2) { // doi nhac nen 2
					bird.stopThemeCurent(themeType);
					if (themeType != 2) {
						themeType = 2;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_F3) { // doi nhac nen 3
					bird.stopThemeCurent(themeType);
					if (themeType != 3) {
						themeType = 3;
					}
				}else if (e.getKeyCode() == KeyEvent.VK_4) { // doi nen 3
					chimneyType = 0; //
				}else if (e.getKeyCode() == KeyEvent.VK_5) { // doi nen 3
					chimneyType = 1; //
				}else if (e.getKeyCode() == KeyEvent.VK_6) { // doi nen 3
					chimneyType = 2; //
				}
			}

		} else if (CurrentScreen == GAMEPLAY_SCREEN) {
			if (bird.getLive() && e.getKeyCode() == KeyEvent.VK_SPACE)
				bird.fly();
		} else if (CurrentScreen == GAMEOVER_SCREEN) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
				CurrentScreen = BEGIN_SCREEN;
		}
	}
}
