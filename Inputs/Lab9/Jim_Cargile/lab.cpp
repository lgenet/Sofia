//Project Name:			Lab9.cpp
//Course:				1311/003
//Student Name:			Jim Cargile
//Assignment Number:	Lab 9
//Due Date:				11/7/2017

//Purpose: To sort an array of 100 random integers in ascending and descending order

#include <iostream>
#include <time.h>
#include <cstdlib>
using namespace std;

int arr[100];
int size = 100;

void printMenu()
{
	cout<<"-------------------------------------"<<endl<<"0. Exit"<<endl<<"1. Get the size for the array"<<endl<<"2. Fill the array with random numbers, 1-100"<<endl<<"3. Print the array with position numbers"<<endl<<"4. Print the array in ascending sequence"<<endl;
	cout<<"5. Sort the array in descending sequence"<<endl<<"6. Sequential search of the array for a target"<<endl<<"7. Binary search of the array for a target"<<endl;
}
void readSize()
{
	cout<<"Please enter a size for the array, 1-100."<<endl;
	cin>>size;
}
void fillArray()
{
	for (int i=0;i<size;++i)
		arr[i]=rand()%101;
}
void printArray()
{
	for (int i=0;i<size;++i)
		cout<<i<<" "<<arr[i]<<endl;
}
void sortAsc()
{
	int temp;
	for (int i = 0;i<size; i++)
	for (int j = i+1;j<size; j++)
	if (arr[i] > arr[j])
	{ 
		temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

}
void sortDes()
{
	bool sorted;
	int temp;
	int numpairs = size-1;
	do
	{	
		sorted = true;
		for (int i = 0; i < numpairs; ++i)
		if (arr[i] < arr[i+1])
			{ 
			temp = arr[i];
			arr[i] = arr[i+1];
			arr[i+1] = temp;
			sorted = false;
			}
		numpairs--;
	}
	while (sorted == false);
}
int sequentialSearch(int target)
{
	for (int i=0;i<size;i++)
		if (arr[i]==target)
			return i;
	return -1;
}
int binarySearch(int target)
{
	int first = 0, middle, last = size-1;
	while (first <= last)
	{
		middle = (first + last)/2;
		if (arr[middle] == target)
		{
			return middle;
		}
		else if (arr[middle] < target)
			first=middle+1;
		else
			last=middle-1;
	}
	return -1;
}
void dispatch(int choice)
{
	int target;
	int pos;
	switch (choice)
	{
		case 0: break;
		case 1: readSize(); 
				break;
		case 2: fillArray();
				break;
		case 3: printArray();
				break;
		case 4: sortAsc();
				break;
		case 5: sortDes();
				break;
		case 6: cout<<"Enter a target to search for."<<endl;
				cin>>target;
				pos=sequentialSearch(target);
				if (pos == -1)
					cout<<"Not found."<<endl;
				else 
					cout <<"Found in position "<<pos<<endl;
				break;
		case 7: cout<<"Enter a target to search for."<<endl;
				cin>>target;
				pos=binarySearch(target);
				if (pos == -1)
					cout<<"Not found."<<endl;
				else
					cout<<"Found in position "<<pos<<endl;
				break;
	}
}
int main ()
{
    int choice = 0;
    printMenu();
    cout << "Type in a choice: ";
    cin >> choice;

    while (choice != 0)
    {
        dispatch(choice);
        printMenu();
        cout << "Type in a choice: ";
        cin >> choice;

    }
    cout << "Coded by Jim Cargile" << endl;
    return 0;
}
