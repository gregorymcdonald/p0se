package poseData;

import java.awt.Color;
import java.awt.Point;

import colorutil.ColorTolerance;

public class JointNode {
	private Point coordinates;
	private Color rgb;
	private ColorTolerance tolerance;
	private int id;
	
	public JointNode(Point xy, Color rgb, int id, ColorTolerance tolerance){
		coordinates = xy;
		this.rgb = rgb;
		this.id = id;
		this.tolerance = tolerance;
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
	
	public ColorTolerance getTolerance(){
		return tolerance;
	}
}
