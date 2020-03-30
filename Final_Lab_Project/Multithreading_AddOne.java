import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
     **********************************************************************************
 
                                 Name: Amal Aljabri
                                    Id: 3550343

                          *** program that launches 1,000 threads. 
                   Each thread adds 1 to a variable sum that initially is 0. ***

     **********************************************************************************
          
                                  Serial AddOne.
                     Parallel AddOne Without Synchronization.
            Parallel AddOne With Synchronization Using synchronized Keyword.
                   Parallel AddOne With Synchronization Using Lock.
          Parallel AddOne With Synchronization Using Semaphore --> permit(1).
          
          
    **********************************************************************************
         
          In this code, a menu will appear for the user at the beginning when 
          running the program, through which he chooses how to implement the 
          Parallel_AddOne without synchronization or with synchronization in 
                  three different techniques. The menu is as follows:
      • When click 1: Parallel_AddOne with synchronization using synchronized keyword. 
      •	When click 2: Parallel_AddOne with synchronization using lock.
      • When click 3: Parallel_AddOne with synchronization using semaphore--> permit(1).
      •	When Click any number: Parallel_AddOne without synchronization.
               
      **********************************************************************************


*/

public class Multithreading_AddOne {

	public static int THREAD_COUNT = 1000;
	public static int SUM = 0;
	public static int chosen_number;

	public static void Serial_AddOne() {
		int SUM = 0;
		for (int i = 0; i < 1000; i++) {
			SUM = i + 1;
		}
		System.out.println(SUM);
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("                     Please, choose the appropriate option!");
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("                            The results of Parallel: ");
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("     Click 1: Parallel AddOne With Synchronization Using synchronized Keyword ");
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("            Click 2: Parallel AddOne With Synchronization Using Lock ");
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("     Click 3: Parallel AddOne With Synchronization Using Semaphore --> permit(1) ");
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("            Click ANY NUMBER: Parallel AddOne Without Synchronization ");
		System.out.println("----------------------------------------------------------------------------------------");

		chosen_number = sc.nextInt();

		
		ExecutorService executor = Executors.newCachedThreadPool();

		long Start_Time_Parallel = System.nanoTime();

		for (int thread = 0; thread < THREAD_COUNT; thread++) {
			executor.execute(new Parallel_AddOne(thread));
		}

		executor.shutdown();

		while (!executor.isTerminated()) {
		}

		long End_Time_Parallel = System.nanoTime();
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.print("                             Serial AddOne, SUM = ");
		long Start_Time_Serial = System.nanoTime();
		Serial_AddOne();
		long End_Time_Serial = System.nanoTime();
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("                   Run-Times of Serial Code is " + (End_Time_Serial - Start_Time_Serial)
				+ " nanoseconds");
		System.out.println("----------------------------------------------------------------------------------------");

		System.out.println("                            Parallel AddOne, SUM = " + SUM);
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("                             Number of threads is " + THREAD_COUNT);
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("                 Run-Times of Parallel Code is " + (End_Time_Parallel - Start_Time_Parallel)
				+ " nanoseconds");
		System.out.println("----------------------------------------------------------------------------------------");
	}

	static class Parallel_AddOne implements Runnable {
		private static Lock lock = new ReentrantLock();
		private static Semaphore sem = new Semaphore(1);
		int THREAD_RANK;

		Parallel_AddOne(int THREAD_RANK) {

			this.THREAD_RANK = THREAD_RANK;
		}

		@Override
		public void run() {

			if (chosen_number == 1) {
				Parallel_AddOne_With_Synchronization_Keyword();
			} else if (chosen_number == 2) {
				Parallel_AddOne_With_Synchronization_Lock();
			} else if (chosen_number == 3) {
				Parallel_AddOne_With_Synchronization_Semaphore();
			} else {
				Parallel_AddOne_Without_Synchronization();
			}
		}

		void Parallel_AddOne_Without_Synchronization() {
			SUM = SUM + 1;
		}

		static synchronized void Parallel_AddOne_With_Synchronization_Keyword() {
			SUM = SUM + 1;
		}

		void Parallel_AddOne_With_Synchronization_Lock() {
			lock.lock();
			try {
				SUM = SUM + 1;
			} finally {
				lock.unlock();
			}
		}

		void Parallel_AddOne_With_Synchronization_Semaphore() {
			try {
				sem.acquire();
				SUM = SUM + 1;
			} catch (InterruptedException exc) {
				System.out.println(exc);
			}

			sem.release();
		}

	}
}
