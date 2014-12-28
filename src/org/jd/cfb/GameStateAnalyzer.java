package org.jd.cfb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameStateAnalyzer {

	private static class DiagonalRowProvider implements RowProvider {
		private final int		startX;

		private final int		startY;

		private final boolean	up;

		public DiagonalRowProvider(final int x, final int y, final boolean up) {
			startX = x;
			startY = y;
			this.up = up;
		}

		@Override
		public Row getRow(final Board board) {
			final Row row = new Row();

			int x = startX;
			int y = startY;

			while (isInBounds(x, y)) {
				row.add(board.getCoin(x, y));

				x++;
				if (up) {
					y++;
				}
				else {
					y--;
				}
			}

			return row;
		}

		private boolean isInBounds(final int x, final int y) {
			return x >= 0 && x <= 6 && y >= 0 && y <= 5;
		}

	}

	private static class HorizontalRowProvider implements RowProvider {
		private final int	y;

		public HorizontalRowProvider(final int y) {
			this.y = y;
		}

		@Override
		public Row getRow(final Board board) {
			final Row row = new Row();
			for (int x = 0; x < 7; x++) {
				row.add(board.getCoin(x, y));
			}
			return row;
		}
	}

	private static class Row {
		private final List<Coin>	coins	= new ArrayList<Coin>(7);

		private int					validCoins;

		public void add(final Coin coin) {
			coins.add(coin);

			if (coin != Coin.NONE) {
				validCoins++;
			}
		}

		public GameState getState() {
			if (validCoins < 4) {
				return GameState.RUNNING;
			}

			Coin lastCoin = null;
			int length = 0;

			for (final Coin coin : coins) {
				if (coin == lastCoin) {
					length++;
					// Four coins in a row? -> Someone has won
					if (length == 4 && coin != Coin.NONE) {
						if (coin == Coin.AI) {
							return GameState.AI_WON;
						}
						else {
							return GameState.PLAYER_WON;
						}
					}
				}
				else {
					lastCoin = coin;
					length = 1;
				}
			}

			return GameState.RUNNING;
		}

		@Override
		public String toString() {
			return "Row [coins=" + coins + "]";
		}
	}

	private interface RowProvider {
		Row getRow(Board board);
	}

	private static class VerticalRowProvider implements RowProvider {
		private final int	x;

		public VerticalRowProvider(final int x) {
			this.x = x;
		}

		@Override
		public Row getRow(final Board board) {
			final Row row = new Row();
			for (int y = 0; y < 6; y++) {
				row.add(board.getCoin(x, y));
			}
			return row;
		}
	}

	private static Collection<RowProvider> createRowProviders() {
		final Collection<RowProvider> rowProviders = new ArrayList<GameStateAnalyzer.RowProvider>();

		for (int y = 0; y < 6; y++) {
			rowProviders.add(new HorizontalRowProvider(y));
		}

		for (int x = 0; x < 7; x++) {
			rowProviders.add(new VerticalRowProvider(x));
		}

		rowProviders.add(new DiagonalRowProvider(0, 2, true));
		rowProviders.add(new DiagonalRowProvider(0, 1, true));
		rowProviders.add(new DiagonalRowProvider(0, 0, true));
		rowProviders.add(new DiagonalRowProvider(1, 0, true));
		rowProviders.add(new DiagonalRowProvider(2, 0, true));
		rowProviders.add(new DiagonalRowProvider(3, 0, true));

		rowProviders.add(new DiagonalRowProvider(0, 5, false));
		rowProviders.add(new DiagonalRowProvider(0, 4, false));
		rowProviders.add(new DiagonalRowProvider(0, 3, false));
		rowProviders.add(new DiagonalRowProvider(1, 5, false));
		rowProviders.add(new DiagonalRowProvider(2, 5, false));
		rowProviders.add(new DiagonalRowProvider(3, 5, false));

		return rowProviders;
	}

	private static final Collection<RowProvider>	ROW_PROVIDERS	= createRowProviders();

	public GameState getState(final Board board) {
		if (isDraw(board)) {
			return GameState.DRAW;
		}

		for (final RowProvider rowProvider : ROW_PROVIDERS) {
			final Row row = rowProvider.getRow(board);
			final GameState state = row.getState();
			if (state != GameState.RUNNING) {
				return state;
			}
		}

		return GameState.RUNNING;
	}

	private boolean isDraw(final Board board) {
		return Boards.getCoinCount(board) == 42;
	}

}
