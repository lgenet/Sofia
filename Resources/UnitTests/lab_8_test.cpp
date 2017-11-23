#include "gtest/gtest.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include "lab_for_testing.cpp"

using namespace std;

class Lab8Test : public ::testing::Test{};
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
string stringify(double input) {
	stringstream ss;
	ss.str("");
	ss << input;
	return ss.str();
}


/****************************************
 * Test Readin Function
 */
TEST_F(Lab8Test, test_readin) {
	// Setup A
	testing::internal::CaptureStdout();
	int expected[] = {70, 95, 62, 88, 90, 85, 75, 79,  50, 80, 82, 88, 81, 93, 75, 78, 62, 55, 89, 94, 73, 82};
 	int actual[22];
	// Run Test
	readData(actual, 22);

	// Expect
	for(int i = 0; i < 22; i++) {
		EXPECT_EQ(actual[i], expected[i]);
	}
}

/****************************************
 * Tests Min and Max Linear Searches
 */
TEST_F(Lab8Test, test_min) {
	// Setup A
	testing::internal::CaptureStdout();
	string valuePhrase = "50 at position 8";

	int starting[] = {70, 95, 62, 88, 90, 85, 75, 79,  50, 80, 82, 88, 81, 93, 75, 78, 62, 55, 89, 94, 73, 82};
	// Run Test
	min(starting, 22);

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	output = sanitize(output);

	bool hasMin = contains(output, "minimum") || contains(output, "min");
	bool hasValue = contains(output, valuePhrase);
	EXPECT_EQ(hasMin, true) << "Min or Minimum should appear in output";
	EXPECT_EQ(hasValue, true) << "Min should have been 50 at pos 8";
}

TEST_F(Lab8Test, test_max) {
	// Setup A
	testing::internal::CaptureStdout();
	string valuePhrase = "95 at position 1";
	int starting[] = {70, 95, 62, 88, 90, 85, 75, 79,  50, 80, 82, 88, 81, 93, 75, 78, 62, 55, 89, 94, 73, 82};

	// Run Test
	max(starting, 22);

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	output = sanitize(output);

	bool hasMin = contains(output, "maximum") || contains(output, "max");
	bool hasValue = contains(output, valuePhrase);
	EXPECT_EQ(hasMin, true) << "max or maximum should appear in output.\n" << output << endl;
	EXPECT_EQ(hasValue, true) << "max should have been 95 at pos 1.\n" << output << endl;
}


/*************************************************
 * Test Average and table
 */
TEST_F(Lab8Test, test_avg) {
	// Setup
	string expected = "78.4545";

	int starting[] = {70, 95, 62, 88, 90, 85, 75, 79,  50, 80, 82, 88, 81, 93, 75, 78, 62, 55, 89, 94, 73, 82};

	// Run Test
	stringstream ss;
	ss.str("");
	ss << getAverage(starting, 22);
	string actual = ss.str();

	// Expect	
	EXPECT_EQ(actual, expected);
}

