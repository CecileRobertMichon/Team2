/*
 *  Team 2
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 *  Derek Yu - 260570997
 *  Ajan Ahmed - 260509046
 *  Georges Assouad - 260567730
 *  Chaohan Wang - 260516712
 */

public class LauncherPositioning {
	// requires (10, 10) in block units is (0, 0) for the launcher

	private Odometer odometer;
	private Robot robot;
	private Navigation nav;
	private Launcher launcher;

	public LauncherPositioning(Odometer odometer, Navigation nav,
			Launcher launcher) {
		this.odometer = odometer;
		this.robot = new Robot();
		this.nav = nav;
		this.launcher = launcher;
	}

	// Assumed robot is at position (8,8) when method is called
	public void targetAcquisition(int x1, int y1, int x2, int y2) {

		// calculate angle to target
		double minTheta = (Math.atan2(x1 - odometer.getX(),
				y1 - odometer.getY()))
				* (180.0 / Math.PI);
		// Make sure theta is positive
		if (minTheta < 0) {
			minTheta += 360;
		}
		nav.turnTo(minTheta);
		lineUp(x1, y1);
		// shoot half the balls
		for (int i = 0; i < robot.BALL_NUMBER / 2; i++) {
			launcher.shootBall();
		}
		minTheta = (Math.atan2(x2 - odometer.getX(), y2 - odometer.getY()))
				* (180.0 / Math.PI);
		if (minTheta < 0) {
			minTheta += 360;
		}
		nav.turnTo(minTheta);
		lineUp(x2, y2);
		// shoot the other half of the balls
		// fire command
		for (int i = 0; i < robot.BALL_NUMBER - (robot.BALL_NUMBER / 2); i++) {
			launcher.shootBall();
		}
	}

	// to move forwards and backwards until target is perfectly in optimal range
	private void lineUp(int xTarget, int yTarget) {
		double x, y;
		x = odometer.getX();
		y = odometer.getY();
		double targetDistance = Math.sqrt(Math.pow((x - xTarget), 2)
				+ Math.pow((y - yTarget), 2));
		double discrepancy = targetDistance - robot.FIRING_DISTANCE;

		if (discrepancy > 0) {
			// move closer to target
			nav.goForward(discrepancy);
		} else {
			// move further away from target
			nav.goBackward(discrepancy);
		}

	}

}
