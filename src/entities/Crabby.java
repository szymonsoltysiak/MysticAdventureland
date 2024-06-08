package entities;

import static utilz.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;

import main.Game;

/**
 * The Crabby class represents a specific type of enemy in the game.
 * It defines the behavior, attributes, and interactions of the Crabby enemy.
 */
public class Crabby extends Enemy {

	private int attackBoxOffsetX;

	/**
	 * Constructs a new Crabby instance at the specified coordinates.
	 *
	 * @param x The x-coordinate of the Crabby's position.
	 * @param y The y-coordinate of the Crabby's position.
	 */
	public Crabby(float x, float y) {
		super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
		initHitbox(22, 19);
		initAttackBox();
	}

	/**
	 * Initializes the attack box for the Crabby.
	 */
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (82 * Game.SCALE), (int) (19 * Game.SCALE));
		attackBoxOffsetX = (int) (Game.SCALE * 30);
	}

	/**
	 * Updates the state and behavior of the Crabby.
	 *
	 * @param lvlData The level data for the current level.
	 * @param player The player instance.
	 */
	public void update(int[][] lvlData, Player player) {
		updateBehavior(lvlData, player);
		updateAnimationTick();
		updateAttackBox();
	}

	/**
	 * Updates the position of the attack box based on the Crabby's hitbox position.
	 */
	private void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX;
		attackBox.y = hitbox.y;
	}

	/**
	 * Updates the behavior of the Crabby based on its current state and interactions.
	 *
	 * @param lvlData The level data for the current level.
	 * @param player The player instance.
	 */
	private void updateBehavior(int[][] lvlData, Player player) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			updateInAir(lvlData);
		else {
			switch (state) {
				case IDLE:
					newState(RUNNING);
					break;
				case RUNNING:
					if (canSeePlayer(lvlData, player)) {
						turnTowardsPlayer(player);
						if (isPlayerCloseForAttack(player))
							newState(ATTACK);
					}

					move(lvlData);
					break;
				case ATTACK:
					if (aniIndex == 0)
						attackChecked = false;
					if (aniIndex == 3 && !attackChecked)
						checkPlayerHit(attackBox, player);
					break;
				case HIT:
					break;
			}
		}
	}

	/**
	 * Determines the x-coordinate flip value for the Crabby's sprite.
	 *
	 * @return The x-coordinate flip value.
	 */
	public int flipX() {
		if (walkDir == RIGHT)
			return width;
		else
			return 0;
	}

	/**
	 * Determines the width flip value for the Crabby's sprite.
	 *
	 * @return The width flip value.
	 */
	public int flipW() {
		if (walkDir == RIGHT)
			return -1;
		else
			return 1;
	}
}
