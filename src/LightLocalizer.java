/*
 *  Group 21
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 */

import java.util.ArrayList;

import lejos.nxt.ColorSensor;

public class LightLocalizer {

	private Odometer odo;
	private Navigation nav;
	private Robot robot;
	private ColorSensor ls;
	private double light;
	private ArrayList<Double> lineAngles = new ArrayList<Double>();
	private int counter = 0;
	private double negativeYTheta;

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
		nav.goBackward(12);

		// start rotating and clock all 4 gridlines
		nav.rotate(true);
		// rotate 360 degrees
		nav.turnTo(1);
		robot.LEFT_MOTOR.rotate(
				robot.convertAngle(robot.RADIUS, robot.WIDTH, 360), true);
		robot.RIGHT_MOTOR.rotate(
				-robot.convertAngle(robot.RADIUS, robot.WIDTH, 360), true);
		int previousLightValue = ls.getNormalizedLightValue();
		while (robot.LEFT_MOTOR.isMoving()) {
			// wait to avoid seeing the same line twice
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
			// record line angles and count lines crossed
			light = ls.getNormalizedLightValue();
			if (previousLightValue - light > robot.LIGHTSENSOR_THRESHOLD) {
				lineAngles.add(odo.getTheta());
				counter++;
				// save the angle of the last y line crossed
				if (counter == 4) {
					negativeYTheta = odo.getTheta();
				}
			}

		}
		nav.stop();

		// set x, y and theta to actual values using tutorial formulas
		odo.setY(-robot.LIGHT_SENSOR_DISTANCE
				* Math.cos(Math.toRadians(lineAngles.get(2) - lineAngles.get(0)) / 2));
		odo.setX(-robot.LIGHT_SENSOR_DISTANCE
				* Math.cos(Math.toRadians(lineAngles.get(3) - lineAngles.get(1)) / 2));

		double deltaTheta = 90 - (negativeYTheta - 180)
				+ ((lineAngles.get(3) - lineAngles.get(1)) / 2);
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