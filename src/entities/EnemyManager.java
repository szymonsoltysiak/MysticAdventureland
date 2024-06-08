package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

public class EnemyManager implements Runnable{
	private boolean running;
	private Playing playing;
	private BufferedImage[][] crabbyArr;
	private ArrayList<Crabby> crabbies = new ArrayList<>();

	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
	}

	public void start() {
		running = true;
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (running) {
			update(playing.getLevelManager().getCurrentLevel().getLevelData(), playing.getPlayer());
			// Add sleep or wait logic to control the update rate
			try {
				Thread.sleep(16); // Approximately 60 updates per second
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	public void stop() {
		running = false;
	}


	public void loadEnemies(Level level) {
		crabbies = level.getCrabs();
	}

	public void update(int[][] lvlData, Player player) {
		boolean isAnyActive = false;
		for (Crabby c : crabbies)
			if (c.isActive()) {
				c.update(lvlData, player);
				isAnyActive = true;
			}
		if (!isAnyActive)
			playing.setLevelCompleted(true);
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawCrabs(g, xLvlOffset);
	}

	private void drawCrabs(Graphics g, int xLvlOffset) {
		for (Crabby c : crabbies)
			if (c.isActive()) {

				g.drawImage(crabbyArr[c.getState()][c.getAniIndex()], (int) c.getHitbox().x - xLvlOffset - CRABBY_DRAWOFFSET_X + c.flipX(), (int) c.getHitbox().y - CRABBY_DRAWOFFSET_Y,
						CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);

//				c.drawHitbox(g, xLvlOffset);
//				c.drawAttackBox(g, xLvlOffset);
			}

	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		for (Crabby c : crabbies)
			if (c.isActive())
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(10);
					return;
				}
	}

	private void loadEnemyImgs() {
		crabbyArr = new BufferedImage[5][9];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
		for (int j = 0; j < crabbyArr.length; j++)
			for (int i = 0; i < crabbyArr[j].length; i++)
				crabbyArr[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
	}

	public void resetAllEnemies() {
		for (Crabby c : crabbies)
			c.resetEnemy();
	}

}
