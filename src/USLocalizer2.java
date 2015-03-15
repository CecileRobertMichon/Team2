/*
 *  Team 2
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 *  Derek Yu - 260570997
 *  Ajan Ahmed - 260509046
 *  Georges Assouad - 260567730
 *  Chaohan Wang - 260516712
 */

public class USLocalizer2 {
	public enum LocalizationType {
		FALLING_EDGE, RISING_EDGE
	};

	private int distance;
	private Odometer odo;
	private LocalizationType locType;
	private Navigation nav;
	private USFilter filter;
	private Robot robot = new Robot();
	
	private final int NOISE = robot.NOISE;
	public final int WALL_CENTER = robot.WALL_CENTER;

	public USLocalizer2(Odometer odo, LocalizationType locType, Navigation nav,
			USFilter filter) {
		this.odo = odo;
		this.locType = locType;
		this.nav = nav;
		this.filter = filter;
	}

	public void doLocalization() {
		double angleA, angleB;

		if (locType == LocalizationType.FALLING_EDGE) {
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
			odo.setPosition(new double[] { 0.0, 0.0, 0.0 }, new boolean[] {
					true, true, true });
			nav.turnTo(-90);
			double initialX=filter.getDistance()-30.48;
			nav.turnTo(-180);
			double initialY=filter.getDistance()-30.48;
			nav.turnTo(0);
			odo.setPosition(new double[] { initialX, initialY, 0.0 }, new boolean[] {
					true, true, true });
			
		} else {
			/*
			 * The robot should turn until it sees the wall, then look for the
			 * "rising edges:" the points where it no longer sees the wall. This
			 * is very similar to the FALLING_EDGE routine, but the robot will
			 * face toward the wall for most of it.
			 */

			// rotate until robot sees the wall
			findWall(true);
			odo.setTheta(0);

			// rotate until it no longer sees the wall and latch angle
			awayFromWall(true);
			angleA = odo.getTheta();

			findWall(false);

			// rotate in opposite direction until it sees a wall
			awayFromWall(false);
			angleB = odo.getTheta();

			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			double deltaTheta = calculateHeadingRising(angleA, angleB);

			// update the odometer position (example to follow:)
			nav.turnTo(-deltaTheta);
			odo.setPosition(new double[] { 0.0, 0.0, 0.0 }, new boolean[] {
					true, true, true });

		}
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
			angle = 50.0 - angle;
		} else {
			angle = 230.0 - angle;
		}
		return (angle % 360);
	}

	private double calculateHeadingRising(double angleA, double angleB) {
		double angle = (angleA + angleB) / 2.0;

		if (angleB > angleA) {
			angle = 45.0 - angle;
		} else {
			angle = 230.0 - angle;
		}

		return (angle % 360);
	}

	private int getFilteredData() {
		int distance;

		distance = filter.getMedianDistance();
		if (distance > 50) {
			distance = 50;
		}
		return distance;

	}

}
