package objects;

import static utilz.Constants.ObjectConstants.*;

import main.Game;

/**
 * The GameContainer class represents a container object in the game.
 * It extends the GameObject class and provides specific functionality for containers.
 */
public class GameContainer extends GameObject {

	/**
	 * Constructs a new GameContainer object with the specified coordinates and object type.
	 *
	 * @param x        The x-coordinate of the container.
	 * @param y        The y-coordinate of the container.
	 * @param objType  The type of the container object.
	 */
	public GameContainer(int x, int y, int objType) {
		super(x, y, objType);
		createHitbox();
	}

	private void createHitbox() {
		if (objType == BOX) {
			initHitbox(25, 18);
			xDrawOffset = (int) (7 * Game.SCALE);
			yDrawOffset = (int) (12 * Game.SCALE);
		} else {
			initHitbox(23, 25);
			xDrawOffset = (int) (8 * Game.SCALE);
			yDrawOffset = (int) (5 * Game.SCALE);
		}
		hitbox.y += yDrawOffset + (int) (Game.SCALE * 2);
		hitbox.x += xDrawOffset / 2;
	}

	/**
	 * Updates the container object.
	 * This method is called during the game update loop.
	 */
	public void update() {
		if (doAnimation)
			updateAnimationTick();
	}
}
