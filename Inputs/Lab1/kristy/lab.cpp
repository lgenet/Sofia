// Program name:    lab1.cpp
// Class:           CSE 1311 - 001
// Student Name:    Kristy A. Pilgrim
// Assignment:      Lab 1 - Area of a Square
// Due Date:        8/10/17

// Purpose:         To calculate the area and perimeter of a Square, after reading in its dimensions from the user

##include <iostream>

using namespace std;

int main() {
    int topSize = 0, rightSide = 0, bottomSide = 0, leftSide = 0;
    cout << "Please enter the top side: ";
    cin >> topSide;
    cout << "Please enter the right side: ";
    cin >> rightSide;
    cout << "Please enter the left side: ";
    cin >> leftSide;
    cout << "Please enter the bottom side: ";
    cin >> bottomSide;

    double area = leftSide * topSide;
    double perimeter = topside + bottomSide + rightSide + leftSide;

    cout << "The top side is " << topSide << endl;
    cout << "The slanted sides are " << slantSide << endl;
    cout << "The bottom side is " << bottomSide << endl;
    cout << "The height side is " << height << endl;


    cout << "The area is " << area << endl;
    cout << "The perimeter is " << perimeter << endl;

    cout << "Coded by Logan A. Genet - 2017" << endl;
    return 0;
}