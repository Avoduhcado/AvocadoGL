package core.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import core.keyboard.Keybinds;
import core.utilities.sounds.Ensemble;

public class Config {

	public static void loadConfig() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/config"));

	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		if(line.matches("<AUDIO>")) {
	    			String[] temp = reader.readLine().split("=");
	    			Ensemble.get().setMasterVolume(Float.parseFloat(temp[1]));
	    		} else if(line.matches("<VIDEO>")) {
	    			// TODO Custom video settings
	    		} else if(line.matches("<KEYS>")) {
	    			while((line = reader.readLine()) != null) {
	    				Keybinds.changeBind(line);
	    			}
	    		}
	    	}

	    	reader.close();
	    } catch (FileNotFoundException e) {
	    	createConfig();
	    } catch (IOException e) {
	    	System.out.println("Config file failed to load!");
	    	e.printStackTrace();
	    }
	}
	
	public static void createConfig() {
		File dir = new File(System.getProperty("user.dir") + "/config");
		
		// Create save directory
		if(dir.mkdir()) {
			try {
				saveConfig();
			} catch(Exception e) {
				System.err.println("Config file failed to be created");
				e.fillInStackTrace();
			}
			
			System.out.println("Config file created");
		} else {
			// Save directory failed to be created
			System.err.println("Config file was not created");
		}
	}
	
	public static void saveConfig() throws IOException {
		File current = new File(System.getProperty("user.dir") + "/config");
		if(current.exists()) {
			current.delete();
		}
		current.createNewFile();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(current));
		
		writer.write("<AUDIO>");
		writer.newLine();
		writer.write("volume=" + Ensemble.get().getMasterVolume());
		writer.newLine();
		writer.newLine();
		
		writer.write("<KEYS>");
		writer.newLine();
		for(int x = 0; x<Keybinds.values().length; x++) {
			writer.write(Keybinds.values()[x].name() + "=" + Keybinds.values()[x].getKey());
			writer.newLine();
		}
		
		writer.close();
	}
	
}
