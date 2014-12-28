package org.jd.cfb;

public class RandomAI implements AI {

	@Override
	public int getNextMove(final Board board) {
		while (true) {
			final int x = (int) Math.floor(Math.random() * 7);
			if (Boards.getHeight(x, board) != 6) {
				return x;
			}
		}
	}

}
