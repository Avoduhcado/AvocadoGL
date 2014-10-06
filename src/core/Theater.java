package core;

import java.io.File;
import core.keyboard.Keybinds;
import core.render.Screen;
import core.scene.Stage;
import core.utilities.Config;
import core.utilities.ErrorLogger;
import core.utilities.Input;
import core.utilities.Save;
import core.utilities.Screenshot;
import core.utilities.menu.MainMenu;

public class Theater {

	/* TODO
	 * Actor-
	 * Make animations less shit to work with
	 * 
	 * Options menu-
	 * Volume slider
	 * Key mapping interface
	 * 
	 */
	
	public static String version = "v0.0.090";
	private static long currentTime;
	private static long lastLoopTime;
	public static int fps = 0;
	public static float delta; // Should be 5 every loop when capped at 200
	public static float deltaMax = 25f;
	
	private boolean playing;
	public static boolean debug = false;
	public static int drawcount = 0;
	@SuppressWarnings("unused")
	private static long loadTime;
	private long start;
	public static String saveFile = "New";
	private boolean recording = false;
	private double recordTime = 0;
	public static boolean paused = false;
	public static boolean toBeClosed = false;
		
	private Screen screen;
	private Stage stage;
	private MainMenu menu;
	
	private static Theater theater;
	
	public static void init() {
		theater = new Theater();
		Config.loadConfig();
	}
	
	public static Theater get() {
		return theater;
	}
	
	public Theater() {
		screen = new Screen();
		stage = new Stage();
	}
	
	public void loadStage() {
		//stage.loadStage("tempMaze");
		stage.loadStage("Forest3");
		stage.addScenery();
		screen.setFocus(stage.player);
		screen.centerOn();
	}
	
	public static boolean debug() {
		return debug;
	}
	
	public void close() {
		screen.close();
		Save.get().clearField();
		System.exit(0);
	}
	
	public void update() {
		getFps();
		screen.draw(stage);
		if(menu.isOpen()) {
			menu.draw();
			menu.update(stage);
		}
		screen.update();
		if(!paused)
			stage.act();
		Input.checkInput(stage);
		
		if(screen.toBeClosed() || Keybinds.EXIT.clicked() || toBeClosed) {
			close();
		}
	}
		
	public void getFps() {
		delta = getTime() - currentTime;
		currentTime = getTime();
		lastLoopTime += delta;
		fps++;
		if(lastLoopTime >= 1000) {
			screen.updateHeader();
			fps = 0;
			lastLoopTime = 0;
			/*if(debug) {
				System.out.println("Load Time: " + loadTime + "ms\nDraw Time: " + Screen.drawTime + "ms\nAct Time:  " + Stage.actTime + "ms");
				System.out.println("Draws per frame: " + Theater.drawcount);
			}*/
		}
	}
	
	public void play() {
		playing = true;
		currentTime = getTime();
				
		menu = new MainMenu();
		Save.get().clearField();
		update();
		Keybinds.clear();
		
		try { 
			while(playing) {
				if(debug)
					start = getTime();
				update();
							
				if(screen.resized()) {
					screen.resize();
					currentTime = getTime();
				}
				
				while(paused) {
					update();
					if(Keybinds.CONFIRM.clicked() || (Keybinds.PAUSE.clicked() && !Keybinds.PAUSE.held())) {
						paused = false;
						break;
					}
				}
				
				if(recording) {
					recordTime += getDeltaSpeed(0.025f);
					if(recordTime >= 0.25) {
						Screenshot.saveScreenshot(new File(System.getProperty("user.dir")), 800, 600);
						recordTime = 0;
					}
				}
				
				if(debug)
					loadTime = getTime() - start;
			}
		} catch(Exception e) {
			e.printStackTrace();
			ErrorLogger.logError(e);
			while(true) {
				screen.update();
				screen.drawError("Error check game files for log.");
				Keybinds.update();
				if(Keybinds.CONFIRM.clicked() || Keybinds.EXIT.clicked() || screen.toBeClosed())
					break;
			}
			System.exit(-1);
		}
	}
	
	public Screen getScreen() {
		return screen;
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public static long getTime() {
		return System.nanoTime() / 1000000;
	}
	
	public static long getExactTime() {
		return System.nanoTime();
	}
	
	/**
	 * 0.025 as speed to get as close as you can to reducing something by 1 every second
	 */
	public static float getDeltaSpeed(float speed) {
		return (Theater.delta * speed) / Theater.deltaMax;
	}
	
	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/natives");
		System.setProperty("resources", System.getProperty("user.dir") + "/resources");
		System.setProperty("saves", System.getProperty("user.dir") + "/Saved Games");
		
		try {
			Theater.init();
			theater.play();
		} catch(Exception e) {
			e.printStackTrace();
			ErrorLogger.logError(e);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			System.exit(-1);
		}
	}
}
