package org.jd.cfb;

public class DefaultBoard implements Board {

	private final Coin[][]	coins	= new Coin[7][6];

	@Override
	public Coin getCoin(final int x, final int y) {
		final Coin coin = coins[x][y];
		if (coin == null) {
			return Coin.NONE;
		}
		else {
			return coin;
		}
	}

	@Override
	public void setCoin(final int x, final int y, final Coin coin) {
		coins[x][y] = coin;
	}

}
