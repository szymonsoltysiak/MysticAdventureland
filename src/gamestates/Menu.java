package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

/**
 * The Menu class represents the main menu state of the game.
 * It handles the display of menu buttons and their interaction.
 */
public class Menu extends State implements Statemethods {

	private MenuButton[] buttons = new MenuButton[3];
	private BufferedImage backgroundImg, backgroundImgPink;
	private int menuX, menuY, menuWidth, menuHeight;

	/**
	 * Constructs a Menu object.
	 *
	 * @param game The game instance.
	 */
	public Menu(Game game) {
		super(game);
		loadButtons();
		loadBackground();
		backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
	}

	/**
	 * Loads the background image for the menu screen.
	 * This method initializes the background image, sets its dimensions, and calculates its position.
	 */
	private void loadBackground() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
		menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
		menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
		menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
		menuY = (int) (45 * Game.SCALE);
	}

	/**
	 * Loads the buttons for the menu screen.
	 * This method initializes each button with its position and associated game state.
	 */
	private void loadButtons() {
		buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (150 * Game.SCALE), 0, Gamestate.PLAYING);
		buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (220 * Game.SCALE), 1, Gamestate.OPTIONS);
		buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (290 * Game.SCALE), 2, Gamestate.QUIT);
	}

	/**
	 * Updates the menu screen by iterating through each button and updating its state.
	 */
	@Override
	public void update() {
		for (MenuButton mb : buttons)
			mb.update();
	}

	/**
	 * Draws the menu screen.
	 * This method draws the background image and buttons onto the graphics object provided.
	 *
	 * @param g The graphics object to draw onto.
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImgPink, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
		for (MenuButton mb : buttons)
			mb.draw(g);
	}

	/**
	 * Invoked when the mouse button has been clicked (pressed and released) on the component.
	 * This method is overridden from the MouseListener interface.
	 *
	 * @param e The MouseEvent object containing information about the mouse event.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO: Implement mouse click functionality
	}

	/**
	 * Invoked when a mouse button has been pressed on the component.
	 * This method is overridden from the MouseListener interface.
	 *
	 * @param e The MouseEvent object containing information about the mouse event.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				mb.setMousePressed(true);
			}
		}
	}

	/**
	 * Invoked when a mouse button has been released on the component.
	 * This method is overridden from the MouseListener interface.
	 *
	 * @param e The MouseEvent object containing information about the mouse event.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				if (mb.isMousePressed())
					mb.applyGamestate();
				break;
			}
		}
		resetButtons();
	}

	/**
	 * Resets the state of all menu buttons.
	 * This method is used to reset mouse press and mouse over states of all buttons.
	 */
	private void resetButtons() {
		for (MenuButton mb : buttons)
			mb.resetBools();
	}

	/**
	 * Invoked when the mouse cursor has been moved onto a component but no buttons have been pressed.
	 * This method is overridden from the MouseMotionListener interface.
	 *
	 * @param e The MouseEvent object containing information about the mouse event.
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		for (MenuButton mb : buttons)
			mb.setMouseOver(false);
		for (MenuButton mb : buttons)
			if (isIn(e, mb)) {
				mb.setMouseOver(true);
				break;
			}
	}

	/**
	 * Invoked when a key has been pressed.
	 * This method is overridden from the KeyListener interface.
	 *
	 * @param e The KeyEvent object containing information about the key event.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			Gamestate.state = Gamestate.PLAYING;
	}

	/**
	 * Invoked when a key has been released.
	 * This method is overridden from the KeyListener interface.
	 *
	 * @param e The KeyEvent object containing information about the key event.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO: Implement key release functionality
	}
}
