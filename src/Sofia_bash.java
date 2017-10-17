import java.io.PrintWriter;

/**
 * Created by Logan on 10/14/2017.
 */
public class Sofia_bash {

    public void printRubric() {

       // PrintWriter os = new PrintWriter("grade.txt");
    }
}
// Ask for Lab number
// Add option/flow for generating the report (see generate report classes provided)
//      Javac GenerateGradeReport.java
//      Javac GenerateGrade Report $2 $sectionlist $assignment name
// Menu option to assign a late penalty
// Sanitize and Compile module
//      Sanitize is provided by the sanitizeCPP.java file provided
//      Use PERL Provided to get student name        sname=`perl -n -e 's/\r\n|\n|\r/\n/; s/[ \t][ \t]*$//;' -e 'print $1, "\n" if m/^.*student[\s]*name[\s]*:[\s]*([\S].*)$/i;' "$file" | perl -p -e 's/[ \t]/_/g;' | tr -cd "[:print:]"`
//      Make a Directory with the students name in the Output folder
//      Copy their CPP there, along with the compiler, unit tests, run the sed lines and so on that we added
//      This bit is going to require system commands
// Generate a section list from a list of students
// ability to add a list of students
// Allow grading to start at any point/name
// Check if grade exists, and if so skip it
/*
#!/bin/bash

        if [ -z "$OPT_N" ]; then
        #build assignments
        if [ "$LANGUAGE" == "c++" ]; then
        echo "Building c++"
        javac SanitizeCpp.java
        java SanitizeCpp "$1"
        find "$1" -type f -name "*.cpp" -print | while read file; do
        fname=`basename "$file" .cpp | tr -d " "`


        echo Building: "'$sname'"
        if [ -z "$sname" ]; then  # Show the first 10 lines of the file and figure out the name
        head -10 "$file"
        echo "Enter student name: \c"
        read sname </dev/tty
        sname=`echo "$sname" | perl -p -e 's/[\s][\s]*$//;' -e 's/[ \t]/_/g;' | tr -cd "[:print:]"`
        fi

        while [ -f "$2/$sname/" ]; do
        sname="${sname}x"
        done
        if [ ! -d "$2/$sname" ]; then
        mkdir "$2/$sname"
        fi
        cp "$file" "$2/$sname/lab".cpp
        (cd "$2/$sname";  # move to students folder
        cp ../../lab_test.cpp ./;  # move Unit test code to student folder
        sed -i 's/int main/int lab/g' lab.cpp; # Rename main to lab for tester
        g++ -std=c++14 -isystem ../../googletest/googletest/include -pthread ./lab_test.cpp  ../../libgtest.a>err.txt 2>&1) # Compile GUNIT test
        if [ ! -z "$INPUT_FILE" ]; then
        cat "$INPUT_FILE" > "$2/$sname/input.txt"
        fi

        echo ""
        echo "Are student names ok? [Enter when ready]: ";read start
        echo "Enter directory name to start (blank to start from beginning) []: ";read start

        ls >$TMP.a
        if [ -z "$start" ]; then
        cp $TMP.a $TMP.b
        else
        sed -n "/^$start\$/,\$p" <$TMP.a >$TMP.b
        fi

        for file in `cat $TMP.b`; do
        replace_grade=yes
        if [ -f "$file/grade.txt" ]; then
        #Grade exists
        echo "Grade file exists, overwrite? (y/n) [n]: ";read replace_grade
        case $replace_grade in
        yes|y)
        replace_grade=yes
        rm "$file/grade.txt"
        ;;
        no|n)
        replace_grade=
        ;;
        *)
        #default
replace_grade=
        ;;
        esac
        fi

        if [ -z "$replace_grade" ]; then
        #skip
        echo "Skipping student $file"
        else
        if [ "$LANGUAGE" == "c++" ]; then
        (cd "$file"
        if [ -f a.out ]; then
        echo "$file"
        echo "Running a.out:"
        trap : 2
        ./a.out  #check for java or c++ here
        echo ""
        else
        echo "Error text:"
        cat err.txt
        fi )
        fi
        echo "(g)rade (n)ext (q)uit"
        echo "(g): ";read action
        case $action in
        quit|q)
        ;;
        next|n)
        #move to next assignment
        ;;
        grade|g)
        ;;
        *)
        #default
action=g
        ;;
        esac
        if [ -z "$action" ]; then
        action=g
        fi
        case $action in
        quit|q)
        #exit case
        exit 0
        ;;
        next|n)
        #move to next assignment

    fi
done
cd ..
javac Generate_grade_report.java
java Generate_grade_report "$2" "$sectionlist" "$ASSIGNMENT_NAME"















#print grade report
#for file in `cat $TMP.a`; do
#   name=`echo tail -1 "$file/grade.txt" | cut -f 1 -d : | perl -e
#        %tbl = ();
#        open(F, "'"$sectionlist"'") || die "can't open ";
#        $_= <>;
#        while($_) {
#            chomp;
#            $mainname = $0;
#            $tbl{$mainname} = $mainname;
#            $_ = <>;
#            while ($_ && m/^\s/) {
#                chomp;
#                s/^\s+//;
#                $tbl{$mainname} = $0;
#                $_ = <>;
#            }
#        }
#        $_ = <>;
#        chomp;
#        print $tbl{$_}, "\n";
#        close(F);`

#    if [ -z $name ]; then
#        echo Missing Name
#    else
#        echo $name
#    fi



#    echo `tail -1 "$file/grade.txt" | cut -f 1 -d :`
#    echo `tail -1 "$file/grade.txt" | cut -f 2 -d :`
#done
*/