import lejos.nxt.*;

/*
 *  Team 2
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 *  Derek Yu - 260570997
 *  Ajan Ahmed - 260509046
 *  Georges Assouad - 260567730
 *  Chaohan Wang - 260516712
 */

public class Launcher {

	public void shootBall() {
		NXTRegulatedMotor loader = Motor.C;
		loader.setSpeed(1000000);// wanted to use largest value possible
		loader.setAcceleration(100000);// wanted to use largest value possible
		for (int i = 0; i < 1; i++) { // shoots 1 ball
			loader.rotate(-360);
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
			}
			loader.stop();
		}

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		loader.stop();
	}
}