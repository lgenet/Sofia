// Program name:    lab1.cpp
// Class:           CSE 1311 - 001
// Student Name:    Logan A. Genet
// Assignment:      Lab 1 - Area of a Trapezoid
// Due Date:        8/10/17

// Purpose:         To calculate the area and perimeter of a trapezoid, after reading in itd dimensions from the user

##include <iostream>

using namespace std;

int main() {
    int topSize = 0, slantSide = 0, bottomSide = 0, height = 0;
    cout << "Please enter the top side: ";
    cin >> topSide;
    cout << "Please enter the slant side: ";
    cin >> slantSide;
    cout << "Please enter the bottom side: ";
    cin >> bottomSide;
    cout << "Please enter the height side: ";
    cin >> height;

    double area = 1.0/2.0 * (topSide + bottomSide) * height;
    double perimeter = topside + bottomSide + slantSide + slantSide;

    cout << "The top side is " << topSide << endl;
    cout << "The slanted sides are " << slantSide << endl;
    cout << "The bottom side is " << bottomSide << endl;
    cout << "The height side is " << height << endl;


    cout << "The area is " << area << endl;
    cout << "The perimeter is " << perimeter << endl;

    cout << "Coded by Logan A. Genet - 2017" << endl;
    return 0;
}