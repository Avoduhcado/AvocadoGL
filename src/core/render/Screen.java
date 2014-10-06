package core.render;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.util.ResourceLoader;

import core.Theater;
import core.entity.Actor;
import core.entity.Entity;
import core.scene.DeathScreen;
import core.scene.LoadingScreen;
import core.scene.PropQuad;
import core.scene.Stage;
import core.utilities.pathfinding.PathQuad;
import core.utilities.sounds.Ensemble;
import core.utilities.text.Text;
import de.matthiasmann.twl.utils.PNGDecoder;

public class Screen {

	private final int WIDTH = 800;
	private final int HEIGHT = 600;
	public int displayWidth = WIDTH;
	public int displayHeight = HEIGHT;
	
	private Actor focus;
	public Rectangle2D camera = new Rectangle2D.Double(0, 0, displayWidth, displayHeight);
	public Point2D light = new Point2D.Double(displayWidth / 2, displayHeight / 2);
	private static LoadingScreen loadingScreen;
	private static Color words = new Color(0, 0, 0);
		
	public static long drawTime;
	private static long startTime;
	
	//RED = 0xFF0000FF
    //GREEN = 0xFF00FF00
    //BLUE = 0xFFFF0000
    //WHITE = 0xFFFFFFFF
    //LITEGRAY = 0xFFBFBFBF
    //GRAY =  0xFF7F7F7F / ff808080
    //DARKGRAY = 0xFF3F3F3F
    //BLACK = 0xFF000000
	
