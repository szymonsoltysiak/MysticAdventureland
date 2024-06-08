package objects;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.ObjectConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

/**
 * The GameObject class represents a generic game object.
 * It provides basic functionalities common to all game objects.
 */
public class GameObject {

	protected int x, y, objType;
	protected Rectangle2D.Float hitbox;
	protected boolean doAnimation, active = true;
	protected int aniTick, aniIndex;
	protected int xDrawOffset, yDrawOffset;

	/**
	 * Constructs a new GameObject with the specified coordinates and object type.
	 *
	 * @param x        The x-coordinate of the object.
	 * @param y        The y-coordinate of the object.
	 * @param objType  The type of the object.
	 */
	public GameObject(int x, int y, int objType) {
		this.x = x;
		this.y = y;
		this.objType = objType;
	}

	/**
	 * Updates the animation tick of the object.
	 */
	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(objType)) {
				aniIndex = 0;
				if (objType == BARREL || objType == BOX) {
					doAnimation = false;
					active = false;
				} else if (objType == CANNON_LEFT || objType == CANNON_RIGHT)
					doAnimation = false;
			}
		}
	}

	/**
	 * Resets the object's animation state.
	 */
	public void reset() {
		aniIndex = 0;
		aniTick = 0;
		active = true;

		if (objType == BARREL || objType == BOX || objType == CANNON_LEFT || objType == CANNON_RIGHT)
			doAnimation = false;
		else
			doAnimation = true;
	}

	/**
	 * Initializes the hitbox of the object with the specified dimensions.
	 *
	 * @param width   The width of the hitbox.
	 * @param height  The height of the hitbox.
	 */
	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
	}

	/**
	 * Draws the hitbox of the object.
	 *
	 * @param g          The Graphics object to draw on.
	 * @param xLvlOffset The level offset in the x-direction.
	 */
	public void drawHitbox(Graphics g, int xLvlOffset) {
		g.setColor(Color.PINK);
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}

	/**
	 * Returns the type of the object.
	 *
	 * @return The type of the object.
	 */
	public int getObjType() {
		return objType;
	}

	/**
	 * Returns the hitbox of the object.
	 *
	 * @return The hitbox of the object.
	 */
	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	/**
	 * Returns whether the object is active.
	 *
	 * @return true if the object is active, false otherwise.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the activity state of the object.
	 *
	 * @param active The activity state to set.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Sets the animation state of the object.
	 *
	 * @param doAnimation The animation state to set.
	 */
	public void setAnimation(boolean doAnimation) {
		this.doAnimation = doAnimation;
	}

	/**
	 * Returns the x draw offset of the object.
	 *
	 * @return The x draw offset of the object.
	 */
	public int getxDrawOffset() {
		return xDrawOffset;
	}

	/**
	 * Returns the y draw offset of the object.
	 *
	 * @return The y draw offset of the object.
	 */
	public int getyDrawOffset() {
		return yDrawOffset;
	}

	/**
	 * Returns the animation index of the object.
	 *
	 * @return The animation index of the object.
	 */
	public int getAniIndex() {
		return aniIndex;
	}

	/**
	 * Returns the animation tick of the object.
	 *
	 * @return The animation tick of the object.
	 */
	public int getAniTick() {
		return aniTick;
	}
}
