package objects;

import main.Game;

/**
 * Represents a potion object in the game.
 * Potions provide various effects to the player when collected.
 */
public class Potion extends GameObject {

	private float hoverOffset;
	private int maxHoverOffset, hoverDir = 1;

	/**
	 * Constructs a new Potion object.
	 *
	 * @param x       The x-coordinate of the potion's position.
	 * @param y       The y-coordinate of the potion's position.
	 * @param objType The type of the potion object.
	 */
	public Potion(int x, int y, int objType) {
		super(x, y, objType);
		doAnimation = true;

		initHitbox(7, 14);

		xDrawOffset = (int) (3 * Game.SCALE);
		yDrawOffset = (int) (2 * Game.SCALE);

		maxHoverOffset = (int) (10 * Game.SCALE);
	}

	/**
	 * Updates the potion object.
	 */
	public void update() {
		updateAnimationTick();
		updateHover();
	}

	/**
	 * Updates the hover effect of the potion object.
	 * The potion moves up and down within a certain range.
	 */
	private void updateHover() {
		hoverOffset += (0.075f * Game.SCALE * hoverDir);

		if (hoverOffset >= maxHoverOffset)
			hoverDir = -1;
		else if (hoverOffset < 0)
			hoverDir = 1;

		hitbox.y = y + hoverOffset;
	}
}
