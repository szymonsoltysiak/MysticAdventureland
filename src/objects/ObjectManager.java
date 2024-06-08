package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Player;
import gamestates.Playing;
import levels.Level;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.ObjectConstants.*;
import static utilz.HelpMethods.CanCannonSeePlayer;
import static utilz.HelpMethods.IsProjectileHittingLevel;
import static utilz.Constants.Projectiles.*;

/**
 * The ObjectManager class manages all the game objects such as potions, containers, spikes, cannons, and projectiles.
 * It controls their creation, updates, and rendering.
 */
public class ObjectManager implements Runnable{
	private boolean running;
	private Playing playing;
	private BufferedImage[][] potionImgs, containerImgs;
	private BufferedImage[] cannonImgs;
	private BufferedImage spikeImg, cannonBallImg;
	private ArrayList<Potion> potions;
	private ArrayList<GameContainer> containers;
	private ArrayList<Spike> spikes;
	private ArrayList<Cannon> cannons;
	private ArrayList<Projectile> projectiles = new ArrayList<>();

	/**
	 * Constructs an ObjectManager with the specified Playing instance.
	 *
	 * @param playing The Playing instance to associate with the ObjectManager.
	 */
	public ObjectManager(Playing playing) {
		this.playing = playing;
		loadImgs();
	}

	/**
	 * Starts the ObjectManager's update loop in a new thread.
	 */
	public void start() {
		running = true;
		new Thread(this).start();
	}

	/**
	 * The run method for the ObjectManager thread.
	 * It continuously updates the game objects based on the current level data and player position.
	 * Uses a sleep period to control the update rate.
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
	 * Stops the ObjectManager's update loop.
	 */
	public void stop() {
		running = false;
	}

	/**
	 * Checks if the player touches any spikes and kills the player if so.
	 *
	 * @param p The player entity.
	 */
	public void checkSpikesTouched(Player p) {
		for (Spike s : spikes)
			if (s.getHitbox().intersects(p.getHitbox()))
				p.kill();
	}


	public void checkObjectTouched(Rectangle2D.Float hitbox) {
		for (Potion p : potions)
			if (p.isActive()) {
				if (hitbox.intersects(p.getHitbox())) {
					p.setActive(false);
					applyEffectToPlayer(p);
				}
			}
	}

	/**
	 * Applies the effect of a potion to the player.
	 *
	 * @param p The potion object whose effect needs to be applied to the player.
	 */
	public void applyEffectToPlayer(Potion p) {
		if (p.getObjType() == RED_POTION)
			playing.getPlayer().changeHealth(RED_POTION_VALUE);
		else
			playing.getPlayer().changePower(BLUE_POTION_VALUE);
	}

