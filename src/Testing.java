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
import lejos.nxt.Sound;
import lejos.robotics.Color;

public class Testing {

	public static void main(String[] args) {

		// setup the odometer, correction, display, and ultrasonic and light
		// sensors
		Odometer odo = new Odometer();
		Robot robot = new Robot();
		USFilter filterStraight = new USFilter(robot.US);
		USFilter filterLeft = new USFilter(robot.US2);
		Navigation nav = new Navigation(odo, filterStraight, filterLeft);
		OdometryCorrection correct = new OdometryCorrection(odo, nav, robot);
		OdometryDisplay lcd = new OdometryDisplay(odo, filterStraight);
		Launcher launcher = new Launcher();
		LauncherPositioning position = new LauncherPositioning(odo, nav,
				launcher);

		odo.start();
		lcd.start();
		filterStraight.start();
		filterLeft.start();

		// Calibration Code - used to find exact radius and width values

		// robot.LEFT_MOTOR.setAcceleration(2000);
		// robot.RIGHT_MOTOR.setAcceleration(2000);
		// robot.LEFT_MOTOR.setSpeed(100);
		// robot.RIGHT_MOTOR.setSpeed(100);
		// robot.LEFT_MOTOR.rotate(1675, true);
		// robot.RIGHT_MOTOR.rotate(1675, false);
		// robot.LEFT_MOTOR.rotate(2832, true);
		// robot.RIGHT_MOTOR.rotate(-2832, false);
		
		// set navigation to localization mode - no obstacle detection
		nav.setIsLocalizing(true);
//		
//		USLocalizer usl = new USLocalizer(odo, nav, filterStraight, filterLeft);
//		usl.doLocalization();
//
//		// when done travel to (0,0) and turn to 0 degrees
//		nav.travelTo(0, 0);
//		nav.turnTo(30);

		
//		LightLocalizer lsl = new LightLocalizer(odo, nav, robot);
//
//
//		lsl.doLocalization();

//		nav.travelTo(0, 0);
//		nav.turnTo(0);
//		Sound.beep();

		correct.start();
		
		nav.travelTo(0, 1.5);
		nav.travelTo(1.5, 1.5);
		nav.travelTo(1.5, 0);
		nav.travelTo(0, 0);



//
//		nav.travelTo(0.5, 0);
//		nav.travelTo(0.5, 1.7);
//		
//		// correct the angle
//		correct.findLine();
//		correct.correctAngle();
//
//		nav.travelTo(0.5, 3);
//		nav.travelTo(2, 10);
//
//		// nav.travelTo(0, 2);
//		// nav.travelTo(2, 2);
//		nav.turnTo(20);
//
//		lsl = new LightLocalizer(odo, nav);
//		lsl.doLocalization();
//		odo.setX(2 * robot.TILE_LENGTH + odo.getX());
//		odo.setY(10 * robot.TILE_LENGTH + odo.getY());
//		nav.travelTo(2, 10);
//		nav.turnTo(0);
//		Sound.beep();

		//
		// // go to shooting spot relative to target 1
		// // shoot half the balls
		// // go to shooting spot relative to target 2
		// // shoot the other half
		// position.targetAcquisition(robot.TARGET_ONE_X, robot.TARGET_ONE_Y,
		// robot.TARGET_TWO_X, robot.TARGET_TWO_Y);
		//

		// return to (0,0)

		Button.waitForAnyPress();
		System.exit(0);
	}
}