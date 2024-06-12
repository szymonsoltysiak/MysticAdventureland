package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;
import static utilz.Constants.Environment.*;

/**
 * The Playing class represents the state of the game where the player is actively playing.
 * It handles the game logic, updates, and rendering during gameplay.
 */
public class Playing extends State implements Statemethods {
	private final Object lock = new Object();
	private Thread playerThread;
	private Thread enemyManagerThread;
	private Thread objectManagerThread;

	private Player player;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;

	private LevelManager levelManager;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;
	private boolean paused = false;

	private int xLvlOffset;
	private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
	private int maxLvlOffsetX;

	private BufferedImage backgroundImg, bigCloud, smallCloud;
	private int[] smallCloudsPos;
	private Random rnd = new Random();

	private boolean gameOver;
	private boolean lvlCompleted;
	private boolean playerDying;

	/**
	 * Constructs a new Playing state.
	 *
	 * @param game The Game instance.
	 */
	public Playing(Game game) {
		super(game);
		initClasses();

		// Load assets
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
		bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
		smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
		smallCloudsPos = new int[8];
		for (int i = 0; i < smallCloudsPos.length; i++)
			smallCloudsPos[i] = (int) (90 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE));

		calcLvlOffset();
		loadStartLevel();

		// Start threads
		playerThread = new Thread(player);
		enemyManagerThread = new Thread(enemyManager);
		objectManagerThread = new Thread(objectManager);

