/*
 *  Group 21
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 */

import java.util.ArrayList;

import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Sound;

public class LightLocalizer {

	private Odometer odo;
	private Navigation nav;
	private Robot robot;
	private ColorSensor ls;
	private int light;
	private ArrayList<Double> lineAngles = new ArrayList<Double>();
	private int counter = 0;

	public LightLocalizer(Odometer odo, Navigation nav) {
		this.odo = odo;
		this.robot = new Robot();
		this.ls = robot.COLOR_SENSOR;
		this.nav = nav;
		// turn on the light
		ls.setFloodlight(true);
	}

	public void doLocalization() {
		// drive to location listed in tutorial
		nav.turnTo(45);
		findLine();

		// Go backwards
		nav.goBackward(13);

		// start rotating and clock all 4 gridlines
		nav.rotate(true);
		// rotate 360 degrees
		nav.turnTo(1);
		robot.LEFT_MOTOR.rotate(
				robot.convertAngle(robot.RADIUS, robot.WIDTH, 360), true);
		robot.RIGHT_MOTOR.rotate(
				-robot.convertAngle(robot.RADIUS, robot.WIDTH, 360), true);
		int previousLightValue = ls.getNormalizedLightValue();
		light = ls.getNormalizedLightValue();
		while (robot.LEFT_MOTOR.isMoving()) {

			light = ls.getNormalizedLightValue();
			// record line angles and count lines crossed
			if (previousLightValue - light > robot.LIGHTSENSOR_THRESHOLD) {
				Sound.beep();
				lineAngles.add(odo.getTheta());
				counter++;
				
				// wait to avoid seeing the same line twice
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
				}
			}
		}
		nav.stop();

		double thetaX1 = lineAngles.get(0);
		double thetaY1 = lineAngles.get(1);
		double thetaX2 = lineAngles.get(2);
		double thetaY2 = lineAngles.get(3);

		//LCD.drawString("1 : " + thetaX1, 0, 1);
		//LCD.drawString("2 : " + thetaY1, 0, 2);
		//LCD.drawString("3 : " + thetaX2, 0, 3);
		//LCD.drawString("4 : " + thetaY2, 0, 4);

		if (thetaY2 < 20) {
			thetaY2 += 360;
		}

		// set x, y and theta to actual values using tutorial formulas
		odo.setX(-robot.LIGHT_SENSOR_DISTANCE
				* Math.cos(Math.toRadians(thetaY2 - thetaY1) / 2));
		odo.setY(-robot.LIGHT_SENSOR_DISTANCE
				* Math.cos(Math.toRadians(thetaX2 - thetaX1) / 2));

		double deltaTheta = 90 - (thetaY2 - 180) + ((thetaY2 - thetaY1) / 2);
		odo.setTheta(odo.getTheta() + deltaTheta);
	}

	// helper method to find first line ahead
	private void findLine() {
		int previousLightValue = ls.getNormalizedLightValue();
		this.light = ls.getNormalizedLightValue();

		// Go forward until sensor detects line
		while (previousLightValue - light < robot.LIGHTSENSOR_THRESHOLD) {
			robot.LEFT_MOTOR.setSpeed(robot.MOTOR_STRAIGHT);
			robot.RIGHT_MOTOR.setSpeed(robot.MOTOR_STRAIGHT);
			robot.RIGHT_MOTOR.forward();
			robot.LEFT_MOTOR.forward();
			light = ls.getNormalizedLightValue();
		}

		// found first line, stop robot
		nav.stop();
	}
}