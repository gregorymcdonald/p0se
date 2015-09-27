package colorutil;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ColorFinder {
    private static final int TILE_SIZE_DIVISOR = 64;
    
    public static Rectangle[] findColor(BufferedImage image, Color color, ColorTolerance tolerance){
        Rectangle[] colorTiles = findMatchingTiles(image, color, tolerance);
        ArrayList<ArrayList<Rectangle>> rectangleGroupings = new ArrayList<ArrayList<Rectangle>>();
        
        //Assemble adjacent rectangles into groups
        for(Rectangle rect : colorTiles){
            boolean grouped = false;
            for(int i = 0; i < rectangleGroupings.size(); i++){
                ArrayList<Rectangle> grouping = rectangleGroupings.get(i);
                if(isAdjacent(rect, grouping)){
                    grouping.add(rect);
                    grouped = true;
                    break;
                }//if: rect is adjacent to an existing group, add it
            }//for: all rectangle groupings
            if(!grouped){
                ArrayList<Rectangle> group = new ArrayList<Rectangle>();
                group.add(rect);
                rectangleGroupings.add(group);
            }//if: not grouped, add to a new group
        }//for: all color tiles
        System.out.println(rectangleGroupings.size() + " rectangle groupings found");
        
        //Construct overall color group rectangles
        Rectangle[] colorGroups = new Rectangle[rectangleGroupings.size()];
        for(int i = 0; i < rectangleGroupings.size(); i++){
            ArrayList<Rectangle> grouping = rectangleGroupings.get(i);
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            for(Rectangle rect : grouping){
                if(rect.x < minX){
                    minX = rect.x;
                }//if: this rectangle's x-value is less than the min
                if(rect.y < minY){
                    minY = rect.y;
                }//if: this rectangle's y-value is less than the min
                if(rect.x > maxX){
                    maxX = rect.x;
                }//if: this rectangle's x-value is greater than the max
                if(rect.y > maxY){
                    maxY = rect.y;
                }//if: this rectangle's x-value is greater than the max
            }//for: all rectangles in the grouping
            
            //Building the color group rectangle
            int groupWidth = maxX - minX + grouping.get(0).width;
            int groupHeight = maxY - minY + grouping.get(0).height;
            colorGroups[i] = new Rectangle(minX, minY, groupWidth, groupHeight);
        }//for: all rectangle groupings
        return colorGroups;
    }//method: findColor
    
    private static boolean isAdjacent(Rectangle rect, ArrayList<Rectangle> otherRects){
        Rectangle expandedRectangle = new Rectangle(rect.x - 1, rect.y - 1, rect.width + 2, rect.height + 2);
        for(Rectangle otherRectangle : otherRects){
            if(expandedRectangle.intersects(otherRectangle)){
                return true;
            }//if: the input rectangle is adjacent to another rect
        }//for: all Rectangles in otherRects
        return false;
    }//method: isAdjacent
    
    public static Rectangle[] findMatchingTiles(BufferedImage image, Color color, ColorTolerance tolerance){
        ArrayList<Rectangle> matchingTiles = new ArrayList<Rectangle>();
        //matchingTiles.add(new Rectangle(100, 100, 16, 16)); //debugging
        
        //Finding matching tiles
        int tileWidth = image.getWidth() / TILE_SIZE_DIVISOR;
        int tileHeight = image.getHeight() / TILE_SIZE_DIVISOR;
        for(int x = 0; x < image.getWidth(); x += tileWidth){
            for(int y = 0; y < image.getHeight(); y += tileHeight){
            	//Update tile position
                Rectangle currentTile = new Rectangle(x, y, tileWidth, tileHeight);
            	
                Color averageTileColor = ColorUtil.averageRegionColor(image, currentTile);
                boolean matchResult = ColorUtil.matches(color, averageTileColor, tolerance);
                if(matchResult == true){
                    matchingTiles.add(currentTile);
                }//if: the average tile color matches the input color, within tolerance
            }//for: all y positions of tiles
        }//for: all x positions of tiles
        
        //Convert to array and return
        Rectangle[] matchingTilesArray = new Rectangle[matchingTiles.size()];
        return matchingTiles.toArray(matchingTilesArray);
    }//function: findMatchingTiles
}//class: ColorFinder
