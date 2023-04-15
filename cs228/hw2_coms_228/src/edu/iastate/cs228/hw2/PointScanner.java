package edu.iastate.cs228.hw2;
import edu.iastate.cs228.hw2.AbstractSorter;

import java.io.File;

/**
 * 
 * @author 
 *
 */

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 *  
 * @author andrew mcmahon
 *
 */

/**
 * 
 * This class sorts all the points in an array of 2D points to determine a reference point whose x and y 
 * coordinates are respectively the medians of the x and y coordinates of the original points. 
 * 
 * It records the employed sorting algorithm as well as the sorting time for comparison. 
 *
 */
public class PointScanner  
{
	private Point[] points; 
	
	private Point medianCoordinatePoint;  // point whose x and y coordinates are respectively the medians of 
	                                      // the x coordinates and y coordinates of those points in the array points[].
	private Algorithm sortingAlgorithm;    
	
	
		
	protected long scanTime; 	       // execution time in nanoseconds. 

	
	
	/**
	 * This constructor accepts an array of points and one of the four sorting algorithms as input. Copy 
	 * the points into the array points[].
	 * 
	 * @param  pts  input array of points 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public PointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException
	{
		if(pts == null || pts.length == 0) {
			throw new IllegalArgumentException();
		}
		this.sortingAlgorithm = algo;
		
		this.points = new Point[pts.length];
		for(int i = 0; i < pts.length; i++) {
		
			this.points[i] = pts[i];
		}
		
	}

	
	/**
	 * This constructor reads points from a file. 
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException   if the input file contains an odd number of integers
	 */
	protected PointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException
	{
		this.sortingAlgorithm = algo;
		Scanner scanner = new Scanner(new File(inputFileName));
        int count = 0;
        ArrayList<Integer> list = new ArrayList<Integer>();
        
        //read every val
		while(scanner.hasNextInt())
		{
		    list.add(scanner.nextInt());
		    count++;
		}
		
		if(list.size()%2 != 0) {
			throw new InputMismatchException();
		}
		
		int c = 0;
		for(int x = 0; x < list.size()-1; x++) {
			//bound is size-1 because i will take 2 points at once and add to this.points
			this.points[c] = new Point(list.get(x),list.get(x+1));
			x += 2;
			c++;
		}
		
	}

	
	/**
	 * Carry out two rounds of sorting using the algorithm designated by sortingAlgorithm as follows:  
	 *    
	 *     a) Sort points[] by the x-coordinate to get the median x-coordinate. 
	 *     b) Sort points[] again by the y-coordinate to get the median y-coordinate.
	 *     c) Construct medianCoordinatePoint using the obtained median x- and y-coordinates.     
	 *  
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter, InsertionSorter, MergeSorter,
	 * or QuickSorter to carry out sorting.       
	 * @param algo
	 * @return
	 */
	public void scan()
	{
		AbstractSorter aSorter;
		//check helper method sortMedTimer - it does most of the work here, 
		//meant to reduce code duplication
		this.scanTime = 0;
		if(this.sortingAlgorithm == Algorithm.MergeSort) {
			aSorter = new MergeSorter(this.points);
			sortMedTimer(aSorter);

		}
		else if(this.sortingAlgorithm == Algorithm.SelectionSort) {
			aSorter = new SelectionSorter(this.points);
			sortMedTimer(aSorter);

		}
		else if(this.sortingAlgorithm == Algorithm.QuickSort) {
			aSorter = new QuickSorter(this.points);
			sortMedTimer(aSorter);
			
		}
		else if(this.sortingAlgorithm == Algorithm.InsertionSort) {
			aSorter = new InsertionSorter(this.points);
			sortMedTimer(aSorter);
			
		}
	}
	
	
	/**
	 * Outputs performance statistics in the format: 
	 * 
	 * <sorting algorithm> <size>  <time>
	 * 
	 * For instance, 
	 * 
	 * selection sort   1000	  9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description. 
	 */
	public String stats()
	{
		return this.sortingAlgorithm + "	  " + this.points.length + "  " + this.scanTime;
	}
	
	
	/**
	 * Write MCP after a call to scan(),  in the format "MCP: (x, y)"   The x and y coordinates of the point are displayed on the same line with exactly one blank space 
	 * in between. 
	 */
	@Override
	public String toString()
	{
		if(this.medianCoordinatePoint != null) {
			return "MCP: (" + this.medianCoordinatePoint.getX() + ", " + this.medianCoordinatePoint.getY() +")";
		}
		else {
			return null;
		}
	}

	
	/**
	 *  
	 * This method, called after scanning, writes point data into a file by outputFileName. The format 
	 * of data in the file is the same as printed out from toString().  The file can help you verify 
	 * the full correctness of a sorting result and debug the underlying algorithm. 
	 * 
	 * @throws FileNotFoundException
	 */
	public void writeMCPToFile() throws FileNotFoundException
	{
		String outputFileName = "newFile.txt";
		try {
			FileWriter fw = new FileWriter(outputFileName);
			fw.write("MCP: (" + this.medianCoordinatePoint.getX() + ", " + this.medianCoordinatePoint.getY() +")");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	    
	}

	/**
	 * this helper method was created by me to reduce code duplication when calculating the sorting
	 * time and total median point. i saw i was copying & pasting a lot of the same code
	 * so i figured making a helper method for it would make it a little easier to grade/look through.
	 * 
	 ** @param aSorter sorter which type is determined by if statements in scan()\
	 * 
	 */
	private void sortMedTimer(AbstractSorter aSorter) {
		//round 1
		aSorter.setComparator(0);
		//calculate time for round 1
		
		long startTime1 = System.nanoTime();
		aSorter.sort();
		Point medX = new Point(this.points[this.points.length/2].getX(),0);
		long stopTime1 = System.nanoTime();
		this.scanTime += stopTime1 - startTime1;
		
		//round 2
		aSorter.setComparator(1);
		//calculate time for round 2
		long startTime2 = System.nanoTime();
		aSorter.sort();
		Point medY = new Point(0,this.points[this.points.length/2].getY());
		long stopTime2 = System.nanoTime();
		this.scanTime += stopTime2 - startTime2;
		
		this.medianCoordinatePoint = new Point(medX.getX(),medY.getY());
	}	
}