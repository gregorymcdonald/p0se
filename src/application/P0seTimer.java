package application;

import poseData.Pose;

public class P0seTimer {
	public Pose p0se;
	public int timeRemaining; //in milliseconds
	
	public P0seTimer(Pose p0se, int timeInMilliseconds){
		this.p0se = p0se;
		timeRemaining = timeInMilliseconds;
	}//constructor: default
	
	public void tick(int milliseconds){
		timeRemaining -= milliseconds;
	}
	
	public boolean timerReached(){
		return timeRemaining <= 0;
	}
}//class: P0seTimer
