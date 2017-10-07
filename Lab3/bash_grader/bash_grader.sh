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
	########## User Input [5]
	    read_number "Did not declared uncessary variables like R,S,P [2]: "
            uncessary_vars=$GLOBAL_RETURN
            if [ -z "$uncessary_vars" ]; then
                uncessary_vars=2
            fi
	    read_number "Allowed user to input letter choice: R,S,P [3]: "
            user_input_allowed=$GLOBAL_RETURN
            if [ -z "$user_input_allowed" ]; then
                user_input_allowed=3
            fi
	    read_number "Prompt was descriptive [2]: "
            user_input_prompt=$GLOBAL_RETURN
            if [ -z "$user_input_prompt" ]; then
                user_input_prompt=2
            fi
	########## Random Generation [8]
	    read_number "Generates Computer random move (0,1,2) [5]: "
            gen_computer_random=$GLOBAL_RETURN
            if [ -z "$gen_computer_random" ]; then
                gen_computer_random=5
            fi
	    read_number "Seeds Random number [3]: "
            seeded_random_gen=$GLOBAL_RETURN
            if [ -z "$seeded_random_gen" ]; then
                seeded_random_gen=3
            fi
        ########## Data Conversions [15]
   	    read_number "Converts Computer number to Letter or Vice Versa [5]: "
            conversion_done=$GLOBAL_RETURN
            if [ -z "$conversion_done" ]; then
                conversion_done=5
            fi
   	    read_number "Properly used a switch statement to convert [10]: "
            conversion_used_switch=$GLOBAL_RETURN
            if [ -z "$conversion_used_switch" ]; then
                conversion_used_switch=10
            fi
	########## Win Logic [26]
	    read_number "Used braces [1]: "
            used_braces=$GLOBAL_RETURN
            if [ -z "$used_braces" ]; then
                used_braces=1
            fi
	    read_number "Logic is optimized [1]: "
            logic_overal_sound=$GLOBAL_RETURN
            if [ -z "$logic_overal_sound" ]; then
                logic_overal_sound=1
            fi	

	    read_number "Handles Scissors v Scissors case [4]: "
            logic_s_v_s=$GLOBAL_RETURN
            if [ -z "$logic_s_v_s" ]; then
                logic_s_v_s=4
            fi

	    read_number "Handles Paper v Paper case [4]: "
            logic_p_v_p=$GLOBAL_RETURN
            if [ -z "$logic_p_v_p" ]; then
                logic_p_v_p=4
            fi	
	    read_number "Handles Rock v Rock case [4]: "
            logic_r_v_r=$GLOBAL_RETURN
            if [ -z "$logic_r_v_r" ]; then
                logic_r_v_r=4
            fi	

	    read_number "Handles Rock v Scissors case [2]: "
            logic_r_v_s=$GLOBAL_RETURN
            if [ -z "$logic_r_v_s" ]; then
                logic_r_v_s=2
            fi	
	    read_number "Handles Rock v Paper case [2]: "
            logic_r_v_p=$GLOBAL_RETURN
            if [ -z "$logic_r_v_p" ]; then
                logic_r_v_p=2
            fi	

	    read_number "Handles Paper v Rock case [2]: "
            logic_p_v_r=$GLOBAL_RETURN
            if [ -z "$logic_p_v_r" ]; then
                logic_p_v_r=2
            fi	
	    read_number "Handles Paper v Scissors case [2]: "
            logic_p_v_s=$GLOBAL_RETURN
            if [ -z "$logic_p_v_s" ]; then
                logic_p_v_s=2
            fi	

	    read_number "Handles Scissors v Rock case [2]: "
            logic_s_v_r=$GLOBAL_RETURN
            if [ -z "$logic_s_v_r" ]; then
                logic_s_v_r=2
            fi	
	    read_number "Handles Scissors v Paper case [2]: "
            logic_s_v_p=$GLOBAL_RETURN
            if [ -z "$logic_s_v_p" ]; then
                logic_s_v_p=2
            fi	


	########## Output Questions [18]
            read_number "Printed Computer Choice [5]: "
            printed_comp_choice=$GLOBAL_RETURN
            if [ -z "$printed_comp_choice" ]; then
                printed_comp_choice=5
            fi
            read_number "Printed User Choice [3]: "
            printed_user_choice=$GLOBAL_RETURN
            if [ -z "$printed_user_choice" ]; then
                printed_user_choice=3
            fi
            read_number "Output text is descriptive [10]: "
            output_text_descriptive_grade=$GLOBAL_RETURN
            if [ -z "$output_text_descriptive_grade" ]; then
                output_text_descriptive_grade=10
            fi

	########## Answered Question [5]
	    read_number "Answered Question [5]: "
            answered_question=$GLOBAL_RETURN
            if [ -z "$answered_question" ]; then
                answered_question=5
            fi	
	########## Extra Credit
            read_number "Extra credit for Lizard Spock [8]: "
            extra_credit=$GLOBAL_RETURN
            if [ -z "$extra_credit" ]; then
                extra_credit=0
            fi
	########## Late Penalty
            read_number "Late penalty [$LATE_P]: "
            TEMP_LATE_P=$GLOBAL_RETURN
            if [ -z "$TEMP_LATE_P" ]; then
                TEMP_LATE_P=0
            fi
	########## Reads in grader comments
            echo "Comments: ";read comments

            total=$((printed_comp_choice + printed_user_choice + output_text_descriptive_grade + header_grade + purpose_grade + indentation_grade + extra_imports + section_headings_grade)) # Calculate Standard Grade
	    total=$((total + user_input_allowed + user_input_prompt + uncessary_vars + gen_computer_random + seeded_random_gen)) # Calculate random and input grade
	    total=$((total + conversion_done + conversion_used_switch)) # Calculate conversion grade
	    total=$((total + logic_s_v_s + logic_p_v_p + logic_r_v_r + logic_r_v_s + logic_r_v_p + logic_p_v_r + logic_p_v_s + logic_s_v_r + logic_s_v_p + logic_overal_sound + used_braces))
	    total=$((total + answered_question)) # Adds Question Answer grade
	    total=$((total + extra_credit)) # Adds Extra Credit grade
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
            echo "2.	Program handles player and computer moves (5)">>"$file/grade.txt"
            echo "	a.	Did not define any extraneous variables (2): $uncessary_vars">>"$file/grade.txt"
            echo "	b.	Allowed user to input a letter choice (R, S, P) (3): $user_input_allowed">>"$file/grade.txt"
            echo "	c.	Prompt to the user was descriptive for the action they needed to take (2): $user_input_prompt">>"$file/grade.txt"
            echo "	d.	Generates a random computer move (0, 1, 2) (10): $gen_computer_random">>"$file/grade.txt"
            echo "	e.	Seeded the random number generator (5): $seeded_random_gen">>"$file/grade.txt"
            echo "3.	Program correctly converts unlike data into like formats (8)">>"$file/grade.txt"
            echo "	a.	The computer number is converted to a letter or vice versa (5): $conversion_done">>"$file/grade.txt"
            echo "	b.	Properly used a switch statement to convert the data (10): $conversion_used_switch">>"$file/grade.txt"
            echo "4.	Program accurately determines a winner (logic) (26)">>"$file/grade.txt"
            echo "	a.	Correctly handles the Paper vs Paper case (4): $logic_p_v_p">>"$file/grade.txt"
            echo "	b.	Correctly handles the Scissors vs Scissors case (4): $logic_s_v_s">>"$file/grade.txt"
            echo "	c.	Correctly handles the Rock vs Rock case (4): $logic_r_v_r">>"$file/grade.txt"
            echo "	d.	Correctly handles the Rock vs Scissors case (4): $logic_r_v_s">>"$file/grade.txt"
            echo "	e.	Correctly handles the Rock vs Paper case (4): $logic_r_v_p">>"$file/grade.txt"
            echo "	f.	Correctly handles the Paper vs Rock case (4): $logic_p_v_r">>"$file/grade.txt"
            echo "	g.	Correctly handles the Paper vs Scissors case (4): $logic_p_v_s">>"$file/grade.txt"
            echo "	h.	Correctly handles the Scissors vs Paper case (4): $logic_s_v_p">>"$file/grade.txt"
            echo "	i.	Correctly handles the Scissors vs Rock case (4): $logic_s_v_r">>"$file/grade.txt"
            echo "	j.	Overall Logic is sound, clean, clear, and optimized (1): $logic_overal_sound">>"$file/grade.txt"
            echo "	k.	Used braces to clearly organize/arrange code (1): $used_braces">>"$file/grade.txt"
            echo "5.	Program produces correct output (18)">>"$file/grade.txt"
            echo "	a.	Outputed the Computer's choice (5): $printed_comp_choice">>"$file/grade.txt"
            echo "	b.	Outputed the Player's choice (3): $printed__choice">>"$file/grade.txt"
            echo "	c.	Output is descriptive (10): $output_text_descriptive_grade">>"$file/grade.txt"
            echo "6.	Student answered Lab Question on testing correctly (5): $answered_question" >>"$file/grade.txt"
            echo "7.	[Extra Credit] Implemented Lizard Spock (8): $extra_credit" >>"$file/grade.txt"
            echo "8.	Late penalty: $LATE_P" >>"$file/grade.txt"
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
