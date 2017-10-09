#!/bin/bash
#Lab and assignment grader
#./bash_grader <flags> [INPUT_DIR] [OUTPUT_DIR]
#Supported arguments:

#-n	No Build. Does not build students assignments from the input directory, starts evaluating
#at the first student in the output directory. Any previous evaluations will be lost

#-r	Report. Generate the grade report from existing evaluations. Does not build or evaluate

#-h	Help. Shows basic operating instructions

#-l [integer] Late Penalty. This will deduct a late penalty from each assignment that is evaluated

GLOBAL_RETURN=
LANGUAGE=
INPUT_FILE=
ASSIGNMENT_NAME=
TMP=/tmp/aaa$$
OPT_N=
OPT_R=
OPT_L=
LATE_P=0

read_number(){
#$1 prompt
    GLOBAL_RETURN=
    while true; do
    echo "$1";read input_string
    case $input_string in
        *[!0-9]*)
        echo "Invalid: Input must be a number"
        ;;
        *)
        GLOBAL_RETURN=$input_string
        break
        ;;
    esac
done
}

. bash_grader.cfg

while getopts :nrhl: FLAG; do
  case $FLAG in
    n)  #set option for no build
		#no compile
      OPT_N=yes
      ;;
    r)  #set option for report
        shift $((OPTIND-1))  #This tells getopts to move on to the next argument.
        javac Generate_grade_report.java
        java Generate_grade_report "$2" "$sectionlist" "$ASSIGNMENT_NAME"
        exit 0
      ;;
    h)  #show help
        printf "\nSyntax: bash_grade <flags> [input_directory] [output_directory]\n"
        printf "\nSyntax: bash_grade <flags> [student_list.txt]\n\n"

        printf "Supported flags:\n"

        printf "\t-n\tNo Build. Does not build students assignments from the input directory, starts evaluating at the first student in the output directory. Any previous evaluations will be lost\n"

        printf "\t-r\tReport. Generate the grade report from existing evaluations. Does not build or evaluate\n"

        printf "\t-h\tHelp. Shows basic operating instructions\n"

        printf "\t-l\t[integer] Late Penalty. This will deduct a late penalty from each assignment that is evaluated\n"
        printf "\t-g\tGenerate Section List. Given as input a .txt file with student names, one per line, will generate a default Section List"
        exit 0
      ;;
	l)
        #Set option for late penalty
        OPT_L=yes
        LATE_P="$OPTARG"
	  ;;
    g)
        #Generate section list
        javac Generate_Default_Section_List.java
        java Generate_Default_Section_List "$1"
        exit 0
    ;;
    \?) #unrecognized option - show help
        echo "Unrecognized option" >&2
        exit 2
      ;;
    :)
        echo "Option -$OPTARG requires an argument." >&2
        exit 2
        ;;
	  esac
done

shift $((OPTIND-1))  #This tells getopts to move on to the next argument.

if [ -z "$LANGUAGE" ]; then
    echo 'Language [c++]: ';read LANGUAGE
    if [ -z "$LANGUAGE" ]; then
        LANGUAGE='c++'
    fi
fi

if [ -z "$ASSIGNMENT_NAME" ]; then
    echo 'Assignment Name [Lab]: ';read ASSIGNMENT_NAME
    if [ -z "$ASSIGNMENT_NAME" ]; then
        ASSIGNMENT_NAME='Lab'
    fi
fi

if [ -z "$INPUT_FILE" ]; then
    echo 'Input File (enter for none) []: ';read INPUT_FILE
    if [ -z "$INPUT_FILE" ]; then
        INPUT_FILE=
    fi
fi

#echo "$LANGUAGE"
#echo "$ASSIGNMENT_NAME"
#echo "$INPUT_FILE"

if [ -n "$OPT_R" ]; then
    #run grade report from output dir '$2'
    echo "option -r selected"
fi

