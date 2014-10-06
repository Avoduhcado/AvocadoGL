package core.utilities.sounds;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.easyogg.OggClip;

public class SoundEffect {

	private OggClip clip;
	private float volume;
	
	public SoundEffect(String ref, float volume) {
		try {
			clip = new OggClip(new FileInputStream(System.getProperty("resources") + "/sounds/" + ref));
			this.volume = volume;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		if(Ensemble.get().getMasterVolume() < volume)
			clip.setGain(Ensemble.get().getMasterVolume());
		else
			clip.setGain(volume);
		clip.play();
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
	}
	
	public void adjustVolume(float masterVolume) {
		if(masterVolume < volume) {
			clip.setGain(masterVolume);
		} else
			clip.setGain(volume);
	}
	
	public OggClip getClip() {
		return clip;
	}
	
}
