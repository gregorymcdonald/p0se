package poseData;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class Pose {
	private ArrayList <JointNode> joints;
	private ArrayList<Double> angles = new ArrayList<Double>();
	private int id;
	
	// Static Vars
	private static ArrayList<Integer> takenIds = new ArrayList<Integer>();;
	
	public Pose(ArrayList<JointNode> joints){
		id = 1;
		takenIds.add(id);
		this.joints = joints;
		this.angles = generateAngles(this.joints);
	}
	
	public Pose(ArrayList<Double> angles, int id){
		this.id = id;
		this.angles = angles;
	}
	
	private ArrayList<Double> generateAngles(ArrayList<JointNode> joints){
	    if(joints == null || joints.isEmpty()){
	        return new ArrayList<Double>();
	    }//if: invalid input
	    
		// Angles to be calculated in reference to the first joint in the list
		JointNode baseNode = joints.get(0);
		for(JointNode joint : joints){
			if(joint != null){
				System.out.println(joint.getLoc());
				double angle = calculateAngle(baseNode.getLoc(), joint.getLoc());
				if(angle >= 0){
					angles.add(angle);
				}
			}
		}
		return angles;
	}
	
	private double calculateAngle(Point a, Point b){
		if(a != null && b != null){
			final double deltaY = (a.y - b.y);
		    final double deltaX = (b.x - a.x);
		    final double result = Math.toDegrees(Math.atan2(deltaY, deltaX)); 
		    return (result < 0) ? (360d + result) : result;
		} else {
			return -1;
		}
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
	
	public void savePose() throws IOException{
		// Write out a representation of the angles to a file
		String fileName = "p0se" + this.id + ".txt";
		FileWriter writer = new FileWriter(fileName);
		for(int i = 0; i < angles.size(); i++){
			writer.write(this.angles.get(i) + "\n");
		}
		writer.close();
	}
	
	private static Pose readPose(int id){
		Scanner sc = new Scanner("p0se" + id + ".txt");
		ArrayList<Double> readAngles = new ArrayList<Double>();
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			double angle = Double.parseDouble(line);
			readAngles.add(angle);
		}
		sc.close();
		return new Pose(readAngles, id);
	}
	
	public ArrayList<Double> getAngles(){
		return this.angles;
	}
}
