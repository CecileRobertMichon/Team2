/*
 *  Team 2
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 *  Derek Yu - 260570997
 *  Ajan Ahmed - 260509046
 *  Georges Assouad - 260567730
 *  Chaohan Wang - 260516712
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
	private int lineCounter;
	private ArrayList<Double> lineAngles = new ArrayList<Double>();

	public LightLocalizer(Odometer odo, Navigation nav) {
		this.odo = odo;
		this.robot = new Robot();
		this.ls = robot.COLOR_SENSOR;
		this.nav = nav;
		this.lineCounter = 0;
		// turn on the light
		ls.setFloodlight(true);
	}

	public void doLocalization() {

		// start rotating and clock all 4 gridlines
		nav.rotate(true);

		// rotate 360 degrees
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
				lineCounter++;
				lineAngles.add(odo.getTheta());

				// wait to avoid seeing the same line twice
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
				}
			}
		}
		nav.stop();

		if (lineCounter == 4) {
			double thetaX1 = lineAngles.get(0);
			double thetaY1 = lineAngles.get(1);
			double thetaX2 = lineAngles.get(2);
			double thetaY2 = lineAngles.get(3);

			if (thetaY2 < 20) {
				thetaY2 += 360;
			}

			// set x, y and theta to actual values using tutorial formulas
			odo.setX(-robot.LIGHT_SENSOR_DISTANCE
					* Math.cos(Math.toRadians(thetaY2 - thetaY1) / 2));
			odo.setY(-robot.LIGHT_SENSOR_DISTANCE
					* Math.cos(Math.toRadians(thetaX2 - thetaX1) / 2));

			double deltaTheta = 89 - (thetaY2 - 180)
					+ ((thetaY2 - thetaY1) / 2);
			odo.setTheta(odo.getTheta() + deltaTheta);
		}
	}
}