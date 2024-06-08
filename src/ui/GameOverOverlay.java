package ui;

import static utilz.Constants.UI.URMButtons.URM_SIZE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

/**
 * Represents the overlay displayed when the game ends.
 * It includes options to return to the main menu or restart the game.
 */
public class GameOverOverlay {

	private Playing playing;
	private BufferedImage img;
	private int imgX, imgY, imgW, imgH;
	private UrmButton menu, play;

	/**
	 * Constructs a GameOverOverlay object.
	 *
	 * @param playing The Playing game state.
	 */
	public GameOverOverlay(Playing playing) {
		this.playing = playing;
		createImg();
		createButtons();
	}

	/**
	 * Creates the buttons for the game over screen overlay.
	 */
	private void createButtons() {
		int menuX = (int) (335 * Game.SCALE);
		int playX = (int) (440 * Game.SCALE);
		int y = (int) (195 * Game.SCALE);
		play = new UrmButton(playX, y, URM_SIZE, URM_SIZE, 0);
		menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
	}

	/**
	 * Creates the image for the game over screen overlay.
	 */
	private void createImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
		imgW = (int) (img.getWidth() * Game.SCALE);
		imgH = (int) (img.getHeight() * Game.SCALE);
		imgX = Game.GAME_WIDTH / 2 - imgW / 2;
		imgY = (int) (100 * Game.SCALE);
	}

	/**
	 * Draws the game over overlay.
	 *
	 * @param g The graphics context.
	 */
	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		g.drawImage(img, imgX, imgY, imgW, imgH, null);

		menu.draw(g);
		play.draw(g);
	}

	/**
	 * Updates the game over overlay.
	 */
	public void update() {
		menu.update();
		play.update();
	}

	/**
	 * Handles key press events.
	 *
	 * @param e The KeyEvent.
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			playing.resetAll();
			Gamestate.state = Gamestate.MENU;
		}
	}

	/**
	 * Checks if a mouse event is within the bounds of a specified button.
	 *
	 * @param b The button to check.
	 * @param e The MouseEvent.
	 * @return {@code true} if the mouse event is within the bounds of the button, {@code false} otherwise.
	 */
	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	/**
	 * Handles mouse movement events.
	 *
	 * @param e The MouseEvent.
	 */
	public void mouseMoved(MouseEvent e) {
		play.setMouseOver(false);
		menu.setMouseOver(false);

		if (isIn(menu, e))
			menu.setMouseOver(true);
		else if (isIn(play, e))
			play.setMouseOver(true);
	}

	/**
	 * Handles mouse release events.
	 *
	 * @param e The MouseEvent.
	 */
	public void mouseReleased(MouseEvent e) {
		if (isIn(menu, e)) {
			if (menu.isMousePressed()) {
				playing.resetAll();
				Gamestate.state = Gamestate.MENU;
			}
		} else if (isIn(play, e))
			if (play.isMousePressed())
				playing.resetAll();

		menu.resetBools();
		play.resetBools();
	}

	/**
	 * Handles mouse press events.
	 *
	 * @param e The MouseEvent.
	 */
	public void mousePressed(MouseEvent e) {
		if (isIn(menu, e))
			menu.setMousePressed(true);
		else if (isIn(play, e))
			play.setMousePressed(true);
	}

}
