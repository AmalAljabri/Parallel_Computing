import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Odd_Even_Transposition_Sort_Parallel {
	public static int THREAD_COUNT = 3;
	public static int[] myList;

	public static void readFiles() throws FileNotFoundException {

		File file_myList = new File(
				"C:\\Users\\urt54\\eclipse-workspace\\Odd_Even_Transposition_Sort_Parallel\\src\\mylist.txt");
		ArrayList<Integer> myList1 = new ArrayList<Integer>();
		Scanner scanner = new Scanner(file_myList);
		int i = 0;
		while (scanner.hasNextInt()) {
			myList1.add(scanner.nextInt());
		}
		myList = myList1.stream().mapToInt(Integer::intValue).toArray();
	}

	public static void main(String[] args) throws FileNotFoundException {
		readFiles();
		int n = myList.length;
		System.out.println("myList size" + n);
		System.out.println("myList before bubble sort algorithm");
		for (int i = 0; i < n; i++) {
			System.out.print(myList[i] + " ");
		}
		System.out.println();

		ExecutorService executor = Executors.newCachedThreadPool();

		long Start_Time = System.nanoTime();

		for (int thread = 0; thread < THREAD_COUNT; thread++) {
			executor.execute(new Parallel_OddEven_Variation_of_bubbleSort(thread));
		}

		executor.shutdown();

		while (!executor.isTerminated()) {
		}

		long End_Time = System.nanoTime();
		for (int i = 0; i < n; i++) {
			System.out.print(myList[i] + " ");
		}
		System.out.println();
		System.out.println("-------------------------------------------------");
		System.out.println("  Number of threads is " + THREAD_COUNT);
		System.out.println("-------------------------------------------------");
		System.out.println("  Run-Times is " + (End_Time - Start_Time) + " nanoseconds");
		System.out.println("-------------------------------------------------");
		System.out.println();

	}

	static class Parallel_OddEven_Variation_of_bubbleSort implements Runnable {
		private static Lock lock = new ReentrantLock();
		int THREAD_RANK;
		int my_output[];

		Parallel_OddEven_Variation_of_bubbleSort(int THREAD_RANK) {
			this.THREAD_RANK = THREAD_RANK;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Parallel(THREAD_RANK);

		}

		void Parallel(int THREAD_RANK) {

			lock.lock();
			int local_n = (myList.length + THREAD_COUNT - 1) / THREAD_COUNT;
			int first = local_n * THREAD_RANK;
			int last = Math.min(first + local_n, myList.length);
			my_output = new int[last];
			System.out.println("THREAD_RANK " + THREAD_RANK);
			for (int i = first; i < last; i++) {

				my_output[i] = myList[i];
				// OddEven_Variation_of_bubbleSort(my_output,my_output.length );

				System.out.println(my_output[i]);

			}
			//
			System.out.println();
			lock.unlock();

		}

		public static int[] OddEven_Variation_of_bubbleSort(int[] myList, int n) {
			int num = n;
			int temporary = 0;
			for (int i = 0; i <= n; i++) {
				if (i % 2 == 0) {
					for (int j = 1; j < n; j += 2) {
						if (myList[j - 1] > myList[j]) {
							temporary = myList[j - 1];
							myList[j - 1] = myList[j];
							myList[j] = temporary;
						}
					}

				} else {
					for (int j = 1; j < n - 1; j += 2) {
						if (myList[j] > myList[j + 1]) {
							temporary = myList[j + 1];
							myList[j + 1] = myList[j];
							myList[j] = temporary;
						}
					}

				}
			}
			return myList;
		}
	}

}
