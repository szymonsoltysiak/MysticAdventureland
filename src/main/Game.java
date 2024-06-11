package main;

import java.awt.Graphics;

import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import ui.AudioOptions;
import utilz.LoadSave;

/**
 * The main class responsible for managing the game loop and controlling game states.
 */
public class Game implements Runnable {

	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;

	private Playing playing;
	private Menu menu;
	private GameOptions gameOptions;
	private AudioOptions audioOptions;

	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 1.8f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

	/**
	 * Initializes the game and its components.
	 */
	public Game() {
		initClasses();

		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();

		startGameLoop();
	}

	/**
	 * Initializes the various classes required for the game.
	 */
	private void initClasses() {
		audioOptions = new AudioOptions();
		menu = new Menu(this);
		playing = new Playing(this);
		gameOptions = new GameOptions(this);
	}

	/**
	 * Starts the game loop by creating a new thread and starting it.
	 */
	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	/**
	 * Updates the game state.
	 */
	public void update() {
		switch (Gamestate.state) {
		case MENU:
			menu.update();
			break;
		case PLAYING:
			playing.update();
			break;
		case OPTIONS:
			gameOptions.update();
			break;
		case QUIT:
			gamePanel.getGame().playing.stopThreads();
		default:
			System.exit(0);
			break;

		}
	}

	/**
	 * Renders graphics to the game window.
	 *
	 * @param g The Graphics object to render graphics.
	 */
	public void render(Graphics g) {
		switch (Gamestate.state) {
		case MENU:
			menu.draw(g);
			break;
		case PLAYING:
			playing.draw(g);
			break;
		case OPTIONS:
			gameOptions.draw(g);
			break;
		default:
			break;
		}
	}

	/**
	 * The game loop where game updates and rendering occur.
	 */
	@Override
	public void run() {

		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		while (true) {
			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;

			}
		}

	}

	/**
	 * Handles the event when the game window loses focus.
	 */
	public void windowFocusLost() {
		if (Gamestate.state == Gamestate.PLAYING)
			playing.getPlayer().resetDirBooleans();
	}

	/**
	 * Retrieves the menu instance.
	 *
	 * @return The menu instance.
	 */
	public Menu getMenu() {
		return menu;
	}

	/**
	 * Retrieves the playing instance.
	 *
	 * @return The playing instance.
	 */
	public Playing getPlaying() {
		return playing;
	}

	/**
	 * Retrieves the game options instance.
	 *
	 * @return The game options instance.
	 */
	public GameOptions getGameOptions() {
		return gameOptions;
	}

	/**
	 * Retrieves the audio options instance.
	 *
	 * @return The audio options instance.
	 */
	public AudioOptions getAudioOptions() {
		return audioOptions;
	}

}