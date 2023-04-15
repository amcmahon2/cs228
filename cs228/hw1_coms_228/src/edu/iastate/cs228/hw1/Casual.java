package edu.iastate.cs228.hw1;

public class Casual extends TownCell {

	public Casual(Town p, int r, int c) {
		super(p, r, c);
	}

	@Override
	public State who() {
		return State.CASUAL;
	}

	@Override
	public TownCell next(Town tNew) {
		plain.grid[row][col].census(nCensus);	
		// rule 6a check
		if (nCensus[OUTAGE] + nCensus[EMPTY] <= 1) {
			return new Reseller(tNew, this.row, this.col);
		}
		// check normal rules for casual
		else {
			if (nCensus[RESELLER] > 0) {
				return new Outage(tNew, this.row, this.col);
			}
			else if(nCensus[STREAMER] > 0 || nCensus[CASUAL] >= 5) {
				return new Streamer(tNew, this.row, this.col);
			}
			// if none of the conditions apply, the cell remains as a Casual (rule 7)
			return new Casual(tNew, this.row, this.col);
			
		}
	}

}
