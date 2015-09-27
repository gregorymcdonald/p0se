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

	Pose currentPose;
	Boolean called = false;

	private static Color[] jointNodeColors = {
		new Color(48, 114, 139),
		new Color(167, 175, 71)
	};

	ArrayList <JointNode> jointNodes = new ArrayList<JointNode>();
	/*
	 * Gets the current grid points of the nodes from the camera and updates the local map with variables the new Points
	 * 
	 * Must be called before compareCurrentPose()
	 */
	public void updateNodeLocations(BufferedImage image){
		jointNodes = new ArrayList<JointNode>();//resets the array for the new NodeJoints that will be captured in the new image

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

	/*
	 * Compares the current P0se against those of stored P0ses to see if there is a match
	 * 
	 * Return: Returns the pose in storage if there was a match, null otherwise.
	 */
	public void compareCurrentPose(){
		//Pose currentPose = new Pose(jointNodes);
		ArrayList<Double> testObject = new ArrayList<Double>();

		testObject.add(0.0);
		testObject.add(270.0);

		Pose hardcodedPose = new Pose(testObject,1);

		//System.out.println(currentPose.getAngles()); //DEBUGGING
		Runtime rt = Runtime.getRuntime();

		if(currentPose.equals(hardcodedPose, 30) && called == false){
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
