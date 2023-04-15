package edu.iastate.cs228.hw1;

public class Reseller extends TownCell {

	public Reseller(Town p, int r, int c) {
		super(p, r, c);
	}

	@Override
	public State who() {
		return State.RESELLER;
	}

	@Override
	public TownCell next(Town tNew) {
		plain.grid[row][col].census(nCensus);
			if (nCensus[CASUAL] <= 3 || nCensus[EMPTY] >= 3) {
				return new Empty(tNew, this.row, this.col);
			}
			else if(nCensus[CASUAL] >= 5) {
				return new Streamer(tNew, this.row, this.col);
			}
			else {
				return new Reseller(tNew, this.row, this.col);
			}
		}
	}
