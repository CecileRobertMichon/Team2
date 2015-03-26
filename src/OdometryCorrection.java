/*
 *  Team 2
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 *  Derek Yu - 260570997
 *  Ajan Ahmed - 260509046
 *  Georges Assouad - 260567730
 *  Chaohan Wang - 260516712
 */

import lejos.nxt.Sound;

public class OdometryCorrection extends Thread {

	private Odometer odometer;
	private Robot robot;
	private double xLS, yLS, ex, ey;

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

				xLS = odometer.getX()
						+ (-robot.LIGHT_SENSOR_DISTANCE * Math.sin(Math
								.toRadians(odometer.getTheta())));
				yLS = odometer.getY()
						+ (-robot.LIGHT_SENSOR_DISTANCE * Math.cos(Math
								.toRadians(odometer.getTheta())));

				if (xLS % robot.TILE_LENGTH < robot.HALF_TILE) {
					ex = xLS % robot.TILE_LENGTH;
				} else {
					ex = (xLS % robot.TILE_LENGTH) - robot.TILE_LENGTH;
				}
				if (yLS % robot.TILE_LENGTH < robot.HALF_TILE) {
					ey = yLS % robot.TILE_LENGTH;
				} else {
					ey = (yLS % robot.TILE_LENGTH) - robot.TILE_LENGTH;
				}

				// if ((Math.min(Ex, Ey) / Math.max(Ex, Ey) < 0.8) &&
				// Math.sqrt(Ex*Ex + Ey*Ey) > 2.0) {
				if (ex < ey) {
					odometer.setX(odometer.getX() - ex);
				} else {
					odometer.setY(odometer.getY() - ey);
				}

				// this ensure the odometry correction occurs only once every
				// period

			}
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

}