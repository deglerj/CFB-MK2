package org.jd.cfb;

public class SimpleScoreAI implements AI {

	private static final int	WINNING_POINTS							= 100000;

	private static final int	TWO_IN_ROW_WITH_EMPTY_NEIGHBOR_POINTS	= 10;		// Rows with two empty neighbors are counted twice

	private static final int	THREE_IN_ROW_WITH_EMPTY_NEIGHBOR_POINTS	= 100;		// Rows with two empty neighbors are counted twice

	private static final int	ONE_WITH_EMPTY_NEIGHBOR_POINTS			= 2;

	private static final int	BLOCK_ENEMY_TWO_IN_ROW_POINTS			= 30;		// Enemy has one coin with an empty neighbor, now he
	// can't have two

	private static final int	BLOCK_ENEMY_THREE_IN_ROW_POINTS			= 90;		// Enemy has two coins with an empty neighbor, now he
	// can't have three

	private static final int	BLOCK_ENEMY_WIN_POINTS					= 10000;

	@Override
	public int getNextMove(final Board board) {
		int bestScore = 0;
		Integer bestX = null;

		for (int x = 0; x < 7; x++) {
			if (Boards.getHeight(x, board) < 6) {
				final int score = getScore(x, board);

				// Better score? -> Use new solution
				// Same score? -> Use other solution with a probability of 50% (keeps AI from always reacting with the same moves)
				if ((score > bestScore) || (score == bestScore && Math.random() > 0.5)) {
					bestScore = score;
					bestX = x;
				}
			}
		}

		if (bestX == null) {
			return new RandomAI().getNextMove(board);
		}
		else {
			return bestX;
		}
	}

	private void doNextCoin(final int i, final int[] enemyOneWithEmptyNeighbor, final int[] enemyTwoInRowWithEmptyNeighbor,
			final int[] enemyThreeInRowWithEmptyNeighbor, final int[] oneWithEmptyNeighbor, final int[] twoInRowWithEmptyNeighbor,
			final int[] threeInRowWithEmptyNeighbor, final int[] fourInRow, final int[] length, final Coin[] lastCoin, final boolean[] emptyBeforeLastCoin,
			final Coin[] coin, final Coin[] nextCoin) {

		// Same coin as last coin? -> Continue
		if (coin[i] == lastCoin[i]) {
			length[i]++;
		}
		// Coin changed?
		else {

			// Last coins were ours?
			if (lastCoin[i] == Coin.AI) {
				if (length[i] == 1) {
					// We have 1 coin and the coin before was empty? -> oneWithEmptyNeighbor++
					if (emptyBeforeLastCoin[i]) {
						oneWithEmptyNeighbor[i]++;
					}
					// We have 2 in a row and the next coin is empty? -> oneWithEmptyNeighbor++
					if (nextCoin[i] == Coin.NONE) {
						oneWithEmptyNeighbor[i]++;
					}
				}
				else if (length[i] == 2) {
					// We have 2 in a row and the coin before was empty? -> twoInRowWithEmptyNeighbor++
					if (emptyBeforeLastCoin[i]) {
						twoInRowWithEmptyNeighbor[i]++;
					}
					// We have 2 in a row and the next coin is empty? -> twoInRowWithEmptyNeighbor++
					if (nextCoin[i] == Coin.NONE) {
						twoInRowWithEmptyNeighbor[i]++;
					}
				}
				else if (length[i] == 3) {
					// We have 3 in a row and the coin before was empty? -> threeInRowWithEmptyNeighbor++
					if (emptyBeforeLastCoin[i]) {
						threeInRowWithEmptyNeighbor[i]++;
					}
					// We have 3 in a row and the next coin is empty? -> threeInRowWithEmptyNeighbor++
					if (nextCoin[i] == Coin.NONE) {
						threeInRowWithEmptyNeighbor[i]++;
					}
				}
				// We have 4 or more in a row? -> fourInRow++
				else if (length[i] > 3) {
					fourInRow[i]++;
				}
			}
			// Last coins belong to player?
			else if (lastCoin[i] == Coin.PLAYER) {
				if (length[i] == 1) {
					// Enemy has 1 coin and the coin before was empty? -> enemyOneWithEmptyNeighbor++
					if (emptyBeforeLastCoin[i]) {
						enemyOneWithEmptyNeighbor[i]++;
					}
					// Enemy has 1 coin and the next coin is empty? -> enemyOneWithEmptyNeighbor++
					if (nextCoin[i] == Coin.NONE) {
						enemyOneWithEmptyNeighbor[i]++;
					}
				}
				else if (length[i] == 2) {
					// Enemy has 2 in a row and the coin before was empty? -> enemyTwoInRowWithEmptyNeighbor++
					if (emptyBeforeLastCoin[i]) {
						enemyTwoInRowWithEmptyNeighbor[i]++;
					}
					// We have 2 in a row and the next coin is empty? -> enemyTwoInRowWithEmptyNeighbor++
					if (nextCoin[i] == Coin.NONE) {
						enemyTwoInRowWithEmptyNeighbor[i]++;
					}
				}
				else if (length[i] == 3) {
					// Enemy has 3 in a row and the coin before was empty? -> enemyThreeInRowWithEmptyNeighbor++
					if (emptyBeforeLastCoin[i]) {
						enemyThreeInRowWithEmptyNeighbor[i]++;
					}
					// We have 3 in a row and the next coin is empty? -> enemyThreeInRowWithEmptyNeighbor++
					if (nextCoin[i] == Coin.NONE) {
						enemyThreeInRowWithEmptyNeighbor[i]++;
					}
				}
			}

			length[i] = 0;
			emptyBeforeLastCoin[i] = lastCoin[i] == Coin.NONE;
			lastCoin[i] = coin[i];
		}
	}

