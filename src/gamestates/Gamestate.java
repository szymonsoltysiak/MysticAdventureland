package gamestates;

/**
 * The Gamestate enum represents the different states of the game.
 * It includes states such as playing, menu, options, and quit.
 */
public enum Gamestate {

	PLAYING, MENU, OPTIONS, QUIT;

	/** The current state of the game. */
	public static Gamestate state = MENU;
}
