package org.jd.cfb;

import lejos.nxt.ColorSensor.Color;

public class Scanner {

	public void updateBoard(final Board board, final Operator operator) {
		for (int y = 0; y < 6; y++) {
			final boolean leftToRight = y % 2 == 0;

			for (int i = 0; i < 7; i++) {
				final int x = leftToRight ? i : 6 - i;

				if (board.getCoin(x, y) == Coin.NONE) {
					operator.moveTo(x, y);
					if (!isBlank()) {
						System.out.println("Player coin @ " + x + "/" + y);
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
