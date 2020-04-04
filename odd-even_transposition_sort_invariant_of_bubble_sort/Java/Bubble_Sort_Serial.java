import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Bubble_Sort_Serial {
	public static int[] myList;

	public static void readFiles() throws FileNotFoundException {

		File file_myList = new File("C:\\Users\\urt54\\eclipse-workspace\\BubbleSort\\src\\BubbleSort\\mylist.txt");
		ArrayList<Integer> myList1 = new ArrayList<Integer>();
		Scanner scanner = new Scanner(file_myList);
		int i = 0;
		while (scanner.hasNextInt()) {
			myList1.add(scanner.nextInt());
		}
		myList = myList1.stream().mapToInt(Integer::intValue).toArray();
	}

	public static void bubbleSort(int[] myList, int n) {
		int num = n;
		int temporary = 0;
		for (int i = (n - 1); i >= 0; i--) {
			for (int j = 1; j <= i; j++) {
				if (myList[j - 1] > myList[j]) {
					temporary = myList[j - 1];
					myList[j - 1] = myList[j];
					myList[j] = temporary;
				}
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		readFiles();
		int n = myList.length;
		System.out.println("myList size = " + n);
		System.out.println("myList before bubble sort algorithm");
		for (int i = 0; i < n; i++) {
			System.out.print(myList[i] + " ");
		}
		System.out.println();
		bubbleSort(myList, n);
		System.out.println("myList after bubble sort algorithm");
		for (int i = 0; i < n; i++) {
			System.out.print(myList[i] + " ");
		}
	}
}
