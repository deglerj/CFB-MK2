package org.jd.cfb;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;

public class Motors {

	public static NXTRegulatedMotor	HORIZONTAL	= new NXTRegulatedMotor(MotorPort.C);

	public static NXTRegulatedMotor	VERTICAL	= new NXTRegulatedMotor(MotorPort.A);

}
