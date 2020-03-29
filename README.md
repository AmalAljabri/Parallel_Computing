# Parallel_Computing
Introduction to Parallel Computing

We would like to rearrange or sorting a given list of n integers values from a file in increasing order 
we will do this through the use of the simplest sorting algorithm which is bubble sort algorithm.
The bubble sort algorithm works by comparing each value in the list with the value next to it, and swapping them if required. 
The bubble sort algorithm repeats this process until it makes a pass all the way through the list without swapping any values. 
The problem with the bubble sort algorithm appears when myList of values is reverse sorted. 
This problem has Ο(n^2) complexity, so the bubble sort algorithm is slow and inefficient and also difficult to parallelize. 
We will use invariant of bubble sort which is odd-even transposition sort. 
This algorithm uses one loop instead of using two loops (outer and inner loop as is bubble sort) 
and this loop will work in two alternate steps, 
the first step works with even indices and the second step works with odd indices. 
This algorithm has Ο(n) complexity and but not the optimal one. 
We plan to solve the problem and improve the serial solution for odd-even transposition sort invariant of bubble sort
by parallelism and that is through divide the myList into parts and each thread sort its specific parts,
then finally marge that parts of myList in the main thread and sort it. 
This will be implemented through the use of the java multithreading approach.

