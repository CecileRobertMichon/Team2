import lejos.nxt.Motor;

/*
 *  Group 21
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 */

public class AvoidObstacle {
	Navigation nav;
	Odometer odo;
	USFilter filter;
	Robot robot;

	private int distance;

	public AvoidObstacle(USFilter filter, Navigation nav) {
		this.filter = filter;
		this.nav = nav;
		this.odo = new Odometer();
		this.robot = new Robot();
	}

	// Too close
	public void avoid() {

		// turn 90 degrees clockwise
		robot.LEFT_MOTOR.setSpeed(robot.MOTOR_ROTATE);
		robot.RIGHT_MOTOR.setSpeed(robot.MOTOR_ROTATE);

		robot.LEFT_MOTOR.rotate(robot.convertAngle(2.118, 15.0785, 90.0), true);
		robot.RIGHT_MOTOR.rotate(-robot.convertAngle(2.118, 15.0785, 90.0),
				false);

		// rotate sensor to an angle of 90 degrees
		Motor.B.rotate(-90);

		// go forward
		robot.LEFT_MOTOR.forward();
		robot.RIGHT_MOTOR.forward();

		// Follow the obstacle for a 10 seconds
		long start_time = System.currentTimeMillis();
		long wait_time = 10000;
		long end_time = start_time + wait_time;

		while (System.currentTimeMillis() < end_time) {

			this.distance = filter.getMedianDistance();

			// Follow the obstacle using BANG-BANG style

			// calculate deviation from wanted distance
			int delta = distance - robot.BANDCENTER;

			// Within tolerance
			if (Math.abs(delta) <= robot.BANDWIDTH) {
				// go straight
				robot.RIGHT_MOTOR.setSpeed(robot.MOTOR_STRAIGHT);
			}

			// Too far
			else if (delta > robot.BANDWIDTH) {
				// turn right by accelerating right motor
				robot.RIGHT_MOTOR.setSpeed(robot.MOTOR_FAST);
			}

			// Too close
			else if (delta < robot.BANDWIDTH) {
				// turn left by decelerating right motor
				robot.RIGHT_MOTOR.setSpeed(robot.MOTOR_SLOW);
			}

		}
		Motor.B.rotate(90);
		// stop motors
		robot.RIGHT_MOTOR.setSpeed(0);
		robot.LEFT_MOTOR.setSpeed(0);
	}
}
