package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

/**
 * The Entity class represents a generic entity in the game.
 * It defines common attributes and behaviors for all game entities.
 */
public abstract class Entity {

	protected float x, y;
	protected int width, height;
	protected Rectangle2D.Float hitbox;
	protected int aniTick, aniIndex;
	protected int state;
	protected float airSpeed;
	protected boolean inAir = false;
	protected int maxHealth;
	protected int currentHealth;
	protected Rectangle2D.Float attackBox;
	protected float walkSpeed;

	/**
	 * Constructs an Entity instance with the specified position and dimensions.
	 *
	 * @param x      The x-coordinate of the entity's position.
	 * @param y      The y-coordinate of the entity's position.
	 * @param width  The width of the entity.
	 * @param height The height of the entity.
	 */
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Draws the entity's attack box for debugging purposes.
	 *
	 * @param g         The graphics context to draw on.
	 * @param xLvlOffset The x-axis level offset for drawing.
	 */
	protected void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}

	/**
	 * Draws the entity's hitbox for debugging purposes.
	 *
	 * @param g         The graphics context to draw on.
	 * @param xLvlOffset The x-axis level offset for drawing.
	 */
	protected void drawHitbox(Graphics g, int xLvlOffset) {
		g.setColor(Color.PINK);
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}

	/**
	 * Initializes the hitbox for the entity.
	 *
	 * @param width  The width of the hitbox.
	 * @param height The height of the hitbox.
	 */
	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
	}

	/**
	 * Returns the hitbox of the entity.
	 *
	 * @return The hitbox of the entity.
	 */
	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	/**
	 * Returns the current state of the entity.
	 *
	 * @return The current state of the entity.
	 */
	public int getState() {
		return state;
	}

	/**
	 * Returns the current animation index of the entity.
	 *
	 * @return The current animation index of the entity.
	 */
	public int getAniIndex() {
		return aniIndex;
	}
}
