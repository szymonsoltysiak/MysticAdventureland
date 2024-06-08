package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;
import static utilz.Constants.*;

import main.Game;

/**
 * The Enemy class represents a generic enemy in the game.
 * It defines common behavior, attributes, and interactions for all enemies.
 */
public abstract class Enemy extends Entity {
	protected int enemyType;
	protected boolean firstUpdate = true;
	protected int walkDir = LEFT;
	protected int tileY;
	protected float attackDistance = Game.TILES_SIZE;
	protected boolean active = true;
	protected boolean attackChecked;

	/**
	 * Constructs a new Enemy instance with the specified position and dimensions.
	 *
	 * @param x         The x-coordinate of the enemy's position.
	 * @param y         The y-coordinate of the enemy's position.
	 * @param width     The width of the enemy.
	 * @param height    The height of the enemy.
	 * @param enemyType The type of the enemy.
	 */
	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;

		maxHealth = GetMaxHealth(enemyType);
		currentHealth = maxHealth;
		walkSpeed = Game.SCALE * 0.35f;
	}

	/**
	 * Performs the first update check to determine if the enemy is in the air.
	 *
	 * @param lvlData The level data for the current level.
	 */
	protected void firstUpdateCheck(int[][] lvlData) {
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		firstUpdate = false;
	}

	/**
	 * Updates the enemy's position while it is in the air.
	 *
	 * @param lvlData The level data for the current level.
	 */
	protected void updateInAir(int[][] lvlData) {
		if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += airSpeed;
			airSpeed += GRAVITY;
		} else {
			inAir = false;
			hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
		}
	}

	/**
	 * Moves the enemy based on its walking direction and level data.
	 *
	 * @param lvlData The level data for the current level.
	 */
	protected void move(int[][] lvlData) {
		float xSpeed = 0;

		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (IsFloor(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}

		changeWalkDir();
	}

	/**
	 * Turns the enemy towards the player.
	 *
	 * @param player The player instance.
	 */
	protected void turnTowardsPlayer(Player player) {
		if (player.hitbox.x > hitbox.x)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}

	/**
	 * Checks if the enemy can see the player based on their positions and level data.
	 *
	 * @param lvlData The level data for the current level.
	 * @param player  The player instance.
	 * @return true if the enemy can see the player, false otherwise.
	 */
	protected boolean canSeePlayer(int[][] lvlData, Player player) {
		int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
		if (playerTileY == tileY)
			if (isPlayerInRange(player)) {
				if (IsSightClear(lvlData, hitbox, player.hitbox, tileY))
					return true;
			}

		return false;
	}

	/**
	 * Checks if the player is within a certain range of the enemy.
	 *
	 * @param player The player instance.
	 * @return true if the player is within range, false otherwise.
	 */
	protected boolean isPlayerInRange(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= attackDistance * 5;
	}

	/**
	 * Checks if the player is close enough for the enemy to attack.
	 *
	 * @param player The player instance.
	 * @return true if the player is close enough for an attack, false otherwise.
	 */
	protected boolean isPlayerCloseForAttack(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= attackDistance;
	}

	/**
	 * Changes the state of the enemy.
	 *
	 * @param enemyState The new state of the enemy.
	 */
	protected void newState(int enemyState) {
		this.state = enemyState;
		aniTick = 0;
		aniIndex = 0;
	}

	/**
	 * Reduces the enemy's health by the specified amount.
	 *
	 * @param amount The amount of damage to inflict on the enemy.
	 */
	public void hurt(int amount) {
		currentHealth -= amount;
		if (currentHealth <= 0)
			newState(DEAD);
		else
			newState(HIT);
	}

	/**
	 * Checks if the player has been hit by the enemy's attack.
	 *
	 * @param attackBox The attack box of the enemy.
	 * @param player    The player instance.
	 */
	protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
		if (attackBox.intersects(player.hitbox))
			player.changeHealth(-GetEnemyDmg(enemyType));
		attackChecked = true;
	}

	/**
	 * Updates the animation tick for the enemy's animations.
	 */
	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(enemyType, state)) {
				aniIndex = 0;

				switch (state) {
					case ATTACK, HIT -> state = IDLE;
					case DEAD -> active = false;
				}
			}
		}
	}

	/**
	 * Changes the walking direction of the enemy.
	 */
	protected void changeWalkDir() {
		if (walkDir == LEFT)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}

	/**
	 * Resets the enemy to its initial state.
	 */
	public void resetEnemy() {
		hitbox.x = x;
		hitbox.y = y;
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(IDLE);
		active = true;
		airSpeed = 0;
	}

	/**
	 * Checks if the enemy is active.
	 *
	 * @return true if the enemy is active, false otherwise.
	 */
	public boolean isActive() {
		return active;
	}
}
