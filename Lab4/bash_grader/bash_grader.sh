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
            cp "$file" "$2/$sname/$fname".cpp
            (cd "$2/$sname"; g++ "$fname".cpp >err.txt 2>&1)
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
	########## Grading Criteria ##########

	########## Formatting and Documentation [15]
            read_number "Header [5]: "
            header_grade=$GLOBAL_RETURN
            if [ -z "$header_grade" ]; then
                header_grade=5
            fi
            read_number "Purpose [2]: "
            purpose_grade=$GLOBAL_RETURN
            if [ -z "$purpose_grade" ]; then
                purpose_grade=2
            fi
            read_number "Did not includ extra imports [1]: "
            extra_imports=$GLOBAL_RETURN
            if [ -z "$extra_imports" ]; then
                extra_imports=1
            fi
            read_number "Indentation [3]: "
            indentation_grade=$GLOBAL_RETURN
            if [ -z "$indentation_grade" ]; then
                indentation_grade=3
            fi
            read_number "Section Headings [5]: "
            section_headings_grade=$GLOBAL_RETURN
            if [ -z "$section_headings_grade" ]; then
                section_headings_grade=5
            fi
	########## For Loop Input [20]
	    read_number "Used for loop [7]: "
            for_loop_used=$GLOBAL_RETURN
            if [ -z "$for_loop_used" ]; then
                for_loop_used=7
            fi
	    read_number "Used the right condition in for loop [7]: "
            for_loop_condition=$GLOBAL_RETURN
            if [ -z "$for_loop_condition" ]; then
                for_loop_condition=7
            fi
	    read_number "Prompted for the right value [3]: "
            for_loop_prompt=$GLOBAL_RETURN
            if [ -z "$for_loop_prompt" ]; then
                for_loop_prompt=3
            fi
	    read_number "Read and used the value correctly [2]: "
            for_loop_read_value=$GLOBAL_RETURN
            if [ -z "$for_loop_read_value" ]; then
                for_loop_read_value=2
            fi
	    read_number "Did Counting and printing correctly in this program [1]: "
            for_loop_count_print=$GLOBAL_RETURN
            if [ -z "$for_loop_count_print" ]; then
                for_loop_count_print=1
            fi
	########## While Loop Input [20]
	    read_number "Used while loop [7]: "
            while_loop_used=$GLOBAL_RETURN
            if [ -z "$while_loop_used" ]; then
                while_loop_used=7
            fi
	    read_number "Used the right condition in While loop [7]: "
            while_loop_condition=$GLOBAL_RETURN
            if [ -z "$while_loop_condition" ]; then
                while_loop_condition=7
            fi
	    read_number "Prompted for the right value [3]: "
            while_loop_prompt=$GLOBAL_RETURN
            if [ -z "$while_loop_prompt" ]; then
                while_loop_prompt=3
            fi
	    read_number "Read and used the value correctly [2]: "
            while_loop_read_value=$GLOBAL_RETURN
            if [ -z "$while_loop_read_value" ]; then
                while_loop_read_value=2
            fi
	    read_number "Did Counting and printing correctly in this program [1]: "
            while_loop_count_print=$GLOBAL_RETURN
            if [ -z "$while_loop_count_print" ]; then
                while_loop_count_print=1
            fi
	########## Do While Loop Input [20]
	    read_number "Used Do while loop [7]: "
            do_while_loop_used=$GLOBAL_RETURN
            if [ -z "$do_while_loop_used" ]; then
                do_while_loop_used=7
            fi
	    read_number "Used the right condition in Do While loop [7]: "
            do_while_loop_condition=$GLOBAL_RETURN
            if [ -z "$do_while_loop_condition" ]; then
                do_while_loop_condition=7
            fi
	    read_number "Prompted for the right value [3]: "
            do_while_loop_prompt=$GLOBAL_RETURN
            if [ -z "$do_while_loop_prompt" ]; then
                do_while_loop_prompt=3
            fi
	    read_number "Read and used the value correctly [2]: "
            do_while_loop_read_value=$GLOBAL_RETURN
            if [ -z "$do_while_loop_read_value" ]; then
                do_while_loop_read_value=2
            fi
	    read_number "Did Counting and printing correctly in this program [1]: "
            do_while_loop_count_print=$GLOBAL_RETURN
            if [ -z "$do_while_loop_count_print" ]; then
                do_while_loop_count_print=1
            fi
	########## Counting and Printing [20]
	    read_number "Counts Wins [3]: "
            counts_wins=$GLOBAL_RETURN
            if [ -z "$counts_wins" ]; then
                counts_wins=3
            fi
	    read_number "counts Losses [3]: "
            counts_losses=$GLOBAL_RETURN
            if [ -z "$counts_losses" ]; then
                counts_losses=3
            fi
	    read_number "counts Ties [3]: "
            count_ties=$GLOBAL_RETURN
            if [ -z "$count_ties" ]; then
                count_ties=3
            fi
	    read_number "Printed Wins [2]: "
            printed_wins=$GLOBAL_RETURN
            if [ -z "$printed_wins" ]; then
                printed_wins=2
            fi
	    read_number "Printed Losses [2]: "
            printed_losses=$GLOBAL_RETURN
            if [ -z "$printed_losses" ]; then
                printed_losses=2
            fi
	    read_number "Printed Ties [2]: "
            printed_ties=$GLOBAL_RETURN
            if [ -z "$printed_ties" ]; then
                printed_ties=2
            fi
	    read_number "Variables were used in the right place/general code quality [5] "
            other_considerations=$GLOBAL_RETURN
            if [ -z "$other_considerations" ]; then
                other_considerations=5
            fi
       
	########## Late Penalty
            read_number "Late penalty [$LATE_P]: "
            TEMP_LATE_P=$GLOBAL_RETURN
            if [ -z "$TEMP_LATE_P" ]; then
                TEMP_LATE_P=0
            fi
	########## Reads in grader comments
            echo "Comments: ";read comments

            total=$((header_grade + purpose_grade + indentation_grade + extra_imports + section_headings_grade)) # Calculate Standard Grade
	    total=$((total + for_loop_used + for_loop_condition + for_loop_prompt + for_loop_read_value + for_loop_count_print ))

	    total=$((total + while_loop_used + while_loop_condition + while_loop_prompt + while_loop_read_value + while_loop_count_print ))

	    total=$((total + do_while_loop_used + do_while_loop_condition + do_while_loop_prompt + do_while_loop_read_value + do_while_loop_count_print ))

	    total=$((total + counts_wins + counts_losses + count_ties + printed_wins + printed_losses + printed_ties + other_considerations ))
            total=$((total - LATE_P - TEMP_LATE_P)) # Handles late penalty

	########## Print Rubric ##########
            echo "$file:$total" >> "$file/grade.txt"
            echo ""
            echo ""
	    echo "Labs: (95)">>"$file/grade.txt"
	    echo "1.	Code follows formatting guidelines (15)">>"$file/grade.txt"
            echo "	a.	Header (5): $header_grade">>"$file/grade.txt"
            echo "	b.	Purpose (2): $purpose_grade">>"$file/grade.txt"
            echo "	c.	Didn't include extra #includes (1): $extra_imports">>"$file/grade.txt"
            echo "	d.	Indentation (3): $indentation_grade">>"$file/grade.txt"
            echo "      e.      Section Headings (5): $section_headings_grade">>"$file/grade.txt"  
            echo "2.	Lab Part A was completed:  For Loops (20)">>"$file/grade.txt"
            echo "	a.	Used a for loop correctly (7): $for_loop_used">>"$file/grade.txt"
            echo "	b.	For loop condition was correct (7): $for_loop_condition">>"$file/grade.txt"
            echo "	c.	Asked for number of times to run the for loop (3): $for_loop_prompt">>"$file/grade.txt"
            echo "	d.	Allowed user to input variable and used it in the condition (2): $for_loop_read_value">>"$file/grade.txt"
            echo "	e.	Counted/Printed in this program (5): $for_loop_count_print">>"$file/grade.txt"
            echo "3.	Lab Part B was completed:  While Loops (20)">>"$file/grade.txt"
            echo "	a.	Used a while loop correctly (7): $while_loop_used">>"$file/grade.txt"
            echo "	b.	While loop condition was correct (7): $while_loop_condition">>"$file/grade.txt"
            echo "	c.	Asked for number of times to run the while loop (3): $while_loop_prompt">>"$file/grade.txt"
            echo "	d.	Allowed user to input variable and used it in the condition (2): $while_loop_read_value">>"$file/grade.txt"
            echo "	e.	Counted/Printed in this program (5): $while_loop_count_print">>"$file/grade.txt"
            echo "4.	Lab Part C was completed:  Do-While Loops (20)">>"$file/grade.txt"
            echo "	a.	Used a Do-While loop correctly (7): $do_while_loop_used">>"$file/grade.txt"
            echo "	b.	Do-While loop condition was correct (7): $do_while_loop_condition">>"$file/grade.txt"
            echo "	c.	Asked for number of times to run the Do-While loop (3): $do_while_loop_prompt">>"$file/grade.txt"
            echo "	d.	Allowed user to input variable and used it in the condition (2): $do_while_loop_read_value">>"$file/grade.txt"
            echo "	e.	Counted/Printed in this program (5): $do_while_loop_count_print">>"$file/grade.txt"
            echo "5.	Keeps track of Wins/Losses/Ties in the program (20)">>"$file/grade.txt"
            echo "	a.	Correctly counted up the number of wins (3): $counts_wins">>"$file/grade.txt"
            echo "	b.	Correctly counted up the number of losses (3): $counts_losses">>"$file/grade.txt"
            echo "	c.	Correctly counted up the number of ties (3): $count_ties">>"$file/grade.txt"
            echo "	d.	Correctly printed the number of wins (2): $printed_wins">>"$file/grade.txt"
            echo "	e.	Correctly printed the number of losses (2): $printed_losses">>"$file/grade.txt"
            echo "	f.	Correctly printed the number of ties (2): $printed_ties">>"$file/grade.txt"
            echo "	g.	Good coding practices/Used variables correctly/other considerations (5): $other_considerations">>"$file/grade.txt"
            echo "6.	Late penalty: $LATE_P" >>"$file/grade.txt"
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
