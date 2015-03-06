/*
 *  Team 2
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 *  Derek Yu - 
 *  Ajan Ahmed - 
 *  Georges Assouad -
 *  Chaohan Wang -
 */

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class Demo {
	
	private final int TARGET_ONE_X = 0;
	private final int TARGET_ONE_Y = 0;
	private final int TARGET_TWO_X = 0;
	private final int TARGET_TWO_Y = 0;

	public static void main(String[] args) {
		
		// setup the odometer, correction, display, and ultrasonic and light sensors
		Odometer odo = new Odometer();
		OdometryCorrection correc = new OdometryCorrection(odo);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S2);
		ColorSensor ls = new ColorSensor(SensorPort.S1);
		Navigation nav = new Navigation(odo);
		OdometryDisplay lcd = new OdometryDisplay(odo);

		odo.start();
		correc.start();
		lcd.start();

		// perform the ultrasonic localization
		USLocalizer usl = new USLocalizer(odo, us, USLocalizer.LocalizationType.FALLING_EDGE, nav);
		usl.doLocalization();

		// perform the light sensor localization
		LightLocalizer lsl = new LightLocalizer(odo, ls, nav);
		lsl.doLocalization();
		
		// when done travel to (0,0) and turn to 0 degrees
		nav.travelTo(0, 0);
		nav.turnTo(0);
		
		// travel to the right bottom corner of the shooting area while avoiding obstacles
		nav.travelTo(7.2, 7.2);
		
		// light localization
		lsl.doLocalization();
		odo.setX(7 + odo.getX());
		odo.setY(7 + odo.getY());
		nav.turnTo(0);
		
		
		// go to shooting spot relative to target 1
		
		// shoot half the balls
		
		// go to shooting spot relative to target 2
		
		// shoot the other half
		

		// return to (0,0)

		Button.waitForAnyPress();
		System.exit(0);
	}
}