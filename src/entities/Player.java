package entities;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

/**
 * The Player class represents the player entity in the game.
 * It handles player movement, animations, health, and attacks.
 */

public class Player extends Entity implements Runnable {
	private boolean running;
	private BufferedImage[][] animations;
	private boolean moving = false, attacking = false;
	private boolean left, right, jump;
	private int[][] lvlData;
	private float xDrawOffset = 21 * Game.SCALE;
	private float yDrawOffset = 4 * Game.SCALE;
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private BufferedImage statusBarImg;
	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);
	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);
	private int healthWidth = healthBarWidth;
	private int flipX = 0;
	private int flipW = 1;
	private boolean attackChecked;
	private Playing playing;
	private int tileY = 0;

	/**
	 * Constructs a new Player object with the specified parameters.
	 *
	 * @param x       The initial x-coordinate of the player.
	 * @param y       The initial y-coordinate of the player.
	 * @param width   The width of the player.
	 * @param height  The height of the player.
	 * @param playing The current Playing game state.
	 */
	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		this.state = IDLE;
		this.maxHealth = 100;
		this.currentHealth = 35;
		this.walkSpeed = Game.SCALE * 1.0f;
		loadAnimations();
		initHitbox(20, 27);
		initAttackBox();
	}

	/**
	 * Starts the player's thread.
	 */
	public void start() {
		running = true;
		new Thread(this).start();
	}

	/**
	 * Runs the player thread, continuously updating the player state while the player is active.
	 * The thread sleeps for 16 milliseconds between updates.
	 * If the thread is interrupted, it will stop execution.
	 */
	@Override
	public void run() {
		while (running) {
			update();
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * Stops the player's thread.
	 */
	public void stop() {
		running = false;
	}

	/**
	 * Sets the player's spawn point.
	 *
	 * @param spawn The spawn point coordinates.
	 */
	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}

	/**
	 * Initializes the attack hitbox of the player.
	 * The attack hitbox is a rectangle positioned at the player's coordinates with a width and height of (20 * Game.SCALE).
	 */

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));
	}

	/**
	 * Updates the player's state and performs necessary actions such as updating health bar, checking for collision with spikes or potions,
	 * updating attack hitbox, updating position, and managing animations.
	 */
	public void update() {
		updateHealthBar();

		if (currentHealth <= 0) {
			if (state != DEAD) {
				state = DEAD;
				aniTick = 0;
				aniIndex = 0;
				playing.setPlayerDying(true);
			} else if (aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= ANI_SPEED - 1) {
				playing.setGameOver(true);
			} else {
				updateAnimationTick();
			}
			return;
		}

		updateAttackBox();
		updatePos();
		if (moving) {
			checkPotionTouched();
			checkSpikesTouched();
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
		}
		if (attacking) {
			checkAttack();
		}
		updateAnimationTick();
		setAnimation();
	}

	/**
	 * Checks if the player has touched any spikes and handles the interaction accordingly.
	 */
	private void checkSpikesTouched() {
		playing.checkSpikesTouched(this);
	}

	/**
	 * Checks if the player has touched any potions and handles the interaction accordingly.
	 */
	private void checkPotionTouched() {
		playing.checkPotionTouched(hitbox);
	}

	/**
	 * Checks if the player is currently attacking and performs attack collision detection with enemies and objects.
	 * The attack is only checked if it hasn't been already and the player is in the attack animation frame.
	 */
	private void checkAttack() {
		if (attackChecked || aniIndex != 1) {
			return;
		}
		attackChecked = true;
		playing.checkEnemyHit(attackBox);
		playing.checkObjectHit(attackBox);
	}

	/**
	 * Updates the position and dimensions of the attack hitbox based on player direction.
	 */
	private void updateAttackBox() {
		if (right) {
			attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 10);
		} else if (left) {
			attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);
		}
		attackBox.y = hitbox.y + (Game.SCALE * 10);
	}

	/**
	 * Updates the width of the health bar based on the current health value.
	 */
	private void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
	}

	/**
	 * Renders the player's graphics, including animations and UI elements.
	 *
	 * @param g         The graphics object to render onto.
	 * @param lvlOffset The offset of the level rendering.
	 */
	public void render(Graphics g, int lvlOffset) {
		g.drawImage(animations[state][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX, (int) (hitbox.y - yDrawOffset), width * flipW, height, null);
//      drawHitbox(g, lvlOffset);
//      drawAttackBox(g, lvlOffset);
		drawUI(g);
	}

	/**
	 * Draws the player's UI elements, including the status bar and health bar.
	 *
	 * @param g The graphics object to draw onto.
	 */
	private void drawUI(Graphics g) {
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.red);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
	}

	/**
	 * Updates the animation tick count and handles animation looping.
	 * When the animation tick count reaches the animation speed, it resets and increments the animation index.
	 * If the animation index exceeds the number of sprites for the current animation state, it resets the index and flags for attacking.
	 */
	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(state)) {
				aniIndex = 0;
				attacking = false;
				attackChecked = false;
			}
		}
	}

    /**
     * Sets the current animation state based on player actions and conditions.
     * It determines the appropriate animation state (e.g., running, jumping, attacking) based on player movement and status.
     */
	private void setAnimation() {
		int startAni = state;

		if (moving) {
			state = RUNNING;
		} else {
			state = IDLE;
		}

		if (inAir) {
			if (airSpeed < 0) {
				state = JUMP;
			} else {
				state = FALLING;
			}
		}

		if (attacking) {
			state = ATTACK;
			if (startAni != ATTACK) {
				aniIndex = 1;
				aniTick = 0;
				return;
			}
		}
		if (startAni != state) {
			resetAniTick();
		}
	}

	/**
	 * Resets the animation tick count and animation index to their initial values.
	 */
	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	/**
	 * Updates the player's position based on movement and gravity.
	 * Handles player movement, jumping, and falling.
	 * Checks for collisions with the environment and adjusts the position accordingly.
	 */
	private void updatePos() {
		moving = false;

		if (jump) {
			jump();
		}

		if (!inAir) {
			if ((!left && !right) || (right && left)) {
				return;
			}
		}

		float xSpeed = 0;

		if (left) {
			xSpeed -= walkSpeed;
			flipX = width;
			flipW = -1;
		}
		if (right) {
			xSpeed += walkSpeed;
			flipX = 0;
			flipW = 1;
		}

		if (!inAir) {
			if (!IsEntityOnFloor(hitbox, lvlData)) {
				inAir = true;
			}
		}

		if (inAir) {
			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += GRAVITY;
				updateXPos(xSpeed);
			} else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if (airSpeed > 0) {
					resetInAir();
				} else {
					airSpeed = fallSpeedAfterCollision;
				}
				updateXPos(xSpeed);
			}
		} else {
			updateXPos(xSpeed);
		}
		moving = true;
	}

	/**
	 * Initiates a jump action if the player is not already in the air.
	 * Sets the player's state to in air and initializes the jump speed.
	 */
	private void jump() {
		if (inAir) {
			return;
		}
		inAir = true;
		airSpeed = jumpSpeed;
	}

	/**
	 * Resets the player's in air state and air speed to prepare for the next jump or fall action.
	 */
	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	/**
	 * Updates the player's horizontal position based on the provided speed.
	 * Checks for collisions with the environment and adjusts the position accordingly.
	 *
	 * @param xSpeed The horizontal speed of the player.
	 */
	private void updateXPos(float xSpeed) {
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
			hitbox.x += xSpeed;
		} else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
		}
	}

	/**
	 * Changes the player's health by a specified value.
	 *
	 * @param value The value to change the health by.
	 */
	public void changeHealth(int value) {
		currentHealth += value;

		if (currentHealth <= 0) {
			currentHealth = 0;
		} else if (currentHealth >= maxHealth) {
			currentHealth = maxHealth;
		}
	}

	/**
	 * Sets the player's current health to 0, effectively killing the player.
	 * This method is typically called when the player's health reaches zero.
	 */
	public void kill() {
		currentHealth = 0;
	}

	/**
	 * Changes the player's power by the specified value.
	 * This method currently prints a message to the console indicating that power has been added.
	 *
	 * @param value The value by which to change the player's power.
	 */
	public void changePower(int value) {
		System.out.println("Added power!");
	}

	/**
	 * Loads the player's animations from sprite atlases.
	 * This method initializes the animations array with images extracted from sprite atlases.
	 */
	private void loadAnimations() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
		animations = new BufferedImage[7][8];
		for (int j = 0; j < animations.length; j++) {
			for (int i = 0; i < animations[j].length; i++) {
				animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
			}
		}
		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
	}

	/**
	 * Loads level data for collision detection.
	 *
	 * @param lvlData The level data representing collision map.
	 *                 It is typically a 2D array of integers.
	 */
	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (!IsEntityOnFloor(hitbox, lvlData)) {
			inAir = true;
		}
	}

	/**
	 * Resets the directional boolean flags (left and right) to false.
	 * This method is typically called to reset movement direction after movement.
	 */
	public void resetDirBooleans() {
		left = false;
		right = false;
	}

	/**
	 * Sets the attacking state of the player.
	 *
	 * @param attacking true to set the player as attacking, false otherwise.
	 */
	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	/**
	 * Checks if the player is currently moving left.
	 *
	 * @return true if the player is moving left, false otherwise.
	 */
	public boolean isLeft() {
		return left;
	}

	/**
	 * Sets the left movement state of the player.
	 *
	 * @param left true to set the player as moving left, false otherwise.
	 */
	public void setLeft(boolean left) {
		this.left = left;
	}

	/**
	 * Checks if the player is currently moving right.
	 *
	 * @return true if the player is moving right, false otherwise.
	 */
	public boolean isRight() {
		return right;
	}

	/**
	 * Sets the right movement state of the player.
	 *
	 * @param right true to set the player as moving right, false otherwise.
	 */
	public void setRight(boolean right) {
		this.right = right;
	}

	/**
	 * Sets the jump state of the player.
	 *
	 * @param jump true to set the player as jumping, false otherwise.
	 */
	public void setJump(boolean jump) {
		this.jump = jump;
	}

	/**
	 * Resets all player attributes to their default values.
	 * This includes resetting movement flags, health, position, and state.
	 * Additionally, it checks if the player is initially in the air and updates the inAir flag accordingly.
	 */
	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		state = IDLE;
		currentHealth = maxHealth;
		hitbox.x = x;
		hitbox.y = y;

		if (!IsEntityOnFloor(hitbox, lvlData)) {
			inAir = true;
		}
	}

	/**
	 * Retrieves the tile Y position of the player.
	 *
	 * @return The Y position of the player in terms of tiles.
	 */
	public int getTileY() {
		return tileY;
	}

}
