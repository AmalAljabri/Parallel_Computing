package project;

/*    In this code, a menu will appear for the user at the beginning when running the program, 
 *     through which he chooses the appropriate option. The menu is as follows:
 *     
         •	When click 1: will be arranged myList using Serial Odd-Even Variation to Bubble Sort. 
         
         •	When click 2 will be arranged myList using Parallel Odd-Even Variation to Bubble Sort.*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class project {
	static ArrayList<Integer> myList = new ArrayList<>();
	static int THREAD_COUNT = 2;

	/*-------------------------------------------------------------------
	 * Method:  readFile
	 * Purpose:   This method is used for read integer number from file.       
	 *-------------------------------------------------------------------*/
	public static void readFile() throws FileNotFoundException {
		File file_myList = new File(
				"C:\\Users\\urt54\\eclipse-workspace\\Odd_Even_Transposition_Sort_Parallel\\src\\mylist.txt");
		try (Scanner scanner = new Scanner(file_myList)) {
			int i = 0;
			while (scanner.hasNextInt()) {
				myList.add(scanner.nextInt());
			}
		}
		System.out.print(myList);
	}

	/*-------------------------------------------------------------------
	 * Method:  Serial_OddEven_Variation_of_bubbleSort
	 * Purpose:   This the method which implements the algorithm for Serial_OddEven_Variation_of_bubbleSort     
	 *-------------------------------------------------------------------*/
	public static ArrayList<Integer> Serial_OddEven_Variation_of_bubbleSort(ArrayList<Integer> myList2, int n) {
		int num = n;
		for (int i = 0; i <= n; i++) {
			if (i % 2 == 0) {
				for (int j = 1; j < n; j += 2) {
					if (myList2.get(j - 1) > myList2.get(j)) {
						Collections.swap(myList2, j - 1, j);
					}
				}

			} else {
				for (int j = 1; j < n - 1; j += 2) {
					if (myList2.get(j) > myList2.get(j + 1)) {
						Collections.swap(myList2, j, j + 1);
					}
				}

			}
		}
		return myList2;
	}

	/*-------------------------------------------------------------------
	 * Method:  computePartner
	 * Purpose:   This method is used to compute the partner of the thread using the rank of the thread and the phase     
	 *-------------------------------------------------------------------*/
	private static int computePartner(int phase, int myRank) {
		int partner;
		if (phase % 2 == 0) { // If Phase is Even
			if (myRank % 2 != 0) { // If rank is Odd
				partner = myRank - 1;
			} else { // If rank is Even
				partner = myRank + 1;
			}
		} else { // If Phase is Odd
			if (myRank % 2 != 0) { // If rank is Odd
				partner = myRank + 1;
			} else { // If rank is Even
				partner = myRank - 1;
			}
		}
		return partner;
	}

	/*-------------------------------------------------------------------
	 * Method:  Parallel_OddEven_Variation_of_bubbleSort
	 * Purpose: This the method which implements the algorithm for Parallel_OddEven_Variation_of_bubbleSort 
	 * Each thread performs its local sort 
	 * and then performs tasks in each phase(Compute partner, Send and receive, Compare-swap). 
	 *-------------------------------------------------------------------*/
	public static ArrayList<myThread> Parallel_OddEven_Variation_of_bubbleSort(ArrayList<myThread> localThreads)
			throws CloneNotSupportedException, InterruptedException {
		System.out.println();
		// Print the original state of all the Threads
		System.out.print("Start:            ");
		for (myThread thread : localThreads) {
			System.out.print(thread.getElements() + " ");
		}
		System.out.println();

		// Local Sorting the threads
		ArrayList<Thread> threads = new ArrayList<>();
		for (int i = 0; i < localThreads.size(); i++) { // We are traversing for every thread
			Thread th = new Thread(localThreads.get(i)); // We get the thread using the index i and locally sort it
			th.start();
			threads.add(th);
		}

		for (Thread thread : threads) {
			thread.join();
		}
		System.out.print("After Local Sort: ");
		for (myThread thread : localThreads) {
			System.out.print(thread.getElements());
		}
		System.out.println();

		for (int phase = 0; phase < localThreads.size(); phase++) {
			for (myThread thread : localThreads) {
				int myRank = thread.getRank();
				int partnerRank = computePartner(phase, myRank);

				if (partnerRank == -1 || partnerRank == localThreads.size())// (thread 0 and last thread) invalid ranks
																			// don't exchange
					continue; // continues with the next iteration in the loop.

				myThread partnerThread = localThreads.get(partnerRank);
				thread.send(partnerThread);
				thread.receive(partnerThread);

				if (myRank < partnerRank) {// store the smaller of two sublist
					// Keep smaller elements
					thread.selectSmallerElements(); // smaller thread take small elements
					partnerThread.selectLargerElements();// larger thread take large elements
				} else {// store the larger of two sublist
					// Keep larger elements
					thread.selectLargerElements();// larger thread take large Elements
					partnerThread.selectSmallerElements();// smaller thread take small Elements
				}
			}

			System.out.print("After Phase " + phase + ":    ");
			for (myThread thread : localThreads) {
				System.out.print(thread.getElements() + " ");
			}
			System.out.println(" ");
		}
		return localThreads;
	}

	public static void main(String[] args)
			throws CloneNotSupportedException, FileNotFoundException, InterruptedException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please, choose the appropriate option!");
		System.out
				.println("Click on number 1: will be arranged myList using Serial Odd-Even Variation to Bubble Sort!");
		System.out.println(
				"Click on number 2: will be arranged myList using Parallel Odd-Even Variation to Bubble Sort!");
		int chosen_number = sc.nextInt();
		System.out.print("* myList Befor Sort: ");
		readFile();
		System.out.println("\n");
		System.out.println("* Size of myList: " + myList.size() + "\n");
		if (chosen_number == 1) {
			System.out.print("* Sorting with the Serial Algorithm: ");
			long Start_Time_Serial = System.nanoTime();
			System.out.println(Serial_OddEven_Variation_of_bubbleSort(myList, myList.size()));
			long End_Time_Serial = System.nanoTime();
			System.out.println("\n* Run-Times is " + (End_Time_Serial - Start_Time_Serial) + " nanoseconds\n");
		}

		else if (chosen_number == 2) {
			System.out.println("* Number of Threads: " + THREAD_COUNT + "\n");

			if (myList.size() % THREAD_COUNT != 0) {
				System.out.println("* myList cannot be distributed equally!!!");
				System.exit(-1);
			} else {
				System.out.println("* Size of Threads: " + myList.size() / THREAD_COUNT + "\n");
				ArrayList<myThread> threads = new ArrayList<>();
				int Size_of_Threads = myList.size() / THREAD_COUNT;
				long Start_Time_Parallel = System.nanoTime();
				for (int THREAD_RANK = 0; THREAD_RANK < THREAD_COUNT; THREAD_RANK++) {

					myThread thread = new myThread(new ArrayList<Integer>(myList.subList(THREAD_RANK * Size_of_Threads,
							THREAD_RANK * Size_of_Threads + Size_of_Threads)), THREAD_RANK);
					threads.add(thread);

					System.out.print("THREAD_RANK: " + THREAD_RANK + " myList: " + thread.getElements() + " ");
					System.out.println();

				}

				Parallel_OddEven_Variation_of_bubbleSort(threads);

				long End_Time_Parallel = System.nanoTime();
				System.out.println();

				System.out.print("* Sorting with the Parallel Algorithm: ");
				for (myThread mylist : threads) {
					System.out.print(mylist.getElements());
				}
				System.out.println("* Run-Times is " + (End_Time_Parallel - Start_Time_Parallel) + " nanoseconds\n");

			}

		} else {
			System.out.println("* Wrong Choice!");

		}

	}

}

