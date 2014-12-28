package org.jd.cfb;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.ColorSensor.Color;

public class Main {

	public static void main(final String[] args) {
		installEscapeListener();

		final Operator operator = new Operator();

		for (int y = 5; y >= 4; y--) {
			for (int x = 0; x <= 6; x++) {
				operator.moveTo(x, y);

				final Color color = Sensors.COLOR.getRawColor();

				System.out.println(color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "," + color.getBackground());

				Button.ENTER.waitForPressAndRelease();
			}
		}

		for (int x = 0; x < 5; x++) {
			for (int i = 0; i < 6; i++) {
				operator.moveTo(i, i);

			}
		}

		operator.moveTo(0, 0);

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
