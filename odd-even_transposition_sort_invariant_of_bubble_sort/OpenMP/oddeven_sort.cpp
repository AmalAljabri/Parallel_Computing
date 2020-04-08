
//    Program to implement serial and parallel Odd-Even Sort using openmp
/*    In this code, a menu will appear for the user at the beginning when running the program,
 *     through which he chooses the appropriate option. The menu is as follows:
 *
		 •	When click 1: will be arranged myList using Serial Odd-Even Variation to Bubble Sort.
		 •	When click 2 will be arranged myList using Parallel Odd-Even Variation to Bubble Sort.*/

#include <iostream>
#include <fstream>
#include <omp.h>
#include <stdio.h>
using namespace std;
int chosen_number;
int THREAD_COUNT = 16;
int THREAD_RANK;
double Start_Time_Serial;
double End_Time_Serial;
double Run_Time_Serial;
double Start_Time_Parallel;
double End_Time_Parallel;
double Run_Time_Parallel;

void Serial_oddEvenSort(int myList[], int n);// A function to serial Odd-Even sort algorithm 
void print_myList(int arr[], int n);// A function to print myList of size n 
void swap(int i, int j);// A function to swap two element in myList
void Parrallel_oddEvenSort(int myList[], int n);// A function to parallel Odd-Even sort algorithm using #pragma parallel in OpenMP 


/*-------------------------------------------------------------------
	 * Function:  Serial_oddEvenSort
	 * Purpose: A function to serial Odd-Even sort algorithm 
*-------------------------------------------------------------------*/
void Serial_oddEvenSort(int myList[], int n) {
	for (int phase = 0; phase < n; phase++)
	{
		int temp;
		if (phase % 2 == 0)
		{
			for (int i= 1; i < n; i += 2)
			{
				if (myList[i - 1] > myList[i])
				{
					temp = myList[i];
					myList[i] = myList[i - 1];
					myList[i - 1] = temp;
					//swap(myList[i - 1], myList[i]);
				}
			}
		}
		else {

			for (int i = 1; i < n - 1; i += 2)
			{
				if (myList[i] > myList[i + 1])
				{
					temp = myList[i];
					myList[i] = myList[i + 1];
					myList[i + 1] = temp;
					//swap(myList[i], myList[i + 1]);
				}
			}
		}
	}
}


/*-------------------------------------------------------------------
	 * Function: print_myList
	 * Purpose:  A function to print myList of size n 
*-------------------------------------------------------------------*/
void print_myList(int arr[], int n)
{
	for (int i = 0; i < n; i++)
		cout << arr[i] << " ";
	cout << "\n";
}


/*-------------------------------------------------------------------
	 * Function: swap
	 * Purpose:  A function to swap two element in myList
*-------------------------------------------------------------------*/
void swap(int i, int j) {

	int temp = i;
	i = j;
	j = temp;

}

/*-------------------------------------------------------------------
	 * Function: Parrallel_oddEvenSort
	 * Purpose:  A function to parallel Odd-Even sort algorithm using #pragma parallel in OpenMP 
*-------------------------------------------------------------------*/
void Parrallel_oddEvenSort(int myList[], int n)
{
	int phase, i, temp;
#pragma omp parallel num_threads(THREAD_COUNT) shared(myList,n) private(i,phase,temp)
	//printf("\n* rank: %d\n", omp_get_thread_num());
	for (phase = 0; phase < n; phase++)
	{
		if (phase % 2 == 0)
		{
#pragma omp for
			for (i = 1; i < n; i += 2)
			if (myList[i - 1] > myList[i])
			{
				temp = myList[i];
				myList[i] = myList[i - 1];
				myList[i - 1] = temp;
				//swap(myList[i - 1], myList[i]);
			}
		}
		else
		{
#pragma omp for
			for (i = 1; i < n - 1; i += 2)
			if (myList[i] > myList[i + 1])
			{
				temp = myList[i];
				myList[i] = myList[i + 1];
				myList[i + 1] = temp;
				//swap(myList[i], myList[i + 1]);
			}
		}

	}
}

int main() {

	int myList[224000] ;
	int length_myList = (sizeof(myList) / sizeof(*myList));

	ifstream File;
	File.open("mylist.txt");

	int n = 0;
	while (File >> myList[n]) {
		n++;
	}
	File.close();
	

	printf("Please, choose the appropriate option!\n");
	printf("Click on number 1: will be arranged myList using Serial Odd-Even Variation to Bubble Sort!\n");
	printf("Click on number 2: will be arranged myList using Parallel Odd-Even Variation to Bubble Sort!\n");
	scanf_s("%d", &chosen_number);
	printf("* myList Befor Sort: ");
	print_myList(myList, length_myList);
	printf("\n* Size of myList: %d\n", length_myList);
	if (chosen_number == 1) {

		Start_Time_Serial = omp_get_wtime();
		Serial_oddEvenSort(myList, length_myList);
		End_Time_Serial = omp_get_wtime();
		printf("\n* Sorting with the Serial Algorithm: ");
		Run_Time_Serial = End_Time_Serial - Start_Time_Serial;
		print_myList(myList, length_myList);
		printf("\n* Run-Times is: %lf (s)\n", Run_Time_Serial);
	}
	else if (chosen_number == 2) {

		printf("\n* Number of Threads: %d\n", THREAD_COUNT);
		Start_Time_Parallel = omp_get_wtime();
		Parrallel_oddEvenSort(myList, length_myList);
		End_Time_Parallel = omp_get_wtime();
		Run_Time_Parallel = End_Time_Parallel - Start_Time_Parallel;
		printf("\n* Sorting with the Parallel Algorithm: ");
		print_myList(myList, length_myList);
		printf("\n* Run-Times is: %lf (s)\n", Run_Time_Parallel);
	}
	else {
		printf("* Wrong Choice!");

	}

	return 0;
}
