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

public class Demo {

	public static void main(String[] args) {

		// setup the odometer, correction, display, and ultrasonic and light
		// sensors
		Odometer odo = new Odometer();
		Robot robot = new Robot();
		CopyOfOdometryCorrection correct = new CopyOfOdometryCorrection(odo);
		USFilter filterStraight = new USFilter(robot.US);
		USFilter filterLeft = new USFilter(robot.US2);
		Navigation nav = new Navigation(odo, filterStraight, filterLeft);
		OdometryDisplay lcd = new OdometryDisplay(odo, filterStraight);
		Launcher launcher = new Launcher();
		LauncherPositioning position = new LauncherPositioning(odo, nav,
				launcher);

		odo.start();
		lcd.start();
		filterStraight.start();
		filterLeft.start();

		// Calibration Code - used to find exact radius and width values

		//robot.LEFT_MOTOR.setAcceleration(2000);
		//robot.RIGHT_MOTOR.setAcceleration(2000);
		//robot.LEFT_MOTOR.setSpeed(100);
		//robot.RIGHT_MOTOR.setSpeed(100);
		//robot.LEFT_MOTOR.rotate(1675, true);
		//robot.RIGHT_MOTOR.rotate(1675, false);
		//robot.LEFT_MOTOR.rotate(2832, true);
		//robot.RIGHT_MOTOR.rotate(-2832, false);
		

		// set navigation to localization mode - no obstacle detection
		nav.setIsLocalizing(true);

		// perform the ultrasonic localization
		// usl = new USLocalizer(odo,
		//	USLocalizer.LocalizationType.FALLING_EDGE, nav, filterStraight, filterLeft);
		//	usl.doLocalization();
			
		// when done travel to (0,0) and turn to 0 degrees
		// nav.travelTo(0, 0);
		// nav.turnTo(30);

		//LightLocalizer lsl = new LightLocalizer(odo, nav);
	//	lsl.doLocalization();

	//	nav.travelTo(0, 0);
	//	nav.turnTo(0);
	//	Sound.beep();

		// start obstacle detection
		//nav.setIsLocalizing(false);

		correct.start();

		// Square driver for testing
		
		nav.travelTo(1, 4); 
		nav.travelTo(2, 0); 
		nav.travelTo(0, 0);
		 

		// travel to the right bottom corner of the shooting area while avoiding
		// obstacles
	/*	nav.travelTo(-0.6, 2.5);
		nav.travelTo(-0.5, 5.7);
		nav.travelTo(2, 5.7);
		nav.travelTo(2, 6.2);
		nav.turnTo(20);
	*/	
		
		// perform the light sensor localization
		//lsl.doLocalization();
		// odo.setX(2*robot.TILE_LENGTH + odo.getX());
		// odo.setY(6*robot.TILE_LENGTH + odo.getY());
		// nav.travelTo(2, 6);
		// nav.turnTo(0);

		// go to shooting spot relative to target 1
		// shoot half the balls
		// go to shooting spot relative to target 2
		// shoot the other half

		/* **** UNCOMMENT THIS FOR LAUNCHER TEST **** */
		// position.targetAcquisition(robot.TARGET_ONE_X, robot.TARGET_ONE_Y,
		// robot.TARGET_TWO_X, robot.TARGET_TWO_Y);

		// return to (0,0)

		Button.waitForAnyPress();
		System.exit(0);
	}
}