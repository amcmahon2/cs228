package edu.iastate.cs228.hw1;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * @author andrew mcmahon
 *
 * The ISPBusiness class performs simulation over a grid 
 * plain with cells occupied by different TownCell types.
 *
 */
public class ISPBusiness {
	
	/**
	 * Returns a new Town object with updated grid value for next billing cycle.
	 * @param tOld: old/current Town object.
	 * @return: New town object.
	 */
	public static Town updatePlain(Town tOld) {
		Town tNew = new Town(tOld.getLength(), tOld.getWidth());
		for(int i = 0; i < tOld.getLength(); i++) {
			for(int j = 0; j < tOld.getWidth(); j++) {
				tNew.grid[i][j] = tOld.grid[i][j].next(tNew);			
			}
		}
		return tNew;
	}
	
	/**
	 * Returns the profit for the current state in the town grid.
	 * @param town
	 * @return
	 */
	public static int getProfit(Town town) {
		int count = 0;
		for(int i = 0; i < town.getLength(); i++) {
			for(int j = 0; j < town.getWidth(); j++) {
				if(town.grid[i][j].who() == State.CASUAL) {
					count++;
				}
			}
		}
		return count;
	}
	

	/**
	 *  Main method. Interact with the user and ask if user wants to specify elements of grid
	 *  via an input file (option: 1) or wants to generate it randomly (option: 2).
	 *  
	 *  Depending on the user choice, create the Town object using respective constructor and
	 *  if user choice is to populate it randomly, then populate the grid here.
	 *  
	 *  Finally: For 12 billing cycle calculate the profit and update town object (for each cycle).
	 *  Print the final profit in terms of %. You should print the profit percentage
	 *  with two digits after the decimal point:  Example if profit is 35.5600004, your output
	 *  should be:
	 *
	 *	35.56%
	 *  
	 * Note that this method does not throw any exception, so you need to handle all the exceptions
	 * in it.
	 * 
	 * @param args
	 * 
	 */
	public static void main(String []args) {
		int totalProfit = 0;
		Scanner scan = new Scanner(System.in);
		System.out.println("Do you want Town generated via an input file (type 1) or generated randomly? (type 2)");
		int choice = scan.nextInt();
		if(choice == 1) {
			//input file
			System.out.println("Please enter file path: ");
			String filePath = scan.next();
			
			try {
				Town town = new Town(filePath);
				int rows = town.getLength();
				int cols = town.getWidth();

				//make array of towns (easier to debug)
				Town[] townList = new Town[12];
				townList[0] = town;
				//get profit for first month
				totalProfit += getProfit(town);
				//loop through each month
				for(int i = 1; i < 12; i++) {
					Town tNew = updatePlain(townList[i-1]);
					townList[i] = tNew;
					totalProfit += getProfit(townList[i]);
					if(i == 11) {
						//print utilization
						DecimalFormat f = new DecimalFormat("##.00");
						double num = (100*totalProfit)/(rows*cols*12);
						System.out.println(f.format(num) + "%");
					}
				}
				
			} 
			catch (FileNotFoundException e) {
				System.out.println("File not found.");
			}
		}
		else if(choice == 2) {
			//via seed
			System.out.println("Provide rows, cols and seed integer separated by spaces: ");
			int rows = scan.nextInt();
			int cols = scan.nextInt();
			int seed = scan.nextInt();
			Town town = new Town(rows,cols);
			town.randomInit(seed);
			
			//make array of towns (easier to debug)
			Town[] townList = new Town[12];
			townList[0] = town;
			//get profit for first month
			totalProfit += getProfit(town);
			
			//loop through each month
			for(int i = 1; i < 12; i++) {
				Town tNew = updatePlain(townList[i-1]);
				townList[i] = tNew;
				
				totalProfit += getProfit(townList[i]);
				if(i == 11) {
					//print utilization
					DecimalFormat f = new DecimalFormat("##.00");
					double num = (100*totalProfit)/(rows*cols*12);
					System.out.println(f.format(num) + "%");
				}
			}
		}
		else {
			System.out.println("Not an option.");
		}
		scan.close();
	}	
}