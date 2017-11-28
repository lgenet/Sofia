import java.io.*;
import java.util.*;


public class Generate_Default_Section_List {
	
	public static void main(String[] args) throws Exception{
        //Open files
        BufferedWriter fout = new BufferedWriter(new FileWriter("DefautSectionList.txt"));
        BufferedReader fin = new BufferedReader(new FileReader(args[0]));
        String temp = fin.readLine();
        
        //Write header
        fout.write("#bash_grader section list file");
        fout.newLine();
        fout.write("#Lines that are blank or START with a ‘#’ will be ignored");
        fout.newLine();
        fout.write("#This file must be in the format:");
        fout.newLine();
        fout.write("#[Master Name] (Spaces are allowed)");
        fout.newLine();
        fout.write("#	[Nick Name] (Spaces are allowed)");
        fout.newLine();
        fout.write("#	…");
        fout.newLine();
        fout.write("#	[Nick Name]");
        fout.newLine();
        fout.write("#[Master Name]");
        fout.newLine();
        fout.write("#…");
        fout.newLine();
        
        //Write students
        while(temp != null){
            temp = temp.trim();
            if(temp.equals("")){ //blank line, ignore
                
            }else{
                temp = temp.replaceAll("[^A-Za-z0-9 ]+", "");
                String[] nickNames = {"", ""};
                String[] tempData = temp.split(" ");
                for(int i = 0; i< tempData.length-1; i++){
                    nickNames[0] += tempData[i] + "_";
                }
                nickNames[0] += tempData[tempData.length-1];
                
                for(int i = tempData.length-1; i>0; i--){
                    nickNames[1] += tempData[i] + "_";
                }
                nickNames[1] += tempData[0];
                fout.write(temp);
                fout.newLine();
                fout.write("\t" + nickNames[0]);
                fout.newLine();
                fout.write("\t" + nickNames[1]);
                fout.newLine();
                fout.newLine();
            }
            temp = fin.readLine();
        }
        
        fin.close();
        fout.flush();
        fout.close();
        System.out.println("Default Section List Created!");
	}
}
