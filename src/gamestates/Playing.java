package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
 * The playing state of the game.
 * This state represents the main gameplay loop where the player controls the character.
 */
public class Playing extends State implements Statemethods {
	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
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

	private ExecutorService executorService;
	private Future<?> playerFuture;
	private Future<?> enemyManagerFuture;
	private Future<?> objectManagerFuture;

	/**
	 * Constructor for the Playing state.
	 * Initializes the necessary components for the gameplay.
	 *
	 * @param game The main Game instance.
	 */
	public Playing(Game game) {
		super(game);
		initClasses();

		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
		bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
		smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
		smallCloudsPos = new int[8];
		for (int i = 0; i < smallCloudsPos.length; i++)
			smallCloudsPos[i] = (int) (90 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE));

		calcLvlOffset();
		loadStartLevel();

		executorService = Executors.newFixedThreadPool(3);
	}

	/**
	 * Loads the next level.
	 * Resets all game elements and loads the next level from the level manager.
	 * Sets the player spawn point to the spawn point of the current level.
	 */
	public void loadNextLevel() {
		resetAll();
		levelManager.loadNextLevel();
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
	}

	/**
	 * Loads the start level.
	 * Loads enemies and objects for the current level from the level manager.
	 */
	private void loadStartLevel() {
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
	}

	/**
	 * Calculates the level offset.
	 * Retrieves the level offset from the current level and sets it as the maximum level offset.
	 */
	private void calcLvlOffset() {
		maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
	}

	/**
	 * Initializes game classes.
	 * Creates instances of the level manager, enemy manager, and object manager.
	 * Creates a player instance and initializes it with the current level data and spawn point.
	 * Creates instances of the pause overlay, game over overlay, and level completed overlay.
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
	 * Updates the playing state.
	 * Checks if the game is paused, if the level is completed, if the game is over, if the player is dying, or if the game is running normally.
	 * Executes the appropriate update actions accordingly.
	 */
	@Override
	public void update() {
		if (paused) {
			pauseOverlay.update();
		} else if (lvlCompleted) {
			levelCompletedOverlay.update();
		} else if (gameOver) {
			gameOverOverlay.update();
		} else if (playerDying) {
			player.update();
		} else {
			levelManager.update();
			checkCloseToBorder();

			if (playerFuture == null || playerFuture.isDone()) {
				playerFuture = executorService.submit(player::update);
			}

			if (enemyManagerFuture == null || enemyManagerFuture.isDone()) {
				enemyManagerFuture = executorService.submit(() -> enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player));
			}

			if (objectManagerFuture == null || objectManagerFuture.isDone()) {
				objectManagerFuture = executorService.submit(() -> objectManager.update(levelManager.getCurrentLevel().getLevelData(), player));
			}
		}
	}

	/**
	 * Checks if the player is close to the level borders and adjusts the level offset accordingly.
	 * This method ensures that the player remains centered within the playable area.
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
	 * Draws the components of the playing state.
	 * Draws the background, clouds, level, player, enemies, and objects.
	 * Also draws overlays for pause, game over, and level completed screens if necessary.
	 *
	 * @param g The Graphics object to draw on.
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
	 * Draws the clouds on the screen.
	 * Draws big clouds and small clouds at appropriate positions.
	 *
	 * @param g The Graphics object to draw on.
	 */
	private void drawClouds(Graphics g) {
		for (int i = 0; i < 3; i++)
			g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.3), (int) (204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);

		for (int i = 0; i < smallCloudsPos.length; i++)
			g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
	}

	/**
	 * Resets all game-related variables and entities to their initial state.
	 * Resets the game over, paused, level completed, and player dying flags.
	 * Resets the player, enemy manager, and object manager.
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
	 * Sets the game over flag to the specified value.
	 *
	 * @param gameOver The boolean value indicating whether the game is over.
	 */
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	/**
	 * Checks for collision between the player's attack and objects in the game.
	 *
	 * @param attackBox The bounding box of the player's attack.
	 */
	public void checkObjectHit(Rectangle2D.Float attackBox) {
		objectManager.checkObjectHit(attackBox);
	}

	/**
	 * Checks for collision between the player's attack and enemies in the game.
	 *
	 * @param attackBox The bounding box of the player's attack.
	 */
	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}

	/**
	 * Checks if the player's hitbox touches a potion object in the game.
	 *
	 * @param hitbox The bounding box of the player's hitbox.
	 */
	public void checkPotionTouched(Rectangle2D.Float hitbox) {
		objectManager.checkObjectTouched(hitbox);
	}

	/**
	 * Checks if the player touches spikes in the game.
	 *
	 * @param p The Player object to check for spike collision.
	 */
	public void checkSpikesTouched(Player p) {
		objectManager.checkSpikesTouched(p);
	}

	/**
	 * Handles mouse click events.
	 * If the game is not over and the left mouse button is clicked,
	 * sets the player to initiate an attack.
	 *
	 * @param e The MouseEvent representing the mouse click event.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (!gameOver) {
			if (e.getButton() == MouseEvent.BUTTON1)
				player.setAttacking(true);
		}
	}

	/**
	 * Handles key press events.
	 * If the game is over, forwards the key press event to the game over overlay for processing.
	 * If the game is not over:
	 * - Pressing 'A' sets the player to move left.
	 * - Pressing 'D' sets the player to move right.
	 * - Pressing 'Space' makes the player jump.
	 * - Pressing 'Escape' toggles the pause state of the game.
	 *
	 * @param e The KeyEvent representing the key press event.
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
				case KeyEvent.VK_ESCAPE:
					paused = !paused;
					break;
			}
		}
	}

	/**
	 * Handles key release events when the game is not over.
	 *
	 * @param e The KeyEvent representing the key release event.
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
	 * Handles mouse drag events when the game is not over.
	 *
	 * @param e The MouseEvent representing the mouse drag event.
	 */
	public void mouseDragged(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseDragged(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mousePressed(e);
			else if (lvlCompleted)
				levelCompletedOverlay.mousePressed(e);
		} else {
			gameOverOverlay.mousePressed(e);
		}
	}

	/**
	 * Handles mouse press events.
	 * If the game is not over, checks if the game is paused or if the level is completed,
	 * and delegates the mouse press event accordingly.
	 * If the game is over, delegates the mouse press event to the game over overlay.
	 *
	 * @param e The MouseEvent representing the mouse press event.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseReleased(e);
			else if (lvlCompleted)
				levelCompletedOverlay.mouseReleased(e);
		} else {
			gameOverOverlay.mouseReleased(e);
		}
	}

	/**
	 * Handles mouse move events.
	 * If the game is not over, checks if the game is paused or if the level is completed,
	 * and delegates the mouse move event accordingly.
	 * If the game is over, delegates the mouse move event to the game over overlay.
	 *
	 * @param e The MouseEvent representing the mouse move event.
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseMoved(e);
			else if (lvlCompleted)
				levelCompletedOverlay.mouseMoved(e);
		} else {
			gameOverOverlay.mouseMoved(e);
		}
	}
	/**
	 * Sets the flag indicating whether the level is completed.
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
	 * Resumes the game by unpause.
	 */
	public void unpauseGame() {
		paused = false;
	}

	/**
	 * Handles the event when the window loses focus by resetting player direction booleans.
	 */
	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	/**
	 * Gets the player object.
	 *
	 * @return The player object.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the enemy manager object.
	 *
	 * @return The enemy manager object.
	 */
	public EnemyManager getEnemyManager() {
		return enemyManager;
	}

	/**
	 * Gets the object manager object.
	 *
	 * @return The object manager object.
	 */
	public ObjectManager getObjectManager() {
		return objectManager;
	}

	/**
	 * Gets the level manager object.
	 *
	 * @return The level manager object.
	 */
	public LevelManager getLevelManager() {
		return levelManager;
	}

	/**
	 * Sets the flag indicating whether the player is dying.
	 *
	 * @param playerDying True if the player is dying, false otherwise.
	 */
	public void setPlayerDying(boolean playerDying) {
		this.playerDying = playerDying;
	}
}
