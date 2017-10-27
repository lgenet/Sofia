package Lab;

import GUI.MainFrame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Logan on 10/14/2017.
 */
public class LabLoader {
    public static Lab[] buildLabLists(MainFrame context, String[] studentList, String fileName, int lateFee) {
        String rawData = loadFromFile(fileName);
        Lab[] labList = new Lab[studentList.length];
        for(int i = 0; i < labList.length; i++){
            labList[i] = new Lab(context.config.getLabNumber(), studentList[i], buildQuestionList(rawData), lateFee);

            String labPath =  "Lab" + context.config.getLabNumber() + "/";
            String labDocumentPath = context.config.getStudentInputPath() + labPath + studentList[i] + "/lab" + context.config.getLanguageExt();
            String gradePath = context.config.getStudentGradePath() + labPath +studentList[i] +"-Grade.txt";

            labList[i].setLabDocument(LabLoader.loadFromFile(labDocumentPath));
            String gradeTxt = LabLoader.loadFromFile(gradePath);

            if(!gradeTxt.equals("")) {
                labList[i].foundGrade(gradeTxt);
            }
        }
        return labList;
    }

    public static Lab buildLab(String name, int labNumber, String fileName, int lateFee) {
        String rawData = loadFromFile(fileName);
        return new Lab(labNumber, name, buildQuestionList(rawData), lateFee);
    }
    private static String loadFromFile(String fileName) {
        String result = "";
        try {
            result = readFile(fileName);
        }
        catch (Exception e) {
            System.out.println("Error loading file " + fileName + " Exception: " + e);
        }
        return result;
    }

    private static ArrayList<QuestionGroup> buildQuestionList(String rawFileData) {
        ArrayList<QuestionGroup> masterList = new ArrayList<QuestionGroup>();

        String[] questionGroups = rawFileData.split("\\|\\|\\|");
        for(int i = 0; i < questionGroups.length; i++) {
            String questionGroup = questionGroups[i];
            String[] questions = questionGroup.split("\\r\\n");
            if(questions.length > 0) {
                masterList.add(new QuestionGroup(questions));
            }
        }
        return masterList;
    }
    public static String readFile(String fileName) throws Exception {
        String result = "";
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            result =  sb.toString();
        } finally {
            br.close();
        }
        return result;
    }
}
