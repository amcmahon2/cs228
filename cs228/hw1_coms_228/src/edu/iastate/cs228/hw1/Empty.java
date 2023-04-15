package edu.iastate.cs228.hw1;

public class Empty extends TownCell {

	public Empty(Town p, int r, int c) {
		super(p, r, c);
	}

	@Override
	public State who() {
		return State.EMPTY;
	}

	@Override
	public TownCell next(Town tNew) {
		plain.grid[row][col].census(nCensus);
		// rule 6a check
		if (nCensus[OUTAGE] + nCensus[EMPTY] <= 1) {
			return new Reseller(tNew, this.row, this.col);
		}
		else {
			return new Casual(tNew, this.row, this.col);
		}

	}

}
