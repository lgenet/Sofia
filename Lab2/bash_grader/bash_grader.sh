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

            read_number "Output text [5]: "
            output_text_grade=$GLOBAL_RETURN
            if [ -z "$output_text_grade" ]; then
                output_text_grade=5
            fi
	########## Formatting and Documentation
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
            read_number "Indentation [2]: "
            indentation_grade=$GLOBAL_RETURN
            if [ -z "$indentation_grade" ]; then
                indentation_grade=2
            fi
            read_number "Section Headings [5]: "
            section_headings_grade=$GLOBAL_RETURN
            if [ -z "$section_headings_grade" ]; then
                section_headings_grade=5
            fi
	########## Output and Formatting
            read_number "Prints inputs [1]: "
            inputs_grade=$GLOBAL_RETURN
            if [ -z "$inputs_grade" ]; then
                inputs_grade=1
            fi
            read_number "Prints output without modifiers [10]: "
            prints_outputs_grade=$GLOBAL_RETURN
            if [ -z "$prints_outputs_grade" ]; then
                prints_outputs_grade=10
            fi
            read_number "Prints output with fixed [10]: "
            fixed_output_grade=$GLOBAL_RETURN
            if [ -z "$fixed_output_grade" ]; then
                fixed_output_grade=10
            fi
            read_number "Prints output with fixed, precision 3 [10]: "
            precision_grade=$GLOBAL_RETURN
            if [ -z "$precision_grade" ]; then
                precision_grade=10
            fi
            read_number "Prints output with Scientific, precision 4 [10]: "
            scientific_print_grade=$GLOBAL_RETURN
            if [ -z "$scientific_print_grade" ]; then
                scientific_print_grade=10
            fi
            read_number "Used modifiers only were necessary [10]: "
            modifier_placement_grade=$GLOBAL_RETURN
            if [ -z "$modifier_placement_grade" ]; then
                modifier_placement_grade=10
            fi
	########## Data integrity and calculations
            read_number "Used formulas correctly (including data types) [15]: "
            calculations_grade=$GLOBAL_RETURN
            if [ -z "$calculations_grade" ]; then
                calculations_grade=15
            fi
            read_number "Accepted user input for radius [3]: "
            user_inputs_grade=$GLOBAL_RETURN
            if [ -z "$user_inputs_grade" ]; then
                user_inputs_grade=3
            fi
            read_number "Used a constant for PI [5]: "
            used_constant_grade=$GLOBAL_RETURN
            if [ -z "$used_constant_grade" ]; then
                used_constant_grade=5
            fi
	########## Other Consideration
            read_number "Other [5]: "
            other_grade=$GLOBAL_RETURN
            if [ -z "$other_grade" ]; then
                other_grade=5
            fi
	########## Late Penalty
            read_number "Late penalty [$LATE_P]: "
            TEMP_LATE_P=$GLOBAL_RETURN
            if [ -z "$TEMP_LATE_P" ]; then
                TEMP_LATE_P=0
            fi
	########## Reads in grader comments
            echo "Comments: ";read comments

            total=$((output_text_grade + header_grade + purpose_grade + indentation_grade + inputs_grade + section_headings_grade)) # Calculate Standard Grade
	    total=$((total + prints_outputs_grade + fixed_output_grade + precision_grade + scientific_print_grade + modifier_placement_grade)) # Calculate printout grade
	    total=$((total + calculations_grade + user_inputs_grade + used_constant_grade + other_grade )) # Calculate Data grade
            total=$((total - LATE_P - TEMP_LATE_P)) # Handles late penalty

	########## Print Rubric ##########
            echo "$file:$total" >> "$file/grade.txt"
            echo ""
            echo ""
            echo "Labs: (100)">>"$file/grade.txt"
            echo "1.	Receive Output text (5): $output_text_grade">>"$file/grade.txt"
            echo "2.	Code follows formatting guidelines (16)">>"$file/grade.txt"
            echo "	a.	Header (4): $header_grade">>"$file/grade.txt"
            echo "	b.	Purpose (4): $purpose_grade">>"$file/grade.txt"
            echo "	c.	Indentation (2): $indentation_grade">>"$file/grade.txt"
            echo "      d.      Section Headings (5): $section_headings_grade">>"$file/grade.txt"  
            echo "3.	Program functions correctly (logic) (23)">>"$file/grade.txt"
            echo "	a.	Accepts user input for radius (3): $user_inputs_grade">>"$file/grade.txt"
            echo "	b.	Used formulas correctly (including data types) (15): $calculations_grade">>"$file/grade.txt"
            echo "	c.	Used a constant for the value of PI (5): $used_constant_grade">>"$file/grade.txt"
            echo "4.	Program produces correct output (51)">>"$file/grade.txt"
            echo "	a.	Prints input (1): $inputs_grade">>"$file/grade.txt"
            echo "	b.	Prints input and output without modifiers (10): $prints_outputs_grade">>"$file/grade.txt"
            echo "	c.	Prints input and output with the Fixed modifier (10): $fixed_output_grade">>"$file/grade.txt"
            echo "	d.	Prints input and output with the Fixed modifier, set precision to 3 (10): $precision_grade">>"$file/grade.txt"
            echo "	e.	Prints input and output with the Scientific modifier, set precision to 4 (10): $scientific_print_grade">>"$file/grade.txt"
            echo "	f.	Used modifiers only when necessary (10): $modifier_placement_grade">>"$file/grade.txt"
            echo "5.	Other considerations (5): $other_grade" >>"$file/grade.txt"
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
