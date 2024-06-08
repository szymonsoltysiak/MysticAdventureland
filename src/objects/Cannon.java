package objects;

import main.Game;

/**
 * The Cannon class represents a cannon object in the game.
 * It extends the GameObject class and provides specific functionality for cannons.
 */
public class Cannon extends GameObject {

	private int tileY;

	/**
	 * Constructs a new Cannon object with the specified coordinates and object type.
	 *
	 * @param x        The x-coordinate of the cannon.
	 * @param y        The y-coordinate of the cannon.
	 * @param objType  The type of the cannon object.
	 */
	public Cannon(int x, int y, int objType) {
		super(x, y, objType);
		tileY = y / Game.TILES_SIZE;
		initHitbox(40, 26);
		hitbox.x -= (int) (4 * Game.SCALE);
		hitbox.y += (int) (6 * Game.SCALE);
	}

	/**
	 * Updates the cannon object.
	 * This method is called during the game update loop.
	 */
	public void update() {
		if (doAnimation)
			updateAnimationTick();
	}

	/**
	 * Gets the y-coordinate of the tile where the cannon is located.
	 *
	 * @return The y-coordinate of the tile.
	 */
	public int getTileY() {
		return tileY;
	}

}

