package core.utilities.exceptions;

import org.newdawn.slick.opengl.Texture;
import core.utilities.loader.TextureLoader;

public class FailedTextureException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FailedTextureException() { 
		super(); 
	}
	
	public FailedTextureException(String message) {
		super(message); 
	}
	
	public FailedTextureException(String message, Throwable cause) { 
		super(message, cause); 
	}
	
	public FailedTextureException(Throwable cause) { 
		super(cause);
	}
	
	public Texture loadErrorTexture() {
		return TextureLoader.get().getErrorTexture();
	}
	
}
