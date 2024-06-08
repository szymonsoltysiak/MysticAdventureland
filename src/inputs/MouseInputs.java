package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.Gamestate;
import main.GamePanel;

/**
 * Handles mouse input events including mouse clicks and mouse movements.
 */
public class MouseInputs implements MouseListener, MouseMotionListener {
	private GamePanel gamePanel;

	/**
	 * Constructs a MouseInputs object with the specified GamePanel.
	 *
	 * @param gamePanel The GamePanel to associate with this MouseInputs object.
	 */
	public MouseInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	/**
	 * Invoked when a mouse button is pressed on the GamePanel and the mouse is dragged.
	 *
	 * @param e The MouseEvent representing the mouse dragged event.
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		switch (Gamestate.state) {
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseDragged(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().mouseDragged(e);
			break;
		default:
			break;

		}

	}

	/**
	 * Invoked when the mouse cursor has been moved.
	 *
	 * @param e The MouseEvent representing the mouse moved event.
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		switch (Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseMoved(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseMoved(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().mouseMoved(e);
			break;
		default:
			break;

		}

	}

	/**
	 * Invoked when the mouse button has been clicked (pressed and released).
	 *
	 * @param e The MouseEvent representing the mouse click event.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		switch (Gamestate.state) {
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseClicked(e);
			break;
		default:
			break;

		}

	}

	/**
	 * Invoked when a mouse button has been pressed.
	 *
	 * @param e The MouseEvent representing the mouse press event.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		switch (Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().mousePressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mousePressed(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().mousePressed(e);
			break;
		default:
			break;

		}
	}

	/**
	 * Invoked when a mouse button has been released.
	 *
	 * @param e The MouseEvent representing the mouse release event.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		switch (Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseReleased(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseReleased(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().mouseReleased(e);
			break;
		default:
			break;

		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

}