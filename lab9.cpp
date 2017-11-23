#include <iostream>
#include <time.h>
#include <cstdlib>

using namespace std;

int arr[100];
int size = 100;

void printMenu() {
    cout << "0. Exit" << endl;
    cout << "1. Enter Size" << endl;
    cout << "2. Fill Array" << endl;
    cout << "3. Print" << endl;
    cout << "4. Sort Ascending" << endl;
    cout << "5. Sort Descending" << endl;
    cout << "6. Sequential Search" << endl;
    cout << "7. Binary Search" << endl;
}

void readSize() {
    cout << "Please enter size" << endl;
    cin >> size;
}
void fillArray() {
    for(int i = 0; i < size; i++) {
        arr[i] = rand() %100 + 1;
    }
}
void printArray() {
    for(int i = 0; i < size; i++) {
        cout << i << "\t" << arr[i] << endl;
    }
}
void sortAsc() {
    int min, temp;

    for (int index = 0; index <size; index++)
    {
        min = index;

        for (int scan = index + 1; scan < size; scan++)
            if (arr[scan] < arr[min])
                min = scan;

            temp = arr[min];
            arr[min] = arr[index];
            arr[index] = temp;
    }
}
void sortDes() {
    int temp;
    for (int i = 0; i < size; i++)
        for (int j = i+1; j < size; j++)
            if (arr[i] > arr[j])
            {
                temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
}

int sequentialSearch(int target) {
    for(int i = 0; i < size; i++) {
        if(arr[i] == target)
            return i;
    }
}
int binarySearch(int target) {
    int first = 0, middle, last = size-1;

    while (first <= last)
    {
        middle = (first + last)/2;

        if (arr[middle] == target)
            return middle;
        else if (arr[middle] < target)
            first = middle + 1;
        else
            last = middle - 2;

    }

    return -1;
}
void dispatch(int choice) {
    int target = 0;
    switch(choice) {
        case 1:
            readSize();
            break;
        case 2:
            fillArray();
            break;
        case 3:
            printArray();
            break;
        case 4:
            sortAsc();
            break;
        case 5:
            sortDes();
            break;
        case 6:
            cout << "Please enter a number to search for: ";
            cin >> target;
            sequentialSearch(target);
            break;
        case 7:
            cout << "Please enter a number to search for: ";
            cin >> target;
            binarySearch(target);
            break;
        default:
            cout << "Invalid choice" << endl;
    }
}

int lab ()
{
    int choice = 0;
    printMenu();
    cout << "Type in a choice: ";

    cin >> choice;

    while (choice != 0)
    {
        dispatch(choice); // one big switch statement
        printMenu();

        cout << "Type in a choice: ";

        cin >> choice;

    }
    cout << "Coded by Logan A. Genet - 2017" << endl;
    return 0;
}