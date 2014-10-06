package core.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import core.Theater;
import core.scene.Stage;
import core.utilities.text.PlainText;
import core.utilities.text.Textbox;

public class Save {

	private static Save save = new Save();
	
	public static Save get() {
		return save;
	}
	
	private File dir = new File(System.getProperty("saves"));
	
	public void createDirectory() {
		// Create save directory
		if(dir.mkdir()) {
			System.out.println("Save Directory Created");
		} else {
			// Save directory failed to be created
			System.err.println("Save Directory was not created");
		}
	}
	
	public void save(Stage stage, String name) {
		try{
			name = name.trim();
			// If no save directory exists
			if(!dir.exists())
				createDirectory();
			// If it's a new save
			if(Theater.saveFile.matches("New")) {
				// If name is taken add numbers until it isn't
				if(name.matches("New") || name.matches("") || name.matches("field"))
					name += "_";
				File file = new File(System.getProperty("saves") + "/" + name);
				int x = 0;
				while(file.exists()) {
					x++;
					file = new File(System.getProperty("saves") + "/" + name + x);
				}
				file.mkdirs();
				writeFile(stage, file);
				Theater.saveFile = file.getName();
			// Not a new save, update file
			} else {
				File file = new File(System.getProperty("saves") + "/" + Theater.saveFile);
				if(!file.exists())
					file.mkdirs();
				writeFile(stage, file);
			}
			Textbox text = new PlainText(false, 3f, false);
			text.buildText("Game Saved!;Current save file: " + Theater.saveFile);
			text.setSource(Theater.get().getStage().player);
			Theater.get().getStage().addText(text);
		}catch(Exception e){
			e.fillInStackTrace();
		} 
	}
	
	public void fieldSave(Stage stage, String name) {
		try {
			name = name.trim();
			// Create save directory
			if(!dir.exists()) {
				createDirectory();
			}
			// New save file
			if(Theater.saveFile.matches("New")) {
				// Filter out "Special" names
				if(name.matches("New") || name.matches("") || name.matches("field"))
					name += "_";
				// If name is taken add numbers until it isn't
				File file = new File(System.getProperty("saves") + "/" + name + "/field");
				int x = 0;
				while(file.exists()) {
					x++;
					file = new File(System.getProperty("saves") + "/" + name + x + "/field");
				}
				file.mkdirs();
				Theater.saveFile = file.getParentFile().getName();
			// Not a new save file, update current save file
			} else {
				File file = new File(System.getProperty("saves") + "/" + Theater.saveFile + "/field");
				if(!file.exists())
					file.mkdirs();
			}
			// If a temporary field folder exists, merge it with the new save
			File field = new File(System.getProperty("saves") + "/field");
			if(field.exists()) {
				File temp = new File(System.getProperty("saves") + "/" + Theater.saveFile + "/field");
				temp.delete();
				if(!field.renameTo(new File(System.getProperty("saves") + "/" + Theater.saveFile + "/field")))
					System.out.println("Failed to merge field save.");
			}
			// Save the player data and the current field
			writeFile(stage, new File(System.getProperty("saves") + "/" + Theater.saveFile + "/field"));
			writeTemp(stage, new File(System.getProperty("saves") + "/" + Theater.saveFile + "/field"));
		} catch(Exception e) {
			e.fillInStackTrace();
		}
	}
	
	public void saveTempField(Stage stage) {
		try {
			// Create save directory
			if(!dir.exists()) {
				createDirectory();
			}
			// Create temporary field folder
			File file = new File(System.getProperty("saves") + "/field");
			if(!file.exists())
				file.mkdirs();
			// Save current field
			writeTemp(stage, file);
		} catch(Exception e) {
			e.fillInStackTrace();
		}
	}
	
	public void clearField() {
		if(dir.exists()) {
			// Select temporary field folder
			File file = new File(System.getProperty("saves") + "/field");
			// Remove each file and then the folder
			if(file.exists()) {
				// Delete files until directory is empty
				for(File temp : file.listFiles()) {
					temp.delete();
				}
				// Delete directory
				file.delete();
			}
		}
	}
	
	public void clearFieldFromSave() {
		if(dir.exists()) {
			// Select saved field folder
			File file = new File(System.getProperty("saves") + "/" + Theater.saveFile + "/field");
			// Prepare a temporary field to copy files into
			File field = new File(System.getProperty("saves") + "/field");
			if(field.exists())
				field.delete();
			// Transfer files to temporary save, delete previous folder if need be
			if(file.exists()) {
				if(!file.renameTo(field))
					System.out.println("Failed to unmerge field save.");
				// If folder is empty, delete it
				if(file.getParentFile().listFiles().length == 0)
					file.getParentFile().delete();
			}
		}
	}
	
