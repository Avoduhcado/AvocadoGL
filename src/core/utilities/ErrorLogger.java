package core.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorLogger {

	public static void logError(Exception e) {
		File dir = new File(System.getProperty("user.dir") + "/Errors");
		if(!dir.exists()) {
			dir.mkdir();
		}
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
		Date date = new Date();
		
		File file = new File(dir + "/Error " + dateFormat.format(date) + ".txt");
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		StringBuilder error = new StringBuilder();
		error.append(e.toString());
		error.append(System.getProperty("line.separator"));
		for(int x = 0; x<e.getStackTrace().length; x++) {
			error.append(e.getStackTrace()[x]);
			error.append(System.getProperty("line.separator"));
		}
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(error.toString());
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
}
