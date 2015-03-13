import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class Robot {

	// Constants used throughout the project
	
	// Robot design lengths
	public final double RADIUS = 2.09;
	public final double WIDTH = 16.26;
	
	// Motor Speeds
	public final int MOTOR_SLOW = 30;
	public final int MOTOR_ROTATE = 150;
	public final int MOTOR_STRAIGHT = 200;
	public final int MOTOR_FAST = 300;
	
	// Wall follower constants
	public final int BANDCENTER = 18;
	public final int BANDWIDTH = 3;
	
	// odometer update period, in ms
	public final long ODOMETER_PERIOD = 25;
	public final long CORRECTION_PERIOD = 10;

	// Light sensor values
	public final int LIGHTSENSOR_THRESHOLD = 30;
	public final double LIGHT_SENSOR_DISTANCE = 12.4;

	// Motor and sensor ports
	public final NXTRegulatedMotor LEFT_MOTOR = Motor.A, RIGHT_MOTOR = Motor.B, LOADER = Motor.C;
	public ColorSensor COLOR_SENSOR = new ColorSensor(SensorPort.S1);
	public UltrasonicSensor US = new UltrasonicSensor(SensorPort.S2);
	
	// Tile constants
	public final double TILE_LENGTH = 30.48;
	public final double HALF_TILE = 15.24;
	public final double DISTANCE_TOLERANCE = 0.5;
	public final double ANGLE_TOLERANCE = 0.25;
	
	// Target positions
	public final int TARGET_ONE_X = 0;
	public final int TARGET_ONE_Y = 0;
	public final int TARGET_TWO_X = 0;
	public final int TARGET_TWO_Y = 0;

	// Helper methods to convert distance and angle in degrees the motors have
	// to rotate by
	public int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	public int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}

}