	public void writeFile(Stage stage, File file) throws IOException {
		File current = new File(file + "/main.rpg");
		if(current.exists()) {
			current.delete();
		}
		current.createNewFile();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(current));
		
		// Save player
		writer.write("<PLAYER>");
		writer.newLine();
		writer.write("<LOCATION>");
		writer.newLine();
		writer.write(stage.scene + ";" + stage.player.getX() + ";" + stage.player.getY());
		writer.newLine();
		writer.write("<BREAK>");
		writer.newLine();
		writer.write("<STATS>");
		writer.newLine();
		writer.write(stage.player.getStats().getVitality().save());
		writer.newLine();
		writer.write(stage.player.getStats().getVigor().save());
		writer.newLine();
		writer.write(stage.player.getStats().getVirtue().save());
		writer.newLine();
		writer.write(stage.player.getStats().getVivacity().save());
		writer.newLine();
		writer.write("<BREAK>");
		writer.newLine();
		writer.write("<EQUIPMENT>");
		writer.newLine();
		writer.write("0;" + stage.player.getEquipment().getGold());
		writer.newLine();
		writer.write(stage.player.getEquipment().getArmor().save());
		writer.newLine();
		writer.write(stage.player.getEquipment().getWeapons().save());
		writer.newLine();
		writer.write(stage.player.getEquipment().getPockets().save());
		writer.newLine();
		writer.write(stage.player.getEquipment().getQuiver().save());
		writer.newLine();
		writer.write(stage.player.getEquipment().getSpellbook().save());
		writer.newLine();
		writer.write("<BREAK>");
		writer.newLine();
		writer.write("<END>");
		writer.newLine();
		writer.newLine();
				
		// Save party members
		for(int x = 1; x<stage.party.getMembers().size() - 1; x++) {
			System.out.println("Party member saved");
		}
		
		// Save variables
		writer.write("<VARIABLES>");
		writer.newLine();
		writer.write("<INTEGER>");
		writer.newLine();
		Iterator<Entry<String, Integer>> iterInt = Variables.get().integers.entrySet().iterator();
		while(iterInt.hasNext()) {
			Map.Entry<String, Integer> pair = (Entry<String, Integer>) iterInt.next();
			writer.write(pair.getKey() + ";" + pair.getValue());
			writer.newLine();
			iterInt.remove();
		}
		writer.write("<BREAK>");
		writer.newLine();
		writer.write("<STRING>");
		writer.newLine();
		Iterator<Entry<String, String>> iterStr = Variables.get().strings.entrySet().iterator();
		while(iterStr.hasNext()) {
			Map.Entry<String, String> pair = (Entry<String, String>) iterStr.next();
			writer.write(pair.getKey() + ";" + pair.getValue());
			writer.newLine();
			iterStr.remove();
		}
		writer.write("<BREAK>");
		writer.newLine();
		writer.write("<BOOLEAN>");
		writer.newLine();
		Iterator<Entry<String, Boolean>> iterBool = Variables.get().booleans.entrySet().iterator();
		while(iterBool.hasNext()) {
			Map.Entry<String, Boolean> pair = (Entry<String, Boolean>) iterBool.next();
			writer.write(pair.getKey() + ";" + pair.getValue());
			writer.newLine();
			iterBool.remove();
		}
		writer.write("<BREAK>");
		writer.newLine();
		writer.write("<END>");
		
