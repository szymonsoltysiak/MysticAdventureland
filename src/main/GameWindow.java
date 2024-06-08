package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JFrame;

/**
 * The GameWindow class represents the window where the game is displayed.
 * It creates a JFrame and adds the GamePanel to it, handles window focus events,
 * and sets various window properties.
 */
public class GameWindow {
	private JFrame jframe;

	/**
	 * Constructs a GameWindow with the specified GamePanel.
	 *
	 * @param gamePanel The GamePanel to be added to the window.
	 */
	public GameWindow(GamePanel gamePanel) {
		jframe = new JFrame();

		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(gamePanel);

		jframe.setResizable(false);
		jframe.pack();
		jframe.setLocationRelativeTo(null);
		jframe.setVisible(true);
		jframe.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().windowFocusLost();
			}

			@Override
			public void windowGainedFocus(WindowEvent e) {
				// Do nothing
			}
		});
	}
}
