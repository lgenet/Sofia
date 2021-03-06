package Sanitizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// TODO: Write sanitizers for the other languages
// TODO: Revise this to look modular and support other languages
// TODO: Create parent class, and then the other child language sanitizers
// TODO: Move this all to a new package since there will be multiple sanitizers
public class SanitizeCpp extends Sanitizer {

	private static Map<String, String> requiredMethods = new HashMap<String, String>(){{
		put("void readin", "void readData");
		put("2", "two");
		put("3", "three");
	}};

	private static String sanitizeForStudentName(String current) {
		if(!current.toLowerCase().contains("name") || !current.toLowerCase().contains("//")){
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
	private static String sanitizeForMain(String current) {
		if(!current.contains("int main")) {
			return current;
		}
		String suggested = current.replaceAll("int main", "int lab");

		if(askToChange(current, suggested)) {
			return suggested;
		}
		return current;
	}
	private static boolean containsRqeuiredMethod(String current) {
//		for(int i = 0; i < requiredMethods.length; i++) {
//			if(current.contains(requiredMethods[i])) {
//				return true;
//			}
//		}
		return false;
	}
	private static String sanitizeMethods(String current) {
		if(!containsRqeuiredMethod(current)){
			return current;
		}
		return current;
	}

	protected static ArrayList<String> replaceTokens(BufferedReader fin, boolean isTestSubject) throws IOException {
		ArrayList<String> file = new ArrayList<String>();
		String temp = fin.readLine();
		while(temp != null){
			if(!(temp.contains("Student Name:") || temp.contains("namespace") || temp.toLowerCase().contains("program")  || temp.contains("cout"))){
				temp = sanitizeForStudentName(temp);
				temp = sanitizeForIO(temp);
				if(isTestSubject){
					temp = sanitizeForMain(temp);
//					temp = sanitizeMethods(temp);
				}
			}

			file.add(temp);
			temp = fin.readLine();
		}
		return file;
	}

}
