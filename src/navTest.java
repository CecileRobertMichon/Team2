import lejos.nxt.Button;
import lejos.nxt.LCD;


public class navTest {


	public static void main(String args[]){
		

		Odometer odo = new Odometer();
		Robot robot = new Robot();
		//OdometryCorrection correc = new OdometryCorrection(odo);
		USFilter filterStraight = new USFilter(robot.US);
		USFilter filterLeft = new USFilter(robot.US2);
		Navigation nav = new Navigation(odo, filterStraight, filterLeft);
		OdometryDisplay lcd = new OdometryDisplay(odo, filterStraight);
		
		odo.start();
		lcd.start();
		
		//LCD.drawString("Hello", 0, 3);
		//nav.goForward(10);
		//nav.turnTo(60);
		nav.travelTo(1, 1);
	
		Button.waitForAnyPress();
		System.exit(0);
	}
}
