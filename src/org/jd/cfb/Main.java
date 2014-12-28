package org.jd.cfb;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

public class Main {

	public static void main(final String[] args) {
		installEscapeListener();

		final Operator operator = new Operator();
		final AI ai = new RandomAI();
		final Board board = new DefaultBoard();
		final GameStateAnalyzer analyzer = new GameStateAnalyzer();
		final Scanner scanner = new Scanner();

		operator.park();

		while (analyzer.getState(board) == GameState.RUNNING) {
			// Wait for the player to finish his move
			Button.ENTER.waitForPressAndRelease();

			scanner.updateBoard(board, operator);

			if (analyzer.getState(board) != GameState.RUNNING) {
				break;
			}

			final int nextMoveX = ai.getNextMove(board);

			Boards.dropCoin(nextMoveX, Coin.AI, board);
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

}
