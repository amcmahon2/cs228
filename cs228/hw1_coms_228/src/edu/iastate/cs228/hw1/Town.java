package edu.iastate.cs228.hw1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Random;
import java.util.Scanner;
import edu.iastate.cs228.hw1.State;
import static edu.iastate.cs228.hw1.State.*;

/**
 * @author andrew mcmahon
 * Town is a method which is used to create a town object. a town object 
 * has a grid of TownCell objects assigned to it.
 */
public class Town {

	private int length, width; // Row and col (first and second indices)
	public TownCell[][] grid;

	/**
	 * Constructor to be used when user wants to generate grid randomly, with the
	 * given seed. This constructor does not populate each cell of the grid (but
	 * should assign a 2D array to it).
	 * 
	 * @param length
	 * @param width
	 */
	public Town(int length, int width) {
		this.length = length;
		this.width = width;
		TownCell[][] array = new TownCell[this.length][this.width];
		this.grid = array;
	}

	/**
	 * Constructor to be used when user wants to populate grid based on a file.
	 * Please see that it simple throws FileNotFoundException exception instead of
	 * catching it. Ensure that you close any resources (like file or scanner) which
	 * is opened in this function.
	 * 
	 * @param inputFileName
	 * @throws FileNotFoundException
	 */
	public Town(String inputFileName) throws FileNotFoundException {
		try {
			// make new buffered reader to read the given file by line via filereader
			// I did this because it made more sense to scan the whole line and
			// just add by row instead of going 1 by 1
			BufferedReader br = new BufferedReader(new FileReader(inputFileName));

			String rowCols = br.readLine();
			// scan only first line
			Scanner s = new Scanner(rowCols);
			int rows = s.nextInt();
			int cols = s.nextInt();
			this.length = rows;
			this.width = cols;

			TownCell[][] array = new TownCell[this.length][this.width];
			this.grid = array;

			// now read the rest of the file
			for (int i = 0; i < rows; i++) {
				// read the new line
				String newRow = br.readLine();
				Scanner newCol = new Scanner(newRow);
				for (int j = 0; j < cols; j++) {
					// make scanner that checks the new row
					String currentLetter = newCol.next();

					if (currentLetter.equals("R")) {
						this.grid[i][j] = new Reseller(this, i, j);
					} else if (currentLetter.equals("E")) {
						this.grid[i][j] = new Empty(this, i, j);
					} else if (currentLetter.equals("C")) {
						this.grid[i][j] = new Casual(this, i, j);
					} else if (currentLetter.equals("O")) {
						this.grid[i][j] = new Outage(this, i, j);
					} else if (currentLetter.equals("S")) {
						this.grid[i][j] = new Streamer(this, i, j);
					}
				}
				if (i + 1 == rows) {
					newCol.close();
				}
			}
			s.close();
			br.close();
		} catch (IOException e) {
			System.out.println("File Error.");
		}
	}

	/**
	 * Returns width of the grid.
	 * 
	 * @return
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Returns length of the grid.
	 * 
	 * @return
	 */
	public int getLength() {
		return this.length;
	}

	/**
	 * Initialize the grid by randomly assigning cell with one of the following
	 * class object: Casual, Empty, Outage, Reseller OR Streamer
	 */
	public void randomInit(int seed) {
		Random rand = new Random(seed);
		for (int i = 0; i < this.grid.length; i++) {
			for (int j = 0; j < this.grid[0].length; j++) {
				int seedUse = rand.nextInt(5);
				// assign cell at index with object associated with random number just generated
				if (seedUse == 0) {
					this.grid[i][j] = new Reseller(this, i, j);
				} else if (seedUse == 1) {
					this.grid[i][j] = new Empty(this, i, j);
				} else if (seedUse == 2) {
					this.grid[i][j] = new Casual(this, i, j);
				} else if (seedUse == 3) {
					this.grid[i][j] = new Outage(this, i, j);
				} else if (seedUse == 4) {
					this.grid[i][j] = new Streamer(this, i, j);
				}
			}
		}
	}

	/**
	 * Output the town grid. For each square, output the first letter of the cell
	 * type. Each letter should be separated either by a single space or a tab. And
	 * each row should be in a new line. There should not be any extra line between
	 * the rows.
	 */
	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < this.grid.length; i++) {
			for (int j = 0; j < this.grid[0].length; j++) {
				if (this.grid[i][j].who() == RESELLER) {
					s += "R";
				} else if (this.grid[i][j].who() == EMPTY) {
					s += "E";
				} else if (this.grid[i][j].who() == CASUAL) {
					s += "C";
				} else if (this.grid[i][j].who() == OUTAGE) {
					s += "O";
				} else if (this.grid[i][j].who() == STREAMER) {
					s += "S";
				}
				// if grid is not at end of row, input space
				if (j + 1 != this.grid[0].length) {
					s += " ";
				}
				// if it is at the end, add a new line instead
				else {
					s += "\n";
				}
			}
		}
		return s;
	}
}