	private Coin[][] getAscendingRow(final int x, final int y, final Board board) {
		final Coin[][] row = new Coin[7][2];

		// Add starting coin to middle of row
		row[3][0] = Coin.NONE;
		row[3][1] = Coin.AI;
		int coins = 1;

		// Move down left
		int curX = x - 1;
		int curY = y - 1;
		int i = 2;
		while (isInBounds(curX, curY) && i > 0) {
			final Coin coin = board.getCoin(curX, curY);
			row[i][0] = coin;
			row[i][1] = coin;

			curX--;
			curY--;
			coins++;
			i--;
		}

		// Move up right
		curX = x + 1;
		curY = y + 1;
		i = 4;
		while (isInBounds(curX, curY) && i < 7) {
			final Coin coin = board.getCoin(curX, curY);
			row[i][0] = coin;
			row[i][1] = coin;

			curX++;
			curY++;
			coins++;
			i++;
		}

		// Remove all null values from row array
		final Coin[][] nonNullRow = new Coin[coins][2];
		i = 0;
		for (final Coin[] coin : row) {
			if (coin[0] != null) {
				nonNullRow[i] = coin;
				i++;
			}
		}

		return row;
	}

	private Coin[][] getDescendingRow(final int x, final int y, final Board board) {
		final Coin[][] row = new Coin[7][2];

		// Add starting coin to middle of row
		row[3][0] = Coin.NONE;
		row[3][1] = Coin.AI;
		int coins = 1;

		// Move up left
		int curX = x - 1;
		int curY = y - 1;
		int i = 2;
		while (isInBounds(curX, curY) && i > 0) {
			final Coin coin = board.getCoin(curX, curY);
			row[i][0] = coin;
			row[i][1] = coin;
			;

			curX--;
			curY++;
			coins++;
			i--;
		}

		// Move down right
		curX = x + 1;
		curY = y + 1;
		i = 4;
		while (isInBounds(curX, curY) && i < 7) {
			final Coin coin = board.getCoin(curX, curY);
			row[i][0] = coin;
			row[i][1] = coin;

			curX++;
			curY--;
			coins++;
			i++;
		}

		// Remove all null values from row array
		final Coin[][] nonNullRow = new Coin[coins][2];
		i = 0;
		for (final Coin[] coin : row) {
			if (coin[0] != null) {
				nonNullRow[i] = coin;
				i++;
			}
		}

		return row;
	}

	private Coin[][] getHorizontalRow(final int x, final int y, final Board board) {
		final int start = Math.max(0, x - 3);
		final int end = Math.min(6, x + 3);
		final int length = end - start + 1;

		final Coin[][] row = new Coin[length][2];
		for (int i = 0; i < length; i++) {
			final Coin coin = board.getCoin(start + i, y);
			row[i][0] = coin;
			if (x == start + i) {
				row[i][1] = Coin.AI;
			}
			else {
				row[i][1] = coin;
			}
		}

		return row;
	}

