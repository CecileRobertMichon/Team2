
/*
 *  Group 21
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 */

public class Navigation {

	private Odometer odometer;
	private AvoidObstacle obs;
	private USFilter filter;
	private Robot robot;
	private final double DISTANCE_TOLERANCE = 0.5;
	private final double ANGLE_TOLERANCE = 1;
	private boolean running = false;

	// constructor
	public Navigation(Odometer odo, USFilter filter) {
		this.odometer = odo;
		this.obs = new AvoidObstacle(filter, this);
		this.filter = filter;
		this.robot = new Robot();
	}

	public void travelTo(double x, double y) {
		
		//x = x * 30.48;
		//y = y * 30.48;
		running = true;

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
		robot.LEFT_MOTOR.setSpeed(0);
		robot.RIGHT_MOTOR.setSpeed(0);

		running = false;
	}

	public void goForward(double distance) {

		running = true;
		int wallDist;

		// drive straight for given distance
		robot.LEFT_MOTOR.setSpeed(robot.MOTOR_STRAIGHT);
		robot.RIGHT_MOTOR.setSpeed(robot.MOTOR_STRAIGHT);

		int travelDistance = robot.convertDistance(robot.RADIUS, distance);

		robot.LEFT_MOTOR.rotate(travelDistance, true);
		robot.RIGHT_MOTOR.rotate(travelDistance, true);

		// continuously check for US data to detect obstacle while robot is
		// going forward
		while (robot.RIGHT_MOTOR.isMoving()) {
			wallDist = filter.getMedianDistance();
			// call method to avoid obstacle if object is detected
			if (wallDist < 15) {
				obs.avoid();
			}
		}
		running = false;

	}
	
	public void goBackward(double distance) {

		// drive straight for given distance
		robot.LEFT_MOTOR.setSpeed(robot.MOTOR_ROTATE);
		robot.RIGHT_MOTOR.setSpeed(robot.MOTOR_ROTATE);

		int travelDistance = robot.convertDistance(robot.RADIUS, distance);

		robot.LEFT_MOTOR.rotate(-travelDistance, true);
		robot.RIGHT_MOTOR.rotate(-travelDistance, false);

	}
	
	// method to rotate clockwise or counterclockwise
	public void rotate (boolean clockwise){
		robot.LEFT_MOTOR.setSpeed(robot.MOTOR_SLOW);
		robot.RIGHT_MOTOR.setSpeed(robot.MOTOR_SLOW);
		if (clockwise){
			robot.LEFT_MOTOR.forward();
			robot.RIGHT_MOTOR.backward();
		} else {
			robot.LEFT_MOTOR.backward();
			robot.RIGHT_MOTOR.forward();
		}
	}
	
	// stop motors
	public void stop(){
		robot.LEFT_MOTOR.setSpeed(0);
		robot.RIGHT_MOTOR.setSpeed(0);
	}


	public void turnTo(double theta) {
		running = true;
		robot.LEFT_MOTOR.setSpeed(robot.MOTOR_ROTATE);
		robot.RIGHT_MOTOR.setSpeed(robot.MOTOR_ROTATE);

		// relative angle robot has to rotate by
		double errorTheta = theta - odometer.getTheta();

		while (Math.abs(errorTheta % 360) > ANGLE_TOLERANCE) {

			errorTheta = theta - odometer.getTheta();

			if (errorTheta < -180.0) {
				// turn right
				robot.LEFT_MOTOR
						.rotate(robot.convertAngle(robot.RADIUS, robot.WIDTH,
								360 - Math.abs(errorTheta)), true);
				robot.RIGHT_MOTOR
						.rotate(-robot.convertAngle(robot.RADIUS, robot.WIDTH,
								360 - Math.abs(errorTheta)), false);
			} else if (errorTheta < 0.0) {
				// turn left
				robot.LEFT_MOTOR.rotate(
						-robot.convertAngle(robot.RADIUS, robot.WIDTH, Math.abs(errorTheta)),
						true);
				robot.RIGHT_MOTOR.rotate(
						robot.convertAngle(robot.RADIUS, robot.WIDTH, Math.abs(errorTheta)),
						false);
			} else if (errorTheta > 180.0) {
				// turn left
				robot.LEFT_MOTOR.rotate(
						-robot.convertAngle(robot.RADIUS, robot.WIDTH, 360 - errorTheta), true);
				robot.RIGHT_MOTOR.rotate(
						robot.convertAngle(robot.RADIUS, robot.WIDTH, 360 - errorTheta), false);
			} else {
				// turn right
				robot.LEFT_MOTOR.rotate(robot.convertAngle(robot.RADIUS, robot.WIDTH, errorTheta), true);
				robot.RIGHT_MOTOR.rotate(-robot.convertAngle(robot.RADIUS, robot.WIDTH, errorTheta),
						false);
			}

		}
		running = false;
	}

	public boolean isNavigating() {
		return running;
	}
	
}
