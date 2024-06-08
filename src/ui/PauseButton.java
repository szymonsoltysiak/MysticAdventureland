package ui;

import java.awt.Rectangle;

/**
 * Represents a pause button used in the user interface.
 */
public class PauseButton {

	protected int x, y, width, height;
	protected Rectangle bounds;

	/**
	 * Constructs a new PauseButton object.
	 *
	 * @param x      The x-coordinate of the button's position.
	 * @param y      The y-coordinate of the button's position.
	 * @param width  The width of the button.
	 * @param height The height of the button.
	 */
	public PauseButton(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		createBounds();
	}

	/**
	 * Creates the bounding rectangle for the button.
	 */
	private void createBounds() {
		bounds = new Rectangle(x, y, width, height);
	}

	/**
	 * Gets the x-coordinate of the button.
	 *
	 * @return The x-coordinate of the button.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x-coordinate of the button.
	 *
	 * @param x The new x-coordinate of the button.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the y-coordinate of the button.
	 *
	 * @return The y-coordinate of the button.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y-coordinate of the button.
	 *
	 * @param y The new y-coordinate of the button.
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Gets the width of the button.
	 *
	 * @return The width of the button.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width of the button.
	 *
	 * @param width The new width of the button.
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Gets the height of the button.
	 *
	 * @return The height of the button.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height of the button.
	 *
	 * @param height The new height of the button.
	 */
	public void setHeight(int height) {
		this.height = height;
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
	 * Sets the bounding rectangle of the button.
	 *
	 * @param bounds The new bounding rectangle of the button.
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

}
