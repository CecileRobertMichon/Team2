import lejos.nxt.*;

public class Launcher{

	 public void shootsBall() {
	    	NXTRegulatedMotor loader = Motor.C;
	        loader.setSpeed(1000000);//wanted to use largest value possible 
	        loader.setAcceleration(100000);//wanted to use largest value possible
	     for(int i=0; i<3; i++){ //shoots ball 3 times
	         loader.rotate(-360);
	         try {
	             Thread.sleep(1500);
	         } catch (InterruptedException e) {}
	         loader.stop();
	     }
	      
	     
	     try {
	         Thread.sleep(500);
	     } catch (InterruptedException e) {}
	     loader.stop();
	    }
	}