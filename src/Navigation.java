import lejos.nxt.NXTRegulatedMotor;

/*
 *  Group 21
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 */

public class Navigation {

	private Odometer odometer;
	private Robot robot = new Robot();
	private NXTRegulatedMotor leftMotor = robot.LEFT_MOTOR, rightMotor = robot.RIGHT_MOTOR;
	private final double DISTANCE_TOLERANCE = 0.5;
	private final double ANGLE_TOLERANCE = 0.25;
	private final int MOTOR_STRAIGHT = robot.MOTOR_STRAIGHT ;
	private final int MOTOR_ROTATE = robot.MOTOR_ROTATE;
	private final int MOTOR_SLOW = robot.MOTOR_SLOW;
	private final double RADIUS = robot.RADIUS;
	private final double WIDTH = robot.WIDTH;

	// constructor
	public Navigation(Odometer odo, USFilter filter) {
		this.odometer = odo;
	}

	public void travelTo(double x, double y) {

		// while x or y different from odometer's measured coordinates (ie. the
		// robot has not reached its final destination yet)
		while ((Math.abs(x - odometer.getX()) > DISTANCE_TOLERANCE || Math
				.abs(y - odometer.getY()) > DISTANCE_TOLERANCE)) {

			// calculate angle to point
			double minTheta = (Math.atan2(x - odometer.getX(),
					y - odometer.getY()))
					* (180.0 / Math.PI);
			// calculate distance to point
			double distance = Math.sqrt(Math.pow(x - odometer.getX(), 2)
					+ Math.pow(y - odometer.getY(), 2));

			// Make sure theta is positive
			if (minTheta < 0) {
				minTheta += 360;
			}

			// Turn to right angle first
			turnTo(minTheta);
			// Then move forward
			goForward(distance);
		}
		// stop the motors
		leftMotor.setSpeed(0);
		rightMotor.setSpeed(0);

	}

	public void goForward(double distance) {

		// drive straight for given distance
		leftMotor.setSpeed(MOTOR_STRAIGHT);
		rightMotor.setSpeed(MOTOR_STRAIGHT);

		int travelDistance = robot.convertDistance(RADIUS, distance);

		leftMotor.rotate(travelDistance, true);
		rightMotor.rotate(travelDistance, false);

	}
	
	public void goBackward(double distance) {

		// drive straight for given distance
		leftMotor.setSpeed(MOTOR_ROTATE);
		rightMotor.setSpeed(MOTOR_ROTATE);

		int travelDistance = robot.convertDistance(RADIUS, distance);

		leftMotor.rotate(-travelDistance, true);
		rightMotor.rotate(-travelDistance, false);

	}
	
	// method to rotate clockwise or counterclockwise
	public void rotate (boolean clockwise){
		leftMotor.setSpeed(MOTOR_SLOW);
		rightMotor.setSpeed(MOTOR_SLOW);
		if (clockwise){
			leftMotor.forward();
			rightMotor.backward();
		} else {
			leftMotor.backward();
			rightMotor.forward();
		}
	}
	
	// stop motors
	public void stop(){
		leftMotor.setSpeed(0);
		rightMotor.setSpeed(0);
	}


	public void turnTo(double theta) {

		leftMotor.setSpeed(MOTOR_ROTATE);
		rightMotor.setSpeed(MOTOR_ROTATE);

		// relative angle robot has to rotate by
		double errorTheta = theta - odometer.getTheta();

		while (Math.abs(errorTheta % 360) > ANGLE_TOLERANCE) {

			errorTheta = theta - odometer.getTheta();

			if (errorTheta < -180.0) {
				// turn right
				leftMotor
						.rotate(robot.convertAngle(RADIUS, WIDTH,
								360 - Math.abs(errorTheta)), true);
				rightMotor
						.rotate(-robot.convertAngle(RADIUS, WIDTH,
								360 - Math.abs(errorTheta)), false);
			} else if (errorTheta < 0.0) {
				// turn left
				leftMotor.rotate(
						-robot.convertAngle(RADIUS, WIDTH, Math.abs(errorTheta)),
						true);
				rightMotor.rotate(
						robot.convertAngle(RADIUS, WIDTH, Math.abs(errorTheta)),
						false);
			} else if (errorTheta > 180.0) {
				// turn left
				leftMotor.rotate(
						-robot.convertAngle(RADIUS, WIDTH, 360 - errorTheta), true);
				rightMotor.rotate(
						robot.convertAngle(RADIUS, WIDTH, 360 - errorTheta), false);
			} else {
				// turn right
				leftMotor.rotate(robot.convertAngle(RADIUS, WIDTH, errorTheta), true);
				rightMotor.rotate(-robot.convertAngle(RADIUS, WIDTH, errorTheta),
						false);
			}

		}

	}

}
