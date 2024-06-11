package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

/**
 * The EnemyManager class manages the enemies within the game.
 * It handles the loading, updating, drawing, and state changes of enemies.
 */
public class EnemyManager implements Runnable {
	private boolean running;
	private final Playing playing;
	private BufferedImage[][] crabbyArr;
	private ArrayList<Crabby> crabbies = new ArrayList<>();

	/**
	 * Constructs an EnemyManager instance associated with the playing state.
	 *
	 * @param playing The playing state of the game.
	 */
	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
	}

	/**
	 * Starts the enemy manager thread, which updates enemies at a fixed rate.
	 */
	public synchronized void start() {
		running = true;
		new Thread(this).start();
	}

	/**
	 * Runs the enemy manager thread, updating enemies in a loop until stopped.
	 */
	@Override
	public void run() {
		while (running) {
			update(playing.getLevelManager().getCurrentLevel().getLevelData(), playing.getPlayer());
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * Stops the enemy manager thread.
	 */
	public synchronized void stop() {
		running = false;
	}

	/**
	 * Loads enemies for the specified level.
	 *
	 * @param level The level containing enemies to load.
	 */
	public void loadEnemies(Level level) {
		crabbies = level.getCrabs();
	}

	/**
	 * Updates the state of all active enemies.
	 *
	 * @param lvlData The level data for the current level.
	 * @param player  The player instance.
	 */
	public void update(int[][] lvlData, Player player) {
		boolean isAnyActive = false;
		for (Crabby c : crabbies) {
			if (c.isActive()) {
				c.update(lvlData, player);
				isAnyActive = true;
			}
		}
		if (!isAnyActive) {
			playing.setLevelCompleted(true);
		}
	}

	/**
	 * Draws all active enemies on the screen.
	 *
	 * @param g         The graphics context to draw on.
	 * @param xLvlOffset The x-axis level offset for drawing.
	 */
	public void draw(Graphics g, int xLvlOffset) {
		drawCrabs(g, xLvlOffset);
	}

	/**
	 * Draws all active Crabby enemies on the screen.
	 *
	 * @param g         The graphics context to draw on.
	 * @param xLvlOffset The x-axis level offset for drawing.
	 */
	private void drawCrabs(Graphics g, int xLvlOffset) {
		for (Crabby c : crabbies) {
			if (c.isActive()) {
				g.drawImage(crabbyArr[c.getState()][c.getAniIndex()],
						(int) c.getHitbox().x - xLvlOffset - CRABBY_DRAWOFFSET_X + c.flipX(),
						(int) c.getHitbox().y - CRABBY_DRAWOFFSET_Y,
						CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);

				// Uncomment the following lines to draw hitboxes and attack boxes for debugging
				// c.drawHitbox(g, xLvlOffset);
				// c.drawAttackBox(g, xLvlOffset);
			}
		}
	}

	/**
	 * Checks if any enemy has been hit by an attack.
	 *
	 * @param attackBox The attack box to check for intersections with enemies.
	 */
	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		for (Crabby c : crabbies) {
			if (c.isActive() && attackBox.intersects(c.getHitbox())) {
				c.hurt(10);
				return;
			}
		}
	}

	/**
	 * Loads enemy images from the sprite atlas.
	 */
	private void loadEnemyImgs() {
		crabbyArr = new BufferedImage[5][9];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
		for (int j = 0; j < crabbyArr.length; j++) {
			for (int i = 0; i < crabbyArr[j].length; i++) {
				crabbyArr[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
			}
		}
	}

	/**
	 * Resets all enemies to their initial states.
	 */
	public void resetAllEnemies() {
		for (Crabby c : crabbies) {
			c.resetEnemy();
		}
	}
}
