/*
 *  Team 2
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 *  Derek Yu - 260570997
 *  Ajan Ahmed - 260509046
 *  Georges Assouad - 260567730
 *  Chaohan Wang - 260516712
 */

import lejos.nxt.UltrasonicSensor;

public class USFilter extends Thread {

	private int medianDistance;
	private int[] input, sorted;
	private UltrasonicSensor us;
	private int counter;

	public USFilter(UltrasonicSensor us) {
		this.us = us;
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
