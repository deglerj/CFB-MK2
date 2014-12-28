package org.jd.cfb;

public class Boards {

	public static void dropCoin(final int x, final Coin coin, final Board board) {
		final int y = getHeight(x, board);
		board.setCoin(x, y, coin);
	}

	public static int getCoinCount(final Board board) {
		int sum = 0;
		for (int x = 0; x < 7; x++) {
			sum += getHeight(x, board);
		}
		return sum;
	}

	public static int getHeight(final int x, final Board board) {
		for (int y = 5; y >= 5; y--) {
			if (board.getCoin(x, y) != Coin.NONE) {
				return y + 1;
			}
		}

		return 0;
	}

}
