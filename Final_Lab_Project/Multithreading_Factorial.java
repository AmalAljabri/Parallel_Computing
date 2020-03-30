import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/*
            **********************************************************************************
            
                                        Name: Amal Aljabri
                                           Id: 3550343
                                           
            **********************************************************************************
            
             *** program to compute a factorial of a given number in parallel and serial***
             
            **********************************************************************************


*/
public class Multithreading_Factorial {

	public static int NUM = 18;
	public static long output = 1;
	public static int THREAD_COUNT = 3;

	public static void Serial_Factorial(int num) {
		long output = 1;
		int i;
		for (i = 1; i <= num; i++) {
			output = output * i;
		}
		System.out.println(output);
	}

	public static void main(String[] args) {
		System.out.println("---------------------------------------------------------");
		System.out.print("         Serial Factorial of " + NUM + " is: ");
		long Start_Time_Serial = System.nanoTime();
		Serial_Factorial(NUM);
		long End_Time_Serial = System.nanoTime();
		System.out.println("---------------------------------------------------------");
		System.out.println("   Run-Times of Serial Code is " + (End_Time_Serial - Start_Time_Serial) + " nanoseconds");

		ExecutorService executor = Executors.newCachedThreadPool();

		long Start_Time_Parallel = System.nanoTime();

		for (int thread = 0; thread < THREAD_COUNT; thread++) {
			executor.execute(new Parallel_Factorial(thread));
		}

		executor.shutdown();

		while (!executor.isTerminated()) {
		}

		long End_Time_Parallel = System.nanoTime();

		System.out.println("---------------------------------------------------------");
		System.out.println("         Parallel Factorial of " + NUM + " is: " + output);
		System.out.println("---------------------------------------------------------");
		System.out.println("                  Number of threads is " + THREAD_COUNT);
		System.out.println("---------------------------------------------------------");
		System.out.println(
				"    Run-Times of Parallel Code is " + (End_Time_Parallel - Start_Time_Parallel) + " nanoseconds");
		System.out.println("---------------------------------------------------------");
	}

	static class Parallel_Factorial implements Runnable {
		private static Lock lock = new ReentrantLock();

		int THREAD_RANK;
		long my_output = 1;

		Parallel_Factorial(int THREAD_RANK) {

			this.THREAD_RANK = THREAD_RANK;
		}

		@Override
		public void run() {

			int local_n = (NUM + THREAD_COUNT - 1) / THREAD_COUNT;
			int first = local_n * THREAD_RANK;
			int last = Math.min(first + local_n, NUM);

			// System.out.println("THREAD_RANK "+THREAD_RANK);
			for (int i = first; i < last; i++) {
				// System.out.println((i+1)+"*"+my_output);
				my_output = my_output * (i + 1);
				// System.out.println(my_output);

			}
			lock.lock();
			// System.out.println(output+"*"+my_output);
			output = output * my_output;
			lock.unlock();
		}
	}
}