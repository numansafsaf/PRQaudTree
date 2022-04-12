// Represents a point in the xy-plane with integer-valued coordinates.
// Supplies comparison functions specified in the Compare2D interface.
//

public class Point implements Compare2D<Point> {

    public long xcoord;
    public long ycoord;
    
    public Point() {
       xcoord = 0;
       ycoord = 0;
    }
    public Point(long x, long y) {
       xcoord = x;
       ycoord = y;
    }
    public long getX() {
       return xcoord;
    }
    public long getY() {
       return ycoord;
    }
    
    public Direction directionFrom(long X, long Y) {
       
    	long vx = this.xcoord - X;
    	long vy = this.ycoord - Y;
    	
    	double direction = 0;
    	direction = Math.atan2((double)vy,(double)vx);
    	direction = Math.toDegrees(direction);
    	if (direction < 0) {
    		
    		direction = 360 - Math.abs(direction);
    	}
    	
    	if(this.getX() == X && this.getY() == Y) {
    		return Direction.NE;
    	}
    	if(this.getX() == X) {
    		if(this.getY() > Y) {
    			
    		}
    	}
    	
    	if (direction >= 270) {
    		return Direction.SE;
    	}
    	else if (direction < 270 && direction >= 180) {
    		return Direction.SW;
    	}
    	else if (direction < 180 && direction >= 90) {
    		return Direction.NW;
    	}
    	else if (direction < 90 && direction >= 0) {
    		return Direction.NE;
    	}
       return Direction.NOQUADRANT;
    }
    
    public Direction inQuadrant(double xLo, double xHi, double yLo, double yHi) {
       
    	 double xCenter = (xLo + xHi) / 2;
         double yCenter = (yLo + yHi) / 2;       
     
         
         //edge cases
         if(this.getX() > xHi ||this.getX() < xLo || this.getY() > yHi || this.getY() < yLo) {
        	 return Direction.NOQUADRANT;
         }
         return this.directionFrom((long)xCenter, (long)yCenter);
    }
    
    public boolean   inBox(double xLo, double xHi, double yLo, double yHi) {
       
    	  if(this.getX() > xHi ||this.getX() < xLo || this.getY() > yHi || this.getY() < yLo) {
         	 return false;
          }
      return true;
    }
    
    public String toString() {
        
       // Do not change...
       return new String("(" + xcoord + ", " + ycoord + ")");
    }
    
    public boolean equals(Object o) {
       
      
      if (this.getX() == (((Point) o).getX()) && this.getY() == ((Point) o).getY()) {
    	  return true;
      }
      return false;
    }
}
