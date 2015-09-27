package poseData;

import java.awt.Color;
import java.awt.Point;

public class JointNode {
	private Point coordinates;
	private Color rgb;
	private int id;
	
	public JointNode(Point xy, Color rgb, int id){
		coordinates = xy;
		this.rgb = rgb;
		this.id = id;
	}
	
	public Point getLoc(){
		return coordinates;
	}
	
	public Color getColor(){
		return rgb;
	}
	
	public int getId(){
		return id;
	}
}
