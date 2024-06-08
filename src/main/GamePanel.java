package main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

/**
 * The GamePanel class represents the panel where the game graphics are rendered.
 * It extends JPanel and handles user inputs via KeyboardInputs and MouseInputs.
 */
public class GamePanel extends JPanel {

	private MouseInputs mouseInputs;
	private Game game;

	/**
	 * Constructs a GamePanel object.
	 *
	 * @param game The Game object associated with this panel.
	 */
	public GamePanel(Game game) {
		mouseInputs = new MouseInputs(this);
		this.game = game;
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}

	/**
	 * Sets the preferred size of the panel.
	 */
	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
	}

	/**
	 * Updates the game logic.
	 */
	public void updateGame() {
		// Method stub for updating the game logic.
	}

	/**
	 * Overrides the paintComponent method to render the game graphics.
	 *
	 * @param g The Graphics object used for rendering.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}

	/**
	 * Retrieves the associated Game object.
	 *
	 * @return The associated Game object.
	 */
	public Game getGame() {
		return game;
	}
}