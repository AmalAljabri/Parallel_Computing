#include <iostream>
#include <fstream>
#include <omp.h>
int threadNumber = 4;
int my_rank = omp_get_thread_num();
using namespace std;
// A C++ Program to implement serial and parallel Odd-Even Sort using openmp

// A function to serial Odd Even sort algorithm 
void Serial_oddEvenSort(int arr[], int n)
{
	bool isSorted = false; 

	while (!isSorted)
	{
		isSorted = true;

		for (int i = 1; i <= n - 2; i = i + 2)
		{
			if (arr[i] > arr[i + 1])
			{
				swap(arr[i], arr[i + 1]);
				isSorted = false;
			}
		}

		for (int i = 0; i <= n - 2; i = i + 2)
		{
			if (arr[i] > arr[i + 1])
			{
				swap(arr[i], arr[i + 1]);
				isSorted = false;
			}
		}
	}

	return;
}

// A function to parallel Odd Even sort algorithm 
void Parrallel_oddEvenSort(int arr[], int n)
{
	int phase, i, temp;
#pragma omp parallel num_threads(threadNumber) default(none) shared(arr,n) private(i,temp,phase)
	for (phase = 0; phase < n; ++phase)
	{
		
		if (phase % 2 == 0) //even phase
		{
#pragma omp for
			for (i = 1; i < n; i += 2)
				if (arr[i - 1] > arr[i])
				{

					temp = arr[i];
					arr[i] = arr[i - 1];
					arr[i - 1] = temp;
				}
		
		}
		else //odd phase
		{
#pragma omp for
			for (i = 1; i < n - 1; i += 2)
				if (arr[i] > arr[i + 1])
				{
					temp = arr[i];
					arr[i] = arr[i + 1];
					arr[i + 1] = temp;
				}
		}
	}
}
// A function ot print an array of size n 
void printArray(int arr[], int n)
{
	for (int i = 0; i < n; i++)
		cout << arr[i] << " ";
	cout << "\n";
}


int main() {

	int myList[1000];

	ifstream File;
	File.open("mylist.txt");

	int n = 0;
	while (File >> myList[n]) {
		n++;
	}
	File.close();

	printArray(myList, 1000);
	printf("Thread number: %d\n", threadNumber);

	//double Start_Time_Serial = omp_get_wtime();
	//Serial_oddEvenSort(arr, 1000);
	//double End_Time_Serial = omp_get_wtime();
	//printf("Sorting with the Serial Algorithm\n");
	//double Run_Time_Serial = End_Time_Serial - Start_Time_Serial;
	//printArray(arr, 11);
	//printf("Run_Time_Serial: %lf (s)\n", Run_Time_Serial);

	double  Start_Time_Parallel = omp_get_wtime();
	Parrallel_oddEvenSort(myList, 1000);
	double  End_Time_Parallel = omp_get_wtime();
	double Run_Time_Parallel = End_Time_Parallel - Start_Time_Parallel;
	printf("Sorting with the Parallel Algorithm\n");
	printArray(myList, 1000);
	printf("Run_Time_Parallel: %lf (s)\n", Run_Time_Parallel);
	return 0;
}
