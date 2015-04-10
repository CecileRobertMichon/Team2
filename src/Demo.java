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
		USFilter filterStraight = new USFilter(robot.US);
		USFilter filterLeft = new USFilter(robot.US2);
		Navigation nav = new Navigation(odo, filterStraight, filterLeft);
		OdometryDisplay lcd = new OdometryDisplay(odo, filterStraight);
		OdometryCorrection correct = new OdometryCorrection(odo, nav);
		Launcher launcher = new Launcher();
		LauncherPositioning position = new LauncherPositioning(odo, nav,
				launcher);

		odo.start();
		lcd.start();
		filterStraight.start();
		filterLeft.start();

		// set navigation to localization mode - no obstacle detection
		nav.setIsLocalizing(true);

		// perform the ultrasonic localization
		USLocalizer usl = new USLocalizer(odo, nav, filterStraight, filterLeft);
		usl.doLocalization();

		// when done travel to (0,0) and turn to 0 degrees
		nav.travelTo(0, 0);
		nav.turnTo(30);

		LightLocalizer lsl = new LightLocalizer(odo, nav);
		lsl.doLocalization();

		nav.travelTo(0, 0);
		nav.turnTo(0);
		Sound.beep();

		correct.start();

		// travel to the right bottom corner of the shooting area while avoiding
		// obstacles

		if (robot.MAP_NUMBER == 1) {

			nav.travelTo(0, 2);
			nav.travelTo(4.5, 2);
			nav.travelTo(4.5, -0.5);
			nav.travelTo(7.5, -0.5);
			nav.travelTo(7.5, 0.5);
			nav.travelTo(9, 0.5);
			nav.travelTo(9, 2);

			// optional light localization
			nav.turnTo(20);
			lsl = new LightLocalizer(odo, nav);
			lsl.doLocalization();
			odo.setX(9 * robot.TILE_LENGTH + odo.getX());
			odo.setY(2 * robot.TILE_LENGTH + odo.getY());
			nav.travelTo(9, 2);
			nav.turnTo(0);
			Sound.beep();

			nav.travelTo(9, 2.5);
			nav.travelTo(10.5, 2.5);
			nav.travelTo(10.5, 4.7);

			// correct the angle
			correct.findLine();
			correct.correctAngle();
			
			nav.travelTo(10.5, 8);

		} else if (robot.MAP_NUMBER == 2) {

			nav.travelTo(2.5, 0);
			nav.travelTo(2.5, 6);
			nav.travelTo(2, 6);

			// optional light localization
			nav.turnTo(20);
			lsl = new LightLocalizer(odo, nav);
			lsl.doLocalization();
			odo.setX(2 * robot.TILE_LENGTH + odo.getX());
			odo.setY(6 * robot.TILE_LENGTH + odo.getY());
			nav.travelTo(2, 6);
			nav.turnTo(0);
			Sound.beep();

			nav.travelTo(1.5, 6);
			nav.travelTo(1.5, 8.5);
			nav.travelTo(-0.5, 8.5);
			nav.travelTo(-0.5, 10.5);
			nav.travelTo(4.7, 10.5);

			// correct the angle
			correct.findLine();
			correct.correctAngle();

			nav.travelTo(8, 10.5);

		} else {

			nav.travelTo(1.5, 0);
			nav.travelTo(1.5, 5);
			nav.travelTo(2.5, 5);
			nav.travelTo(2.5, 8);
			nav.travelTo(2, 8);

			// optional light localization
			 nav.turnTo(20);
			 lsl = new LightLocalizer(odo, nav);
			 lsl.doLocalization();
			 odo.setX(2 * robot.TILE_LENGTH + odo.getX());
			 odo.setY(8 * robot.TILE_LENGTH + odo.getY());
			 nav.travelTo(2, 8);
			 nav.turnTo(0);
			 Sound.beep();

			nav.travelTo(2, 7.5);
			nav.travelTo(-0.5, 7.5);
			nav.travelTo(-0.5, 9.5);
			nav.travelTo(0.5, 9.5);
			nav.travelTo(0.5, 10.5);
			nav.travelTo(4.7, 10.5);

			// correct the angle
			correct.findLine();
			correct.correctAngle();

			nav.travelTo(8, 10.5);

		}

		nav.travelTo(8, 8);
		nav.turnTo(20);

		// perform the light sensor localization
		lsl = new LightLocalizer(odo, nav);
		lsl.doLocalization();
		odo.setX(8 * robot.TILE_LENGTH + odo.getX());
		odo.setY(8 * robot.TILE_LENGTH + odo.getY());
		nav.travelTo(8, 8);
		nav.turnTo(0);
		Sound.beep();

		// go to shooting spot relative to target 1
		// shoot half the balls
		// go to shooting spot relative to target 2
		// shoot the other half
		position.targetAcquisition(robot.TARGET_ONE_X, robot.TARGET_ONE_Y,
				robot.TARGET_TWO_X, robot.TARGET_TWO_Y);

		// return to shooting area corner
		nav.travelTo(8, 8);
		nav.turnTo(20);
		// perform the light sensor localization
		lsl = new LightLocalizer(odo, nav);
		lsl.doLocalization();
		odo.setX(8 * robot.TILE_LENGTH + odo.getX());
		odo.setY(8 * robot.TILE_LENGTH + odo.getY());
		nav.travelTo(8, 8);
		nav.turnTo(0);
		Sound.beep();

		// return to (0,0)

		if (robot.MAP_NUMBER == 1) {

			nav.travelTo(10.5, 8);
			nav.travelTo(10.5, 5.3);

			// correct the angle
			correct.findLine();
			correct.correctAngle();

			nav.travelTo(10.5, 2.5);
			nav.travelTo(9, 2.5);
			nav.travelTo(9, 2);

			// optional light localization
			 nav.turnTo(20);
			 lsl = new LightLocalizer(odo, nav);
			 lsl.doLocalization();
			 odo.setX(9 * robot.TILE_LENGTH + odo.getX());
			 odo.setY(2 * robot.TILE_LENGTH + odo.getY());
			 nav.travelTo(9, 2);
			 nav.turnTo(0);
			 Sound.beep();

			nav.travelTo(9, 0.5);
			nav.travelTo(7.5, 0.5);
			nav.travelTo(7.5, -0.5);
			nav.travelTo(4.5, -0.5);
			nav.travelTo(4.5, 2);
			nav.travelTo(0, 2);
			nav.travelTo(0, 0);

		} else if (robot.MAP_NUMBER == 2) {

			nav.travelTo(8, 10.5);
			nav.travelTo(5.3, 10.5);

			// correct the angle
			correct.findLine();
			correct.correctAngle();

			nav.travelTo(-0.5, 10.5);
			nav.travelTo(-0.5, 8.5);
			nav.travelTo(1.5, 8.5);
			nav.travelTo(1.5, 6);
			nav.travelTo(2, 6);

			// optional light localization
			 nav.turnTo(20);
			 lsl = new LightLocalizer(odo, nav);
			 lsl.doLocalization();
			 odo.setX(2 * robot.TILE_LENGTH + odo.getX());
			 odo.setY(6 * robot.TILE_LENGTH + odo.getY());
			 nav.travelTo(2, 6);
			 nav.turnTo(0);
			 Sound.beep();

			nav.travelTo(2.5, 6);
			nav.travelTo(2.5, 0);
			nav.travelTo(0, 0);

		} else {

			nav.travelTo(8, 10.5);
			nav.travelTo(5.5, 10.5);
			
			// correct the angle
			correct.findLine();
			correct.correctAngle();

			nav.travelTo(0.5, 10.5);
			nav.travelTo(0.5, 9.5);
			nav.travelTo(-0.5, 9.5);
			nav.travelTo(-0.5, 7.5);
			nav.travelTo(2, 7.5);
			nav.travelTo(2, 8);

			// optional light localization
			 nav.turnTo(20);
			 lsl = new LightLocalizer(odo, nav);
			 lsl.doLocalization();
			 odo.setX(2 * robot.TILE_LENGTH + odo.getX());
			 odo.setY(8 * robot.TILE_LENGTH + odo.getY());
			 nav.travelTo(2, 8);
			 nav.turnTo(0);
			 Sound.beep();

			nav.travelTo(2.5, 8);
			nav.travelTo(2.5, 5);
			nav.travelTo(1.5, 5);
			nav.travelTo(1.5, 0);
			nav.travelTo(0, 0);

		}

		// localize at (0,0)
		nav.turnTo(20);

		lsl = new LightLocalizer(odo, nav);
		lsl.doLocalization();
		nav.travelTo(0, 0);
		nav.turnTo(0);
		Sound.buzz();

		Button.waitForAnyPress();
		System.exit(0);
	}
}