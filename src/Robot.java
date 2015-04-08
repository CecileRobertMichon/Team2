/*
 *  Team 2
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 *  Derek Yu - 260570997
 *  Ajan Ahmed - 260509046
 *  Georges Assouad - 260567730
 *  Chaohan Wang - 260516712
 */

import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class Robot {

	// Constants used throughout the project

	// Robot design lengths
	public final double RADIUS = 2.149; // 2.149;
	public final double WIDTH = 16.63; // 16.41;

	// Motor Speeds
	public final int MOTOR_SLOW = 30;
	public final int MOTOR_ROTATE = 250; // 150;
	public final int MOTOR_LOCALIZE = 250;
	public final int MOTOR_STRAIGHT = 300; // 200;
	public final int MOTOR_FAST = 400;

	// Wall follower constants
	public final int BANDCENTER = 18;
	public final int BANDWIDTH = 3;
	public final int WALL_CENTER = 75;
	public final int NOISE = 5;

	// odometer update period, in ms
	public final long ODOMETER_PERIOD = 25;
	public final long CORRECTION_PERIOD = 10;
	public final long DISPLAY_PERIOD = 250;

	// Light sensor values
	public final int LIGHTSENSOR_THRESHOLD = 30;
	public final double LIGHT_SENSOR_DISTANCE = 12.4;

	// Ultrasonic sensor values
	public final double US_SENSOR_DISTANCE = 9.9;
	public final double US_LEFT_SENSOR_DISTANCE = 7.7;

	// Motor and sensor ports
	public final NXTRegulatedMotor LEFT_MOTOR = Motor.A, RIGHT_MOTOR = Motor.B,
			LOADER = Motor.C;
	public ColorSensor COLOR_SENSOR = new ColorSensor(SensorPort.S1);
	public UltrasonicSensor US = new UltrasonicSensor(SensorPort.S2);
	public UltrasonicSensor US2 = new UltrasonicSensor(SensorPort.S3);

	// Tile constants
	public final double TILE_LENGTH = 30.48;
	public final double HALF_TILE = 15.24;
	public final double DISTANCE_TOLERANCE = 0.5;
	public final double ANGLE_TOLERANCE = 0.5;

	// Firing constants
	public final double FIRING_DISTANCE = 138;
	
	// Shooting area
	public final int CORNER = 2; //8;

	// Target positions integer (ie. 1 = 30.48)
	public final int TARGET_ONE_X = 2;
	public final int TARGET_ONE_Y = 8;
	public final int TARGET_TWO_X = -2;
	public final int TARGET_TWO_Y = 5;

	// Number of balls
	public final int BALL_NUMBER = 2;

	// Helper methods to convert distance and angle in degrees the motors have
	// to rotate by
	public int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	public int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}

}