import lejos.nxt.ColorSensor;
import lejos.nxt.Sound;

/*
 *  Team 2
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 *  Derek Yu - 260570997
 *  Ajan Ahmed - 260509046
 *  Georges Assouad - 260567730
 *  Chaohan Wang - 260516712
 */

public class OdometryCorrection extends Thread {

	private Odometer odometer;
	private Robot robot;
	private Navigation nav;
	private boolean isCorrectingAngle;
	private double xLS, yLS, ex, ey;

	// constructor
	public OdometryCorrection(Odometer odometer, Navigation nav) {
		this.odometer = odometer;
		this.robot = new Robot();
		this.nav = nav;
		this.isCorrectingAngle = false;
	}

	// run method (required for Thread)
	public void run() {
		long correctionStart, correctionEnd;
		int previousLight = robot.COLOR_SENSOR.getNormalizedLightValue();

		while (true) {
			if (!isCorrectingAngle) {
				correctionStart = System.currentTimeMillis();

				// put your correction code here
				int light = robot.COLOR_SENSOR.getNormalizedLightValue();

				// detect if crossed line
				if (previousLight - light > robot.LIGHTSENSOR_THRESHOLD) {
					Sound.beep();
					// calculate light sensor expected position
					xLS = odometer.getX()
							+ (-robot.LIGHT_SENSOR_DISTANCE * Math.sin(Math
									.toRadians(odometer.getTheta())));
					yLS = odometer.getY()
							+ (-robot.LIGHT_SENSOR_DISTANCE * Math.cos(Math
									.toRadians(odometer.getTheta())));

					// calculate light sensor position x and y error
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

					// correct the smallest error
					if ((Math.min(ex, ey) / Math.max(ex, ey) < 0.8)
							&& Math.sqrt(ex * ex + ey * ey) > 2.0) {
						if (ex < ey) {
							odometer.setX(odometer.getX() - ex);
						} else {
							odometer.setY(odometer.getY() - ey);
						}
					}
				}

				// this ensure the odometry correction occurs only once every
				// period
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

	public void correctAngle() {
		this.isCorrectingAngle = true;
		nav.goBackward(robot.LIGHT_SENSOR_DISTANCE);
		int previousLight = robot.COLOR_SENSOR.getNormalizedLightValue();
		int light = robot.COLOR_SENSOR.getNormalizedLightValue();
		while (previousLight - light < robot.LIGHTSENSOR_THRESHOLD) {
			nav.rotate(true);
			light = robot.COLOR_SENSOR.getNormalizedLightValue();
		}
		nav.stop();
		if (odometer.getTheta() > 340 || odometer.getTheta() < 20) {
			// facing north
			odometer.setTheta(0);
		} else if (odometer.getTheta() > 70 && odometer.getTheta() < 110) {
			// facing east
			odometer.setTheta(90);
		} else if (odometer.getTheta() > 160 && odometer.getTheta() < 200) {
			// facing south
			odometer.setTheta(180);
		} else if (odometer.getTheta() > 250 && odometer.getTheta() < 290) {
			// facing west
			odometer.setTheta(270);
		}
		this.isCorrectingAngle = false;
	}

	public void findLine() {
		this.isCorrectingAngle = true;
		int light = robot.COLOR_SENSOR.getNormalizedLightValue();
		int previousLight = robot.COLOR_SENSOR.getNormalizedLightValue();

		// Go forward until sensor detects line
		while (previousLight - light < robot.LIGHTSENSOR_THRESHOLD) {
			robot.LEFT_MOTOR.setSpeed(robot.MOTOR_ROTATE);
			robot.RIGHT_MOTOR.setSpeed(robot.MOTOR_ROTATE);
			robot.RIGHT_MOTOR.forward();
			robot.LEFT_MOTOR.forward();
			light = robot.COLOR_SENSOR.getNormalizedLightValue();
		}

		// found first line, stop robot
		Sound.beep();
		nav.stop();
		this.isCorrectingAngle = false;
	}

}