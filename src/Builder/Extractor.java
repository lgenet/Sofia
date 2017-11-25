package Builder;

import GUI.MainFrame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;

/**
 * Created by Logan on 10/20/2017.
 */
public class Extractor {
    private final static String unArchivedPath = "Temporary/Unarchived";
    private final static String rawStudentExtractPath = "Temporary/raw";
    private final static String studentLabDestinationPath = "Lab";
    private final static String logFilePath = "./extraction-errors.log";
    private final static String temporaryPrefix = "Temporary";
    private static PrintWriter logWriter;

    private static MainFrame context;

    private static int unknownCount = 0;    // TODO: make this not gross
    private static int labCount = 0;        // TODO: Do something better here
    private static int labTestCount = 0;    // TODO: Do something better here too

    public static void setContext (MainFrame mf) {
        context = mf;
        initPaths();
    }
    private static void initPaths() {
        createPath(getUnArchivedPath());
        createPath(getRawStudentExtractPath());
        createPath(getStudentLabDestinationPath());
    }

    private static void createPath(String path) {
        new File(path).getParentFile().mkdirs();
    }

    private static String getTemporaryRoot() {
        return context.config.getStudentInputPath() + temporaryPrefix;
    }
    private static String getUnArchivedPath() {
        return context.config.getStudentInputPath() + unArchivedPath;
    }
    private static String getRawStudentExtractPath() {
        return context.config.getStudentInputPath() + rawStudentExtractPath;
    }
    private static String getStudentLabDestinationPath() {
        return context.config.getStudentInputPath() + studentLabDestinationPath + context.config.getLabNumber();
    }
    public static String getLogFilePath() {
        return logFilePath;
    }

    private static String getStudentNameFromFile(File current) {
        String[] nameParts = current.getName().split("-");
        String studentName = "UNKNOWN_" + unknownCount;
        if(nameParts.length > 2) {
            studentName = nameParts[2];
        }
        else {
            unknownCount++;
        }
        return studentName.trim().replaceAll(" ", "_");
    }
    private static String getFileNameForCondenser(Path p) {
        String fileName = "" + p.getFileName();
        String languageExt = context.config.getLanguageExt();
        if(fileName.indexOf(languageExt) != -1) {
            fileName = getLabName("lab", languageExt, labCount);
        }
        return fileName;
    }
    private static String getLabName(String labName, String languageExt, Integer count) {
        String fileName;
        if(count == 0) {
            fileName = labName + languageExt;
        }
        else {
            fileName = labName + (char) ('a' + count) + languageExt;
        }
        count++;
        return fileName;
    }

    private static void bundleStudentSubmission(File current, String labPath) throws  IOException {
        File path = new File(labPath);
        if(!path.exists()) {
            path.mkdirs();
        }
        FileLoader.copyFile(current.toPath().toString(), (labPath + "/" + current.getName()));
    }
    private static void extractStudentSubmission(File current, String labPath) throws IOException {
        UnzipUtil.unzip(current.getPath(), labPath);
    }
    private static void copyUnitTestFile(String studentPath) {
        String unitTestPath = "./Resources/UnitTests/lab_" + context.config.getLabNumber() + "_test.cpp";
        if(!new File(studentPath).exists()) {
            context.displayDebugMessage("I could not copy the unit test script to " + studentPath + " because it does not exist");
            return;
        }
        try {
            FileLoader.copyFile(Paths.get(unitTestPath), Paths.get(studentPath + "lab_test.cpp"));
        } catch (IOException e) {
            context.displayError("I am sorry about this but I could not add the unit test file to the student's submission package");
        }
    }
    private static String condenseLabDirectory(String studentLabTemporaryPath, String studentName) throws IOException {
        labCount = 0;
        String finalStudentPath = getStudentLabDestinationPath() + "/" + studentName + "/";
        String languageExt = context.config.getLanguageExt();
        Files.walk(Paths.get(studentLabTemporaryPath))
            .forEach((from) -> {
                String fileName =  getFileNameForCondenser(from);
                String fullPath = finalStudentPath + fileName;
                Path to = Paths.get(fullPath);

                if (!from.toFile().isDirectory()) {
                    new File(fullPath).getParentFile().mkdirs();

                    try {
                        FileLoader.copyFile(from, to);
                        if(fileName.contains("lab") && fileName.contains(languageExt)) {
                            String labTestPath = getLabName("lab_for_testing", languageExt, labTestCount);
                            Path toDuplicateLab = Paths.get(finalStudentPath + labTestPath);
                            FileLoader.copyFile(from, toDuplicateLab);
                        }
                    } catch (IOException e) {
                        writeToLog(from, to);
                        context.displayMessage("I am sorry but, I could not copy a file from its temporary location to its final " +
                                "destination during lab extraction...  Here is all I know:\n" + e);
                    }
                }

            });
        return finalStudentPath;
    }

    private static void writeToLog(Path from, Path to) {
        logWriter.println("Could not move: \t" + from + "\tTO\t" + to);
    }

    private static void openLogWriter() {
        createPath(logFilePath);
        try {
            logWriter = new PrintWriter(logFilePath);
        }
        catch (FileNotFoundException e) {
            context.displayError("Could not open extraction log writer");
            logWriter = null;
        }
    }
    private static void closeLogWriter() {
        if(logWriter != null) {
            logWriter.close();
        }
    }

    public static void cleanupTmpFiles() {
        File f = new File(getTemporaryRoot());
        if(f.exists()){
            f.delete();
        }
    }
    public static void cleanupTmpFiles(MainFrame mf) {
        setContext(mf);
        cleanupTmpFiles();
    }
    public static void extract(File archiveFile, MainFrame mf) {
        setContext(mf);
        extract(archiveFile);
    }

    public static void extract(File archiveFile) {
        try{
            UnzipUtil.unzip(archiveFile.getPath(), getUnArchivedPath());
        }
        catch (Exception e) {
            context.displayError("I am sorry, I could not extract the Lab Archive for you.  Here are some more details \n" + e, "Extract Error - Archive");
            return; // end early if the first step fails
        }

        openLogWriter();

        File folder = new File(getUnArchivedPath());
        File[] listOfStudentZips = folder.listFiles();

        for (int i = 0; i < listOfStudentZips.length; i++) {
            File current = listOfStudentZips[i];
            String studentName = getStudentNameFromFile(current); // TODO: Eval if we want to update student list here?

            try {
                String studentLabTemporaryPath = getRawStudentExtractPath() + "/" + studentName;
                if(current.getName().contains(".zip")){
                    extractStudentSubmission(current, studentLabTemporaryPath);
                }
                else if(current.getName().contains((".rar"))) {
                    context.displayError("Student: " + studentName + " used a .rar file to submit.  I can not handle this!");
                }
                else if(current.getName().contains(".")) {
                    bundleStudentSubmission(current, studentLabTemporaryPath);
                }
                String studentLabFinalPath = condenseLabDirectory(studentLabTemporaryPath, studentName);
                copyUnitTestFile(studentLabFinalPath);
            }
            catch(IOException ioe) {
                context.displayError("I am sorry, but I could not extract the lab file for " + studentName +
                        ".  Here is more info:\n" + ioe, "Error Extracting");
            }
        }
        closeLogWriter();
    }
}