class myThread implements Runnable {
	private ArrayList<Integer> elements;
	private int THREAD_RANK;
	private myThread partner;

	public myThread getPartner() {
		return partner;
	}

	myThread(ArrayList<Integer> elements, int rank) {
		this.elements = elements;
		this.THREAD_RANK = rank;
	}

	/*-------------------------------------------------------------------
	 * Method:  getElements
	 * Purpose: This method for get element thread
	 *-------------------------------------------------------------------*/
	public ArrayList<Integer> getElements() {
		return elements;
	}

	/*-------------------------------------------------------------------
	 * Method:  getRank
	 * Purpose: This method for get rank
	 *-------------------------------------------------------------------*/
	public int getRank() {
		return THREAD_RANK;
	}

	/*-------------------------------------------------------------------
	 * Method:  send
	 * Purpose: This method for send a copy of itself to partner thread
	 *-------------------------------------------------------------------*/
	public void send(myThread process) throws CloneNotSupportedException {
		process.receive(this);
	}

	/*-------------------------------------------------------------------
	 * Method:  receive
	 * Purpose: This method for receive a copy of partner thread from partner
	 *-------------------------------------------------------------------*/
	public void receive(myThread process) throws CloneNotSupportedException {
		this.partner = new myThread((ArrayList<Integer>) process.getElements(), process.getRank());
	}

	/*-------------------------------------------------------------------
	 * Method:  selectSmallerElements
	 * Purpose: This method for selecting smaller elements
	 *-------------------------------------------------------------------*/
	public void selectSmallerElements() {
		ArrayList<Integer> newElements = new ArrayList<>();

		int i = 0;
		int j = 0;

		while ((i + j) < this.elements.size()) {
			if (this.elements.get(i) < this.partner.getElements().get(j)) {
				newElements.add(this.elements.get(i));
				i++;

			} else {
				newElements.add(this.partner.getElements().get(j));
				j++;
			}
		}
		this.elements = newElements;
	}

	/*-------------------------------------------------------------------
	 * Method:  selectLargerElements
	 * Purpose: This method for selecting larger elements
	 *-------------------------------------------------------------------*/
	public void selectLargerElements() {
		ArrayList<Integer> newElements = new ArrayList<>();

		int i = this.elements.size() - 1;
		int j = this.elements.size() - 1;

		while ((i + j) + 1 >= this.elements.size()) {
			if (this.elements.get(i) > this.partner.getElements().get(j)) {
				newElements.add(this.elements.get(i));
				i--;

			} else {
				newElements.add(this.partner.getElements().get(j));
				j--;
			}
		}
		Collections.sort(newElements);
		this.elements = newElements;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// System.out.println("Thread " + this.getRank() + " local sorting started." );
		localSort();
		// System.out.println("Thread " + this.getRank() + " local sorting ended." );

	}

	/*-------------------------------------------------------------------
	 * Method:  localSort
	 * Purpose: This method is used to sort the elements locally in a thread using sort method in the Arraylist 
	 *-------------------------------------------------------------------*/
	public void localSort() {
		Collections.sort(this.elements);
	}

}