		playerThread.start();
		enemyManagerThread.start();
		objectManagerThread.start();
	}

	/**
	 * Loads the next level in the game.
	 */
	public void loadNextLevel() {
		resetAll();
		levelManager.loadNextLevel();
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
	}

	/**
	 * Loads the starting level.
	 */
	private void loadStartLevel() {
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
	}

	/**
	 * Calculates the maximum level offset.
	 */
	private void calcLvlOffset() {
		maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
	}

	/**
	 * Initializes the game classes.
	 */
	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);

		player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
	}

	/**
	 * Updates the game state.
	 */
	public void update() {
		if (paused) {
			synchronized (lock) {
				pauseOverlay.update();
			}
		} else if (lvlCompleted) {
			synchronized (lock) {
				levelCompletedOverlay.update();
			}
		} else if (gameOver) {
			synchronized (lock) {
				gameOverOverlay.update();
			}
		} else if (playerDying) {
			synchronized (lock) {
				player.update();
			}
		} else {
			synchronized (lock) {
				levelManager.update();
				objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
				player.update();
				enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
				checkCloseToBorder();
			}
		}
	}

	/**
	 * Checks if the player is close to the level border and updates the level offset accordingly.
	 */
	private void checkCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - xLvlOffset;

		if (diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if (diff < leftBorder)
			xLvlOffset += diff - leftBorder;

		if (xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if (xLvlOffset < 0)
			xLvlOffset = 0;
	}

	/**
	 * Renders the game graphics.
	 * Draws the background image, clouds, player, enemies, and objects.
	 * If the game is paused, it draws a semi-transparent overlay and the pause menu.
	 * If the game is over, it draws the game over overlay.
	 * If the level is completed, it draws the level completed overlay.
	 *
	 * @param g The Graphics object used for rendering.
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		drawClouds(g);
		levelManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset);
		objectManager.draw(g, xLvlOffset);

		if (paused) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		} else if (gameOver) {
			gameOverOverlay.draw(g);
		} else if (lvlCompleted) {
			levelCompletedOverlay.draw(g);
		}
	}

	/**
	 * Draws the clouds in the game background.
	 *
	 * @param g The Graphics object.
	 */
	private void drawClouds(Graphics g) {
		for (int i = 0; i < 3; i++) {
			g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.3), (int) (204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
		}

		for (int i = 0; i < smallCloudsPos.length; i++) {
			g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
		}
	}

	/**
	 * Resets all game states and entities.
	 */
	public void resetAll() {
		gameOver = false;
		paused = false;
		lvlCompleted = false;
		playerDying = false;
		player.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObjects();
	}

	/**
	 * Sets the game over state.
	 *
	 * @param gameOver True if the game is over, false otherwise.
	 */
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	/**
	 * Checks if an object has been hit by the player's attack.
	 *
	 * @param attackBox The attack box of the player.
	 */
	public void checkObjectHit(Rectangle2D.Float attackBox) {
		objectManager.checkObjectHit(attackBox);
	}

	/**
	 * Checks if an enemy has been hit by the player's attack.
	 *
	 * @param attackBox The attack box of the player.
	 */
	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}

	/**
	 * Checks if a potion has been touched by the player.
	 *
	 * @param hitbox The hitbox of the player.
	 */
	public void checkPotionTouched(Rectangle2D.Float hitbox) {
		objectManager.checkObjectTouched(hitbox);
	}

	/**
	 * Checks if the player has touched spikes.
	 *
	 * @param p The player instance.
	 */
	public void checkSpikesTouched(Player p) {
		objectManager.checkSpikesTouched(p);
	}

	/**
	 * Handles mouse clicked events.
	 * If the game is not over and the left mouse button is clicked,
	 * it sets the player to attacking state.
	 *
	 * @param e The MouseEvent object representing the mouse click event.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (!gameOver) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				player.setAttacking(true);
			}
		}
	}

	/**
	 * Handles key pressed events.
	 * If the game is over, it forwards the event to the game over overlay.
	 * Otherwise, it checks which key was pressed and updates the player's state or toggles pause mode accordingly.
	 *
	 * @param e The KeyEvent object representing the key press event.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (gameOver) {
			gameOverOverlay.keyPressed(e);
		} else {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					player.setLeft(true);
					break;
				case KeyEvent.VK_D:
					player.setRight(true);
					break;
				case KeyEvent.VK_SPACE:
					player.setJump(true);
					break;
				case KeyEvent.VK_K:
					player.setAttacking(true);
					break;
				case KeyEvent.VK_ESCAPE:
					paused = !paused;
					break;
			}
		}
	}

	/**
	 * Handles key released events.
	 * If the game is not over, it checks which key was released and updates the player's state accordingly.
	 *
	 * @param e The KeyEvent object representing the key release event.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					player.setLeft(false);
					break;
				case KeyEvent.VK_D:
					player.setRight(false);
					break;
				case KeyEvent.VK_SPACE:
					player.setJump(false);
					break;
			}
		}
	}

	/**
	 * Handles mouse drag events.
	 *
	 * @param e The MouseEvent object.
	 */
	public void mouseDragged(MouseEvent e) {
		if (!gameOver) {
			if (paused) {
				pauseOverlay.mouseDragged(e);
			}
		}
	}

	/**
	 * Handles mouse pressed events.
	 * If the game is not over, it checks if the game is paused or the level is completed,
	 * and forwards the event to the corresponding overlay.
	 * If the game is over, it forwards the event to the game over overlay.
	 *
	 * @param e The MouseEvent object representing the mouse press event.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (!gameOver) {
			if (paused) {
				pauseOverlay.mousePressed(e);
			} else if (lvlCompleted) {
				levelCompletedOverlay.mousePressed(e);
			}
		} else {
			gameOverOverlay.mousePressed(e);
		}
	}

	/**
	 * Handles mouse released events.
	 * If the game is not over, it checks if the game is paused or the level is completed,
	 * and forwards the event to the corresponding overlay.
	 * If the game is over, it forwards the event to the game over overlay.
	 *
	 * @param e The MouseEvent object representing the mouse release event.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (!gameOver) {
			if (paused) {
				pauseOverlay.mouseReleased(e);
			} else if (lvlCompleted) {
				levelCompletedOverlay.mouseReleased(e);
			}
		} else {
			gameOverOverlay.mouseReleased(e);
		}
	}

	/**
	 * Handles mouse moved events.
	 * If the game is not over, it checks if the game is paused or the level is completed,
	 * and forwards the event to the corresponding overlay.
	 * If the game is over, it forwards the event to the game over overlay.
	 *
	 * @param e The MouseEvent object representing the mouse movement event.
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		if (!gameOver) {
			if (paused) {
				pauseOverlay.mouseMoved(e);
			} else if (lvlCompleted) {
				levelCompletedOverlay.mouseMoved(e);
			}
		} else {
			gameOverOverlay.mouseMoved(e);
		}
	}

	/**
	 * Sets the level completed state.
	 *
	 * @param levelCompleted True if the level is completed, false otherwise.
	 */
	public void setLevelCompleted(boolean levelCompleted) {
		this.lvlCompleted = levelCompleted;
	}

	/**
	 * Sets the maximum level offset.
	 *
	 * @param lvlOffset The maximum level offset.
	 */
	public void setMaxLvlOffset(int lvlOffset) {
		this.maxLvlOffsetX = lvlOffset;
	}

	/**
	 * Unpauses the game.
	 */
	public void unpauseGame() {
		paused = false;
	}

	/**
	 * Resets the player's direction booleans when the window focus is lost.
	 */
	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	/**
	 * Returns the player instance.
	 *
	 * @return The player instance.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Returns the enemy manager instance.
	 *
	 * @return The enemy manager instance.
	 */
	public EnemyManager getEnemyManager() {
		return enemyManager;
	}

	/**
	 * Returns the object manager instance.
	 *
	 * @return The object manager instance.
	 */
	public ObjectManager getObjectManager() {
		return objectManager;
	}

	/**
	 * Returns the level manager instance.
	 *
	 * @return The level manager instance.
	 */
	public LevelManager getLevelManager() {
		return levelManager;
	}

	/**
	 * Sets the player dying state.
	 *
	 * @param playerDying True if the player is dying, false otherwise.
	 */
	public void setPlayerDying(boolean playerDying) {
		this.playerDying = playerDying;
	}

	/**
	 * Stops all running threads.
	 */
	public void stopThreads() {
		if (playerThread != null && playerThread.isAlive()) {
			playerThread.interrupt();
		}
		if (enemyManagerThread != null && enemyManagerThread.isAlive()) {
			enemyManagerThread.interrupt();
		}
		if (objectManagerThread != null && objectManagerThread.isAlive()) {
			objectManagerThread.interrupt();
		}
	}
}