	/**
	 * Checks if the player touches any active potions and applies their effects to the player.
	 *
	 * @param hitbox The hitbox representing the area where the player touches an object.
	 */
	public void checkObjectHit(Rectangle2D.Float attackbox) {
		for (GameContainer gc : containers)
			if (gc.isActive() && !gc.doAnimation) {
				if (gc.getHitbox().intersects(attackbox)) {
					gc.setAnimation(true);
					int type = 0;
					if (gc.getObjType() == BARREL)
						type = 1;
					potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width / 2), (int) (gc.getHitbox().y - gc.getHitbox().height / 2), type));
					return;
				}
			}
	}

	/**
	 * Loads objects such as potions, containers, spikes, cannons, and clears the list of projectiles.
	 *
	 * @param newLevel The new level from which objects are loaded.
	 */
	public void loadObjects(Level newLevel) {
		potions = new ArrayList<>(newLevel.getPotions());
		containers = new ArrayList<>(newLevel.getContainers());
		spikes = newLevel.getSpikes();
		cannons = newLevel.getCannons();
		projectiles.clear();
	}

	/**
	 * Loads images for potions, containers, spikes, cannons, and cannonballs.
	 */
	private void loadImgs() {
		BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
		potionImgs = new BufferedImage[2][7];

		for (int j = 0; j < potionImgs.length; j++)
			for (int i = 0; i < potionImgs[j].length; i++)
				potionImgs[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);

		BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
		containerImgs = new BufferedImage[2][8];

		for (int j = 0; j < containerImgs.length; j++)
			for (int i = 0; i < containerImgs[j].length; i++)
				containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);

		spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);

		cannonImgs = new BufferedImage[7];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);

		for (int i = 0; i < cannonImgs.length; i++)
			cannonImgs[i] = temp.getSubimage(i * 40, 0, 40, 26);

		cannonBallImg = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);

	}

	/**
	 * Updates the state of objects in the game, including potions, containers, cannons, and projectiles.
	 *
	 * @param lvlData The level data representing the game map.
	 * @param player The player object in the game.
	 */
	public void update(int[][] lvlData, Player player) {
		for (Potion p : potions)
			if (p.isActive())
				p.update();

		for (GameContainer gc : containers)
			if (gc.isActive())
				gc.update();

		updateCannons(lvlData, player);
		updateProjectiles(lvlData, player);
	}

	/**
	 * Updates the state of projectiles, checks for collisions with the player and the game map, and applies damage accordingly.
	 *
	 * @param lvlData The level data representing the game map.
	 * @param player The player object in the game.
	 */
	private void updateProjectiles(int[][] lvlData, Player player) {
		for (Projectile p : projectiles)
			if (p.isActive()) {
				p.updatePos();
				if (p.getHitbox().intersects(player.getHitbox())) {
					player.changeHealth(-25);
					p.setActive(false);
				} else if (IsProjectileHittingLevel(p, lvlData))
					p.setActive(false);
			}
	}

	/**
	 * Checks if the player is within the range of a cannon to be targeted.
	 *
	 * @param c The cannon object.
	 * @param player The player object.
	 * @return {@code true} if the player is within range of the cannon, {@code false} otherwise.
	 */
	private boolean isPlayerInRange(Cannon c, Player player) {
		int absValue = (int) Math.abs(player.getHitbox().x - c.getHitbox().x);
		return absValue <= Game.TILES_SIZE * 5;
	}

	/**
	 * Checks if the player is positioned in front of a cannon based on its direction.
	 *
	 * @param c The cannon object.
	 * @param player The player object.
	 * @return {@code true} if the player is in front of the cannon, {@code false} otherwise.
	 */
	private boolean isPlayerInfrontOfCannon(Cannon c, Player player) {
		if (c.getObjType() == CANNON_LEFT) {
			if (c.getHitbox().x > player.getHitbox().x)
				return true;

		} else if (c.getHitbox().x < player.getHitbox().x)
			return true;
		return false;
	}

	/**
	 * Updates the state of cannons and initiates shooting if conditions are met.
	 *
	 * @param lvlData The level data.
	 * @param player The player object.
	 */
	private void updateCannons(int[][] lvlData, Player player) {
		for (Cannon c : cannons) {
			if (!c.doAnimation)
				if (c.getTileY() == player.getTileY())
					if (isPlayerInRange(c, player))
						if (isPlayerInfrontOfCannon(c, player))
							if (CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY()))
								c.setAnimation(true);

			c.update();
			if (c.getAniIndex() == 4 && c.getAniTick() == 0)
				shootCannon(c);
		}
	}

	/**
	 * Initiates the shooting action for a cannon.
	 *
	 * @param c The cannon object.
	 */
	private void shootCannon(Cannon c) {
		int dir = 1;
		if (c.getObjType() == CANNON_LEFT)
			dir = -1;

		projectiles.add(new Projectile((int) c.getHitbox().x, (int) c.getHitbox().y, dir));
	}

	/**
	 * Draws all objects including potions, containers, traps, cannons, and projectiles.
	 *
	 * @param g          The graphics context.
	 * @param xLvlOffset The horizontal level offset.
	 */
	public void draw(Graphics g, int xLvlOffset) {
		drawPotions(g, xLvlOffset);
		drawContainers(g, xLvlOffset);
		drawTraps(g, xLvlOffset);
		drawCannons(g, xLvlOffset);
		drawProjectiles(g, xLvlOffset);
	}

	/**
	 * Draws active projectiles on the screen.
	 *
	 * @param g          The graphics context.
	 * @param xLvlOffset The horizontal level offset.
	 */
	private void drawProjectiles(Graphics g, int xLvlOffset) {
		for (Projectile p : projectiles)
			if (p.isActive())
				g.drawImage(cannonBallImg, (int) (p.getHitbox().x - xLvlOffset), (int) (p.getHitbox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);

	}

	/**
	 * Draws all active cannons on the screen.
	 *
	 * @param g          The graphics context.
	 * @param xLvlOffset The horizontal level offset.
	 */
	private void drawCannons(Graphics g, int xLvlOffset) {
		for (Cannon c : cannons) {
			int x = (int) (c.getHitbox().x - xLvlOffset);
			int width = CANNON_WIDTH;

			if (c.getObjType() == CANNON_RIGHT) {
				x += width;
				width *= -1;
			}

			g.drawImage(cannonImgs[c.getAniIndex()], x, (int) (c.getHitbox().y), width, CANNON_HEIGHT, null);
		}

	}

	/**
	 * Draws all active spikes on the screen.
	 *
	 * @param g          The graphics context.
	 * @param xLvlOffset The horizontal level offset.
	 */
	private void drawTraps(Graphics g, int xLvlOffset) {
		for (Spike s : spikes)
			g.drawImage(spikeImg, (int) (s.getHitbox().x - xLvlOffset), (int) (s.getHitbox().y - s.getyDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);

	}

	/**
	 * Draws all active containers on the screen.
	 *
	 * @param g          The graphics context.
	 * @param xLvlOffset The horizontal level offset.
	 */
	private void drawContainers(Graphics g, int xLvlOffset) {
		for (GameContainer gc : containers)
			if (gc.isActive()) {
				int type = 0;
				if (gc.getObjType() == BARREL)
					type = 1;
				g.drawImage(containerImgs[type][gc.getAniIndex()], (int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset), (int) (gc.getHitbox().y - gc.getyDrawOffset()), CONTAINER_WIDTH,
						CONTAINER_HEIGHT, null);
			}
	}

	/**
	 * Draws all active potions on the screen.
	 *
	 * @param g          The graphics context.
	 * @param xLvlOffset The horizontal level offset.
	 */
	private void drawPotions(Graphics g, int xLvlOffset) {
		for (Potion p : potions)
			if (p.isActive()) {
				int type = 0;
				if (p.getObjType() == RED_POTION)
					type = 1;
				g.drawImage(potionImgs[type][p.getAniIndex()], (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), (int) (p.getHitbox().y - p.getyDrawOffset()), POTION_WIDTH, POTION_HEIGHT,
						null);
			}
	}

	/**
	 * Resets all objects to their initial state.
	 */
	public void resetAllObjects() {
		loadObjects(playing.getLevelManager().getCurrentLevel());
		for (Potion p : potions)
			p.reset();
		for (GameContainer gc : containers)
			gc.reset();
		for (Cannon c : cannons)
			c.reset();
	}
}
