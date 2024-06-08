package levels;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;
import main.Game;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;
import utilz.HelpMethods;

import static utilz.HelpMethods.GetLevelData;
import static utilz.HelpMethods.GetCrabs;
import static utilz.HelpMethods.GetPlayerSpawn;

/**
 * Represents a game level.
 */
public class Level {

	private BufferedImage img;
	private int[][] lvlData;
	private ArrayList<Crabby> crabs;
	private ArrayList<Potion> potions;
	private ArrayList<Spike> spikes;
	private ArrayList<GameContainer> containers;
	private ArrayList<Cannon> cannons;
	private int lvlTilesWide;
	private int maxTilesOffset;
	private int maxLvlOffsetX;
	private Point playerSpawn;

	/**
	 * Constructs a Level object with the specified image.
	 *
	 * @param img The BufferedImage representing the level image.
	 */
	public Level(BufferedImage img) {
		this.img = img;
		createLevelData();
		createEnemies();
		createPotions();
		createContainers();
		createSpikes();
		createCannons();
		calcLvlOffsets();
		calcPlayerSpawn();
	}

	/**
	 * Creates cannons based on the level image.
	 */
	private void createCannons() {
		cannons = HelpMethods.GetCannons(img);
	}

	/**
	 * Creates spikes based on the level image.
	 */
	private void createSpikes() {
		spikes = HelpMethods.GetSpikes(img);
	}

	/**
	 * Creates containers based on the level image.
	 */
	private void createContainers() {
		containers = HelpMethods.GetContainers(img);
	}

	/**
	 * Creates potions based on the level image.
	 */
	private void createPotions() {
		potions = HelpMethods.GetPotions(img);
	}

	/**
	 * Calculates the player spawn point based on the level image.
	 */
	private void calcPlayerSpawn() {
		playerSpawn = GetPlayerSpawn(img);
	}

	/**
	 * Calculates the level offsets based on the level image dimensions and game settings.
	 */
	private void calcLvlOffsets() {
		lvlTilesWide = img.getWidth();
		maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
		maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
	}

	/**
	 * Creates enemies (crabs) based on the level image.
	 */
	private void createEnemies() {
		crabs = GetCrabs(img);
	}

	/**
	 * Creates the level data array based on the level image.
	 */
	private void createLevelData() {
		lvlData = GetLevelData(img);
	}

	/**
	 * Retrieves the sprite index at the specified position in the level data array.
	 *
	 * @param x The x-coordinate of the sprite.
	 * @param y The y-coordinate of the sprite.
	 * @return The index of the sprite at the specified position.
	 */
	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];
	}

	/**
	 * Gets the level data array.
	 *
	 * @return The level data array.
	 */
	public int[][] getLevelData() {
		return lvlData;
	}

	/**
	 * Gets the maximum level offset in the x-direction.
	 *
	 * @return The maximum level offset in pixels.
	 */
	public int getLvlOffset() {
		return maxLvlOffsetX;
	}

	/**
	 * Gets the list of crabs in the level.
	 *
	 * @return The list of crabs.
	 */
	public ArrayList<Crabby> getCrabs() {
		return crabs;
	}

	/**
	 * Gets the player spawn point.
	 *
	 * @return The player spawn point.
	 */
	public Point getPlayerSpawn() {
		return playerSpawn;
	}

	/**
	 * Gets the list of potions in the level.
	 *
	 * @return The list of potions.
	 */
	public ArrayList<Potion> getPotions() {
		return potions;
	}

	/**
	 * Gets the list of game containers in the level.
	 *
	 * @return The list of game containers.
	 */
	public ArrayList<GameContainer> getContainers() {
		return containers;
	}

	/**
	 * Gets the list of spikes in the level.
	 *
	 * @return The list of spikes.
	 */
	public ArrayList<Spike> getSpikes() {
		return spikes;
	}

	/**
	 * Gets the list of cannons in the level.
	 *
	 * @return The list of cannons.
	 */
	public ArrayList<Cannon> getCannons(){
		return cannons;
	}
}
