package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import utilz.LoadSave;
import static utilz.Constants.UI.Buttons.*;

/**
 * Represents a button used in the game menu.
 */
public class MenuButton {
	private int xPos, yPos, rowIndex, index;
	private int xOffsetCenter = B_WIDTH / 2;
	private Gamestate state;
	private BufferedImage[] imgs;
	private boolean mouseOver, mousePressed;
	private Rectangle bounds;

	/**
	 * Constructs a new MenuButton object.
	 *
	 * @param xPos     The x-coordinate of the button's position.
	 * @param yPos     The y-coordinate of the button's position.
	 * @param rowIndex The index of the row in the sprite atlas.
	 * @param state    The gamestate associated with the button.
	 */
	public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rowIndex = rowIndex;
		this.state = state;
		loadImgs();
		initBounds();
	}

	/**
	 * Initializes the bounds of the button.
	 */
	private void initBounds() {
		bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
	}

	/**
	 * Loads the images for the button from the sprite atlas.
	 */
	private void loadImgs() {
		imgs = new BufferedImage[3];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
		for (int i = 0; i < imgs.length; i++)
			imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
	}

	/**
	 * Draws the button on the screen.
	 *
	 * @param g The Graphics object.
	 */
	public void draw(Graphics g) {
		g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
	}

	/**
	 * Updates the state of the button.
	 */
	public void update() {
		index = 0;
		if (mouseOver)
			index = 1;
		if (mousePressed)
			index = 2;
	}

	/**
	 * Checks if the mouse is currently over the button.
	 *
	 * @return true if the mouse is over the button, false otherwise.
	 */
	public boolean isMouseOver() {
		return mouseOver;
	}

	/**
	 * Sets the state of mouseOver for the button.
	 *
	 * @param mouseOver The new state of mouseOver.
	 */
	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	/**
	 * Checks if the button is currently being pressed by the mouse.
	 *
	 * @return true if the button is being pressed, false otherwise.
	 */
	public boolean isMousePressed() {
		return mousePressed;
	}

	/**
	 * Sets the state of mousePressed for the button.
	 *
	 * @param mousePressed The new state of mousePressed.
	 */
	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	/**
	 * Gets the bounding rectangle of the button.
	 *
	 * @return The bounding rectangle of the button.
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * Applies the associated gamestate when the button is clicked.
	 */
	public void applyGamestate() {
		Gamestate.state = state;
	}

	/**
	 * Resets the mouseOver and mousePressed states of the button.
	 */
	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}
}
