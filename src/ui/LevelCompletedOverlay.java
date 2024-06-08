package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.*;

/**
 * Represents the overlay displayed when a level is completed.
 * It includes options to proceed to the next level or return to the main menu.
 */
public class LevelCompletedOverlay {

	private Playing playing;
	private UrmButton menu, next;
	private BufferedImage img;
	private int bgX, bgY, bgW, bgH;

	/**
	 * Constructs a new LevelCompletedOverlay object.
	 *
	 * @param playing The playing gamestate.
	 */
	public LevelCompletedOverlay(Playing playing) {
		this.playing = playing;
		initImg();
		initButtons();
	}

	/**
	 * Initializes the buttons for the level completed overlay.
	 */
	private void initButtons() {
		int menuX = (int) (330 * Game.SCALE);
		int nextX = (int) (445 * Game.SCALE);
		int y = (int) (195 * Game.SCALE);
		next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
		menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
	}

	/**
	 * Initializes the image for the level completed overlay.
	 */
	private void initImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_IMG);
		bgW = (int) (img.getWidth() * Game.SCALE);
		bgH = (int) (img.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (75 * Game.SCALE);
	}

	/**
	 * Draws the level completed overlay on the screen.
	 *
	 * @param g The Graphics object.
	 */
	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		g.drawImage(img, bgX, bgY, bgW, bgH, null);
		next.draw(g);
		menu.draw(g);
	}

	/**
	 * Updates the level completed overlay.
	 */
	public void update() {
		next.update();
		menu.update();
	}

	/**
	 * Checks if the mouse is moved over any button of the overlay.
	 *
	 * @param e The MouseEvent object.
	 */
	public void mouseMoved(MouseEvent e) {
		next.setMouseOver(false);
		menu.setMouseOver(false);

		if (isIn(menu, e))
			menu.setMouseOver(true);
		else if (isIn(next, e))
			next.setMouseOver(true);
	}

	/**
	 * Handles mouse release events on the overlay buttons.
	 *
	 * @param e The MouseEvent object.
	 */
	public void mouseReleased(MouseEvent e) {
		if (isIn(menu, e)) {
			if (menu.isMousePressed()) {
				playing.resetAll();
				Gamestate.state = Gamestate.MENU;
			}
		} else if (isIn(next, e))
			if (next.isMousePressed())
				playing.loadNextLevel();

		menu.resetBools();
		next.resetBools();
	}

	/**
	 * Handles mouse press events on the overlay buttons.
	 *
	 * @param e The MouseEvent object.
	 */
	public void mousePressed(MouseEvent e) {
		if (isIn(menu, e))
			menu.setMousePressed(true);
		else if (isIn(next, e))
			next.setMousePressed(true);
	}

	/**
	 * Checks if a given MouseEvent occurs within a specific UrmButton.
	 *
	 * @param b The UrmButton to check against.
	 * @param e The MouseEvent object.
	 * @return true if the MouseEvent occurs within the bounds of the UrmButton, false otherwise.
	 */
	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}
}
