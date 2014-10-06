package core.utilities.sounds;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.easyogg.OggClip;

import core.Theater;

public class Track {

	private OggClip clip;
	private String name;
	private float volume;
	private float fading;
	private float fadeIn;
	private float fadeOut;
	private boolean loop;
	
	public Track(String ref, float volume, float fadeIn, boolean loop) {
		try {
			clip = new OggClip(new FileInputStream(System.getProperty("resources") + "/sounds/" + ref));
			name = ref;
			this.volume = volume;
			if(fadeIn != 0) {
				this.fadeIn = 0;
				fading = fadeIn;
				clip.setGain(0.0f);
			}
			this.loop = loop;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		if(fading != 0 && Ensemble.get().getMasterVolume() > 0f) {
			if(fading > 0) {
				fadeIn += Theater.getDeltaSpeed(0.025f);
				float adjust = (((Ensemble.get().getMasterVolume() != Ensemble.MAX_VOLUME ? Ensemble.get().getMasterVolume() : volume) / fading)
						* Theater.getDeltaSpeed(0.025f));
				if(clip.getGain() + adjust > (Ensemble.get().getMasterVolume() != Ensemble.MAX_VOLUME ? Ensemble.get().getMasterVolume() : volume))
					clip.setGain(Ensemble.get().getMasterVolume() != Ensemble.MAX_VOLUME ? Ensemble.get().getMasterVolume() : volume);
				else
					clip.setGain(clip.getGain() + adjust);
				
				if(fadeIn >= fading) {
					fading = 0;
					fadeIn = 0;
				}
			} else if(fading < 0) {
				fadeOut -= Theater.getDeltaSpeed(0.025f);
				float adjust = (((Ensemble.get().getMasterVolume() != Ensemble.MAX_VOLUME ? Ensemble.get().getMasterVolume() : volume) / Math.abs(fading))
						* Theater.getDeltaSpeed(0.025f));
				if(clip.getGain() - adjust <= 0)
					clip.setGain(0.0f);
				else
					clip.setGain(clip.getGain() - adjust);
				
				if(fadeOut <= fading || clip.getGain() == 0f) {
					fading = 0;
					fadeOut = 0;
					clip.setGain(0f);
				}
			}
		}
	}
	
	public void play() {
		if(fading == 0) {
			if(Ensemble.get().getMasterVolume() < volume)
				clip.setGain(Ensemble.get().getMasterVolume());
			else
				clip.setGain(volume);
		}
		clip.play();
	}
	
	public void adjustVolume(float masterVolume) {
		if(fading == 0) {
			if(masterVolume < volume) {
				clip.setGain(masterVolume);
			} else
				clip.setGain(volume);
		}
	}
	
	public OggClip getClip() {
		return clip;
	}
	
	public String getName() {
		return name;
	}

	public float getFadeIn() {
		return fadeIn;
	}
	
	public void setFadeIn(float fadeIn) {
		fading = fadeIn;
	}
	
	public float getFadeOut() {
		return fadeOut;
	}
	
	public void setFadeOut(float fadeOut) {
		fading = fadeOut;
	}
	
	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}
	
}
