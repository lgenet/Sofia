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

    private static int unknownCount = 0;// TODO: make this not gross
    private static int labCount = 0;    // TODO: Do something better here

    private static String getStudentNameFromFile(File current) {
        String[] nameParts = current.getName().split("-");
        String studentName = "UNKNOWN_" + unknownCount;
        if(nameParts.length > 2) {
            studentName = nameParts[2];
        }
        else {
            unknownCount++;
        }
        return studentName.trim();
    }
    private static String getFileNameForCondenser(Path p) {
        String fileName = "" + p.getFileName();
        if(fileName.indexOf(".cpp") != -1) {
            if(labCount == 0) {
                fileName = "lab.cpp";
            }
            else {
                fileName = "lab" + (char) ('a' + labCount) + ".cpp";
            }
            labCount++;
        }
        return fileName;
    }

    private static String extractStudentSubmission(File current, String studentName) throws IOException {
        String temporaryLabPath = getRawStudentExtractPath() + "/" + studentName;
        UnzipUtil.unzip(current.getPath(), temporaryLabPath);

        return temporaryLabPath;
    }
    private static void condenseLabDirectory(String studentLabTemporaryPath, String studentName) throws IOException {
        labCount = 0;
        Files.walk(Paths.get(studentLabTemporaryPath))
            .forEach((FROM) -> {
                String fullPath = getStudentLabDestinationPath() + "/" + studentName + "/" + getFileNameForCondenser(FROM);
                Path TO = Paths.get(fullPath);

                if (!FROM.toFile().isDirectory()) {
                    new File(fullPath).getParentFile().mkdirs();

                    //overwrite existing file, if exists
                    CopyOption[] options = new CopyOption[]{
                            StandardCopyOption.REPLACE_EXISTING,
                            StandardCopyOption.COPY_ATTRIBUTES
                    };
                    try {
                        Files.copy(FROM, TO, options);
                    } catch (IOException ioe) {
                        writeToLog(FROM, TO);
                        context.displayError("Failed to copy files to temporary location: " + ioe, "Extract Error - Copy");
                    }
                }

            });
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
        f.delete();
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
            String studentName = getStudentNameFromFile(current);

            try {
                String studentLabTemporaryPath = extractStudentSubmission(current, studentName);
                condenseLabDirectory(studentLabTemporaryPath, studentName);
            }
            catch(IOException ioe) {
                context.displayError("I am sorry, but I could not extract the lab file for " + studentName +
                        ".  Here is more info:\n" + ioe, "Error Extracting");
            }
        }
        closeLogWriter();
    }
}
