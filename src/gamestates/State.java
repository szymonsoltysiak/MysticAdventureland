package gamestates;

import java.awt.event.MouseEvent;

import main.Game;
import ui.MenuButton;

/**
 * Represents a state in the game.
 */
public class State {

	protected Game game;

	/**
	 * Initializes a new instance of the State class.
	 *
	 * @param game The game object.
	 */
	public State(Game game) {
		this.game = game;
	}

	/**
	 * Checks if a mouse event is within the boundaries of a menu button.
	 *
	 * @param e  The MouseEvent to check.
	 * @param mb The MenuButton to check against.
	 * @return   True if the mouse event is within the boundaries of the menu button, otherwise false.
	 */
	public boolean isIn(MouseEvent e, MenuButton mb) {
		return mb.getBounds().contains(e.getX(), e.getY());
	}

	/**
	 * Gets the game object associated with this state.
	 *
	 * @return The game object.
	 */
	public Game getGame() {
		return game;
	}
}
