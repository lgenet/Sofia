package Lab;

import GUI.MainFrame;

import java.util.ArrayList;

/**
 * Created by Logan on 10/14/2017.
 */
public class QuestionGroup {
    private ArrayList<Question> questionList = new ArrayList<Question>();
    private String groupRubricStatement;
    private int maxGradePoints;
    private int earnedGradePoints;

    public QuestionGroup(String[] lines) {
        groupRubricStatement = lines[0];
        maxGradePoints = 0;
        massAddQuestions(1, lines);
    }
    public QuestionGroup(String[] lines, String groupTitle) {
        groupRubricStatement = groupTitle;
        maxGradePoints = 0;
        massAddQuestions(0, lines);
    }
    private void massAddQuestions(int i, String[] lines) {
        for(; i < lines.length; i++ ){
            String[] parts = lines[i].split("\\|");
            if(parts.length != 4) { // TODO: Bad patch around invalid formatted questiosn
                System.out.println("Error: Question did not have correct format\n" + lines[i]);
                continue;
            }
            int c = 0;
            try {
                c = Integer.parseInt(parts[3].trim());
            }
            catch(Exception e) {
                System.out.println("Error parsing int" + e + " ||| " + parts[3]); // TODO: Evaluate this
            }
            maxGradePoints += c;
            Question q = new Question(parts[0], parts[1], parts[2], c);
            this.addQuestion(q);
        }
    }
    public void grade(MainFrame context, TestResult tr) {
        Question current;
        String input;
        earnedGradePoints = 0;
        for(int i = 0; i < questionList.size(); i++){
            current = questionList.get(i);
            context.appendGradingScreenTextSameLine(current.displayToGrader() + " ");

            if(tr.isTestPassed(current.getTestName())) {
                giveMaxPoints(context, current);
            }
            else {
                input = context.getInput();

                if (input.equals("")) {
                    giveMaxPoints(context, current);
                } else {
                    calculateGrade(context, current, input);
                }
            }
        }
    }
    private void giveMaxPoints(MainFrame context, Question current) {
        current.grade();
        earnedGradePoints += current.getMaxPoints();
        context.appendGradingScreenText(current.getMaxPoints());
    }
    private void calculateGrade(MainFrame context, Question current, String input) {
        int grade = 0;
        try {
            grade = Integer.parseInt(input);
        }
        catch(Exception e){
            context.appendGradingScreenText("Grade could not be parsed" + input + " Exception: " + e);
        }
        current.grade(grade);
        earnedGradePoints += grade;
        context.appendGradingScreenText(grade);
    }
    public int getScore() {
        int score = 0;
        for(int i = 0; i < questionList.size(); i++){
           score += questionList.get(i).getGrade();
        }
        return score;
    }
    public String toString() {
        String res = groupRubricStatement + " (" + maxGradePoints + "): " + earnedGradePoints + "\n";
        char letter = 'a';
        for(int i = 0; i < questionList.size(); i++, letter++ ){
            res += "\t" + letter + "\t" + questionList.get(i) + "\n";
        }
        return res;
    }

    public void addQuestion(Question q) {
        questionList.add(q);
    }
    public void removeQuestion(Question q) {
        for(int i = 0; i < questionList.size(); i++) {
            if(questionList.get(i) == q || questionList.get(i).equals(q)) {
                questionList.remove(i);
                break;
            }
        }
    }
    public void removeQuestion(int i) {
        questionList.remove(i);
    }
}
