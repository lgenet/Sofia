package Builder;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Logan on 10/20/2017.
 */
public class FileLoader {

    public static String[] loadStudentList(File file) {
        try {
            Scanner sf = new Scanner(file);
            ArrayList<String> temp = new ArrayList<String>();
            while (sf.hasNext())
                temp.add(sf.nextLine());
            String[] studentList = new String[temp.size()];
            for (int i = 0; i < studentList.length; i++) {
                studentList[i] = temp.get(i);
            }
            return studentList;
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "I am sorry about this, but I can not seem to load the list of students you asked for.  This info might help explain" +
                    "\n" + e);
            return new String[0];
        }

    }
}
