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

            int c = 0;
            try {
                c = Integer.parseInt(parts[2].trim());
            }
            catch(Exception e) {
                System.out.println("Error parsing int" + e + " ||| " + parts[2]);
            }
            maxGradePoints += c;
            Question q = new Question(parts[0], parts[1], c);
            this.addQuestion(q);
        }
    }
    public void grade(MainFrame context) {
        Question current;
        String input;
        earnedGradePoints = 0;
        for(int i = 0; i < questionList.size(); i++){
            current = questionList.get(i);
            System.out.print(current.displayToGrader() + " ");

            input = context.getInput();
            if(input.equals("")){
                current.grade();
                earnedGradePoints += current.getMaxPoints();
                System.out.println(current.getMaxPoints());
            }
            else {
                int grade = 0;
                try {
                    grade = Integer.parseInt(input);
                }
                catch(Exception e){
                    System.out.println("Grade could not be parsed" + input + " Exception: " + e);
                }
                current.grade(grade);
                earnedGradePoints += grade;
                System.out.println(grade);
            }
        }
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
