#include "gtest/gtest.h"
#include <iostream>
#include <fstream>
#include <string.h>
#include <stdio.h>
#include <cstring>
#include <sstream>
#include "lab6.cpp"

using namespace std;

class Lab6Test : public ::testing::Test{};
/****************************************
 * Utility Functions
 */
int getNumberFromString(string line) {
	string temp = "";
	stringstream lineParser(line);
	int i = -1;
	while(!lineParser.eof()) {
		lineParser >> temp;
		try {
			i = stoi(temp);
			break;
		}catch(...) {}
	}
	return i;
}
int getNumLines(string output) {
	stringstream ss;
	ss << output;	
	string temp = "";
	int size = 1;
	getline(ss, temp);
	while(temp != "") {
		getline(ss, temp);
		size++;
	}
	return size;
}
string toLowerCase(string s) {
	string newStr = "";
	for(int i = 0; i < s.length(); i++ ){
		newStr += tolower(s[i]);	
	}
	return newStr;
}
string stripWhiteSpace(string s) {
	stringstream ss(s);
	string newStr = "", temp = "";
	while(!ss.eof()) {
		ss >> temp;
		newStr += temp;
	}
	return newStr;
}
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
bool contains(string s1, string s2) {
	return strstr(s1.c_str(), s2.c_str());
}

/****************************************
 * Tests Prime Number
 */
TEST_F(Lab6Test, test_prime) {
	// Setup A
	testing::internal::CaptureStdout();
	istringstream stream("1 12 -1");
	streambuf* cin_backup = std::cin.rdbuf(stream.rdbuf());
	string expected = "2 3 5 7 11";

	// Run Test
	lab();

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	output = sanitize(output);

	// Expect	
	bool hasAnswer = contains(output, expected);
	EXPECT_EQ(hasAnswer, true);
}
TEST_F(Lab6Test, test_prime_2) {
	// Setup A
	testing::internal::CaptureStdout();
	istringstream stream("1 15 -1");
	streambuf* cin_backup = std::cin.rdbuf(stream.rdbuf());
	string expected = "2 3 5 7 11 13";

	// Run Test
	lab();

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	output = sanitize(output);

	// Expect	
	bool hasAnswer = contains(output, expected);
	EXPECT_EQ(hasAnswer, true);
}
TEST_F(Lab6Test, test_prime_extra_credit) {
	// Setup A
	testing::internal::CaptureStdout();
	istringstream stream("4 11 -1"); 
	streambuf* cin_backup = std::cin.rdbuf(stream.rdbuf());
	string expected = "2 3 5 7 11 13 17 19 23 29 31";
	// Run Test
	lab();

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	output = sanitize(output);
	// Expect	
	bool hasAnswer = contains(output, expected);
	EXPECT_EQ(hasAnswer, true);
}

/****************************************
 * Is Even/Odd Tests
 */
TEST_F(Lab6Test, test_even_odd_0) {
	// Setup A
	testing::internal::CaptureStdout();
	istringstream stream("3 0 -1"); 
	streambuf* cin_backup = std::cin.rdbuf(stream.rdbuf());

	// Run Test
	lab();

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	output = sanitize(output);

	// Expect	
	bool hasAnswer = contains(output, "is even") || contains(output, "is not odd");
	EXPECT_EQ(hasAnswer, true);
}
TEST_F(Lab6Test, test_even_odd_1) {
	// Setup A
	testing::internal::CaptureStdout();
	istringstream stream("3 1 -1"); 
	streambuf* cin_backup = std::cin.rdbuf(stream.rdbuf());

	// Run Test
	lab();

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	output = sanitize(output);

	// Expect	
	bool hasAnswer = contains(output, "is odd") || contains(output, "is not even");
	EXPECT_EQ(hasAnswer, true);
}
TEST_F(Lab6Test, test_even_odd_negative_55) {
	// Setup A
	testing::internal::CaptureStdout();
	istringstream stream("3 -55 -1"); 
	streambuf* cin_backup = std::cin.rdbuf(stream.rdbuf());

	// Run Test
	lab();

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	output = sanitize(output);

	// Expect	
	bool hasAnswer = contains(output, "is odd") || contains(output, "is not even");
	EXPECT_EQ(hasAnswer, true);
}
TEST_F(Lab6Test, test_even_odd_1032) {
	// Setup A
	testing::internal::CaptureStdout();
	istringstream stream("3 1032 -1"); 
	streambuf* cin_backup = std::cin.rdbuf(stream.rdbuf());

	// Run Test
	lab();

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	output = sanitize(output);

	// Expect	
	bool hasAnswer = contains(output, "is even") || contains(output, "is not odd");
	EXPECT_EQ(hasAnswer, true);
}
TEST_F(Lab6Test, test_even_odd_2) {
	// Setup A
	testing::internal::CaptureStdout();
	istringstream stream("3 2 -1"); 
	streambuf* cin_backup = std::cin.rdbuf(stream.rdbuf());

	// Run Test
	lab();

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	output = sanitize(output);

	// Expect	
	bool hasAnswer = contains(output, "is even") || contains(output, "is not odd");
	EXPECT_EQ(hasAnswer, true);
}
/****************************************
 * Weighted Coin Tests
 */