if [ -z "$OPT_N" ]; then
    #build assignments
    if [ "$LANGUAGE" == "c++" ]; then
    echo "Building c++"
        javac SanitizeCpp.java
        java SanitizeCpp "$1"
        find "$1" -type f -name "*.cpp" -print | while read file; do
            fname=`basename "$file" .cpp | tr -d " "`

            sname=`perl -n -e 's/\r\n|\n|\r/\n/; s/[ \t][ \t]*$//;' -e 'print $1, "\n" if m/^.*student[\s]*name[\s]*:[\s]*([\S].*)$/i;' "$file" | perl -p -e 's/[ \t]/_/g;' | tr -cd "[:print:]"`

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

        done
    elif [ "$LANGUAGE" == "java" ]; then
        echo "Will build Java"
    fi
fi

cd "$2"
ls

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
            ;;
            grade|g)
            cat "$file"/*.cpp

	    ########################################
	    # Grading Section
	    ########################################
            read_number "Header [5]: "
            header_grade=$GLOBAL_RETURN
            if [ -z "$header_grade" ]; then
                header_grade=5
            fi
            read_number "Purpose [4]: "
            purpose_grade=$GLOBAL_RETURN
            if [ -z "$purpose_grade" ]; then
                purpose_grade=4
            fi
            read_number "Indentation [3]: "
            indentation_grade=$GLOBAL_RETURN
            if [ -z "$indentation_grade" ]; then
                indentation_grade=3
            fi
            read_number "Section Headers [5]: "
            section_headers_grade=$GLOBAL_RETURN
            if [ -z "$section_headers_grade" ]; then
                section_headers_grade=5
            fi
            read_number "No extra include statements [1]: "
            include_statements_grade=$GLOBAL_RETURN
            if [ -z "$include_statements_grade" ]; then
                include_statements_grade=1
            fi
            read_number "Good Practices [2]: "
            good_practices_grade=$GLOBAL_RETURN
            if [ -z "$good_practices_grade" ]; then
                good_practices_grade=2
            fi



            read_number "Program Functions correclty (passes tests) [75]: "
            passes_unit_tests_grade=$GLOBAL_RETURN
            if [ -z "$passes_unit_tests_grade" ]; then
                passes_unit_tests_grade=75
            fi

            read_number "Late penalty [$LATE_P]: "
            TEMP_LATE_P=$GLOBAL_RETURN
            if [ -z "$TEMP_LATE_P" ]; then
                TEMP_LATE_P=0
            fi
            echo "Comments: ";read comments

            total=$((include_statements_grade + header_grade + purpose_grade + indentation_grade + section_headers_grade + good_practices_grade + passes_unit_tests_grade))
            total=$((total - LATE_P - TEMP_LATE_P))

            echo "$file:$total" >> "$file/grade.txt"
            echo ""
            echo ""
            echo "Labs: (95)">>"$file/grade.txt"
            echo "1.	Code follows formatting guidelines (10)">>"$file/grade.txt"
            echo "	a.	Header (5): $header_grade">>"$file/grade.txt"
            echo "	b.	Purpose (3): $purpose_grade">>"$file/grade.txt"
            echo "	c.	Indentation (2): $indentation_grade">>"$file/grade.txt"
            echo "	d.	Section Headers are present and correct (5): $section_headers_grade">>"$file/grade.txt"
            echo "	e.	Follows good coding standards (2): $good_practices_grade">>"$file/grade.txt"
            echo "	f.	Does not include unneeded #includes (1): $include_statements_grade">>"$file/grade.txt"
            echo "2.	Program functions correctly (75)">>"$file/grade.txt"
            echo "	a.	Passes Unit Tests (75): $passes_unit_tests_grade">>"$file/grade.txt"
            echo "3.	Late penalty: $LATE_P" >>"$file/grade.txt"
            echo "Total: $total">>"$file/grade.txt"
            echo "" >>"$file/grade.txt"
            echo "" >>"$file/grade.txt"
            echo "Comments: $comments" >>"$file/grade.txt"
            ;;
            *)
            #default
            ;;
        esac
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
