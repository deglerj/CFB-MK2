package org.jd.cfb;

public class OverlayBoard implements Board {

	private final Coin[][]	overlay	= new Coin[7][6];

	private final Board		base;

	public OverlayBoard(final Board base) {
		this.base = base;
	}

	@Override
	public Coin getCoin(final int x, final int y) {
		if (overlay[x][y] == null) {
			return base.getCoin(x, y);
		}
		else {
			return overlay[x][y];
		}
	}

	@Override
	public void setCoin(final int x, final int y, final Coin coin) {
		overlay[x][y] = coin;
	}

	@Override
	public String toString() {
		return Boards.toString(this);
	}

}
