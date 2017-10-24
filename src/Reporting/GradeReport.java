package Reporting;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Logan on 10/16/2017.
 */
// TODO Refactor
public class GradeReport {
    public static void main(String[] args) throws Exception{
        //args[0] - Output Directory, args[1]- Section List, args[2]- report name

        //Import Section list
        ArrayList<Student> SectionList = new ArrayList<Student>();
        BufferedReader fin = new BufferedReader(new FileReader(args[1]));
        String temp = fin.readLine();
        while(temp != null){
            if(temp.equals("") || temp.charAt(0) == '#'){
                //skip
            }else if (!(Character.isWhitespace(temp.charAt(0)))) {
                SectionList.add(new Student(temp.trim()));
            }else{
                SectionList.get(SectionList.size()-1).addName(temp.trim());
            }
            temp = fin.readLine();
        }
        fin.close();
        //Find all grade.txt files
        FileSearch fileSearch = new FileSearch();
        fileSearch.searchDirectory(new File(args[0]), "grade.txt");
        int count = fileSearch.getResult().size();
        if(count ==0){
            System.out.println("\nNo result found!");
        }
        else{
            System.out.println("\nFound " + count + " result!\n");
            String[] tempData;
            for (String matched : fileSearch.getResult()){
                fin = new BufferedReader(new FileReader(matched));
                temp = fin.readLine();
                if(temp != null){
                    tempData = temp.split(":");
                    crossReference(SectionList, tempData[0], Double.parseDouble(tempData[1]));
                }else{
                    System.out.println("No grade found for: " + matched);
                }
                fin.close();
            }
        }

        //Write Grade report
        BufferedWriter fout = new BufferedWriter(new FileWriter(args[2] + "Report.csv"));
        fout.write("Master Name,Matched Name,Grade");
        fout.newLine();
        for(Student s : SectionList){
            if(!s.isMissing()){
                fout.write(s.getMasterName() + "," + s.getMatchedName() + "," + s.getGrade());
                fout.newLine();
            }
        }
        fout.write("------------------------------------\n                  Not 95\n------------------------------------\n");
        fout.newLine();
        for(Student s : SectionList){
            if(s.getGrade() < 95.0){
                fout.write(s.getMasterName() + "," + s.getMatchedName() + "," + s.getGrade());
                fout.newLine();
            }
        }
        fout.write("------------------------------------\n                  Missing\n------------------------------------\n");
        fout.newLine();
        for(Student s : SectionList){
            if(s.isMissing()){
                fout.write(s.getMasterName() + "," + s.getGrade());
                fout.newLine();
            }
        }
        fout.flush();
        fout.close();
        System.out.println("Grade Report Created");
    }

    public static void crossReference(ArrayList<Student> SL, String n, double g){
        for(Student s : SL){
            if(s.matchName(n)){
                //found match
                s.setGrade(g);
                return;
            }
        }
        SL.add(new Student(n, true, g));
    }

    public static class FileSearch{
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

    public static class Student{
        ArrayList<String> names;
        boolean missing;
        double grade;
        String matchedName;

        public Student(){
            names = new ArrayList<String>();
            missing = false;
            grade = 0.0;
            matchedName = "";
        }

        public Student(String name){
            names = new ArrayList<String>();
            names.add(name);
            missing = false;
            grade = 0.0;
            matchedName = "";
        }

        public Student(String name, boolean m, double g){
            names = new ArrayList<String>();
            names.add(name);
            missing = m;
            grade = g;
            matchedName = "";
        }

        public void addName(String n){
            names.add(n);
        }

        public String getMasterName(){
            return names.get(0);
        }

        public String getMatchedName(){
            return matchedName;
        }

        public double getGrade(){
            return grade;
        }

        public void setGrade(double g){
            grade = g;
        }

        public boolean isMissing(){
            return missing;
        }

        public boolean matchName(String n){
            for(String s : names){
                if(s.equals(n)){
                    matchedName = n;
                    return true;
                }
            }
            return false;
        }

        public String toString(){
            String result = "";
            for(String s : names){
                result += s + "\t";
            }
            result += grade;
            result += matchedName;
            return result;
        }
    }
}
