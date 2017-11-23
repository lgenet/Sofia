#include "gtest/gtest.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include "lab_for_testing.cpp"

using namespace std;

class Lab10Test : public ::testing::Test{};
/****************************************
 * Utility Functions
 */

string trim(string s) {
	int first = 0;
	int last = s.length();
	string temp = "";
	for(int i = 0; i < s.length(); i++) {
		if(s[i] != ' ' && s[i] != '\n' && s[i] != '\t') {
			first = i;
			break;
		}	
	}
	for(int i = s.length()-1; i >=0; i--) {
		if(s[i] != ' ' && s[i] != '\n' && s[i] != '\t') {
			last = i;
			break;
		}	
	}
	for(int i = first; i <= last; i++) {
		temp+= s[i];
	}
	return temp;
}
string sanitize(string s) {
	s = trim(s);
	for(int i = 0; i < s.length(); i++) {
		if(s[i] == '\t')
			s[i] = ' ';	
	}
	return s;
}
string sanitizeComma(string s) {
	s = trim(s);
	string a ="";
	for(int i = 0; i < s.length(); i++) {
		if(s[i] == '\t')
			a += ' ';
		else if( s[i] == ',') 
			;
		else
		  a += s[i];
	}
	return a;
}
string sanitizeExtreme(string s) {
	s = trim(s);
	string a ="";
	for(int i = 0; i < s.length(); i++) {
		if(s[i] == '\t' || s[i] == '\n')
			a += ' ';
		else if( s[i] == ',') 
			;
		else
		  a += s[i];
	}
	return a;
}
bool contains(string s1, string s2) {
	return strstr(s1.c_str(), s2.c_str());
}
/*************************
* Specific Test Helpers
*/
	int original[6][6] = {
    	{42, 68, 35, 1 , 70, 25},
       	{79, 59, 63, 65, 6 , 46},
       	{82, 28, 62, 92, 96, 43},
       	{28, 37, 92, 5 , 3 , 54},
       	{93, 83, 22, 17, 19, 96},
       	{48, 27, 72, 39, 70, 13}
    };
void primeArrayForTest() {
	for(int i = 0; i < 6; i++)
		for(int j = 0; j < 6; j++)
			arr[i][j] = original[i][j];
}

/****************************************
 * Hard Tests
 */
TEST_F(Lab10Test, test_fill_array) {
	// Run Test
	fill();

	// Expect
	for(int i = 0; i < 6; i++)
		for(int j = 0; j < 6; j++)
			EXPECT_EQ(arr[i][j], original[i][j]);
}

TEST_F(Lab10Test, test_transpose) {
		// Setup
    	int transposed[6][6] = {
			{42, 79, 82, 28, 93, 48},
			{68, 59, 28, 37, 83, 27},
			{35, 63, 62, 92, 22, 72},
			{1 , 65, 92, 5 , 17, 39},
			{70, 6 , 96, 3 , 19, 70},
			{25, 46, 43, 54, 96, 13}
    	};

		primeArrayForTest();
    	// Run Test
    	transpose();

    	// Expect
    	for(int i = 0; i < 6; i++)
    		for(int j = 0; j < 6; j++)
    			EXPECT_EQ(arr[i][j], transposed[i][j]);
}
/****************************************
 * Soft Tests
 */

  TEST_F(Lab10Test, test_min) {
  	// Setup A
  	testing::internal::CaptureStdout();
	primeArrayForTest();

  	// Run Test
  	find_min();

  	// Setup B
  	string output = testing::internal::GetCapturedStdout();
  	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
  	output = sanitize(output);

  	bool hasMin = contains(output, "minimum") || contains(output, "min") || contains(output, "smallest")
  		|| contains(output, "lowest") || contains(output, "low")|| contains(output, "smallest");
  	bool hasValue = contains(output, "1");
  	bool hasCorrectPos = contains(output, "0") && contains(output, "3");
  	bool hasAltPos = contains(output, "1") && contains(output, "4");
  	bool hasPos = hasCorrectPos || hasAltPos;

  	EXPECT_EQ(hasMin, true) << "Low, Lowest, Smallest, Min or Minimum should appear in output";
  	EXPECT_EQ(hasValue, true) << "Min should have been 1 at pos 0,3 or 1,4.  But got\n"<< output;
  	EXPECT_EQ(hasPos, true) << "Min should have been 1 at pos 0,3 or 1,4.  But got\n"<< output;
  }

  TEST_F(Lab10Test, test_max) {
  	// Setup A
  	testing::internal::CaptureStdout();
	primeArrayForTest();

  	// Run Test
  	find_max();

  	// Setup B
  	string output = testing::internal::GetCapturedStdout();
  	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
  	output = sanitize(output);

  	bool hasMax = contains(output, "maximum") || contains(output, "max") || contains(output, "largest")
  		|| contains(output, "highest") || contains(output, "high")
		|| contains(output, "biggest");
  	bool hasValue = contains(output, "96");
  	bool hasCorrectPos = contains(output, "2") && contains(output, "4");
  	bool hasAltPos = contains(output, "3") && contains(output, "5");
  	bool hasPos = hasCorrectPos || hasAltPos;

  	EXPECT_EQ(hasMax, true) << "Biggest, High, Highest, Max or Maximum should appear in output";
  	EXPECT_EQ(hasValue, true) << "Min should have been 1 at pos 0,3 or 1,4.  But got\n"<< output;
  	EXPECT_EQ(hasPos, true) << "Min should have been 1 at pos 0,3 or 1,4.  But got\n"<< output;
  }

TEST_F(Lab10Test, test_print_function) {
	// Setup A
	testing::internal::CaptureStdout();
	string expected [] = {
		"42\t68\t35\t1\t70\t25",
		"79\t59\t63\t65\t6\t46",
        "82\t28\t62\t92\t96\t43",
        "28\t37\t92\t5\t3\t54",
        "93\t83\t22\t17\t19\t96",
        "48\t27\t72\t39\t70\t13"
	};

	// Run Test
	print();

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);

	cout << "User Output looks like" << endl;
	cout << "================================================" << endl;
	cout << output << endl;
	cout << "================================================" << endl;
	for(int i = 0; i < 6; i++) {
		bool containsLine = contains(output, expected[i]);
		EXPECT_EQ(containsLine, true);
	}
}

TEST_F(Lab10Test, test_coded_by) {
	// Setup A
	testing::internal::CaptureStdout();

	// Run Test
	lab();

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	
	// Expect	
	bool hasCodedBy = contains(output, "coded by") || contains(output,"programmed by")  || contains(output,"program was written by")
		|| contains(output,"program created by") || contains(output,"program was made by");
	ASSERT_EQ(hasCodedBy, true);
}

int main(int argsc, char **argv) {
	::testing::InitGoogleTest(&argsc, argv);
	return RUN_ALL_TESTS();
}

/*TO Run
 g++ -std=c++14 -isystem ./googletest/googletest/include -pthread ./lab_9_test.cpp ./libgtest.a 
*/
