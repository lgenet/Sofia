//C++ Program		lab#7.cpp
//Corse				CSE1311 SEC3
// Student Name: Aaron Hiller
//Assignment#:		Lab#9
//Due Date:			10/07/2017
//Purpose:
/*
Declares variables and globals
Asks user what they want to use
Uses a function to decide which function to use
Has a function to change the size of the array
Has a function to fill the array with random numbers from 1-100
Has a function to print out the array with the position
Has a function to sort the array in ascending order
Has a function to sort the array in descending order
Has a function to find the target using a sequential search
Has a funciton to find the target using a binary search
Uses a while to loop until the user asks to leave
*/
#include <iostream>
#include <time.h>
#include <cstdlib>
using namespace std;

int arr[100];
int size = 100;
//function to print the user the options
void printMenu()
{
	cout<<"Type 0 to exit"<<endl;
	cout<<"Type 1 to get the size needed for today's use of the array."<<endl;
	cout<<"Type 2 to get fill the array with random numbers from 1-100"<<endl;
	cout<<"Type 3 to print the array with position numbers."<<endl;
	cout<<"Type 4 to sort the array in ascending sequence."<<endl;
	cout<<"Type 5 to sort the array in descending sequence."	<<endl;
	cout<<"Type 6 to Sequential search of the array for the target."<<endl;
	cout<<"Type 7 to do a binary search of the array for a target.\n"<<endl;
}
//function used to change the sixe of the arraty
void readSize()
{
	cout<<"What is the size wanted for the array?"<<endl;
	cin>>size;
}
//function to fill array with random numbers
void fillArray()
{
	//seed the random number generator
	srand(time(0));
	//for loop
	for(int i=0; i<size; i++)
	{
	arr[i]=rand()%100+1;
	}
}
//function to print array
void printArray()
{
	int value= arr[0];
	//for loop
	for(int i=0; i<size; i++)
	{
	value=arr[i];
	cout<<i<<"\t\t"<<value<<"\t"<<endl;
	}
	cout<<"\n";
}
//function to sort the array in ascending order
void sortAsc()
{
	//variable decleration
	int min, temp;
	//for loop
	for (int index = 0; index < size; index++)
	{
		min = index;
		for (int scan = index + 1; scan < size; scan++)
			if (arr[scan] < arr[min])
				min = scan;
	
		// Swap the values
		temp = arr[min];
		arr[min] = arr[index];
		arr[index] = temp;
	}

}
void sortDes() // Must be a different sort than the sort ascending function use (ie Selection if you did bubble for the first one.)
{
	//variable decleration
	int temp;
	//for loop
	for (int i = 0; i<size; i++)
	//nested for loop
	for (int j = i+1; j<size; j++)
	//if statement
	if (arr[i] < arr[j])
	{ 
		temp = arr[i];
	    arr[i] = arr[j];
	    arr[j] = temp;
	}
}
int sequentialSearch(int target)
{
	//for loop
	for (int i = 0; i < size; i++)
	//if statement
	if (arr[i] == target)
	{
		return i;	
	} 	
	return -1;	
}
//function to find target using binary searvh
int binarySearch(int target)
{
	//Declares variables
	int first = 0, middle, last = size-1;
	//while loop
	while (first <= last)
		{
		   middle = (first + last)/2;
		   //if statement
		   if (arr[middle] == target)
			{ 
   			   return middle;	
			}
		   else if (arr[middle] < target)
			   first = middle + 1;
			else
			   last = middle - 1;
 		}  
	return -1;				
	}

void dispatch(int choice)
{
	//variable declerations
	int target,pos,pos1;
	//Switch statement
	switch (choice)
	{
		case 0: cout<<"You have left."<<endl;
				break;
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
		case 6: cout<<"What is the target?"<<endl;
				//user input
				cin>>target;
				//function
				pos=sequentialSearch(target);
				//if statement
				if(pos==-1)
				cout<<"Target not found\n"<<endl;
				else
				cout<<"Target poistion is "<<pos<<"\n"<<endl;
				break;
		case 7: cout<<"What is the target?"<<endl;
				//user input
				cin>>target;
				//function
				pos1=binarySearch(target);
				//if statement
				if(pos1==-1)
				cout<<"Target not found\n"<<endl;
				else
				cout<<"Target poistion is "<<pos1<<"\n"<<endl;
				break;
		default: cout<<"Bad Code"<<endl;
	}
}
int lab ()
{
	//Variable Declerations
    int choice = 0;
    //functions
    printMenu();
    cout << "Type in a choice:";
    //User input
    cin >> choice;
    while (choice != 0)
    {
    	//functions
        dispatch(choice); 
        printMenu();
        cout << "Type in a choice: ";
		//user input
        cin >> choice;

    }
    cout << "Coded by Aaron Hiller" << endl;
    return 0;
}
