package P0seRecognizer;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import application.P0seData;
import poseData.JointNode;
import poseData.Pose;
import colorutil.ColorFinder;
import colorutil.ColorTolerance;



public class P0seRecognizer {

	private static final int POSE_MATCH_TOLERANCE = 30;
	
	Pose currentPose;
	Boolean called = false;

	/*
	 * Gets the current grid points of the nodes from the camera and updates the local map with variables the new Points
	 * 
	 * Must be called before compareCurrentPose()
	 */
	public void updateNodeLocations(BufferedImage image){
		ArrayList <JointNode> jointNodes = new ArrayList<JointNode>();

		for(int i = 0; i < P0seData.DEFAULT_JOINT_NODE_COLORS.length; i++){
			Color currentColor = P0seData.DEFAULT_JOINT_NODE_COLORS[i].color;
			ColorTolerance currentTolerance = P0seData.DEFAULT_JOINT_NODE_COLORS[i].tolerance;
			Point colorLocation = ColorFinder.findColor(image, currentColor, currentTolerance);
			if(colorLocation != null){
				JointNode node = new JointNode(colorLocation, currentColor, i, currentTolerance);
				jointNodes.add(node);
			}
		}

		currentPose = new Pose(jointNodes);
	}
	
	public Pose getPose(BufferedImage image){
		ArrayList <JointNode> jointNodes = new ArrayList<JointNode>();

		for(int i = 0; i < P0seData.DEFAULT_JOINT_NODE_COLORS.length; i++){
			Color currentColor = P0seData.DEFAULT_JOINT_NODE_COLORS[i].color;
			ColorTolerance currentTolerance = P0seData.DEFAULT_JOINT_NODE_COLORS[i].tolerance;
			Point colorLocation = ColorFinder.findColor(image, currentColor, currentTolerance);
			if(colorLocation != null){
				JointNode node = new JointNode(colorLocation, currentColor, i, currentTolerance);
				jointNodes.add(node);
			}
		}

		Pose result = new Pose(jointNodes);
		System.out.println(result.getAngles());
		return result;
	}//method: readPose

	public Pose[] matchPose(Pose pose, Pose[] potentialMatches){
		ArrayList<Pose> matchedPoses = new ArrayList<Pose>();
		
		for(int i = 0; i < potentialMatches.length; i++){
			if(pose.equals(potentialMatches[i], POSE_MATCH_TOLERANCE)){
				matchedPoses.add(potentialMatches[i]);
			}//if: pose matches the potential match
		}//for: all potential matches
		
		Pose[] matchedPosesArray = new Pose[matchedPoses.size()];
		return matchedPoses.toArray(matchedPosesArray);
	}//method: matchPose
	
	/*
	 * Compares the current P0se against those of stored P0ses to see if there is a match
	 * 
	 * Return: Returns the pose in storage if there was a match, null otherwise.
	 */
	public void compareCurrentPose(){
		Pose testP0se = P0seData.findPose("Yellow over Blue");

		//System.out.println(currentPose.getAngles()); //DEBUGGING
		Runtime rt = Runtime.getRuntime();

		if(currentPose.equals(testP0se, 30) && called == false){
			if(System.getProperty("os.name").startsWith("Windows")){
				try {
					rt.exec(new String[]{"cmd", "/c", "start", "chrome", "/new-window"});

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				try {
					rt.exec(new String[]{"open", "-a", "Google Chrome", "--new", "--args"});
					//rt.exec(new String[]{"spotify_cmd Next"});

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			called = true;
		}else{
			//System.out.println("Failed Test");
		}
	}
}
