package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.AudioOptions;
import ui.PauseButton;
import ui.UrmButton;
import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.*;

/**
 * The GameOptions class represents the game options state, where players can adjust settings like audio.
 * It extends the State class and implements the Statemethods interface to handle state-specific behavior.
 */
public class GameOptions extends State implements Statemethods {

	private AudioOptions audioOptions;
	private BufferedImage backgroundImg, optionsBackgroundImg;
	private int bgX, bgY, bgW, bgH;
	private UrmButton menuB;

	/**
	 * Constructs a new GameOptions instance.
	 *
	 * @param game The Game instance this state belongs to.
	 */
	public GameOptions(Game game) {
		super(game);
		loadImgs();
		loadButton();
		audioOptions = game.getAudioOptions();
	}

	/**
	 * Loads the buttons used in the game options menu.
	 */
	private void loadButton() {
		int menuX = (int) (387 * Game.SCALE);
		int menuY = (int) (325 * Game.SCALE);
		menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
	}

	/**
	 * Loads the images used in the game options menu.
	 */
	private void loadImgs() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
		optionsBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_MENU);
		bgW = (int) (optionsBackgroundImg.getWidth() * Game.SCALE);
		bgH = (int) (optionsBackgroundImg.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (33 * Game.SCALE);
	}

	/**
	 * Updates the state of the game options, including buttons and audio options.
	 */
	@Override
	public void update() {
		menuB.update();
		audioOptions.update();
	}

	/**
	 * Draws the game options menu, including background and buttons.
	 *
	 * @param g The graphics context to draw on.
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH, null);
		menuB.draw(g);
		audioOptions.draw(g);
	}

	/**
	 * Handles mouse drag events for audio options.
	 *
	 * @param e The MouseEvent triggered by dragging the mouse.
	 */
	public void mouseDragged(MouseEvent e) {
		audioOptions.mouseDragged(e);
	}

	/**
	 * Handles mouse press events, updating button states accordingly.
	 *
	 * @param e The MouseEvent triggered by pressing the mouse button.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (isIn(e, menuB)) {
			menuB.setMousePressed(true);
		} else {
			audioOptions.mousePressed(e);
		}
	}

	/**
	 * Handles mouse release events, changing the game state if necessary.
	 *
	 * @param e The MouseEvent triggered by releasing the mouse button.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (isIn(e, menuB)) {
			if (menuB.isMousePressed()) {
				Gamestate.state = Gamestate.MENU;
			}
		} else {
			audioOptions.mouseReleased(e);
		}
		menuB.resetBools();
	}

	/**
	 * Handles mouse move events, updating button hover states.
	 *
	 * @param e The MouseEvent triggered by moving the mouse.
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		menuB.setMouseOver(false);
		if (isIn(e, menuB)) {
			menuB.setMouseOver(true);
		} else {
			audioOptions.mouseMoved(e);
		}
	}

	/**
	 * Handles key press events, such as pressing the escape key to return to the menu.
	 *
	 * @param e The KeyEvent triggered by pressing a key.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Gamestate.state = Gamestate.MENU;
		}
	}

	/**
	 * Handles key release events. This method is currently not implemented.
	 *
	 * @param e The KeyEvent triggered by releasing a key.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	/**
	 * Handles mouse click events. This method is currently not implemented.
	 *
	 * @param e The MouseEvent triggered by clicking the mouse button.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	/**
	 * Checks if the mouse event occurred within the bounds of a given button.
	 *
	 * @param e The MouseEvent to check.
	 * @param b The PauseButton to check against.
	 * @return True if the mouse event is within the button's bounds, false otherwise.
	 */
	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}
}
