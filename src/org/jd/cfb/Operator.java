package org.jd.cfb;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;

public class Operator {

	private static class ParkAction implements Runnable {

		private final NXTRegulatedMotor	motor;

		private final TouchSensor		sensor;

		private final boolean			forward;

		private final int				speed;

		private ParkAction(final NXTRegulatedMotor motor, final TouchSensor sensor, final int speed, final boolean forward) {
			this.motor = motor;
			this.sensor = sensor;
			this.forward = forward;
			this.speed = speed;
		}

		@Override
		public void run() {
			motor.setSpeed(speed);

			if (forward) {
				motor.forward();
			}
			else {
				motor.backward();
			}

			while (!sensor.isPressed()) {
				Thread.yield();
			}

			motor.stop();
		}

	}

	public void park() {
		final ParkAction parkHorizontal = new ParkAction(Motors.HORIZONTAL, Sensors.HORIZONTAL_STOPPER, 200, true);
		final ParkAction parkVertical = new ParkAction(Motors.VERTICAL, Sensors.VERTICAL_STOPPER, 300, false);

		Runnables.runAndWait(parkHorizontal, parkVertical);
	}
}