void runWeightedCoinTests(string output, int totalCount, int deviation) {
	int expectedHeads = 60;
	int expectedTails = 40;
	
	// Define storage	
	string line = "";

	string headsCountLine = "";
	string tailsCountLine = "";
	string headsPercentLine = "";
	string tailsPercentLine = "";

	// Find output lines that contain the data we want
	stringstream parser(output);
	while(!parser.eof()) {
		getline(parser, line);
		bool hasPercent = contains(line, "percent") || contains(line, "%");
		if(contains(line, "heads") && !hasPercent) {
			headsCountLine = line;
		}
		if(contains(line, "tails") && !hasPercent) {
			tailsCountLine = line;		
		}
		if(contains(line, "heads") && hasPercent) {
			headsPercentLine = line;
		}
		if(contains(line, "tails") && hasPercent) { 
			tailsPercentLine = line;		
		}
	}
	
	// Get the number out of those lines
	int headCount = getNumberFromString(headsCountLine);
	int tailCount = getNumberFromString(tailsCountLine);
	int headPercent = getNumberFromString(headsPercentLine);
	int tailPercent = getNumberFromString(tailsPercentLine);
	
	// Define Upper and Lower bounds for the counts
	int headCountLowerBound = ((expectedHeads - deviation)/100.0 * totalCount);
	int headCountUpperBound = ((expectedHeads + deviation)/100.0 * totalCount);

	int tailCountLowerBound = ((expectedTails - deviation)/100.0 * totalCount);
	int tailCountUpperBound = ((expectedTails + deviation)/100.0 * totalCount);
	
	// Evaluate if count is in range
	bool headCountIsGood = headCountLowerBound < headCount && headCount < headCountUpperBound;
	bool tailCountIsGood = tailCountLowerBound < tailCount && tailCount < tailCountUpperBound;

	// Evaluate if percent is in range
	bool headPercentIsGood = (expectedHeads - deviation) < headPercent && headPercent < (expectedHeads + deviation);
	bool tailPercentIsGood = (expectedTails - deviation) < tailPercent && tailPercent < (expectedTails + deviation);

	// Expect	
	EXPECT_EQ(headCountIsGood, true) << "Count of heads was not in range " << headCountLowerBound << " - " << headCountUpperBound << " Actual: " << headCount;
	EXPECT_EQ(tailCountIsGood, true) << "Count of tails was not in range " << tailCountLowerBound << " - " << tailCountUpperBound << " Actual: " << tailCount;
	EXPECT_EQ(headPercentIsGood, true) <<"Percent of heads was not in range "<<(expectedHeads - deviation)<<" - "<<(expectedHeads + deviation)<<". Actual: "<<headPercent;
	EXPECT_EQ(tailPercentIsGood, true) <<"Percent of tails was not in range "<<(expectedTails - deviation)<<" - "<<(expectedTails + deviation)<<". Actual: "<<tailPercent;
}

TEST_F(Lab6Test, test_coin_500) {
	// Setup A
	testing::internal::CaptureStdout();
	istringstream stream("2 500 -1"); 
	streambuf* cin_backup = std::cin.rdbuf(stream.rdbuf());

	// Run Test
	lab();

	// Process Results
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	output = sanitize(output);
	
	runWeightedCoinTests(output, 500, 5);
}
TEST_F(Lab6Test, test_coin_5500) {
	// Setup A
	testing::internal::CaptureStdout();
	istringstream stream("2 5500 -1"); 
	streambuf* cin_backup = std::cin.rdbuf(stream.rdbuf());

	// Run Test
	lab();

	// Process Results
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	output = sanitize(output);
	
	runWeightedCoinTests(output, 5500, 5);
}
TEST_F(Lab6Test, test_coin_10000) {
	// Setup A
	testing::internal::CaptureStdout();
	istringstream stream("2 10000 -1"); 
	streambuf* cin_backup = std::cin.rdbuf(stream.rdbuf());

	// Run Test
	lab();

	// Process Results
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	output = sanitize(output);
	
	runWeightedCoinTests(output, 10000, 1);
}

TEST_F(Lab6Test, test_coin_rand_func) {
	ifstream fin("lab6.cpp"); // TODO: change this to lab
	string line = "";
	bool foundRand = false;
	bool foundSrand = false;
	while(!fin.eof()) {
		getline(fin, line);
		line = stripWhiteSpace(line);
		bool hasRandGen = contains(line, "rand()%100");
		bool hasSrand = contains(line, "srand(time_t(0))") || contains(line, "srand(time_t(NULL))");
		foundRand = foundRand || hasRandGen;
		foundSrand = foundSrand || hasSrand;
	}
	EXPECT_EQ(foundRand, true) << "Did not use random generator to get 100 numbers";
	EXPECT_EQ(foundSrand, true) << "Did not use srand";
	fin.close();
}
/****************************************
 * Main Function Tests
 */

TEST_F(Lab6Test, test_main_default_choice) {
	// Setup A
	testing::internal::CaptureStdout();
	istringstream stream("587 -1");
	streambuf* cin_backup = std::cin.rdbuf(stream.rdbuf());

	// Run Test
	lab();

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	
	// Expect	
	bool hasCodedBy = contains(output, "bad code") || contains(output, "invalid");
	EXPECT_EQ(hasCodedBy, true);
}
/****************************************
 * General Tests
 */
TEST_F(Lab6Test, test_coded_by) {
	// Setup A
	testing::internal::CaptureStdout();
	istringstream stream("-1");
	streambuf* cin_backup = std::cin.rdbuf(stream.rdbuf());

	// Run Test
	lab();

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	
	// Expect	
	bool hasCodedBy = contains(output, "coded by") || contains(output,"programmed by");
	ASSERT_EQ(hasCodedBy, true);
}

int main(int argsc, char **argv) {
	::testing::InitGoogleTest(&argsc, argv);
	return RUN_ALL_TESTS();
}

/*TO Run
 g++ -std=c++14 -isystem ./googletest/googletest/include -pthread ./lab_6_test.cpp ./libgtest.a 
*/
