package objects;

import main.Game;

/**
 * Represents a spike object in the game.
 */
public class Spike extends GameObject{

	/**
	 * Constructs a new Spike object.
	 *
	 * @param x       The x-coordinate of the spike's position.
	 * @param y       The y-coordinate of the spike's position.
	 * @param objType The type of the spike object.
	 */
	public Spike(int x, int y, int objType) {
		super(x, y, objType);

		initHitbox(32, 16);
		xDrawOffset = 0;
		yDrawOffset = (int)(Game.SCALE * 16);
		hitbox.y += yDrawOffset;
	}
}
