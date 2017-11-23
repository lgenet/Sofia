#include "gtest/gtest.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include "lab_for_testing.cpp"

using namespace std;

class Lab9Test : public ::testing::Test{};
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
void clearOutArray() {
	for(int i = 0; i < 100; i++) {
		arr[i] = -10;	
	}
}

/****************************************
 * Test Setup Array Function
 */
TEST_F(Lab9Test, test_read_size) {
	// Setup
 	istringstream stream("53");
	streambuf* cin_backup = std::cin.rdbuf(stream.rdbuf());

	// Pretest
	EXPECT_EQ(size, 100);

	// Run Test
	readSize();

	// Expect	
	EXPECT_EQ(size, 53);
}

TEST_F(Lab9Test, test_fill_array) {
	// Setup
	clearOutArray();
	
	// Pretest
	EXPECT_EQ(size, 53);

	// Run Test
	fillArray();

	// Expect
	for(int i = 0; i < 53; i++) {
		EXPECT_NE(arr[i], -10);
	}
	for(int i = 53; i < 100; i++) {
		EXPECT_EQ(arr[i], -10);
	}
}

/****************************************
 * Test Search Function
 */
void setupSearchTest() {
	clearOutArray();
	size = 10;
	arr[0] = 1;
	arr[1] = 4;
	arr[2] = 3;
	arr[3] = 72;
	arr[4] = 15;
	arr[5] = 52;
	arr[6] = 71;
	arr[7] = 63;
	arr[8] = 14;
	arr[9] = 2;
}

TEST_F(Lab9Test, test_sequential_search_found) {
	// Setup
	setupSearchTest();

	// Pretest
	EXPECT_EQ(size, 10);

	// Run Test
	int pos = sequentialSearch(52);
	
	// Expect
	EXPECT_EQ(pos, 5) << "Expected to find 52 at position 5, but found it at position " << pos;
}
TEST_F(Lab9Test, test_sequential_search_not_found) {
	// Setup
	setupSearchTest();	

	// Pretest
	EXPECT_EQ(size, 10);

	// Run Test
	int pos = sequentialSearch(523);
	
	// Expect
	EXPECT_EQ(pos, -1) << "Expected to find 523 at position -1, but found it at position " << pos;
}
TEST_F(Lab9Test, test_binary_search_found_high) {
	// Setup
	setupSearchTest();	
	sortAsc();

	// Pretest
	EXPECT_EQ(size, 10);

	// Run Test
	int pos = binarySearch(71);
	
	// Expect
	EXPECT_EQ(pos, 8) << "Expected to find 71 at position 8, but found it at position " << pos;
}
TEST_F(Lab9Test, test_binary_search_found_low) {
	// Setup
	setupSearchTest();	
	sortAsc();

	// Pretest
	EXPECT_EQ(size, 10);

	// Run Test
	int pos = binarySearch(4);
	
	// Expect
	EXPECT_EQ(pos, 3) << "Expected to find 4 at position 3, but found it at position " << pos;
}
TEST_F(Lab9Test, test_binary_search_found_mid) {
	// Setup
	setupSearchTest();	
	sortAsc();

	// Pretest
	EXPECT_EQ(size, 10);

	// Run Test
	int pos = binarySearch(15);
	
	// Expect
	EXPECT_EQ(pos, 5) << "Expected to find 15 at position 5, but found it at position " << pos;
}
TEST_F(Lab9Test, test_binary_search_not_found) {
	// Setup
	setupSearchTest();	
	sortAsc();

	// Pretest
	EXPECT_EQ(size, 10);

	// Run Test
	int pos = binarySearch(11235);
	
	// Expect
	EXPECT_EQ(pos, -1) << "Expected to find 11235 at position -1, but found it at position " << pos;
}

/****************************************
 * Test Sorting Function
 */
bool find(int source[], int key) {
	for(int i = 0; i < size; i++) {
		if(source[i] == key) return true;
	}
	return false;
}

TEST_F(Lab9Test, test_sort_ascending_01) {
	// Setup
	clearOutArray();
	
	// Pretest
	size = 53;
	EXPECT_EQ(size, 53);
	int original[100];
	for(int i = 0; i < 100; i++) {
		original[i] = arr[i];
	}
	// Run Test
	sortAsc();
	
	// Expect
	for(int i = 0; i < 52; i++) {
		EXPECT_LE(arr[i], arr[i+1]);
		EXPECT_EQ(find(original, arr[i]), true);
	}
}
TEST_F(Lab9Test, test_sort_descending_01) {
	// Setup
	clearOutArray();
	
	// Pretest
	size = 53;
	EXPECT_EQ(size, 53);
	int original[100];
	for(int i = 0; i < 100; i++) {
		original[i] = arr[i];
	}

	// Run Test
	sortDes();
	
	// Expect
	for(int i = 0; i < 52; i++) {
		EXPECT_GE(arr[i], arr[i+1]);
		EXPECT_EQ(find(original, arr[i]), true);
	}
}

/****************************************
 * Printing Test
 */
TEST_F(Lab9Test, test_print_function) {
	// Setup A
	testing::internal::CaptureStdout();
	setupSearchTest();

	// Run Test
	printArray();

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);
	
	// Expect	
	bool hasCodedBy = (contains(output, "0\t1") && contains(output,"1\t4"))  || 
		(contains(output,"0 1") && contains(output,"1 4"));
	ASSERT_EQ(hasCodedBy, true);
}

TEST_F(Lab9Test, test_coded_by) {
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
 g++ -std=c++14 -isystem ./googletest/googletest/include -pthread ./lab_9_test.cpp ./libgtest.a 
*/
