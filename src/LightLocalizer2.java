/**********************************************************CLASS TO DO LIGHT LOCALIZATION******************************************************/
import lejos.nxt.ColorSensor;
import lejos.nxt.*;
/***********************************************************CONSTRUCTOR METHOD*****************************************************************/
public class LightLocalizer2 {
	//Class variables
	private Odometer odometer;
	private Robot robot;
	private ColorSensor lightSensor;
    private Navigation nav;             
    public LightLocalizer2(Odometer odo, Navigation nav) {
		//Constructor Method
		this.odometer = odo;
		this.nav = nav;
		this.robot = new Robot();
		this.lightSensor = robot.COLOR_SENSOR;
		// turn on the light
		lightSensor.setFloodlight(true);
	}
/************************************************************LIGHT LOCALIZER*******************************************************************/
	public void doLocalization() {
		   double sensorDistance =12.5;//the distance from the light sensor to wheel center
		   //Precaution to move it away from black line if standing on it
		   nav.turnTo(30);
		   //getting angles for trigonometry
	        int previousLightValue = lightSensor.getNormalizedLightValue();         
	        int lightValue = lightSensor.getNormalizedLightValue();  
	        // unless the line is black the robot keeps turning
	        while (previousLightValue - lightValue > robot.LIGHTSENSOR_THRESHOLD){
	        	robot.LEFT_MOTOR.setSpeed(robot.MOTOR_ROTATE);
				robot.RIGHT_MOTOR.setSpeed(robot.MOTOR_ROTATE);
				previousLightValue = lightValue;
	            lightValue = lightSensor.getNormalizedLightValue();
	        }
	        // if line is black loop breaks
	        double thetaX1 = odometer.getTheta();        //get angle
	        // sleep to catch exceptions
	        try {Thread.sleep(1000);} catch (InterruptedException e) {}
	        previousLightValue = lightSensor.getNormalizedLightValue();         
	        lightValue = lightSensor.getNormalizedLightValue();
	        // while line is not black, robot rotates
	        while (previousLightValue - lightValue > robot.LIGHTSENSOR_THRESHOLD){
	        	robot.LEFT_MOTOR.setSpeed(robot.MOTOR_ROTATE);
				robot.RIGHT_MOTOR.setSpeed(robot.MOTOR_ROTATE);
				previousLightValue = lightValue; 
	            lightValue = lightSensor.getNormalizedLightValue(); 
	        }
	        // if line is black while loop breaks
	        double thetaY1 = odometer.getTheta();    // get angle
	        // sleep
	        try {Thread.sleep(1000);} catch (InterruptedException e) {}
	        previousLightValue = lightSensor.getNormalizedLightValue();         
	        lightValue = lightSensor.getNormalizedLightValue(); 
	  
	        // while line is not black, robot rotates
	        while (previousLightValue - lightValue > robot.LIGHTSENSOR_THRESHOLD){
	        	robot.LEFT_MOTOR.setSpeed(robot.MOTOR_ROTATE);
				robot.RIGHT_MOTOR.setSpeed(robot.MOTOR_ROTATE);
				previousLightValue = lightValue; 
	            lightValue = lightSensor.getNormalizedLightValue();      
	        }
	        // if line is black while loop breaks
	        double thetaX2 =odometer.getTheta();         // get angle
	        // sleep
	        try {Thread.sleep(1000);} catch (InterruptedException e) {}
	        lightValue = lightSensor.getNormalizedLightValue();          
	        LCD.drawInt(lightValue, 0,5); // light sensor reading
	        // while line is not black, robot rotates
	        while (previousLightValue - lightValue > robot.LIGHTSENSOR_THRESHOLD){
	        	robot.LEFT_MOTOR.setSpeed(robot.MOTOR_ROTATE);
				robot.RIGHT_MOTOR.setSpeed(robot.MOTOR_ROTATE);
				previousLightValue = lightValue; 
	            lightValue = lightSensor.getNormalizedLightValue();      
	        }
	        // if line is black while loop breaks
	        Sound.beep();
	        double thetaY2=odometer.getTheta();      // get angle
	        // sleep
	       try {Thread.sleep(1000);} catch (InterruptedException e) {}
	       	robot.LEFT_MOTOR.setSpeed(0);
			robot.RIGHT_MOTOR.setSpeed(0);
	        lightValue = lightSensor.getNormalizedLightValue();              
	        
	        //starting calculations
	        double thetaX = thetaX2-thetaX1;        // compute difference between x-axis angles
	        double thetaY = thetaY2-thetaY1;        // compute difference between y-axis angles 
	        double updatedX = -sensorDistance*Math.cos(Math.toRadians(thetaY/2)); //calculate new X angle
	        double updatedY = -sensorDistance*Math.cos(Math.toRadians(thetaX/2)); //calculate new Y angle
	        double deltaTheta = (thetaY/2) + 90 - (thetaY2-180); //Theta to be changed
	        double theta = odometer.getTheta();      // get current angle
	        double correctTheta= (theta + deltaTheta);  // calculate corrected theta
	        // Correcting odometer values
	        odometer.setPosition(new double [] {updatedX,updatedY, correctTheta}, new boolean [] {true, true, true});
	        // stop robot motion
	        robot.LEFT_MOTOR.setSpeed(0);
			robot.RIGHT_MOTOR.setSpeed(0);
			
	        nav.turnTo(90); // move to 90 degrees from north
	        nav.travelTo(-updatedX, odometer.getY()); //Move to updated X
	        nav.turnTo(0);; // Move back north facing
	        nav.travelTo(odometer.getX(), -updatedY); //Move to updated Y
	        robot.LEFT_MOTOR.setSpeed(0);
			robot.RIGHT_MOTOR.setSpeed(0);   // stop robot 
	    } 
	}
/***********************************************************THE END******************************************************************************/

