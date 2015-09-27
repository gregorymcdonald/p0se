package colorutil;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ColorFinder {
    private static final int TILE_SIZE_DIVISOR = 32;
    
    public static Point findColor(BufferedImage image, Color color, ColorTolerance tolerance){
        Rectangle[] colorTiles = findMatchingTiles(image, color, tolerance);
        if(colorTiles.length == 0){
            return null;
        }//if: no color was found
        
        int totalX = 0;
        int totalY = 0;
        for(int i = 0; i < colorTiles.length; i++){
            totalX += colorTiles[i].x;
            totalY += colorTiles[i].y;
        }//for: all color tiles
        int averageX = (totalX / colorTiles.length) + (colorTiles[0].width / 2);
        int averageY = (totalY / colorTiles.length) + (colorTiles[0].height / 2);
        
        //Result as rectangle
        //Rectangle[] result = new Rectangle[1];
        //result[0] = new Rectangle(averageX, averageY, 4, 4);
        Point result = new Point(averageX, averageY);
        return result;
    }//method: findColor
    
    public static Rectangle[] findColorRegions(BufferedImage image, Color color, ColorTolerance tolerance){
        //Alternative Solution: Recursive flood fill of colorTiles
        Rectangle[] colorTiles = findMatchingTiles(image, color, tolerance);
        if(colorTiles.length == 0){
            return new Rectangle[0];
        }//if: no color was found
        
        //Assemble adjacent rectangles into groups
        ArrayList<ArrayList<Rectangle>> rectangleGroupings = new ArrayList<ArrayList<Rectangle>>();
        ArrayList<Rectangle> firstRectangleGroup = new ArrayList<Rectangle>();
        firstRectangleGroup.add(colorTiles[0]);
        rectangleGroupings.add(firstRectangleGroup);
        for(int j = 1; j < colorTiles.length; j++){
            Rectangle rect = colorTiles[j];
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
        //System.out.println(rectangleGroupings.size() + " rectangle groupings found");
        
        //Construct overall color group rectangles
        ArrayList<Rectangle> colorGroups = new ArrayList<Rectangle>();
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
            Rectangle colorGroup = new Rectangle(minX, minY, groupWidth, groupHeight);
            colorGroups.add(colorGroup);
        }//for: all rectangle groupings
        
        //Merge color rectangles
        ArrayList<Rectangle> mergedRectangles = new ArrayList<Rectangle>();
        for(int i = 0; i < colorGroups.size(); i++){
            Rectangle current = colorGroups.get(i);
            for(int j = 0; j < colorGroups.size(); j++){
                Rectangle mergeCandidate = colorGroups.get(j);
                if(!mergeCandidate.equals(current)){
                    if(current.intersects(mergeCandidate)){
                        Rectangle mergeResult = mergeRectangles(current, mergeCandidate);
                        mergedRectangles.add(mergeResult);
                        
                        //Remove merged rect, and adjust counters
                        colorGroups.set(i, mergeResult);
                        current = mergeResult;
                        colorGroups.remove(j);
                        j = 0;
                    }
                }//if: not looking at yourself
            }//for: all rectangles in colorGroups
        }//for: all rectangles in colorGroups
        
        //Convert to array
        Rectangle[] mergedRectanglesArray = new Rectangle[mergedRectangles.size()];
        return mergedRectangles.toArray(mergedRectanglesArray);
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
    
    private static Rectangle mergeRectangles(Rectangle rect1, Rectangle rect2){
        if(rect1 == null || rect2 == null){
            return null;
        }//if: invalid input
        
        //Merging
        if(rect1.contains(rect2)){
            return rect1;
        }//if: rect1 entirely encompasses rect2
        if(rect2.contains(rect1)){
            return rect2;
        }//if: rect2 entirely encompasses rect1
        
        //Building a merged rectangle
        int minX = (rect1.x <= rect2.x) ? rect1.x : rect2.x;
        int minY = (rect1.y <= rect2.y) ? rect1.y : rect2.y;
        int maxX = (rect1.x + rect1.width <= rect2.x + rect2.width) ? rect1.x + rect1.width : rect2.x + rect2.width;
        int maxY = (rect1.y + rect1.height <= rect2.y + rect2.height) ? rect1.y + rect1.height : rect2.y + rect2.height;
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }//method: mergeRectangles
    
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