string getWideTable() {
	stringstream ss;
  	ss.str("");
	ss <<   "0\t\t70\t-8.45455\n" << 
		"1\t\t95\t16.5455\n" << 
		"2\t\t62\t-16.4545\n" <<
		"3\t\t88\t9.54545\n" <<
		"4\t\t90\t11.5455\n" <<
		"5\t\t85\t6.54545\n" <<
		"6\t\t75\t-3.45455\n" <<
		"7\t\t79\t0.545455\n" <<
		"8\t\t50\t-28.4545\n" <<
		"9\t\t80\t1.54545\n" <<
		"10\t\t82\t3.54545\n" <<
		"11\t\t88\t9.54545\n" <<
		"12\t\t81\t2.54545\n" <<
		"13\t\t93\t14.5455\n" <<
		"14\t\t75\t-3.45455\n" <<
		"15\t\t78\t-0.454545\n" <<
		"16\t\t62\t-16.4545\n" <<
		"17\t\t55\t-23.4545\n" <<
		"18\t\t89\t10.5455\n" <<
		"19\t\t94\t15.5455\n" <<
		"20\t\t73\t-5.45455\n" <<
		"21\t\t82\t3.54545";
 	return ss.str();
}
string getNarrowTable() {
	stringstream ss;
  	ss.str("");
	ss <<   "0\t70\t-8.45455\n" << 
		"1\t95\t16.5455\n" << 
		"2\t62\t-16.4545\n" <<
		"3\t88\t9.54545\n" <<
		"4\t90\t11.5455\n" <<
		"5\t85\t6.54545\n" <<
		"6\t75\t-3.45455\n" <<
		"7\t79\t0.545455\n" <<
		"8\t50\t-28.4545\n" <<
		"9\t80\t1.54545\n" <<
		"10\t82\t3.54545\n" <<
		"11\t88\t9.54545\n" <<
		"12\t81\t2.54545\n" <<
		"13\t93\t14.5455\n" <<
		"14\t75\t-3.45455\n" <<
		"15\t78\t-0.454545\n" <<
		"16\t62\t-16.4545\n" <<
		"17\t55\t-23.4545\n" <<
		"18\t89\t10.5455\n" <<
		"19\t94\t15.5455\n" <<
		"20\t73\t-5.45455\n" <<
		"21\t82\t3.54545";
 	return ss.str();
}
TEST_F(Lab8Test, test_table_print_body) {
	// Setup A
	testing::internal::CaptureStdout();

	string expectedNarrowTableValues = getNarrowTable();
	string expectedWideTableValues = getWideTable();
	int starting[] = {70, 95, 62, 88, 90, 85, 75, 79,  50, 80, 82, 88, 81, 93, 75, 78, 62, 55, 89, 94, 73, 82};

	// Run Test
	printGradesTable(starting, 22);

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);

	bool hasWideBody = contains(output, expectedWideTableValues);
	bool hasNarrowBody = contains(output, expectedNarrowTableValues);

	bool hasBody = hasWideBody || hasNarrowBody;
	EXPECT_EQ(hasBody, true) << "Table body did not appear correctly";
}

TEST_F(Lab8Test, test_table_print_header) {
	// Setup A
	testing::internal::CaptureStdout();

	string expectedWideHeader = "position no.\tgrade\tdifference from avg\n";
	string expectedNarrowHeader = "pos\tgrade\tdifference from avg\n";

	int starting[] = {70, 95, 62, 88, 90, 85, 75, 79,  50, 80, 82, 88, 81, 93, 75, 78, 62, 55, 89, 94, 73, 82};

	// Run Test
	printGradesTable(starting, 22);

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);

	bool hasWideHeader = contains(output, expectedWideHeader);
	bool hasNarrowHeader = contains(output, expectedNarrowHeader);


	bool hasHeader = hasWideHeader || hasNarrowHeader;
	EXPECT_EQ(hasHeader, true) << "Table header did not appear correctly";
}


/**********************************************
 * Test Sort
 */

TEST_F(Lab8Test, test_sort) {
	// Setup A
	testing::internal::CaptureStdout();
 	int expected[] = {95, 94, 93, 90, 89, 88, 88, 85, 82, 82, 81, 80, 79, 78, 75, 75, 73, 70, 62, 62, 55, 50};
	int starting[] = {70, 95, 62, 88, 90, 85, 75, 79,  50, 80, 82, 88, 81, 93, 75, 78, 62, 55, 89, 94, 73, 82};

	// Run Test
	sort(starting, 22);

	// Setup B
	for(int i = 0; i < 22; i++) {
		EXPECT_EQ(starting[i], expected[i]);
	}
}

/****************************************
 * General Tests
 */
TEST_F(Lab8Test, test_coded_by) {
	// Setup A
	testing::internal::CaptureStdout();

	// Run Test
	lab();

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	
	// Expect	
	bool hasCodedBy = contains(output, "coded by") || contains(output,"programmed by")  || contains(output,"programmed was written by") 
		|| contains(output,"program created by") || contains(output,"program was made by");
	ASSERT_EQ(hasCodedBy, true);
}

int main(int argsc, char **argv) {
	::testing::InitGoogleTest(&argsc, argv);
	return RUN_ALL_TESTS();
}

/*TO Run
 g++ -std=c++14 -isystem ./googletest/googletest/include -pthread ./lab_8_test.cpp ./libgtest.a 
*/
