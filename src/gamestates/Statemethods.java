package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Defines the methods that must be implemented by classes representing different game states.
 */
public interface Statemethods {

	/**
	 * Updates the state of the game.
	 */
	public void update();

	/**
	 * Draws the graphical elements of the game.
	 *
	 * @param g The Graphics object used for rendering.
	 */
	public void draw(Graphics g);

	/**
	 * Handles mouse click events.
	 *
	 * @param e The MouseEvent representing the mouse click event.
	 */
	public void mouseClicked(MouseEvent e);

	/**
	 * Handles mouse press events.
	 *
	 * @param e The MouseEvent representing the mouse press event.
	 */
	public void mousePressed(MouseEvent e);

	/**
	 * Handles mouse release events.
	 *
	 * @param e The MouseEvent representing the mouse release event.
	 */
	public void mouseReleased(MouseEvent e);

	/**
	 * Handles mouse move events.
	 *
	 * @param e The MouseEvent representing the mouse move event.
	 */
	public void mouseMoved(MouseEvent e);

	/**
	 * Handles key press events.
	 *
	 * @param e The KeyEvent representing the key press event.
	 */
	public void keyPressed(KeyEvent e);

	/**
	 * Handles key release events.
	 *
	 * @param e The KeyEvent representing the key release event.
	 */
	public void keyReleased(KeyEvent e);

}
