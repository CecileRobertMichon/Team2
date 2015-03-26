import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.NXTRegulatedMotor;
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

public class Navigation {

	private Odometer odometer;
	private AvoidObstacle obs;
	private USFilter filterTravel;
	private USFilterLeft filterAvoid;
	private Robot robot = new Robot();
	private NXTRegulatedMotor leftMotor = robot.LEFT_MOTOR,
			rightMotor = robot.RIGHT_MOTOR;
	private final int MOTOR_STRAIGHT = robot.MOTOR_STRAIGHT;
	private final int MOTOR_ROTATE = robot.MOTOR_ROTATE;
	private final double RADIUS = robot.RADIUS;
	private final double WIDTH = robot.WIDTH;
	private boolean isLocalizing = true;

	// constructor
	public Navigation(Odometer odo, USFilter filterStraight,
			USFilterLeft filterLeft) {
		this.odometer = odo;
		this.filterTravel = filterStraight;
		this.filterAvoid = filterLeft;
		this.obs = new AvoidObstacle(filterAvoid);
	}

	public void travelTo(double x, double y) {

		x = x * robot.TILE_LENGTH;
		y = y * robot.TILE_LENGTH;

		// while x or y different from odometer's measured coordinates (ie. the
		// robot has not reached its final destination yet)
		while ((Math.abs(x - odometer.getX()) > robot.DISTANCE_TOLERANCE || Math
				.abs(y - odometer.getY()) > robot.DISTANCE_TOLERANCE)) {

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

		int wallDist;

		// drive straight for given distance
		leftMotor.setSpeed(MOTOR_STRAIGHT);
		rightMotor.setSpeed(MOTOR_STRAIGHT);

		int travelDistance = robot.convertDistance(RADIUS, distance);

		leftMotor.rotate(travelDistance, true);
		rightMotor.rotate(travelDistance, true);

		// continuously check for US data to detect obstacle while robot is
		// going forward
		while (rightMotor.isMoving()) {
			wallDist = filterTravel.getMedianDistance();
			// call method to avoid obstacle if object is detected
			if (wallDist < 15 && !isLocalizing) {
				obs.avoid();
			}
		}

	}

	public void goBackward(double distance) {

		// drive straight for given distance
		leftMotor.setSpeed(MOTOR_STRAIGHT);
		rightMotor.setSpeed(MOTOR_STRAIGHT);

		int travelDistance = robot.convertDistance(RADIUS, distance);

		leftMotor.rotate(-travelDistance, true);
		rightMotor.rotate(-travelDistance, false);

	}

	// method to rotate clockwise or counterclockwise
	public void rotate(boolean clockwise) {
		leftMotor.setSpeed(MOTOR_ROTATE);
		rightMotor.setSpeed(MOTOR_ROTATE);
		if (clockwise) {
			leftMotor.forward();
			rightMotor.backward();
		} else {
			leftMotor.backward();
			rightMotor.forward();
		}
	}

	// stop motors
	public void stop() {
		leftMotor.setSpeed(0);
		rightMotor.setSpeed(0);
	}

	public void turnTo(double theta) {

		boolean temp = this.isLocalizing;
		this.setIsLocalizing(false);

		leftMotor.setSpeed(MOTOR_ROTATE);
		rightMotor.setSpeed(MOTOR_ROTATE);

		// relative angle robot has to rotate by
		double errorTheta = theta - odometer.getTheta();

		while (Math.abs(errorTheta % 360) > robot.ANGLE_TOLERANCE) {

			errorTheta = theta - odometer.getTheta();

			if (errorTheta < -180.0) {
				// turn right
				leftMotor.rotate(
						robot.convertAngle(RADIUS, WIDTH,
								360 - Math.abs(errorTheta)), true);
				rightMotor.rotate(
						-robot.convertAngle(RADIUS, WIDTH,
								360 - Math.abs(errorTheta)), false);
			} else if (errorTheta < 0.0) {
				// turn left
				leftMotor.rotate(-robot.convertAngle(RADIUS, WIDTH,
						Math.abs(errorTheta)), true);
				rightMotor
						.rotate(robot.convertAngle(RADIUS, WIDTH,
								Math.abs(errorTheta)), false);
			} else if (errorTheta > 180.0) {
				// turn left
				leftMotor.rotate(
						-robot.convertAngle(RADIUS, WIDTH, 360 - errorTheta),
						true);
				rightMotor.rotate(
						robot.convertAngle(RADIUS, WIDTH, 360 - errorTheta),
						false);
			} else {
				// turn right
				leftMotor.rotate(robot.convertAngle(RADIUS, WIDTH, errorTheta),
						true);
				rightMotor.rotate(
						-robot.convertAngle(RADIUS, WIDTH, errorTheta), false);
			}
		}

		this.setIsLocalizing(temp);

	}

	public void setIsLocalizing(boolean localization) {
		this.isLocalizing = localization;
	}

}
