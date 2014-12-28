package org.jd.cfb;

public class Boards {

	public static void dropCoin(final int x, final Coin coin, final Board board) {
		final int y = getHeight(x, board);
		board.setCoin(x, y, coin);
	}

	public static int getHeight(final int x, final Board board) {
		for (int y = 0; y < 6; y++) {
			if (board.getCoin(x, y) != Coin.NONE) {
				return y + 1;
			}
		}

		return 0;
	}

}
