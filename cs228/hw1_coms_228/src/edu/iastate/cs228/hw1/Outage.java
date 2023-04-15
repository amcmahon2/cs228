package edu.iastate.cs228.hw1;

public class Outage extends TownCell {

	public Outage(Town p, int r, int c) {
		super(p, r, c);
	}

	@Override
	public State who() {
		return State.OUTAGE;
	}

	@Override
	public TownCell next(Town tNew) {
		plain.grid[row][col].census(nCensus);
		return new Empty(tNew, this.row, this.col);
	}

}
