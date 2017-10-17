package Sanitizer;

import java.io.*;
import java.util.*;


public class SanitizeCpp {

	private String fileNameToSearch;
	private List<String> result = new ArrayList<String>();
	
	public String getFileNameToSearch() {
		return fileNameToSearch;
	}
	
	public void setFileNameToSearch(String fileNameToSearch) {
		this.fileNameToSearch = fileNameToSearch;
	}
	
	public List<String> getResult() {
		return result;
	}
	
	public static void main(String[] args) {
		
		SanitizeCpp fileSearch = new SanitizeCpp();
		
		//try different directory and filename :)
		fileSearch.searchDirectory(new File(args[0]), ".cpp");
		//fileSearch.searchDirectory(new File("/Users/bill/Downloads/Lab%205%20Download%20Feb%2026%2C%202016%20337%20PM"), ".cpp");
		Scanner scan = new Scanner(System.in);
		System.out.print("Sanitize? [y/n]: ");
		String userContinue = scan.nextLine();
		if(userContinue.equalsIgnoreCase("n")){
			//Do nothing, skip
		}
		else{
			
			int count = fileSearch.getResult().size();
			if(count ==0){
			    System.out.println("\nNo result found!");
			}
			else{
			    System.out.println("\nFound " + count + " result!\n");
			    for (String matched : fileSearch.getResult()){
			    	//System.out.println("Found : " + matched);
			    	replaceToken(matched, scan);
			    }
			}
		}
	}
	
	public static void replaceToken(String path, Scanner scan){
		try {
			ArrayList<String> file = new ArrayList<String>();
			File cppfile = new File(path);
			FileReader fr = new FileReader(cppfile);
			BufferedReader fin = new BufferedReader(fr);
            String temp = fin.readLine();
            while(temp != null){
                if(temp.contains("Student Name:") || temp.contains("namespace") || temp.contains("Program") || temp.contains("program")  || temp.contains("cout")){
                    //skip
                }else if(temp.contains("name")){
                    String x = "";
                    System.out.println("---" + temp);
                    System.out.println("Change line? [y]");
                    x = scan.nextLine();
                    if(x.equalsIgnoreCase("") || x.equalsIgnoreCase("y")){
                        temp = temp.replaceAll(":", "");
                        String[] tempData = temp.split("(name)");
                        temp = "//";
                        temp += "Student Name:";
                        temp += tempData[1].trim();
                    }
                }else if(temp.contains("Name")){
                    String x = "";
                    System.out.println("---" + temp);
                    System.out.println("Change line? [y]");
                    x = scan.nextLine();
                    if(x.equalsIgnoreCase("") || x.equalsIgnoreCase("y")){
                        temp = temp.replaceAll(":", "");
                        String[] tempData = temp.split("(Name)");
                        temp = "//";
                        temp += "Student Name:";
                        temp += tempData[1].trim();
                    }
                }else if(temp.contains("NAME")){
                    String x = "";
                    System.out.println("---" + temp);
                    System.out.println("Change line? [y]");
                    x = scan.nextLine();
                    if(x.equalsIgnoreCase("") || x.equalsIgnoreCase("y")){
                        temp = temp.replaceAll(":", "");
                        String[] tempData = temp.split("(NAME)");
                        temp = "//";
                        temp += "Student Name:";
                        temp += tempData[1].trim();
                    }
                }else if(temp.contains(".txt")){
                    if(temp.contains("ofstream") || temp.contains("output") || temp.contains("out")){
                        //skip
                    }else{
                        String x = "";
                        System.out.println("---" + temp);
                        System.out.println("Change line? [y]");
                        x = scan.nextLine();
                        if(x.equalsIgnoreCase("") || x.equalsIgnoreCase("y")){
                            String newLine = temp.substring(0,temp.indexOf("\""));
                            newLine += "\"input.txt\"";
                            newLine += temp.substring(temp.lastIndexOf(")"), temp.length());
                            temp = newLine;
                        }
                    }
                }				
				
				file.add(temp);
				temp = fin.readLine();
			}
			
			
			cppfile.delete();
			
			
			FileWriter fw = new FileWriter(path);
			BufferedWriter fout = new BufferedWriter(fw);
			for(int i = 0; i<file.size(); i++){
				fout.write(file.get(i));
				fout.newLine();
			}
			
			fout.flush();
			fin.close();
			fout.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void searchDirectory(File directory, String fileNameToSearch) {
		
		setFileNameToSearch(fileNameToSearch);
		
		if (directory.isDirectory()) {
		    search(directory);
		} else {
		    System.out.println(directory.getAbsoluteFile() + " is not a directory!");
		}
		
	}
	
	private void search(File file) {
		
		if (file.isDirectory()) {
			System.out.println("Searching directory ... " + file.getAbsoluteFile());
			
			//do you have permission to read this directory?	
		    if (file.canRead()) {
				for (File temp : file.listFiles()) {
					if (temp.isDirectory()) {
						search(temp);
					} else {
						if (temp.getName().toLowerCase().contains(getFileNameToSearch())) {			
							result.add(temp.getAbsoluteFile().toString());
						}
						
					}
				}
				
			} else {
				System.out.println(file.getAbsoluteFile() + "Permission Denied");
			}
		}
		
	}
	
}

//public class grade2 {

//	public static void main(String[] args) {
//		File parent = new File("/Users/bill/Desktop/Intro to C++/Grade/Lab3Sec002");
//
//		if (parent.isDirectory()) {
//		    // Take action of the directory
//			File[] children = parent.listFiles(new FileFilter() {
//		        public boolean accept(File file) {
//		            return file.isDirectory() || file.getName().toLowerCase().endsWith(".cpp");
//		        }
//		    });
//			
//			for(int i = 0; i<children.length; i++){
//				System.out.println(children[i].getPath());
//				
//			}
//
//		}
//
//	}

//}
