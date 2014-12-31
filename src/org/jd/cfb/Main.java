package org.jd.cfb;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

public class Main {

	public static void main(final String[] args) {
		installEscapeListener();

		final Operator operator = new Operator();
		final AI ai = new SimpleScoreAI();
		final Board board = new DefaultBoard();
		final GameStateAnalyzer analyzer = new GameStateAnalyzer();
		final Scanner scanner = new Scanner();

		installPrintListener(board);

		operator.park();

		while (analyzer.getState(board) == GameState.RUNNING) {
			Samples.play("Your move.wav");
			Button.ENTER.waitForPressAndRelease();

			scanner.updateBoard(board, operator);

			if (analyzer.getState(board) != GameState.RUNNING) {
				break;
			}

			final int nextMoveX = ai.getNextMove(board);

			dropCoin(nextMoveX, board);
		}

		switch (analyzer.getState(board)) {
			case AI_WON:
				Samples.play("You lost.wav");
				break;
			case PLAYER_WON:
				Samples.play("You won.wav");
				break;
			case DRAW:
				Samples.play("Draw.wav");
				break;
			default:
				throw new RuntimeException("Game finished with unexpected state");
		}

		Button.ENTER.waitForPressAndRelease();
	}

	private static void dropCoin(final int x, final Board board) {
		Samples.play("Please drop.wav", (x + 1) + ".wav");
		Button.ENTER.waitForPressAndRelease();
		Boards.dropCoin(x, Coin.AI, board);
	}

	private static void installEscapeListener() {
		Button.ESCAPE.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(final Button b) {
				System.exit(1);
			}

			@Override
			public void buttonReleased(final Button b) {

			}
		});
	}

	private static void installPrintListener(final Board board) {
		Button.RIGHT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(final Button b) {
				Boards.print(board);
			}

			@Override
			public void buttonReleased(final Button b) {
			}
		});

	}

}
