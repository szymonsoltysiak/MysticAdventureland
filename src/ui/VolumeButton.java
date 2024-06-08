package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;
import static utilz.Constants.UI.VolumeButtons.*;

/**
 * Represents a volume control button used in the UI.
 */
public class VolumeButton extends PauseButton {

	private BufferedImage[] imgs;
	private BufferedImage slider;
	private int index = 0;
	private boolean mouseOver, mousePressed;
	private int buttonX, minX, maxX;

	/**
	 * Constructs a new VolumeButton object.
	 *
	 * @param x      The x-coordinate of the button.
	 * @param y      The y-coordinate of the button.
	 * @param width  The width of the button.
	 * @param height The height of the button.
	 */
	public VolumeButton(int x, int y, int width, int height) {
		super(x + width / 2, y, VOLUME_WIDTH, height);
		bounds.x -= VOLUME_WIDTH / 2;
		buttonX = x + width / 2;
		this.x = x;
		this.width = width;
		minX = x + VOLUME_WIDTH / 2;
		maxX = x + width - VOLUME_WIDTH / 2;
		loadImgs();
	}

	/**
	 * Loads the button images.
	 */
	private void loadImgs() {
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
		imgs = new BufferedImage[3];
		for (int i = 0; i < imgs.length; i++)
			imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

		slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

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

		g.drawImage(slider, x, y, width, height, null);
		g.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);

	}

	/**
	 * Changes the x-coordinate of the button.
	 *
	 * @param x The new x-coordinate.
	 */
	public void changeX(int x) {
		if (x < minX)
			buttonX = minX;
		else if (x > maxX)
			buttonX = maxX;
		else
			buttonX = x;

		bounds.x = buttonX - VOLUME_WIDTH / 2;

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
