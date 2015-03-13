import lejos.nxt.Sound;

/* 
 * OdometryCorrection.java
 */

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
				if (directionNorthSouth(odometer.getTheta())) {
					correctY();

					// if vertical line
				} else {
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