		writer.close();
	}

	public void writeTemp(Stage stage, File file) throws IOException {
		File current = new File(file + "/" + stage.scene);
		if(current.exists()) {
			current.delete();
		}
		current.createNewFile();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(current));
		
		for(int x = 0; x<stage.cast.size(); x++) {
			writer.write("<ACTOR>");
			writer.newLine();
			writer.write(stage.cast.get(x).getName() + ";" + stage.cast.get(x).getX() + ";" + stage.cast.get(x).getY() + ";" + stage.cast.get(x).getID());
			writer.newLine();
			if(stage.cast.get(x).getSpeed() != 2) {
				writer.write("<SPEED>");
				writer.newLine();
				writer.write(stage.cast.get(x).getSpeed() + "");
				writer.newLine();
				writer.write("<BREAK>");
				writer.newLine();
			}
			writer.write("<STATS>");
			writer.newLine();
			writer.write(stage.cast.get(x).getStats().getVitality().save());
			writer.newLine();
			writer.write(stage.cast.get(x).getStats().getVigor().save());
			writer.newLine();
			writer.write(stage.cast.get(x).getStats().getVirtue().save());
			writer.newLine();
			writer.write(stage.cast.get(x).getStats().getVivacity().save());
			writer.newLine();
			writer.write("<BREAK>");
			writer.newLine();
			writer.write("<EQUIPMENT>");
			writer.newLine();
			writer.write("0;" + stage.cast.get(x).getEquipment().getGold());
			writer.newLine();
			writer.write(stage.cast.get(x).getEquipment().getArmor().save());
			writer.newLine();
			writer.write(stage.cast.get(x).getEquipment().getWeapons().save());
			writer.newLine();
			writer.write(stage.cast.get(x).getEquipment().getPockets().save());
			writer.newLine();
			writer.write(stage.cast.get(x).getEquipment().getQuiver().save());
			writer.newLine();
			writer.write(stage.cast.get(x).getEquipment().getSpellbook().save());
			writer.newLine();
			writer.write("<BREAK>");
			writer.newLine();
			if(stage.cast.get(x).getAction() != null) {
				writer.write("<DIALOGUE>");
				writer.newLine();
				writer.write(stage.cast.get(x).getAction().saveSwitches());
				writer.newLine();
				for(int d = 0; d<stage.cast.get(x).getAction().getActions().length; d++) {
					writer.write(stage.cast.get(x).getAction().getActions()[d]);
					writer.newLine();
				}
				writer.write("<BREAK>");
				writer.newLine();
			}
			writer.write("<END>");
			writer.newLine();
			writer.newLine();
		}
		
		for(int x = 0; x<stage.props.size(); x++) {
			if(stage.props.get(x).isUnique()) {
				writer.write("<PROP>");
				writer.newLine();
				writer.write(stage.props.get(x).getName() + ";" + stage.props.get(x).getX() + ";" + stage.props.get(x).getY() + ";" + stage.props.get(x).getID() + 
						";" + stage.props.get(x).isFlat());
				writer.newLine();
				if(stage.props.get(x).getAction() != null) {
					writer.write("<DIALOGUE>");
					writer.newLine();
					writer.write(stage.props.get(x).getAction().saveSwitches());
					writer.newLine();
					for(int d = 0; d<stage.props.get(x).getAction().getActions().length; d++) {
						writer.write(stage.props.get(x).getAction().getActions()[d]);
						writer.newLine();
					}
					writer.write("<BREAK>");
					writer.newLine();
				}
				if(stage.props.get(x).getContainer() != null) {
					writer.write("<CONTAINER>");
					writer.newLine();
					writer.write(stage.props.get(x).getContainer().save());
					writer.newLine();
					writer.write("<BREAK>");
					writer.newLine();
				}
				writer.write("<END>");
				writer.newLine();
				writer.newLine();
			}
		}
		
		writer.close();
	}
	
	public String getSaves() {
		try {
			if(!dir.exists()) {
				createDirectory();
			} else {
				FileFilter filter = new FileFilter() {
					public boolean accept(File dir) {
						return dir.isDirectory();
					}
				};
				if(dir.listFiles(filter).length > 0) {
					FilenameFilter nameFilter = new FilenameFilter() {
					    public boolean accept(File dir, String name) {
					    	return name.endsWith(".rpg") || name.matches("field");
					    }
					};
					
					File[] files = dir.listFiles(filter);
					String saves = "";
					for(File file : files) {
						if(file.list(nameFilter).length > 0)
							saves += file.getName() + ";";
					}
					return saves;
				}
				/*FilenameFilter filter = new FilenameFilter() {
				    public boolean accept(File dir, String name) {
				    	return name.endsWith(".rpg");
				    }
				};
				if(dir.list(filter).length > 0) {
					File[] files = dir.listFiles(filter);
					String saves = "";
					for(int x = 0; x<files.length; x++) {
						saves += files[x].getName().substring(0, files[x].getName().lastIndexOf('.')) + ";";
					}
					return saves;
				}*/
			}
		}catch(Exception e){
			e.fillInStackTrace();
		}
		return null;
	}
	
	public boolean hasFieldSave() {
		if(new File(dir + "/" + Theater.saveFile + "/field").exists())
			return true;
		
		return false;
	}
	
	public void loadSave(Stage stage, boolean field) {
		BufferedReader reader;
		try {
			if(field)
				reader = new BufferedReader(new FileReader(dir + "/" + Theater.saveFile + "/field/main.rpg"));
			else
				reader = new BufferedReader(new FileReader(dir + "/" + Theater.saveFile + "/main.rpg"));
			
			String line;
			while((line = reader.readLine()) != null) {
				if(line.matches("<PLAYER>")) {
					stage.loadPlayer(reader);
				} else if(line.matches("<PARTY>")) {
					System.out.println("Loading party member");
				} else if(line.matches("<VARIABLES>")) {
					Variables.get().loadVariables(reader);
				}
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
	    	System.out.println("The save file has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Save file failed to load!");
	    	e.printStackTrace();
	    }
	}
	
}
