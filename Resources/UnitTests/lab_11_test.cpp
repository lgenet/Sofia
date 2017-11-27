#include "gtest/gtest.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include "lab_for_testing.cpp"

using namespace std;

class Lab11Test : public ::testing::Test{};
/****************************************
 * Utility Functions
 */
bool contains(string s1, string s2) {
	return strstr(s1.c_str(), s2.c_str());
}

/**************************************
 * Lab 11 Unique Helpers
 */
void testReadData(ifstream& fin) {
	for(int i = 0; i < size; i++)
		for(int j = 0; j < size; j++) {
			fin >> box[i][j];
		}
}

int testSumRow(int r) {
 	    int total = 0;
        for(int c = 0; c < size; c++) {
            total += box[r][c];
        }
        return total;
 }
int testSumCol(int c) {
	    int total = 0;
        for(int r = 0; r < size; r++) {
            total += box[r][c];
        }
        return total;
 }

int testSumMainDiagonal() {
    int total = 0;
    for(int i = 0; i < size; i++) {
        total += box[i][i];
    }
    return total;
}
int testSumReverseDiagonal() {
    int total = 0;
    for(int r = 0; r < size; r++) {
        total += box[r][size-r-1];
    }
    return total;
}
/***********************************
 * Test Read Functions
 */
TEST_F(Lab11Test, test_read_size) {
	ifstream fin("input.txt");

	EXPECT_NE(size, 3);
	// DO action
	readSize(fin);

	// DO Test
	EXPECT_EQ(size, 3);
}
TEST_F(Lab11Test, test_read_data) {
	// Setup
	ifstream fin("input.txt");
	fin >> size;
	int expected[3][3] = {
		{8, 1, 6},
        {3, 5, 7},
        {4, 9, 2}
	};

	// Do Action
	readData(fin);

	// Do Tests
	for(int i = 0; i < size; i++) {
		for(int j = 0; j < size; j++) {
			EXPECT_EQ(box[i][j], expected[i][j]);
		}
	}

	fin.close();
}

TEST_F(Lab11Test, test_print_function) {
	// Setup A
	testing::internal::CaptureStdout();
	string expected [] = {
		"8\t1\t6",
		"3\t5\t7",
        "4\t9\t2"
	};

	// Run Test
	printMatrix();

	// Setup B
	string output = testing::internal::GetCapturedStdout();
	std::transform(output.begin(), output.end(), output.begin(), ::tolower);

	cout << "User Output looks like" << endl;
	cout << "================================================" << endl;
	cout << output << endl;
	cout << "================================================" << endl;

	// Run Test
	for(int i = 0; i < 3; i++) {
		bool containsLine = contains(output, expected[i]);
		EXPECT_EQ(containsLine, true);
	}
}

/****************************************
 * Test Row and Column Sums
 */
TEST_F(Lab11Test, test_sum_row) {
	// Setup
	ifstream fin("input.txt");
	fin >> size;
	testReadData(fin);

	// DO Tests
	while(size != -1) {
		for(int i = 0; i < size; i++) {
			EXPECT_EQ(sumRow(i), testSumRow(i));
		}
		fin >> size;
		testReadData(fin);
	}
}
TEST_F(Lab11Test, test_sum_col) {
	// Setup
	ifstream fin("input.txt");
	fin >> size;
	testReadData(fin);

	// DO Tests
	while(size != -1) {
		for(int i = 0; i < size; i++) {
			EXPECT_EQ(sumCol(i), testSumCol(i));
		}
		fin >> size;
		testReadData(fin);
	}
}

/*******************************************
 * Test Diagonal
 */
TEST_F(Lab11Test, test_sum_main_diagonal) {
	// Setup
	ifstream fin("input.txt");
	fin >> size;
	testReadData(fin);

	// DO Tests
	while(size != -1) {
		EXPECT_EQ(sumMainDiagonal(), testSumMainDiagonal());
		fin >> size;
		testReadData(fin);
	}
}
TEST_F(Lab11Test, test_sum_other_diagonal) {
	// Setup
	ifstream fin("input.txt");
	fin >> size;
	testReadData(fin);

	// DO Tests
	while(size != -1) {
		EXPECT_EQ(sumReverseDiagonal(), testSumReverseDiagonal());
		fin >> size;
		testReadData(fin);
	}
}

TEST_F(Lab11Test, test_is_magic) {
		// Setup
    	ifstream fin("input.txt");
    	fin >> size;
    	testReadData(fin);
		bool expected[] = {true, true, true, false, false, false, false};

    	// DO Tests
    	for(int i = 0; size != -1; i++) {
    		EXPECT_EQ(isMagic(), expected[i]) << "For Box Number: " << i + 1 << endl;
    		fin >> size;
    		testReadData(fin);
    	}
}

TEST_F(Lab11Test, test_coded_by) {
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
 g++ -std=c++14 -isystem ./googletest/googletest/include -pthread ./lab_test.cpp ./libgtest.a
*/
