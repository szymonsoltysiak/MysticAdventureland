package utilz;

import static utilz.Constants.EnemyConstants.CRABBY;
import static utilz.Constants.ObjectConstants.*;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;
import main.Game;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Projectile;
import objects.Spike;

/**
 * Utility methods for various operations in the game.
 */
public class HelpMethods {

	/**
	 * Checks if an entity can move to the specified position on the level.
	 *
	 * @param x       The x-coordinate of the entity.
	 * @param y       The y-coordinate of the entity.
	 * @param width   The width of the entity.
	 * @param height  The height of the entity.
	 * @param lvlData The level data.
	 * @return True if the entity can move to the specified position, false otherwise.
	 */
	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
		if (!IsSolid(x, y, lvlData))
			if (!IsSolid(x + width, y + height, lvlData))
				if (!IsSolid(x + width, y, lvlData))
					if (!IsSolid(x, y + height, lvlData))
						return true;
		return false;
	}

	/**
	 * Checks if the specified position (x, y) is solid within the level data.
	 *
	 * @param x       The x-coordinate to check.
	 * @param y       The y-coordinate to check.
	 * @param lvlData The level data representing solidity of tiles.
	 * @return True if the position is solid, false otherwise.
	 */
	private static boolean IsSolid(float x, float y, int[][] lvlData) {
		int maxWidth = lvlData[0].length * Game.TILES_SIZE;
		if (x < 0 || x >= maxWidth)
			return true;
		if (y < 0 || y >= Game.GAME_HEIGHT)
			return true;
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;

		return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
	}

	/**
	 * Checks if the specified projectile is hitting the level terrain.
	 *
	 * @param p       The projectile to check.
	 * @param lvlData The level data representing solidity of tiles.
	 * @return True if the projectile is hitting the level terrain, false otherwise.
	 */
	public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData) {
		return IsSolid(p.getHitbox().x + p.getHitbox().width / 2, p.getHitbox().y + p.getHitbox().height / 2, lvlData);

	}

	/**
	 * Checks if the tile at the specified coordinates is solid.
	 *
	 * @param xTile   The x-coordinate of the tile.
	 * @param yTile   The y-coordinate of the tile.
	 * @param lvlData The level data representing solidity of tiles.
	 * @return True if the tile is solid, false otherwise.
	 */
	public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
		int value = lvlData[yTile][xTile];

		if (value >= 48 || value < 0 || value != 11)
			return true;
		return false;
	}

	/**
	 * Calculates the x-coordinate where an entity should be positioned next to a wall based on its hitbox and horizontal speed.
	 *
	 * @param hitbox  The hitbox of the entity.
	 * @param xSpeed  The horizontal speed of the entity.
	 * @return The x-coordinate where the entity should be positioned next to a wall.
	 */
	public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
		if (xSpeed > 0) {
			// Right
			int tileXPos = currentTile * Game.TILES_SIZE;
			int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
			return tileXPos + xOffset - 1;
		} else
			// Left
			return currentTile * Game.TILES_SIZE;
	}

	/**
	 * Calculates the y-coordinate where an entity should be positioned under a roof or above the floor based on its hitbox and vertical speed.
	 *
	 * @param hitbox   The hitbox of the entity.
	 * @param airSpeed The vertical speed of the entity.
	 * @return The y-coordinate where the entity should be positioned under a roof or above the floor.
	 */
	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
		if (airSpeed > 0) {
			// Falling - touching floor
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
			return tileYPos + yOffset - 1;
		} else
			// Jumping
			return currentTile * Game.TILES_SIZE;

	}

	/**
	 * Checks if the entity represented by the given hitbox is on the floor.
	 *
	 * @param hitbox  The hitbox of the entity.
	 * @param lvlData The level data representing the map.
	 * @return {@code true} if the entity is on the floor, {@code false} otherwise.
	 */
	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
			if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
				return false;
		return true;
	}

	/**
	 * Checks if there is a floor beneath the entity represented by the given hitbox.
	 *
	 * @param hitbox  The hitbox of the entity.
	 * @param xSpeed  The horizontal speed of the entity.
	 * @param lvlData The level data representing the map.
	 * @return {@code true} if there is a floor beneath the entity, {@code false} otherwise.
	 */
	public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
		if (xSpeed > 0)
			return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
		else
			return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
	}

	/**
	 * Checks if a cannon has a clear line of sight to the player.
	 *
	 * @param lvlData      The level data representing the map.
	 * @param firstHitbox  The hitbox of the first position of the cannon.
	 * @param secondHitbox The hitbox of the second position of the cannon.
	 * @param yTile        The y-coordinate of the tile where the cannon is positioned.
	 * @return {@code true} if the cannon has a clear line of sight to the player, {@code false} otherwise.
	 */
	public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
		int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
		int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

		if (firstXTile > secondXTile)
			return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
		else
			return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
	}

	/**
	 * Checks if all tiles within a specified range along the x-axis at a given y-coordinate are clear.
	 *
	 * @param xStart  The starting x-coordinate of the range (inclusive).
	 * @param xEnd    The ending x-coordinate of the range (exclusive).
	 * @param y       The y-coordinate of the tiles to check.
	 * @param lvlData The level data representing the map.
	 * @return {@code true} if all tiles within the specified range are clear, {@code false} otherwise.
	 */
	public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
		for (int i = 0; i < xEnd - xStart; i++)
			if (IsTileSolid(xStart + i, y, lvlData))
				return false;
		return true;
	}

	/**
	 * Checks if all tiles within a specified range along the x-axis at a given y-coordinate are walkable.
	 *
	 * @param xStart  The starting x-coordinate of the range (inclusive).
	 * @param xEnd    The ending x-coordinate of the range (exclusive).
	 * @param y       The y-coordinate of the tiles to check.
	 * @param lvlData The level data representing the map.
	 * @return {@code true} if all tiles within the specified range are walkable, {@code false} otherwise.
	 */
	public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
		if (IsAllTilesClear(xStart, xEnd, y, lvlData))
			for (int i = 0; i < xEnd - xStart; i++) {
				if (!IsTileSolid(xStart + i, y + 1, lvlData))
					return false;
			}
		return true;
	}

	/**
	 * Checks if there is a clear line of sight between two hitboxes at a specified y-coordinate.
	 *
	 * @param lvlData       The level data representing the map.
	 * @param firstHitbox   The hitbox of the first entity.
	 * @param secondHitbox  The hitbox of the second entity.
	 * @param yTile         The y-coordinate at which to check for clear sight.
	 * @return {@code true} if there is a clear line of sight between the hitboxes, {@code false} otherwise.
	 */
	public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
		int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
		int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

		if (firstXTile > secondXTile)
			return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
		else
			return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
	}

	/**
	 * Converts the pixel data from an image to a 2D array representing the level map.
	 *
	 * @param img The image containing the level map.
	 * @return A 2D array representing the level map, where each element corresponds to a tile in the map.
	 */
	public static int[][] GetLevelData(BufferedImage img) {
		int[][] lvlData = new int[img.getHeight()][img.getWidth()];
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getRed();
				if (value >= 48)
					value = 0;
				lvlData[j][i] = value;
			}
		return lvlData;
	}

	/**
	 * Extracts the positions of crabs from an image representing the level map.
	 *
	 * @param img The image containing the level map with crab positions.
	 * @return An ArrayList containing Crabby objects representing the positions of crabs in the level.
	 */
	public static ArrayList<Crabby> GetCrabs(BufferedImage img) {
		ArrayList<Crabby> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == CRABBY)
					list.add(new Crabby(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
			}
		return list;
	}

	/**
	 * Finds the spawn point for the player in the level map image.
	 *
	 * @param img The image representing the level map.
	 * @return A Point object representing the spawn coordinates of the player.
	 */
	public static Point GetPlayerSpawn(BufferedImage img) {
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == 100)
					return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
			}
		return new Point(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);
	}

	/**
	 * Extracts potion objects from the level map image.
	 *
	 * @param img The image representing the level map.
	 * @return An ArrayList containing Potion objects representing the potions extracted from the image.
	 */
	public static ArrayList<Potion> GetPotions(BufferedImage img) {
		ArrayList<Potion> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == RED_POTION || value == BLUE_POTION)
					list.add(new Potion(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
			}

		return list;
	}

	/**
	 * Extracts container objects from the level map image.
	 *
	 * @param img The image representing the level map.
	 * @return An ArrayList containing GameContainer objects representing the containers extracted from the image.
	 */
	public static ArrayList<GameContainer> GetContainers(BufferedImage img) {
		ArrayList<GameContainer> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == BOX || value == BARREL)
					list.add(new GameContainer(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
			}

		return list;
	}

	/**
	 * Extracts spike objects from the level map image.
	 *
	 * @param img The image representing the level map.
	 * @return An ArrayList containing Spike objects representing the spikes extracted from the image.
	 */
	public static ArrayList<Spike> GetSpikes(BufferedImage img) {
		ArrayList<Spike> list = new ArrayList<>();

		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == SPIKE)
					list.add(new Spike(i * Game.TILES_SIZE, j * Game.TILES_SIZE, SPIKE));
			}

		return list;
	}

	/**
	 * Extracts cannon objects from the level map image.
	 *
	 * @param img The image representing the level map.
	 * @return An ArrayList containing Cannon objects representing the cannons extracted from the image.
	 */
	public static ArrayList<Cannon> GetCannons(BufferedImage img) {
		ArrayList<Cannon> list = new ArrayList<>();

		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == CANNON_LEFT || value == CANNON_RIGHT)
					list.add(new Cannon(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
			}

		return list;
	}

}