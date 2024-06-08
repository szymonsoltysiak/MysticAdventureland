package ui;

import static utilz.Constants.UI.PauseButtons.SOUND_SIZE;
import static utilz.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utilz.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import gamestates.Gamestate;
import main.Game;

/**
 * Class for displaying the audio options in the game
 */
public class AudioOptions {

	private VolumeButton volumeButton;
	private SoundButton musicButton, sfxButton;

	/**
	 * Constructs a new AudioOptions object and initializes sound buttons and volume button.
	 */
	public AudioOptions() {
		createSoundButtons();
		createVolumeButton();
	}

	/**
	 * Creates the volume button and sets its initial position.
	 */
	private void createVolumeButton() {
		int vX = (int) (309 * Game.SCALE);
		int vY = (int) (278 * Game.SCALE);
		volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
	}

	/**
	 * Creates the sound button and sets its initial position.
	 */
	private void createSoundButtons() {
		int soundX = (int) (450 * Game.SCALE);
		int musicY = (int) (140 * Game.SCALE);
		int sfxY = (int) (186 * Game.SCALE);
		musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
		sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
	}

	/**
	 * Updates the audio options.
	 */
	public void update() {
		musicButton.update();
		sfxButton.update();

		volumeButton.update();
	}

	/**
	 * Draws the audio options on the screen.
	 *
	 * @param g The graphics object used for rendering.
	 */
	public void draw(Graphics g) {
		// Sound buttons
		musicButton.draw(g);
		sfxButton.draw(g);

		// Volume Button
		volumeButton.draw(g);
	}

	/**
	 * Handles the mouse dragging event for adjusting the volume slider.
	 *
	 * @param e The MouseEvent containing information about the mouse drag event.
	 */
	public void mouseDragged(MouseEvent e) {
		if (volumeButton.isMousePressed()) {
			volumeButton.changeX(e.getX());
		}
	}

	/**
	 * Handles the mouse pressed event.
	 *
	 * @param e The MouseEvent containing information about the mouse press event.
	 */
	public void mousePressed(MouseEvent e) {
		if (isIn(e, musicButton))
			musicButton.setMousePressed(true);
		else if (isIn(e, sfxButton))
			sfxButton.setMousePressed(true);
		else if (isIn(e, volumeButton))
			volumeButton.setMousePressed(true);
	}

	/**
	 * Handles the mouse released event.
	 *
	 * @param e The MouseEvent containing information about the mouse press event.
	 */
	public void mouseReleased(MouseEvent e) {
		if (isIn(e, musicButton)) {
			if (musicButton.isMousePressed())
				musicButton.setMuted(!musicButton.isMuted());

		} else if (isIn(e, sfxButton)) {
			if (sfxButton.isMousePressed())
				sfxButton.setMuted(!sfxButton.isMuted());
		}

		musicButton.resetBools();
		sfxButton.resetBools();

		volumeButton.resetBools();
	}

	/**
	 * Handles the mouse moved event.
	 *
	 * @param e The MouseEvent containing information about the mouse press event.
	 */
	public void mouseMoved(MouseEvent e) {
		musicButton.setMouseOver(false);
		sfxButton.setMouseOver(false);

		volumeButton.setMouseOver(false);

		if (isIn(e, musicButton))
			musicButton.setMouseOver(true);
		else if (isIn(e, sfxButton))
			sfxButton.setMouseOver(true);
		else if (isIn(e, volumeButton))
			volumeButton.setMouseOver(true);
	}

	/**
	 * Checks if the given MouseEvent coordinates are within the bounds of the specified PauseButton.
	 *
	 * @param e The MouseEvent containing coordinates to check.
	 * @param b The PauseButton to check against.
	 * @return {@code true} if the coordinates are within the button's bounds, {@code false} otherwise.
	 */
	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

}
