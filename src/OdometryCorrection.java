/*
 *  Team 2
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 *  Derek Yu - 260570997
 *  Ajan Ahmed - 260509046
 *  Georges Assouad - 260567730
 *  Chaohan Wang - 260516712
 */

import lejos.nxt.LCD;
import lejos.nxt.Sound;

public class OdometryCorrection extends Thread {

	private Odometer odometer;
	private Robot robot;

	// constructor
	public OdometryCorrection(Odometer odometer) {
		this.odometer = odometer;
		this.robot = new Robot();
	}

	// run method (required for Thread)
	public void run() {
		long correctionStart, correctionEnd;
		int previousLight = robot.COLOR_SENSOR.getNormalizedLightValue();

		while (true) {
			correctionStart = System.currentTimeMillis();

			// put your correction code here
			int light = robot.COLOR_SENSOR.getNormalizedLightValue();

			// LCD.drawString("Light : " + light, 0, 5);
			if (previousLight - light > robot.LIGHTSENSOR_THRESHOLD) {

				Sound.beep();

				// if horizontal line
				if (isLine(odometer.getY(), false)) {
					LCD.drawString("Correcting Y", 0, 1);
					// correctY();

					// if vertical line
				}
				if (isLine(odometer.getX(), true)) {
					LCD.drawString("Correcting X", 0, 1);
					// correctX();
				}

			}

			// this ensure the odometry correction occurs only once every period
			correctionEnd = System.currentTimeMillis();
			if (correctionEnd - correctionStart < robot.CORRECTION_PERIOD) {
				try {
					Thread.sleep(robot.CORRECTION_PERIOD
							- (correctionEnd - correctionStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometry correction will be
					// interrupted by another thread
				}
			}
		}
	}

	// returns true if the robot is following the y direction, false if
	// following the x direction
	private boolean directionNorthSouth(double theta) {
		if (Math.sin(Math.toRadians(theta)) < 0.5
				&& Math.sin(Math.toRadians(theta)) > -0.5) {
			// The line crossed is a Y line
			return true;
		} else {
			// The line crossed is a X line
			return false;
		}
	}

	private boolean isLine(double var, boolean isX) {
		if (isX) {
			var = var - robot.LIGHT_SENSOR_DISTANCE
					* Math.sin(Math.toRadians(odometer.getTheta()));
		} else {
			var = var - robot.LIGHT_SENSOR_DISTANCE
					* Math.cos(Math.toRadians(odometer.getTheta()));
		}
		double remainder = var % robot.TILE_LENGTH;
		if (remainder < 5 || remainder > robot.TILE_LENGTH - 5) {
			return true;
		} else {
			return false;
		}
	}

	private void correctX() {
		// calculate distance from closest line
		double offset = robot.LIGHT_SENSOR_DISTANCE
				* Math.sin(Math.toRadians(odometer.getTheta()));
		// Add the line's distance from 0 to the offset
		double realX = odometer.getX() - (odometer.getX() % robot.TILE_LENGTH)
				+ robot.HALF_TILE + offset;
		;
		odometer.setX(realX);
	}

	private void correctY() {
		// calculate distance from closest line
		double offset = robot.LIGHT_SENSOR_DISTANCE
				* Math.cos(Math.toRadians(odometer.getTheta()));
		// Add the line's distance from 0 to the offset
		double realY = odometer.getY() - (odometer.getY() % robot.TILE_LENGTH)
				+ robot.HALF_TILE + offset;
		;
		odometer.setY(realY);
	}

}