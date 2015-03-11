import lejos.nxt.UltrasonicSensor;

public class USFilter extends Thread {

	private int medianDistance;
	private int[] input, sorted;
	private UltrasonicSensor us;
	private Robot robot;
	private int counter;

	public USFilter() {
		this.robot = new Robot();
		this.us = robot.US;
		medianDistance = 0;
		input = new int[5];
		for (int i = 0; i < input.length; i++) {
			input[i] = us.getDistance();
		}
		sorted = new int[5];
		counter = 0;
	}

	public void run() {
		while (true) {
			input[counter] = us.getDistance();
			counter++;
			counter = counter % 5;
			bubbleSort(input);
			medianDistance = calculateMedian();
		}
	}

	public int getMedianDistance() {
		return this.medianDistance;
	}

	public void bubbleSort(int value[]) {
		int swap;
		sorted = value;
		for (int c = 0; c < (value.length - 1); c++) {
			for (int d = 0; d < value.length - c - 1; d++) {
				if (sorted[d] > sorted[d + 1]) /* For descending order use < */
				{
					swap = sorted[d];
					sorted[d] = sorted[d + 1];
					sorted[d + 1] = swap;
				}
			}
		}
	}
	
	//returns median to the sorted moving array by taking the middle value 
	  public int calculateMedian() {
	    int median;
	    if ((sorted.length % 2) == 1) { 
	    //array is of odd length, chose middle value
	      median = sorted[(int) (sorted.length + 1) / 2];
	    }
	    else { 
	    //array is of even length, average out the two middle values
	      median = (sorted[ (int) (sorted.length / 2) ] + sorted[(int) (sorted.length + 1) / 2]) / 2;
	    }
	    return median;
	  }

}