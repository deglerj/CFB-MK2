package org.jd.cfb;

import lejos.nxt.ColorSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class Sensors {

	public static TouchSensor	HORIZONTAL_STOPPER	= new TouchSensor(SensorPort.S4);

	public static TouchSensor	VERTICAL_STOPPER	= new TouchSensor(SensorPort.S2);

	public static ColorSensor	COLOR				= new ColorSensor(SensorPort.S1);

}
