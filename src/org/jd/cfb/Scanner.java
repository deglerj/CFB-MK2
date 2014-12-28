package org.jd.cfb;

import java.util.Arrays;

import lejos.nxt.ColorSensor.Color;

public class Scanner {

	public void updateBoard(final Board board, final Operator operator) {
		final boolean[] blankColumns = new boolean[7];
		Arrays.fill(blankColumns, false);

		for (int y = 0; y < 6; y++) {
			final boolean leftToRight = y % 2 == 0;

			for (int i = 0; i < 7; i++) {
				final int x = leftToRight ? i : 6 - i;

				if (blankColumns[x]) {
					continue;
				}

				if (board.getCoin(x, y) == Coin.NONE) {
					operator.moveTo(x, y);
					if (isBlank()) {
						blankColumns[x] = true;
					}
					else {
						System.out.println("Your move was: " + x + "/" + y);
						board.setCoin(x, y, Coin.PLAYER);
						return;
					}
				}
			}
		}
	}

	private boolean isBlank() {
		final Color color = Sensors.COLOR.getRawColor();
		final int sum = color.getBackground() + color.getBlue() + color.getGreen() + color.getRed();

		return sum < 600;
	}

}
