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
	public static int THREAD_COUNT = 4;
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
		System.out.println("* myList size = " + n);
		System.out.println();
		System.out.println("* myList before Parallel_OddEven_Variation_of_bubbleSort");
		System.out.println();
		for (int i = 0; i < n; i++) {
			System.out.print(myList[i] + " ");
		}
		System.out.println();
		System.out.println();

		ExecutorService executor = Executors.newCachedThreadPool();

		long Start_Time = System.nanoTime();

		for (int thread = 0; thread < THREAD_COUNT; thread++) {
			executor.execute(new Parallel_OddEven_Variation_of_bubbleSort(thread, myList));
		}

		executor.shutdown();

		while (!executor.isTerminated()) {
		}

		long End_Time = System.nanoTime();
		System.out.println("* myList after Parallel_OddEven_Variation_of_bubbleSort");
		System.out.println();
		for (int i = 0; i < n; i++) {
			System.out.print(myList[i] + " ");
		}

		System.out.println();
		System.out.println();
		System.out.println("* Number of threads is " + THREAD_COUNT);
		System.out.println();
		System.out.println("* Run-Times is " + (End_Time - Start_Time) + " nanoseconds");
		System.out.println();
		System.out.println();

	}

	static class Parallel_OddEven_Variation_of_bubbleSort implements Runnable {
		private static Lock lock = new ReentrantLock();
		int THREAD_RANK;
		int my_output[];

		Parallel_OddEven_Variation_of_bubbleSort(int THREAD_RANK, int sd[]) {
			this.THREAD_RANK = THREAD_RANK;
			this.my_output = sd;
		}

		@Override
		public void run() {
			lock.lock();
			// TODO Auto-generated method stub
			int start = 0;
			int end = 0;
			int temporary;

			boolean sorted = false;

			int part = my_output.length / THREAD_COUNT;
			start = THREAD_RANK * part;
			// System.out.println("start =" + start);
			if (THREAD_RANK != THREAD_COUNT - 1) {
				end = start + part;
				// System.out.println(end);

			} else {
				end = my_output.length - 2;
			}
			// System.out.println("THREAD_RANK " + THREAD_RANK);

			while (!sorted) {
				sorted = true;

				for (int i = 1; i <= end; i = i + 2) {
					if (my_output[i] > my_output[i + 1]) {
						temporary = my_output[i];
						my_output[i] = my_output[i + 1];
						my_output[i + 1] = temporary;
						my_output[i] = my_output[i];
						my_output[i + 1] = my_output[i + 1];
						sorted = false;
					}
				}

				for (int i = 0; i <= end; i = i + 2) {
					if (my_output[i] > my_output[i + 1]) {
						temporary = my_output[i];
						my_output[i] = my_output[i + 1];
						my_output[i + 1] = temporary;
						my_output[i] = my_output[i];
						my_output[i + 1] = my_output[i + 1];
						sorted = false;
					}
				}
			}

			lock.unlock();
		}

	}

}