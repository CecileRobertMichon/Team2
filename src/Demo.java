/*
 *  Team 2
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 *  Derek Yu - 260570997
 *  Ajan Ahmed - 260509046
 *  Georges Assouad - 260567730
 *  Chaohan Wang - 260516712
 */

import lejos.nxt.Button;

public class Demo {

	public static void main(String[] args) {

		// setup the odometer, correction, display, and ultrasonic and light
		// sensors
		Odometer odo = new Odometer();
		OdometryCorrection correc = new OdometryCorrection(odo);
		USFilter filter = new USFilter();
		Navigation nav = new Navigation(odo, filter);
		OdometryDisplay lcd = new OdometryDisplay(odo, filter);

		odo.start();
		lcd.start();
		filter.start();

		// Calibration Code - used to find exact radius and width values

		// Motor.A.setAcceleration(2000); Motor.B.setAcceleration(2000);
		// Motor.A.setSpeed(100); Motor.B.setSpeed(100);
		// Motor.A.rotate(1625, true); Motor.B.rotate(1625, false);
		// Motor.A.rotate(2786, true); Motor.B.rotate(-2786, false);

		// perform the ultrasonic localization
		//USLocalizer usl = new USLocalizer(odo,
		//		USLocalizer.LocalizationType.FALLING_EDGE, nav, filter);
		//usl.doLocalization();

		// perform the light sensor localization
		LightLocalizer lsl = new LightLocalizer(odo, nav);
		lsl.doLocalization();

		// when done travel to (0,0) and turn to 0 degrees
		nav.travelTo(0, 0);
		nav.turnTo(0);

		// correc.start();
		/*
		 * // travel to the right bottom corner of the shooting area while
		 * avoiding obstacles nav.travelTo(7.2, 7.2);
		 * 
		 * // light localization lsl.doLocalization(); odo.setX(7 + odo.getX());
		 * odo.setY(7 + odo.getY()); nav.turnTo(0);
		 * 
		 * 
		 * // go to shooting spot relative to target 1
		 * 
		 * // shoot half the balls
		 * 
		 * // go to shooting spot relative to target 2
		 * 
		 * // shoot the other half
		 * 
		 * 
		 * // return to (0,0)
		 */
		Button.waitForAnyPress();
		System.exit(0);
	}
}