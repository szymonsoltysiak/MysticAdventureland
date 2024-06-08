package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;
import static utilz.Constants.UI.PauseButtons.*;

/**
 * Represents a sound button used in the pause menu.
 */
public class SoundButton extends PauseButton {

	private BufferedImage[][] soundImgs;
	private boolean mouseOver, mousePressed;
	private boolean muted;
	private int rowIndex, colIndex;

	/**
	 * Constructs a new SoundButton object.
	 *
	 * @param x      The x-coordinate of the button.
	 * @param y      The y-coordinate of the button.
	 * @param width  The width of the button.
	 * @param height The height of the button.
	 */
	public SoundButton(int x, int y, int width, int height) {
		super(x, y, width, height);

		loadSoundImgs();
	}

	/**
	 * Loads the sound images.
	 */
	private void loadSoundImgs() {
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
		soundImgs = new BufferedImage[2][3];
		for (int j = 0; j < soundImgs.length; j++)
			for (int i = 0; i < soundImgs[j].length; i++)
				soundImgs[j][i] = temp.getSubimage(i * SOUND_SIZE_DEFAULT, j * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
	}

	/**
	 * Updates the button state.
	 */
	public void update() {
		if (muted)
			rowIndex = 1;
		else
			rowIndex = 0;

		colIndex = 0;
		if (mouseOver)
			colIndex = 1;
		if (mousePressed)
			colIndex = 2;
	}

	/**
	 * Resets the button state.
	 */
	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}

	/**
	 * Draws the button.
	 *
	 * @param g The graphics object.
	 */
	public void draw(Graphics g) {
		g.drawImage(soundImgs[rowIndex][colIndex], x, y, width, height, null);
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

	/**
	 * Checks if the sound is muted.
	 *
	 * @return True if the sound is muted, false otherwise.
	 */
	public boolean isMuted() {
		return muted;
	}

	/**
	 * Sets the mute state of the button.
	 *
	 * @param muted True to mute the sound, false to unmute.
	 */
	public void setMuted(boolean muted) {
		this.muted = muted;
	}

}
