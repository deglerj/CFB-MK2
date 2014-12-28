package org.jd.cfb;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.Sound;

public class Main {

	public static void main(final String[] args) {
		installEscapeListener();

		final Operator operator = new Operator();
		final AI ai = new RandomAI();
		final Board board = new DefaultBoard();
		final GameStateAnalyzer analyzer = new GameStateAnalyzer();
		final Scanner scanner = new Scanner();

		installPrintListener(board);

		operator.park();

		while (analyzer.getState(board) == GameState.RUNNING) {
			System.out.println("Your move!");
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
				System.out.println("You lost :(");
				break;
			case PLAYER_WON:
				System.out.println("You won!");
				break;
			case DRAW:
				System.out.println("Draw");
				break;
			default:
				throw new RuntimeException("Game finished with unexpected state");
		}

		Button.ENTER.waitForPressAndRelease();
	}

	private static void dropCoin(final int x, final Board board) {
		System.out.println("Please drop at " + x);
		for (int i = 0; i <= x; i++) {
			int frequency = 0;
			switch (i) {
				case 0:
					frequency = 262;
					break;
				case 1:
					frequency = 287;
					break;
				case 2:
					frequency = 320;
					break;
				case 3:
					frequency = 349;
					break;
				case 4:
					frequency = 392;
					break;
				case 5:
					frequency = 440;
					break;
				case 6:
					frequency = 494;
					break;
			}
			Sound.playTone(frequency, 250);
			try {
				Thread.sleep(300);
			}
			catch (final InterruptedException e) {
			}
		}

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
