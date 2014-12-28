package org.jd.cfb;

import java.util.ArrayList;
import java.util.Collection;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;

public class Operator {

	private static class MoveAction implements Runnable {

		private final NXTRegulatedMotor	motor;

		private final int				distance;

		private final int				speed;

		public MoveAction(final NXTRegulatedMotor motor, final int distance, final int speed, final boolean forward) {
			this.motor = motor;
			this.distance = forward ? distance : distance * -1;
			this.speed = speed;
		}

		@Override
		public void run() {
			motor.setSpeed(speed);

			motor.rotate(distance);
		}

	}

	private static abstract class ParkAction implements Runnable {

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

			complete();
		}

		protected abstract void complete();

	}

	private static final int	VERTICAL_HIGH_SPEED				= 350;

	private static final int	HORIZONTAL_HIGH_SPEED			= 200;

	private static final int	VERTICAL_NORMAL_SPEED			= 300;

	private static final int	HORIZONTAL_NORMAL_SPEED			= 150;

	private static final int	HORIZONTAL_PARKING_OFFSET		= 10;

	private static final int	VERTICAL_PARKING_OFFSET			= 10;

	private static final double	HORIZONTAL_POSITION_FACTOR		= 81.2;

	private static final double	VERTICAL_POSITION_FACTOR		= 560;

	private static final int	INITIAL_HORIZONTAL_CONFIDENCE	= 15;

	private static final int	INITIAL_VERTICAL_CONFIDENCE		= 13;

	private final ParkAction	parkVertical					= new ParkAction(Motors.VERTICAL, Sensors.VERTICAL_STOPPER, VERTICAL_HIGH_SPEED, true) {
		@Override
		protected void complete() {
			verticalPosition = 0;
			verticalConfidence = INITIAL_VERTICAL_CONFIDENCE;
		}
	};

	private final ParkAction	parkHorizontal					= new ParkAction(Motors.HORIZONTAL, Sensors.HORIZONTAL_STOPPER, HORIZONTAL_HIGH_SPEED, true) {
		@Override
		protected void complete() {
			horizontalPosition = 0;
			horizontalConfidence = INITIAL_HORIZONTAL_CONFIDENCE;
		}
	};

	private Integer				verticalPosition				= null;

	private Integer				horizontalPosition				= null;

	private int					horizontalConfidence			= INITIAL_HORIZONTAL_CONFIDENCE;

	private int					verticalConfidence				= INITIAL_VERTICAL_CONFIDENCE;

	public void moveTo(final int x, final int y) {
		ensureInitialized();

		/*
		 * Check and if necessary restore confidence:
		 * 1) Confidence at 0 for any axis? -> Park
		 * 2) Moving to 0 on any axis with a low confidence? -> Use this good chance to park
		 */
		final Collection<Runnable> parkActions = new ArrayList<Runnable>(2);
		if (horizontalConfidence == 0 || horizontalConfidence < INITIAL_HORIZONTAL_CONFIDENCE / 3) {
			parkActions.add(parkHorizontal);
		}
		if (verticalConfidence == 0 || verticalConfidence < INITIAL_VERTICAL_CONFIDENCE / 3) {
			parkActions.add(parkVertical);
		}
		Runnables.runAndWait(parkActions);

		final int targetHorizontalPosition = asHorizontalPosition(x);
		final int targetVerticalPosition = asVerticalPosition(y);

		final MoveAction moveHorizontal = new MoveAction(Motors.HORIZONTAL, targetHorizontalPosition - horizontalPosition, HORIZONTAL_NORMAL_SPEED, false);
		final MoveAction moveVertical = new MoveAction(Motors.VERTICAL, targetVerticalPosition - verticalPosition, VERTICAL_NORMAL_SPEED, false);

		horizontalPosition = targetHorizontalPosition;
		verticalPosition = targetVerticalPosition;

		Runnables.runAndWait(moveHorizontal, moveVertical);
	}

	public void park() {
		Runnables.runAndWait(parkHorizontal, parkVertical);
	}

	private int asHorizontalPosition(final int x) {
		return (int) (x * HORIZONTAL_POSITION_FACTOR + HORIZONTAL_PARKING_OFFSET);
	}

	private int asVerticalPosition(final int y) {
		return (int) (y * VERTICAL_POSITION_FACTOR + VERTICAL_PARKING_OFFSET);
	}

	private void ensureInitialized() {
		if (verticalPosition == null || horizontalPosition == null) {
			park();
		}
	}

}