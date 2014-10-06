package core.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Variables {

	/* TODO
	 * Player name?
	 * Death Debt
	 * Integration into Actions system
	 */
	
	public HashMap<String, Integer> integers = new HashMap<String, Integer>();
	public HashMap<String, String> strings = new HashMap<String, String>();
	public HashMap<String, Boolean> booleans = new HashMap<String, Boolean>();
	public HashMap<String, Float> floats = new HashMap<String, Float>();
	
	private static Variables variables = new Variables();
	
	public static Variables get() {
		return variables;
	}
	
	public static void loadVariables() {
		variables.clear();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/variables"));
			
			String line;
			while((line = reader.readLine()) != null) {
				String[] temp = line.split(";");

				if(temp[1].matches("I")) {
					variables.integers.put(temp[0], Integer.parseInt(temp[2]));
				} else if(temp[1].matches("S")) {
					variables.strings.put(temp[0], temp[2]);
				} else if(temp[1].matches("B")) {
					variables.booleans.put(temp[0], Boolean.parseBoolean(temp[2]));
				} else if(temp[1].matches("F")) {
					variables.floats.put(temp[0], Float.parseFloat(temp[2]));
				}
			}
		} catch (FileNotFoundException e) {
	    	System.out.println("The variable database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Variable database failed to load!");
	    	e.printStackTrace();
	    }
	}
	
	public void loadVariables(BufferedReader reader) {
		clear();
		
		try {
			String line;
			String[] temp;
			while((line = reader.readLine()) != null && !line.matches("<END>")) {
				if(line.matches("<INTEGER>")) {
					while((line = reader.readLine()) != null && !line.matches("<BREAK>")) {
						temp = line.split(";");
						integers.put(temp[0], Integer.parseInt(temp[1]));
					}
				} else if(line.matches("<STRING>")) {
					while((line = reader.readLine()) != null && !line.matches("<BREAK>")) {
						temp = line.split(";");
						strings.put(temp[0], temp[1]);
					}
				} else if(line.matches("<BOOLEAN>")) {
					while((line = reader.readLine()) != null && !line.matches("<BREAK>")) {
						temp = line.split(";");
						booleans.put(temp[0], Boolean.parseBoolean(temp[1]));
					}
				} else if(line.matches("<FLOAT>")) {
					while((line = reader.readLine()) != null && !line.matches("<BREAK>")) {
						temp = line.split(";");
						floats.put(temp[0], Float.parseFloat(temp[1]));
					}
				}
			}
		} catch (FileNotFoundException e) {
	    	System.out.println("The variable database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Variable database failed to load!");
	    	e.printStackTrace();
	    }
	}
	
	public void clear() {
		integers.clear();
		strings.clear();
		booleans.clear();
		floats.clear();
	}
	
}
