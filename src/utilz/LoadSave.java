package utilz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class LoadSave {

	public static final String PLAYER_ATLAS = "player_sprites.png";
	public static final String LEVEL_ATLAS = "outside_sprites.png";
	public static final String MENU_BUTTONS = "button_atlas.png";
	public static final String MENU_BACKGROUND = "menu_background.png";
	public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String SOUND_BUTTONS = "sound_button.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";
	public static final String MENU_BACKGROUND_IMG = "background_menu.png";
	public static final String PLAYING_BG_IMG = "playing_bg_img.png";
	public static final String BIG_CLOUDS = "big_clouds.png";
	public static final String SMALL_CLOUDS = "small_clouds.png";
	public static final String CRABBY_SPRITE = "crabby_sprite.png";
	public static final String STATUS_BAR = "health_power_bar.png";
	public static final String COMPLETED_IMG = "completed_sprite.png";
	public static final String POTION_ATLAS = "potions_sprites.png";
	public static final String CONTAINER_ATLAS = "objects_sprites.png";
	public static final String TRAP_ATLAS = "trap_atlas.png";
	public static final String CANNON_ATLAS = "cannon_atlas.png";
	public static final String CANNON_BALL = "ball.png";
	public static final String DEATH_SCREEN = "death_screen.png";
	public static final String OPTIONS_MENU = "options_background.png";

	public static BufferedImage GetSpriteAtlas(String fileName){
		BufferedImage img = null;
		try {
			img = ImageIO.read(new FileInputStream("res/"+fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}


	public static BufferedImage[] GetAllLevels() {
		File dir = new File("res/lvls");
		File[] files = dir.listFiles((d, name) -> name.endsWith(".png"));

		if (files == null) {
			throw new RuntimeException("Directory not found or I/O error: " + dir.getAbsolutePath());
		}

		// Sort files numerically based on file name (e.g., "1.png", "2.png", ...)
		Arrays.sort(files, (f1, f2) -> {
			int num1 = Integer.parseInt(f1.getName().replace(".png", ""));
			int num2 = Integer.parseInt(f2.getName().replace(".png", ""));
			return Integer.compare(num1, num2);
		});

		BufferedImage[] imgs = new BufferedImage[files.length];
		for (int i = 0; i < files.length; i++) {
			try {
				imgs[i] = ImageIO.read(files[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return imgs;
	}


}