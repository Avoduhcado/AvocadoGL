package core.utilities.loader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.newdawn.slick.util.ResourceLoader;

public class ImageLoader {

	private static ImageLoader loader = new ImageLoader();
	
	public static ImageLoader get() {
		return loader;
	}
	
	private HashMap<String, BufferedImage> sprites = new HashMap<String, BufferedImage>();
	
	//Will need fresh map if I want to edit it
	public BufferedImage getSprite(String ref) {
		if(sprites.get(ref) != null){
			return (BufferedImage) sprites.get(ref);
		}
		
		BufferedImage image = null;
		try {
			URL url = ResourceLoader.getResource(ref);

			if (url == null) {
				System.out.println("Can't find ref: "+ref);
			}
			
			image = ImageIO.read(ResourceLoader.getResource(ref));
		} catch (IOException e) {
			System.out.println("Failed to load: "+ref);
		}
		
		sprites.put(ref, image);
		
		return image;
	}
}