	private int getRowScore(final Coin[][] row) {
		final int[] enemyOneWithEmptyNeighbor = { 0, 0 };
		final int[] enemyTwoInRowWithEmptyNeighbor = { 0, 0 };
		final int[] enemyThreeInRowWithEmptyNeighbor = { 0, 0 };
		final int[] oneWithEmptyNeighbor = { 0, 0 };
		final int[] twoInRowWithEmptyNeighbor = { 0, 0 };
		final int[] threeInRowWithEmptyNeighbor = { 0, 0 };
		final int[] fourInRow = { 0, 0 };

		final int[] length = { 0, 0 };
		final Coin[] lastCoin = { null, null };
		final boolean[] emptyBeforeLastCoin = { false, false };
		for (int i = 0; i <= row.length; i++) { // Do one additional loop with a "null coin" to include streaks at the end of the row
			final Coin[] coin = (i > (row.length - 1)) ? new Coin[] { null, null } : row[i];
			final Coin[] nextCoin = (i + 1 > row.length - 1) ? new Coin[] { null, null } : row[i + 1];

			doNextCoin(0, enemyOneWithEmptyNeighbor, enemyTwoInRowWithEmptyNeighbor, enemyThreeInRowWithEmptyNeighbor, oneWithEmptyNeighbor,
					twoInRowWithEmptyNeighbor, threeInRowWithEmptyNeighbor, fourInRow, length, lastCoin, emptyBeforeLastCoin, coin, nextCoin);
			doNextCoin(1, enemyOneWithEmptyNeighbor, enemyTwoInRowWithEmptyNeighbor, enemyThreeInRowWithEmptyNeighbor, oneWithEmptyNeighbor,
					twoInRowWithEmptyNeighbor, threeInRowWithEmptyNeighbor, fourInRow, length, lastCoin, emptyBeforeLastCoin, coin, nextCoin);
		}

		int score = 0;

		score += (enemyOneWithEmptyNeighbor[0] - enemyOneWithEmptyNeighbor[1]) * BLOCK_ENEMY_TWO_IN_ROW_POINTS;
		score += (enemyTwoInRowWithEmptyNeighbor[0] - enemyTwoInRowWithEmptyNeighbor[1]) * BLOCK_ENEMY_THREE_IN_ROW_POINTS;
		score += (enemyThreeInRowWithEmptyNeighbor[0] - enemyThreeInRowWithEmptyNeighbor[1]) * BLOCK_ENEMY_WIN_POINTS;

		score += (oneWithEmptyNeighbor[1] - oneWithEmptyNeighbor[0]) * ONE_WITH_EMPTY_NEIGHBOR_POINTS;
		score += (twoInRowWithEmptyNeighbor[1] - twoInRowWithEmptyNeighbor[0]) * TWO_IN_ROW_WITH_EMPTY_NEIGHBOR_POINTS;
		score += (threeInRowWithEmptyNeighbor[1] - threeInRowWithEmptyNeighbor[0]) * THREE_IN_ROW_WITH_EMPTY_NEIGHBOR_POINTS;
		score += (fourInRow[1] - fourInRow[0]) * WINNING_POINTS;

		return score;
	}

	private int getScore(final int x, final Board board) {
		final int y = Boards.getHeight(x, board);

		assert board.getCoin(x, y) == Coin.NONE;

		int score = 0;

		score += getRowScore(getHorizontalRow(x, y, board));
		score += getRowScore(getVerticalRow(x, y, board));
		score += getRowScore(getAscendingRow(x, y, board));
		score += getRowScore(getDescendingRow(x, y, board));

		return score;
	}

	private Coin[][] getVerticalRow(final int x, final int y, final Board board) {
		final int start = Math.max(0, y - 3);
		final int end = Math.min(5, y + 3);
		final int length = end - start + 1;

		final Coin[][] row = new Coin[length][2];
		for (int i = 0; i < length; i++) {
			final Coin coin = board.getCoin(x, start + i);
			row[i][0] = coin;
			if (y == start + i) {
				row[i][1] = Coin.AI;
			}
			else {
				row[i][1] = coin;
			}
		}

		return row;
	}

	private boolean isInBounds(final int x, final int y) {
		return x >= 0 && x < 7 && y >= 0 && y < 6;
	}

}
