package org.jd.cfb;

public class Main {

	public static void main(final String[] args) {
		final Operator operator = new Operator();

		for (int x = 0; x < 5; x++) {
			for (int i = 0; i < 6; i++) {
				operator.moveTo(i, i);
			}
		}

		operator.moveTo(0, 0);

	}

}