	public Screen() {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("Avocado " + Theater.version + " FPS: " + Theater.fps + " Full of Bugs Edition");
			try {
				Display.setIcon(loadIcon(System.getProperty("resources") + "/ui/Icon.png"));
			} catch (IOException e) {
				System.out.println("Failed to load icon");
			}
			Display.setResizable(true);
			Display.create();

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, WIDTH, HEIGHT, 0, -1, 1);
			GL11.glViewport(0, 0, displayWidth, displayHeight);
		} catch (LWJGLException e) {
			System.err.println("Could not create display.");
		}
			
		camera = new Rectangle2D.Double(0, 0, displayWidth, displayHeight);
	}
	
	public static ByteBuffer[] loadIcon(String ref) throws IOException {
        InputStream fis = ResourceLoader.getResourceAsStream(ref);
        try {
            PNGDecoder decoder = new PNGDecoder(fis);
            ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth()*decoder.getHeight()*4);
            decoder.decode(bb, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
            bb.flip();
            ByteBuffer[] buffer = new ByteBuffer[1];
            buffer[0] = bb;
            return buffer;
        } finally {
            fis.close();
        }
    }
	
	public void draw(Stage stage) {
		if(Theater.debug())
			startTime = Theater.getTime();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		if(loadingScreen == null) {
			Theater.drawcount = 0;

			// Draw the backdrop
			if(stage.backdrop != null)
				stage.backdrop.draw();

			// TODO Draw fireworks (Get rid of this pl0x change to projectiles or something?)
			for(int x = 0; x<stage.fireworks.size(); x++) {
				stage.fireworks.get(x).draw();
				stage.fireworks.get(x).update();
			}
			
			// Draw the tile map
			if(stage.map != null) {
				stage.map.draw();
			}
			
			// Sort the z-buffer 
			for(int x = 0; x<stage.scenery.size(); x++) {
				for(int i = x; i>=0 && i>x-5; i--) {
					if(stage.scenery.get(x).getBox().getMaxY() < stage.scenery.get(i).getBox().getMaxY() && !stage.scenery.get(i).isFlat()) {
						stage.scenery.add(i, stage.scenery.get(x));
						stage.scenery.remove(x+1);
						x--;
					}
				}
			}
			// Draw the scenery
			for(Entity entity : stage.scenery) {
				entity.draw();
			}
			// Draw weather effects
			if(stage.weather != null) {
				drawOverlay(stage.weather.getRed(), stage.weather.getGreen(), stage.weather.getBlue(),
						stage.weather.getOpacity());
				stage.weather.draw();
			}
			// Draw prop quad quadrants during debug
			if(PropQuad.get() instanceof PropQuad && Theater.debug()) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				PathQuad.get().draw();
				PropQuad.get().draw();
				if(stage.debugNodes != null && !stage.debugNodes.getNodes().isEmpty())
					stage.debugNodes.draw();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			// Draw any text boxes
			stage.script.draw();
			// Draw the menu
			if(stage.menu.isOpen()) {
				stage.menu.draw();
			}
			// Draw the paused overlay
			if(Theater.paused) {
				drawOverlay(0, 0, 0, 0.8f);
				Text.drawString("Paused", (displayWidth/2) - 25, displayHeight/2, Color.white);
			}
			if(!stage.scenery.isEmpty()) {
				Text.drawString(stage.scenery.size()+" objects loaded", 0, 0, words);
			}
			/*if(stage.player != null) {
				Textbox.drawString(stage.player.getStats().getVivacity().getStat() + "", 0, 50);
				Textbox.drawString(stage.player.getStats().getEssence().getStat() + "", 0, 75);
				Textbox.drawString(stage.player.getStats().getVigor().getStat() + "", 0, 100);
			}*/
		} else {
			loadingScreen.draw();
		}
		if(Theater.debug())
			drawTime = Theater.getTime() - startTime;
	}
	
	public void drawOverlay(float red, float green, float blue, float opacity) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glPushMatrix();
		GL11.glColor4f(red, green, blue, opacity);
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2d(0, 0);
			GL11.glVertex2d(camera.getWidth(), 0);
			GL11.glVertex2d(camera.getWidth(), camera.getHeight());
			GL11.glVertex2d(0, camera.getHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public void drawCamera() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glPushMatrix();
		//GL11.glTranslatef((float)Screen.camera.getX(), (float)Screen.camera.getY(), 0);
		GL11.glColor3f(0.75f, 0.5f, 1.0f);
		GL11.glLineWidth(5.0f);
		
		GL11.glBegin(GL11.GL_LINE_LOOP);
		{
			GL11.glVertex2d(0, 0);
			GL11.glVertex2d(camera.getWidth(), 0);
			GL11.glVertex2d(camera.getWidth(), camera.getHeight());
			GL11.glVertex2d(0, camera.getHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public void drawError(String error) {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		Text.drawString(error + " Press confirm to exit.", 0, 0, Color.red);
	}
	
	public void dying() {
		Ensemble.get().getBackground().getClip().stop();
		DeathScreen death = new DeathScreen();
		while(death.getTimer() > 0) {
			death.draw();
			Display.update();
			Display.sync(200);
		}
		death = null;
	}
	
	public void follow() {
		if(focus.getDx() != 0 || focus.getDy() != 0) {
			Line2D xBorder = new Line2D.Double(0,0,0,0);
			if(focus.getDx() > 0 && camera.getMaxX() < PropQuad.get().getTopQuad().getMaxX()) {
				xBorder = new Line2D.Double(camera.getX() + (camera.getWidth() * 0.75f), camera.getY(),
						camera.getX() + (camera.getWidth() * 0.75f), camera.getMaxY());
			} else if(focus.getDx() < 0 && camera.getX() > PropQuad.get().getTopQuad().getX()) {
				xBorder = new Line2D.Double(camera.getX() + (camera.getWidth() * 0.25f), camera.getY(),
						camera.getX() + (camera.getWidth() * 0.25f), camera.getMaxY());
			}
			if(focus.getBox().intersectsLine(xBorder)) {
				camera = new Rectangle2D.Double(camera.getX() + focus.getDx(), camera.getY(), camera.getWidth(), camera.getHeight());
			}
			
			Line2D yBorder = new Line2D.Double(0,0,0,0);
			if(focus.getDy() > 0 && camera.getMaxY() < PropQuad.get().getTopQuad().getMaxY()) {
				yBorder = new Line2D.Double(camera.getX(), camera.getY() + (camera.getHeight() * 0.75f),
						camera.getMaxX(), camera.getY() + (camera.getHeight() * 0.75f));
			} else if(focus.getDy() < 0 && camera.getY() > PropQuad.get().getTopQuad().getY()) {
				yBorder = new Line2D.Double(camera.getX(), camera.getY() + (camera.getHeight() * 0.4f),
						camera.getMaxX(), camera.getY() + (camera.getHeight() * 0.4f));
			}
			if(focus.getBox().intersectsLine(yBorder)) {
				camera = new Rectangle2D.Double(camera.getX(), camera.getY() + focus.getDy(), camera.getWidth(), camera.getHeight());
			}
		}
	}
	
	public boolean resized() {
		if(Display.getWidth() != displayWidth || Display.getHeight() != displayHeight)
			return true;
		
		return false;
	}
	
	public void resize() {
		displayWidth = Display.getWidth();
		displayHeight = Display.getHeight();
		/*GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, displayWidth, displayHeight, 0, -1, 1);*/
		GL11.glViewport(0, 0, displayWidth, displayHeight);
		
		camera = new Rectangle2D.Double(0, 0, displayWidth, displayHeight);
		centerOn();
		//if(stage.menu.isOpen())
			//stage.menu.doUpdate();
	}
	
	public void update() {
		Display.update();
		/*if(Display.wasResized()) {
			resize();
		}*/
		Display.sync(200);
	}
	
	public void updateHeader() {
		Display.setTitle("Avocado " + Theater.version + " FPS: " + Theater.fps + " Full of Bugs Edition");
	}
	
	public boolean toBeClosed() {
		if(Display.isCloseRequested())
			return true;
		
		return false;
	}
	
	public void close() {
		Display.destroy();
	}
	
	public void centerOn() {
		if(focus != null) {
			camera.setFrameFromCenter(focus.getBox().getCenterX(), focus.getBox().getCenterY(),
					focus.getBox().getCenterX() - (WIDTH / 2), focus.getBox().getCenterY() - (HEIGHT / 2));
						
			if(camera.getX() < PropQuad.get().getTopQuad().getX()) {
				camera.setFrame(PropQuad.get().getTopQuad().getX(), camera.getY(), camera.getWidth(), camera.getHeight());
			} else if(camera.getMaxX() > PropQuad.get().getTopQuad().getMaxX()) {
				camera.setFrame(PropQuad.get().getTopQuad().getMaxX() - camera.getWidth(), camera.getY(), camera.getWidth(), camera.getHeight());
			}
			if(camera.getY() < PropQuad.get().getTopQuad().getY()) {
				camera = new Rectangle2D.Double(camera.getX(), PropQuad.get().getTopQuad().getY(), camera.getWidth(), camera.getHeight());
			} else if(camera.getMaxY() > PropQuad.get().getTopQuad().getMaxY()) {
				camera = new Rectangle2D.Double(camera.getX(), PropQuad.get().getTopQuad().getMaxY() - camera.getHeight(), camera.getWidth(), camera.getHeight());
			}
		}
	}
	
	public void setFocus(Actor focus) {
		this.focus = focus;
	}
	
	public Actor getFocus() {
		return focus;
	}
	
}
