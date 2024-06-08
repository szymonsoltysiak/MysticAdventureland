package objects;

import java.awt.geom.Rectangle2D;

import main.Game;

import static utilz.Constants.Projectiles.*;

/**
 * Represents a projectile fired by cannons in the game.
 */
public class Projectile {
	private Rectangle2D.Float hitbox;
	private int dir;
	private boolean active = true;

	/**
	 * Constructs a new Projectile object.
	 *
	 * @param x   The x-coordinate of the projectile's position.
	 * @param y   The y-coordinate of the projectile's position.
	 * @param dir The direction of the projectile (1 for right, -1 for left).
	 */
	public Projectile(int x, int y, int dir) {
		int xOffset = (int) (-3 * Game.SCALE);
		int yOffset = (int) (5 * Game.SCALE);

		if (dir == 1)
			xOffset = (int) (29 * Game.SCALE);

		hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
		this.dir = dir;
	}

	/**
	 * Updates the position of the projectile.
	 */
	public void updatePos() {
		hitbox.x += dir * SPEED;
	}


	/**
	 * Sets the position of the projectile.
	 *
	 * @param x The x-coordinate of the new position.
	 * @param y The y-coordinate of the new position.
	 */
	public void setPos(int x, int y) {
		hitbox.x = x;
		hitbox.y = y;
	}

	/**
	 * Retrieves the hitbox of the projectile.
	 *
	 * @return The hitbox of the projectile.
	 */
	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	/**
	 * Sets the active status of the projectile.
	 *
	 * @param active The active status of the projectile.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Checks if the projectile is active.
	 *
	 * @return True if the projectile is active, otherwise false.
	 */
	public boolean isActive() {
		return active;
	}

}
