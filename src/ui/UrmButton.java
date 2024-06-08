package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.*;

/**
 * Represents a button used in the UI.
 */
public class UrmButton extends PauseButton {

	private BufferedImage[] imgs;
	private int rowIndex, index;
	private boolean mouseOver, mousePressed;

	/**
	 * Constructs a new UrmButton object.
	 *
	 * @param x        The x-coordinate of the button.
	 * @param y        The y-coordinate of the button.
	 * @param width    The width of the button.
	 * @param height   The height of the button.
	 * @param rowIndex The row index of the button in the sprite sheet.
	 */
	public UrmButton(int x, int y, int width, int height, int rowIndex) {
		super(x, y, width, height);
		this.rowIndex = rowIndex;
		loadImgs();
	}

	/**
	 * Loads the button images.
	 */
	private void loadImgs() {
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
		imgs = new BufferedImage[3];
		for (int i = 0; i < imgs.length; i++)
			imgs[i] = temp.getSubimage(i * URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);

	}

	/**
	 * Updates the button state.
	 */
	public void update() {
		index = 0;
		if (mouseOver)
			index = 1;
		if (mousePressed)
			index = 2;
	}

	/**
	 * Draws the button.
	 *
	 * @param g The graphics object.
	 */
	public void draw(Graphics g) {
		g.drawImage(imgs[index], x, y, URM_SIZE, URM_SIZE, null);
	}

	/**
	 * Resets the button state.
	 */
	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}

	/**
	 * Checks if the mouse is over the button.
	 *
	 * @return True if the mouse is over the button, false otherwise.
	 */
	public boolean isMouseOver() {
		return mouseOver;
	}

	/**
	 * Sets the mouse over state of the button.
	 *
	 * @param mouseOver True to set the mouse over state, false otherwise.
	 */
	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	/**
	 * Checks if the button is pressed.
	 *
	 * @return True if the button is pressed, false otherwise.
	 */
	public boolean isMousePressed() {
		return mousePressed;
	}

	/**
	 * Sets the pressed state of the button.
	 *
	 * @param mousePressed True to set the pressed state, false otherwise.
	 */
	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

}
