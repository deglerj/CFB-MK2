package org.jd.cfb;

import lejos.nxt.ColorSensor.Color;

public class Scanner {

	public boolean isBlank() {
		final Color color = Sensors.COLOR.getRawColor();
		final int sum = color.getBackground() + color.getBlue() + color.getGreen() + color.getRed();

		return sum < 600;
	}

}
