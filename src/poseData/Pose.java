package poseData;

import java.awt.Point;
import java.util.ArrayList;

public class Pose {
	private ArrayList <JointNode> joints;
	private ArrayList<Double> angles;
	private int id;
	
	public Pose(ArrayList<JointNode> joints){
		id = 1;
		this.joints = joints;
		angles = generateAngles(this.joints);
	}
	
	private ArrayList<Double> generateAngles(ArrayList<JointNode> joints){
		// Angles to be calculated in reference to the first joint in the list
		JointNode baseNode = joints.get(0);
		for(JointNode joint : joints){
			double angle = calculateAngle(baseNode.getLoc(), joint.getLoc());
			angles.add(angle);
		}
		return new ArrayList<Double>();
	}
	
	private double calculateAngle(Point a, Point b){
		final double deltaY = (a.y - b.y);
	    final double deltaX = (b.x - a.x);
	    final double result = Math.toDegrees(Math.atan2(deltaY, deltaX)); 
	    return (result < 0) ? (360d + result) : result;
	}
	
	@Override
	public boolean equals(Object other){
		// Ensure param is a Pose
		if(!(other instanceof Pose)){
			return false;
		}
		
		Pose otherPose = (Pose) other;
		if(this.angles.size() != otherPose.angles.size()){
			return false;
		} else {
			for(int i = 0; i < this.angles.size(); i++){
				if(this.angles.get(i) != otherPose.angles.get(i)){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean equals(Pose otherPose, int tolerance){
		if(this.angles.size() != otherPose.angles.size()){
			return false;
		} else {
			for(int i = 0; i < this.angles.size(); i++){
				if(Math.abs(this.angles.get(i) - otherPose.angles.get(i)) > tolerance){
					return false;
				}
			}
		}
		return true;
	}
}
