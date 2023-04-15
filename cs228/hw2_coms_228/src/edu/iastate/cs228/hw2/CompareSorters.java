package edu.iastate.cs228.hw2;
import edu.iastate.cs228.hw2.PointScanner;

/**
 *  
 * @author andrew mcmahon 
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random; 


public class CompareSorters 
{
	/**
	 * Repeatedly take integer sequences either randomly generated or read from files. 
	 * Use them as coordinates to construct points.  Scan these points with respect to their 
	 * median coordinate point four times, each time using a different sorting algorithm.  
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException
	{	
		//remember to loop the whole thing !!
		int choice;
		do{
			int trialCount = 0;
			PointScanner[] scanners = new PointScanner[4]; 
			Scanner scan = new Scanner(System.in);
			System.out.println("keys:  1 (random integers)  2 (file input)  3 (exit)");
			choice = scan.nextInt();
			System.out.println("Trial " + trialCount + ": " + choice);
			if(choice == 1) {
				System.out.println("Enter number of random points: ");
				int size = scan.nextInt();
				Random generator = new Random();
				//make new random list of points
				Point[] pts = generateRandomPoints(size,generator);
				scanners[0] = new PointScanner(pts,Algorithm.SelectionSort);
				scanners[1] = new PointScanner(pts,Algorithm.InsertionSort);
				scanners[2] = new PointScanner(pts,Algorithm.MergeSort);
				scanners[3] = new PointScanner(pts,Algorithm.QuickSort);
				
				System.out.println("algorithm	size  time (ns)");
				System.out.println("----------------------------------"); 
				
				for(int i = 0; i < scanners.length; i++) {
					scanners[i].scan();
					System.out.println(scanners[i].stats());
				}
				System.out.println("----------------------------------"); 
				
			}
			else if(choice == 2) {
				System.out.println("File name: ");
				String fileName = scan.next();
				scanners[0] = new PointScanner(fileName,Algorithm.SelectionSort);
				scanners[1] = new PointScanner(fileName,Algorithm.InsertionSort);
				scanners[2] = new PointScanner(fileName,Algorithm.MergeSort);
				scanners[3] = new PointScanner(fileName,Algorithm.QuickSort);
				
				
				System.out.println("algorithm	size  time (ns)");
				System.out.println("----------------------------------"); 
				
				for(int i = 0; i < scanners.length; i++) {
					scanners[i].scan();
					System.out.println(scanners[i].stats());
				}
				System.out.println("----------------------------------"); 
				
			}
			else {
				//case where choice = 3 (exit) or choice num DNE
				System.out.println("....");
			}
			
			trialCount++;
			//if choice = 1 or 2, it loops back !
		}while (choice == 1 || choice == 2);
		
	}
	
	
	/**
	 * This method generates a given number of random points.
	 * The coordinates of these points are pseudo-random numbers within the range 
	 * [-50,50] × [-50,50]. Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing. 
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException
	{
		Point[] ptList = new Point[numPts];
		for(int i = 0; i < numPts; i++) {
			int x = rand.nextInt(101)-50; 
			int y = rand.nextInt(101)-50;
			ptList[i] = new Point(x,y);
		}
		return ptList; 
	}
	
}
