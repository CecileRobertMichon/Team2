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

				// Y North
				if (isDirectionNorth()
						&& isLine(odometer.getY() - robot.LIGHT_SENSOR_DISTANCE
								* Math.cos(Math.toRadians(odometer.getTheta())))) {
					Sound.beep();
					correctY();
				}
				// X East
				else if (isDirectionEast()
						&& isLine(odometer.getX() - robot.LIGHT_SENSOR_DISTANCE
								* Math.sin(Math.toRadians(odometer.getTheta())))) {
					Sound.buzz();
					correctX();
				}
				// Y South
				else if (isLine(odometer.getY() + robot.LIGHT_SENSOR_DISTANCE
						* Math.cos(Math.toRadians(odometer.getTheta())))) {
					Sound.beep();
					correctY();
				}
				// X West
				else if (isLine(odometer.getX() + robot.LIGHT_SENSOR_DISTANCE
								* Math.sin(Math.toRadians(odometer.getTheta())))) {
					Sound.buzz();
					correctX();
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
			//previousLight = light;
		}
	}

	private boolean isLine(double var) {
		if (var % 30.48 < 10 || var % 30.48 > 20) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isDirectionNorth() {
		if (odometer.getTheta() > 270 || odometer.getTheta() < 90) {
			// The robot is going up
			return true;
		} else {
			return false;
		}
	}

	private boolean isDirectionEast() {
		if (odometer.getTheta() < 180) {
			// The robot is going right
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
		// realX = ((int) (odometer.getX() / 30.48)) * 30.48 +
		double realX = odometer.getX() - (odometer.getX() % robot.TILE_LENGTH)
				+ offset;
		odometer.setX(realX);
	}

	private void correctY() {
		// calculate distance from closest line
		double offset = robot.LIGHT_SENSOR_DISTANCE
				* Math.cos(Math.toRadians(odometer.getTheta()));
		// Add the line's distance from 0 to the offset
		double realY = odometer.getY() - (odometer.getY() % robot.TILE_LENGTH)
				+ offset;
		odometer.setY(realY);
	}

}