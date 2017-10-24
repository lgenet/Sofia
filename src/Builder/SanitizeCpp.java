package Builder;

import GUI.MainFrame;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class SanitizeCpp {
	private static boolean autoSanitize = false;
	public static void setAutoSanitize(boolean autoSanitize) {
		SanitizeCpp.autoSanitize = autoSanitize;
	}
	public static void sanitize(MainFrame context) throws Exception {
		String labPath = context.config.getStudentInputPath() + "Lab" + context.config.getLabNumber();
		setAutoSanitize(context.config.isAutoSanitize());
		Files.walk(Paths.get(labPath))
			.forEach((FROM) -> {
				if (FROM.getFileName().toString().indexOf(".cpp") != -1) {
					try {
						String filePath = FROM.toString();
						BufferedReader fin = getFileReader(filePath);

						ArrayList<String> fileContents = replaceTokens(fin);
						fin.close();

						if(!new File(filePath).delete()) { // Delete the old file
							context.displayError("Uh... I am not quite sure what happened but I could not delete the old file at " + filePath,
									"Failed to delete old file");
						}

						writeToFile(filePath, fileContents);
					} catch (IOException e) {
						context.displayError("I am sorry, but I could not sanitize the file " + FROM.getFileName().toString()
								+ "\nHere are some more details for you.\n" + e, "Failed to Sanitize");
					}
				}
			});
	}

	private static boolean askToChange(String line, String suggested) {
		if(autoSanitize) {
			return true;
		}
		int res = JOptionPane.showConfirmDialog(null, "Do you want me to change:\n" +
				line + "\n--TO--\n" + suggested, "perform sanitize", JOptionPane.YES_NO_OPTION);
		return res == 0;
	}
	private static String sanitizeForStudentName(String current) {
		if(!current.toLowerCase().contains("name")){
			return current;
		}
		String suggested = current.replaceAll(":", "");
		String[] tempData = suggested.split("((?i)name)");
		suggested = "// ";
		suggested += "Student Name: ";
		suggested += tempData[1].trim();

		if(askToChange(current, suggested)) {
			return suggested;
		}
		return current;
	}
	private static String sanitizeForIO(String current) {
		if(!current.contains(".txt")) {
			return current;
		}
		String fileName = "input.txt";
		if(current.contains("ofstream")) {
			fileName = "output.txt";
		}
		String suggested = current.substring(0, current.indexOf("\""));
		suggested += "\"" + fileName + "\"";
		suggested += current.substring(current.lastIndexOf(")"), current.length());
		if(askToChange(current, suggested)) {
			return suggested;
		}
		return current;
	}

	private static ArrayList<String> replaceTokens(BufferedReader fin) throws IOException {
		ArrayList<String> file = new ArrayList<String>();
		String temp = fin.readLine();
		while(temp != null){
			if(!(temp.contains("Student Name:") || temp.contains("namespace") || temp.toLowerCase().contains("program")  || temp.contains("cout"))){
				temp = sanitizeForStudentName(temp);
				temp = sanitizeForIO(temp);
			}

			file.add(temp);
			temp = fin.readLine();
		}
		return file;
	}
	private static void writeToFile(String path, ArrayList<String> file) throws IOException {
		FileWriter fw = new FileWriter(path);
		BufferedWriter fout = new BufferedWriter(fw);
		for(int i = 0; i<file.size(); i++){
			fout.write(file.get(i));
			fout.newLine();
		}

		fout.flush();
		fout.close();
	}

	private static BufferedReader getFileReader(String path) throws FileNotFoundException {
		File cppfile = new File(path);
		FileReader fr = new FileReader(cppfile);
		BufferedReader fin = new BufferedReader(fr);

		return fin;
	}
}
