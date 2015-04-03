
/*
 *  Team 2
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 *  Derek Yu - 260570997
 *  Ajan Ahmed - 260509046
 *  Georges Assouad - 260567730
 *  Chaohan Wang - 260516712
 */

public class Odometer extends Thread {
	// robot position
	private double x, y, theta;

	// Tachometer count
	private int tachoLeft, tachoRight;

	// Robot object for constants
	private Robot robot;

	// lock object for mutual exclusion
	public Object lock;

	// default constructor
	public Odometer() {
		x = 0.0;
		y = 0.0;
		theta = 0.0;
		lock = new Object();
		robot = new Robot();
	}

	// run method (required for Thread)
	public void run() {
		long updateStart, updateEnd;

		while (true) {
			updateStart = System.currentTimeMillis();

			// Get each motor's tachometer count
			int newTachoLeft = robot.LEFT_MOTOR.getTachoCount();
			int newTachoRight = robot.RIGHT_MOTOR.getTachoCount();

			// calculate change in tachometer
			int deltaTachoLeft = newTachoLeft - tachoLeft;
			int deltaTachoRight = newTachoRight - tachoRight;

			tachoLeft = newTachoLeft;
			tachoRight = newTachoRight;

			// Calculate left and right arc distance according to formula in lab
			// tutorial
			double leftArcDistance = Math.toRadians(deltaTachoLeft
					* robot.RADIUS);
			double rightArcDistance = Math.toRadians(deltaTachoRight
					* robot.RADIUS);

			double deltaTheta = ((leftArcDistance - rightArcDistance) / robot.WIDTH);
			double deltaArcLength = (leftArcDistance + rightArcDistance) / 2.0;

			double tempTheta = Math.toDegrees(deltaTheta) + getTheta();
			if (tempTheta < 0) {
				tempTheta += 360;
			}

			synchronized (lock) {
				// set the new values for x, y and theta
				setX(getX() + deltaArcLength
						* Math.sin(Math.toRadians(getTheta()) + deltaTheta / 2));
				setY(getY() + deltaArcLength
						* Math.cos(Math.toRadians(getTheta()) + deltaTheta / 2));
				setTheta(tempTheta % 360);
			}

			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < robot.ODOMETER_PERIOD) {
				try {
					Thread.sleep(robot.ODOMETER_PERIOD
							- (updateEnd - updateStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometer will be interrupted by
					// another thread
				}
			}
		}
	}

	// accessors
	public void getPosition(double[] pos) {
		synchronized (lock) {
			pos[0] = x;
			pos[1] = y;
			pos[2] = theta;
		}
	}

	public double getX() {
		double result;

		synchronized (lock) {
			result = x;
		}

		return result;
	}

	public double getY() {
		double result;

		synchronized (lock) {
			result = y;
		}

		return result;
	}

	public double getTheta() {
		double result;

		synchronized (lock) {
			result = theta;
		}

		return result;
	}

	// mutators
	public void setPosition(double[] pos, boolean[] update) {
		synchronized (lock) {
			if (update[0])
				x = pos[0];
			if (update[1])
				y = pos[1];
			if (update[2])
				theta = pos[2];
		}
	}

	public void setX(double x) {
		synchronized (lock) {
			this.x = x;
		}
	}

	public void setY(double y) {
		synchronized (lock) {
			this.y = y;
		}
	}

	public void setTheta(double theta) {
		synchronized (lock) {
			this.theta = theta;
		}
	}
}