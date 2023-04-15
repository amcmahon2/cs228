package edu.iastate.cs228.hw1;

public class Streamer extends TownCell {

	public Streamer(Town p, int r, int c) {
		super(p, r, c);
	}

	@Override
	public State who() {
		return State.STREAMER;
	}

	@Override
	public TownCell next(Town tNew) {
		plain.grid[row][col].census(nCensus);
		// rule 6a check
		if (nCensus[OUTAGE] + nCensus[EMPTY] <= 1) {
			return new Reseller(tNew, this.row, this.col);
		} 
		else {
			if (nCensus[RESELLER] > 0) {
				return new Outage(tNew, this.row, this.col);
			} 
			else if (nCensus[OUTAGE] > 0) {
				return new Empty(tNew, this.row, this.col);
			}
			// if none of the conditions apply, the cell remains as a Streamer (rule 7)
			else {
				return new Streamer(tNew, this.row, this.col);
			}
		}
	}
}
