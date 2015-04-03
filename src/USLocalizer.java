/*
 *  Team 2
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 *  Derek Yu - 260570997
 *  Ajan Ahmed - 260509046
 *  Georges Assouad - 260567730
 *  Chaohan Wang - 260516712
 */

public class USLocalizer {

	private int distance;
	private Odometer odo;
	private Navigation nav;
	private USFilter filter;
	private USFilter filterLeft;
	private Robot robot = new Robot();

	private final int NOISE = robot.NOISE;
	public final int WALL_CENTER = robot.WALL_CENTER;

	public USLocalizer(Odometer odo, Navigation nav,
			USFilter filter, USFilter filterLeft) {
		this.odo = odo;
		this.nav = nav;
		this.filter = filter;
		this.filterLeft = filterLeft;
	}

	public void doLocalization() {
		double angleA, angleB;

		// rotate the robot clockwise until it sees no wall
		awayFromWall(true);
		odo.setTheta(0);

		// keep rotating until the robot sees a wall, then latch the angle
		findWall(true);
		angleA = odo.getTheta();

		// switch direction and wait until it sees no wall
		awayFromWall(false);

		// keep rotating until the robot sees a wall, then latch the angle
		findWall(false);
		angleB = odo.getTheta();

		// angleA is clockwise from angleB, so assume the average of the
		// angles to the right of angleB is 45 degrees past 'north'
		double deltaTheta = calculateHeadingFalling(angleA, angleB);

		// update the odometer position
		nav.turnTo(-deltaTheta);
		odo.setPosition(new double[] { odo.getX(), odo.getY(), 0.0 },
				new boolean[] { true, true, true });
		nav.turnTo(-90);
		double initialX = filter.getMedianDistance()
				- (robot.TILE_LENGTH - robot.US_SENSOR_DISTANCE);
		double initialY = filterLeft.getMedianDistance()
				- (robot.TILE_LENGTH - robot.US_LEFT_SENSOR_DISTANCE);
		odo.setPosition(new double[] { initialX, initialY, odo.getTheta() },
				new boolean[] { true, true, true });

	}

	// rotate until it finds a wall
	private void findWall(boolean clockwise) {
		nav.rotate(clockwise);
		this.distance = getFilteredData();
		while (this.distance > WALL_CENTER - NOISE) {
			this.distance = getFilteredData();
		}
		nav.stop();
	}

	// rotate until it is not facing the wall
	private void awayFromWall(boolean clockwise) {
		nav.rotate(clockwise);
		while (this.distance < WALL_CENTER + NOISE) {
			this.distance = getFilteredData();
		}
		nav.stop();
	}

	// use tutorial formulas to calculate the angle to correct the heading by
	private double calculateHeadingFalling(double angleA, double angleB) {
		double angle = (angleA + angleB) / 2.0;

		if (angleA > angleB) {
			angle = 60.0 - angle;
		} else {
			angle = 225.0 - angle;
		}
		return (angle % 360);
	}

	private int getFilteredData() {
		int distance;

		distance = filter.getMedianDistance();
		if (distance > 100) {
			distance = 100;
		}
		return distance;

	}

}