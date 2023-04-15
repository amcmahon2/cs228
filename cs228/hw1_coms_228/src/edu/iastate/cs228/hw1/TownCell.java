package edu.iastate.cs228.hw1;


/**
 * 
 * @author andrew mcmahon 
 * Also provide appropriate comments for this class
 * the class TownCell is used to create objects which are positioned
 * on a town's grid. this method is abstract as there are 5 types of
 * town cells. there is also a class census used to take a census
 * of the neighbors of a specified town cell object
 */
public abstract class TownCell {

	protected Town plain;
	protected int row;
	protected int col;

	// constants to be used as indices.
	protected static final int RESELLER = 0;
	protected static final int EMPTY = 1;
	protected static final int CASUAL = 2;
	protected static final int OUTAGE = 3;
	protected static final int STREAMER = 4;

	public static final int NUM_CELL_TYPE = 5;

	// Use this static array to take census.
	public static final int[] nCensus = new int[NUM_CELL_TYPE];

	public TownCell(Town p, int r, int c) {
		plain = p;
		row = r;
		col = c;
	}

	/**
	 * Checks all neigborhood cell types in the neighborhood. Refer to homework pdf
	 * for neighbor definitions (all adjacent neighbors excluding the center cell).
	 * Use who() method to get who is present in the neighborhood
	 * 
	 * @param counts of all customers
	 */
	public void census(int nCensus[]) {
		// zero the counts of all customers
		nCensus[RESELLER] = 0;
		nCensus[EMPTY] = 0;
		nCensus[CASUAL] = 0;
		nCensus[OUTAGE] = 0;
		nCensus[STREAMER] = 0;
		//create a 2d array of neighbors 
		//also i'm assuming all towns will be at least 2x2
		//if it's already in the top, start there
		int startRow = 0;
		int startCol = 0;
		int endRow,endCol;
		//initialize start positions
		if(this.row != 0 && this.col != 0) {
			//if both aren't zero, subtract one for the start index
			startRow = this.row - 1;
			startCol = this.col - 1;
			endRow = startRow + 2;
			endCol = startCol + 2;
		}
		else if(this.col == 0 && this.row != 0) {
			startRow = this.row - 1;
			endRow = startRow + 2;
			endCol = 1;
		}
		else if(this.row == 0 && this.col != 0) {
			startCol = this.col - 1;
			endCol = startCol + 2;
			endRow = 1;
		}
		else {
			//start pos is 0,0
			endRow = 1;
			endCol = 1;
		}

		
		//if the neighborhood is on a side
		if(endRow >= this.plain.getLength()) {
			endRow = this.row;
		}
		if(endCol >= this.plain.getWidth()) {
			endCol = this.col;
			
		}
		for(int i = startRow; i <= endRow; i++) {

			for(int j = startCol; j <= endCol; j++) {
				
				//don't count original cell
				if(i != this.row || j != this.col) {
					//find state of cell and add it to census
					State temp = this.plain.grid[i][j].who();
					
					
					if(temp.equals(State.RESELLER)) {
						nCensus[RESELLER]++;
					}
					else if(temp.equals(State.EMPTY)) {
						nCensus[EMPTY]++;
					}
					else if(temp.equals(State.CASUAL)) {
						nCensus[CASUAL]++;
					}
					else if(temp.equals(State.OUTAGE)) {
						nCensus[OUTAGE]++;
					}
					else if(temp.equals(State.STREAMER)) {
						nCensus[STREAMER]++;
					}
					
				}
			}
		}
	}
	
	
	

	/**
	 * Gets the identity of the cell.
	 * 
	 * @return State
	 */
	public abstract State who();

	/**
	 * Determines the cell type in the next cycle.
	 * 
	 * @param tNew: town of the next cycle
	 * @return TownCell
	 */
	public abstract TownCell next(Town tNew);
}
