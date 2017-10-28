package Sanitizer;

import GUI.MainFrame;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Logan on 10/26/2017.
 */
public class Sanitizer {
    protected static boolean autoSanitize = false;

    public static void setAutoSanitize(boolean as) {
        autoSanitize = as;
    }
    public static void sanitize(MainFrame context) throws Exception {
        String labPath = context.config.getStudentInputPath() + "Lab" + context.config.getLabNumber();
        setAutoSanitize(context.config.isAutoSanitize());
        Files.walk(Paths.get(labPath))
                .forEach((FROM) -> {
                    String fileName = FROM.getFileName().toString();
                    if (fileName.contains(context.config.getLanguageExt())) {
                        try {
                            String filePath = FROM.toString();
                            BufferedReader fin = getFileReader(filePath);
                            boolean isTestSubject = false;  // Is this lab file for the unit tests or the original
                            if(fileName.contains("for_testing")) {
                                isTestSubject = true;
                            }
                            ArrayList<String> fileContents = runLanguageSpecificSanitizer(context, fin, isTestSubject);
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

    protected static ArrayList<String> runLanguageSpecificSanitizer(MainFrame context, BufferedReader fin, boolean isTestSubject) throws IOException {
        switch(context.config.getLanguageExt()) {
            case ".cpp":
                return SanitizeCpp.replaceTokens(fin, isTestSubject);
        }
        return new ArrayList<String>();
    }
    protected static boolean askToChange(String line, String suggested) {
        if(autoSanitize || suggested.equals(line)) {
            return true;
        }
        int res = JOptionPane.showConfirmDialog(null, "Do you want me to change:\n" +
                line + "\n--TO--\n" + suggested, "perform sanitize", JOptionPane.YES_NO_OPTION);
        return res == 0;
    }

    protected static BufferedReader getFileReader(String path) throws FileNotFoundException {
        File myFile = new File(path);
        FileReader fr = new FileReader(myFile);
        BufferedReader fin = new BufferedReader(fr);

        return fin;
    }

    protected static void writeToFile(String path, ArrayList<String> file) throws IOException {
        FileWriter fw = new FileWriter(path);
        BufferedWriter fout = new BufferedWriter(fw);
        for(int i = 0; i<file.size(); i++){
            fout.write(file.get(i));
            fout.newLine();
        }

        fout.flush();
        fout.close();
    }
}
