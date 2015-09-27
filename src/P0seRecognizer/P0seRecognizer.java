package P0seRecognizer;

import java.awt.Color;
import java.awt.Point;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import poseData.JointNode;
import poseData.Pose;
import colorutil.ColorFinder;
import colorutil.ColorTolerance;



public class P0seRecognizer {
	
	private static Color[] jointNodeColors = {
        new Color(48, 114, 139),
        new Color(167, 175, 71)
    };

	ArrayList <JointNode> JointNodes = new ArrayList<JointNode>();
	/*
	 * Gets the current grid points of the nodes from the camera and updates the local map with variables the new Points
	 * 
	 * Must be called before compareCurrentPose()
	 */
	public void updateNodeLocations(BufferedImage image){
		JointNodes = new ArrayList<JointNode>();//resets the array for the new NodeJoints that will be captured in the new image
		
		for(int color = 0; color < jointNodeColors.length; color++){
			Color currentColor = jointNodeColors[color];
			ColorTolerance tolerance = new ColorTolerance(60);
			JointNode node = new JointNode(ColorFinder.findColor(image, jointNodeColors[color], tolerance), currentColor, color);
			JointNodes.add(node);	
		}
	}
	
	/*
	 * Compares the current P0se against those of stored P0ses to see if there is a match
	 * 
	 * Return: Returns the pose in storage if there was a match, null otherwise.
	 */
	public void compareCurrentPose(){
		Pose currentPose = new Pose(JointNodes);
		Pose hardcodedPose = new Pose(JointNodes);
		Runtime rt = Runtime.getRuntime();
		
		if(currentPose.equals(hardcodedPose)){
			 try {
			        rt.exec(new String[]{"open", "-a", "Google Chrome", "--new", "--args"});
			    	 //rt.exec(new String[]{"spotify_cmd Next"});

			    } catch (IOException e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			    }
		}else{
			System.out.println("Failed Test");
		}
	}
}
