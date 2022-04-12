// A somewhat minimal test harness for the Point type.
// Uses a collection of hard-coded test cases to determine whether the
// user's implementation of the Point methods satisfy the comments given
// in the Compare2D interface.
//
// Depends on:  Compare2D.java and Direction.java

import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.Vector;
import java.util.Random;

public class testPoint {
    
    private static int pointsEarned = 0;
    
    public static void main(String[] args) throws IOException {
        
        try {
            // Test Point constructors:
            try {
               if ( checkPointInitialization() ) {
                  System.out.println("   Passed test of Point initialization.\n");
              }
              else {
                  System.out.println("   Failed test of Point initialization.\n");
                  System.out.print("Aborting remaining tests.\n");
                  logScore();
                  return;
              }
            }
            catch ( Exception e ) {
               System.out.print("Exception caught while testing Point initialization: " + e + "\n");
               System.out.print("Aborting remaining tests.\n");
               logScore();
               return;
            }
            
            // Test directionFrom():
            try {
               if ( checkDirectionFrom() ) {
                  System.out.println("   Passed test of directionFrom().\n");
               }
               else {
                  System.out.println("   Failed test of directionFrom().\n");
               }
            }
            catch ( Exception e ) {
               System.out.print("Exception caught while testing directionFrom(): " + e + "\n");
            }
            
            // Test inQuadrant():
            try {
               if ( checkInQuadrant() ) {
                  System.out.println("   Passed test of inQuadrant().\n");
               }
               else {
                  System.out.println("   Failed test of inQuadrant().\n");
               }
            }
            catch ( Exception e ) {
               System.out.print("Exception caught while testing inQuadrant(): " + e + "\n");
            }
            
            // Test inBox():
            try {
               if ( checkInBox() ) {
                  System.out.println("   Passed test of inBox().\n");
               }
               else {
                  System.out.println("   Failed test of inBox().\n");
               }
            }
            catch ( Exception e ) {
               System.out.print("Exception caught while testing inBox(): " + e + "\n");
            }
         }
        catch (Exception e) {
            System.out.print("Exception caught in main: " + e.getMessage() + "\n");
        }
        
        logScore();
    }
    
    private static boolean checkPointInitialization() throws IOException {
        
        boolean passed = true;
        
        // check default constructor
        System.out.println("Checking default Point constructor.");
        Point p1 = new Point();
        if ( p1.xcoord == 0L && p1.ycoord == 0L ) {
            System.out.println("   Good, both coordinates were set to 0.");
            pointsEarned += 10;
        }
        else {
            System.out.println("   Error:  one or both coordinates are not 0.");
            passed = false;
        }
        
        // check paramaterized constructor
        long x =   37L;
        long y = -109L;
        System.out.println("Checking parameterized Point constructor with values " + 
                           x + " and " + y);
        Point p2 = new Point(x, y);
        if ( p2.xcoord == x && p2.ycoord == y ) {
            System.out.println("   Good, both coordinates were set correctly.");
            pointsEarned += 10;
        }
        else {
            System.out.println("   Error:  one or both coordinates are set incorrectly.");
            System.out.println("   Created Point was: " + p2.toString());
            passed = false;
        }

        return passed;
    }
    
    private static boolean checkDirectionFrom() throws IOException {
        
        boolean passed = true;
        System.out.println("Checking directionFrom().");
        
        // Check comparisons for directly E, directly N, directly W, and directly S.
        // Likely to fail if implementation does map quadrant boundaries as specified.
        long baseX = 100;  // coordinates for "user" point in checks
        long baseY = 200;
        Point dest = new Point(baseX, baseY);
        
        // Check boundary cases:
        passed = passed && logDirectionFromCase(baseX - 10, baseY, dest, Direction.NE);
        passed = passed && logDirectionFromCase(baseX + 10, baseY, dest, Direction.SW);
        passed = passed && logDirectionFromCase(baseX, baseY - 10, dest, Direction.NW);
        passed = passed && logDirectionFromCase(baseX, baseY + 10, dest, Direction.SE);
        passed = passed && logDirectionFromCase(baseX, baseY, dest, Direction.NE);
        
        // Check non-boundary cases:
        passed = passed && logDirectionFromCase(baseX - 10, baseY - 10, dest, Direction.NE);
        passed = passed && logDirectionFromCase(baseX - 10, baseY + 10, dest, Direction.SE);
        passed = passed && logDirectionFromCase(baseX + 10, baseY + 10, dest, Direction.SW);
        passed = passed && logDirectionFromCase(baseX + 10, baseY - 10, dest, Direction.NW);

        return passed;
    }
    
    private static boolean logDirectionFromCase(long srcX, long srcY, Point dest, Direction refAnswer) {
        
        boolean passed = true;
        
        Point src = new Point(srcX, srcY);
        Direction claimed = dest.directionFrom(srcX, srcY);
        if ( claimed.equals( refAnswer ) ) {
            System.out.println("  Good, said " + dest.toString() + " was " + claimed +
                               " from " + src);
            pointsEarned += 10;
        }
        else {
            System.out.println("  Error, said " + dest.toString() + " was " + claimed +
                               " from " + src);
            passed = false;
        }
        return passed;
    }
    
    private static boolean checkInQuadrant() {
        
        boolean passed = true;
        System.out.println("Checking inQuadrant().");
        
        // Define boundaries of "world" for test:
        long left   = -100;
        long right  =  200;
        long bottom = -200;
        long top    = 100;
        Point SEcorner = new Point(left, bottom);
        Point NWcorner = new Point(right, top);
        
        // Log region boundaries:
        System.out.println("Region bounded by " + SEcorner.toString() + " and " + NWcorner.toString());
        
        // Check one point outside the region:
        passed = passed & logInQuadrantCase(left, right, bottom, top, new Point(left - 10, right + 10), Direction.NOQUADRANT);
        
        // Calculate the center point:
        long xCenter = (left + right) / 2;
        long yCenter = (bottom + top) / 2;
        Point Center = new Point(xCenter, yCenter);
        System.out.println("Center of region is at: " + Center.toString() + "\n");
                
        // Check boundary cases:
        passed = passed & logInQuadrantCase(left, right, bottom, top, Center, Direction.NE);
        passed = passed & logInQuadrantCase(left, right, bottom, top, new Point(xCenter + 10, yCenter), Direction.NE);
        passed = passed & logInQuadrantCase(left, right, bottom, top, new Point(xCenter, yCenter + 10), Direction.NW);
        passed = passed & logInQuadrantCase(left, right, bottom, top, new Point(xCenter - 10, yCenter), Direction.SW);
        passed = passed & logInQuadrantCase(left, right, bottom, top, new Point(xCenter, yCenter - 10), Direction.SE);
        
        // Check non-boundary cases:
        passed = passed & logInQuadrantCase(left, right, bottom, top, new Point(xCenter + 10, yCenter + 10), Direction.NE);
        passed = passed & logInQuadrantCase(left, right, bottom, top, new Point(xCenter - 10, yCenter + 10), Direction.NW);
        passed = passed & logInQuadrantCase(left, right, bottom, top, new Point(xCenter - 10, yCenter - 10), Direction.SW);
        passed = passed & logInQuadrantCase(left, right, bottom, top, new Point(xCenter + 10, yCenter - 10), Direction.SE);
        
        return passed;
    }
    
    private static boolean logInQuadrantCase(long left, long right, long bottom, long top, Point pt, Direction refAnswer) {
        
        boolean passed = true;
        
        Direction claimed = pt.inQuadrant(left, right, bottom, top);
        
        if ( claimed.equals( refAnswer ) ) {
            System.out.println("  Good, said " + pt.toString() + " was in " + claimed);
            pointsEarned += 10;
        }
        else {
            System.out.println("  Error, said " + pt.toString() + " was in " + claimed);
            passed = false;
        }
        return passed;
    }
    
    private static boolean checkInBox() {
        
        boolean passed = true;
        System.out.println("Checking inBox().");
        
        // Define boundaries of "world" for test:
        long left   = -100;
        long right  =  200;
        long bottom = -200;
        long top    = 100;
        Point SEcorner = new Point(left, bottom);
        Point NWcorner = new Point(right, top);
        
        // Log region boundaries:
        System.out.println("Region bounded by " + SEcorner.toString() + " and " + NWcorner.toString());
        
        // Check slightly inside region boundaries (4 cases):
        passed = logInBoxCase(left, right, bottom, top, new Point(left + 1, top - 1), true);
        passed = logInBoxCase(left, right, bottom, top, new Point(left + 1, bottom + 1), true);
        passed = logInBoxCase(left, right, bottom, top, new Point(right - 1, top - 1), true);
        passed = logInBoxCase(left, right, bottom, top, new Point(right - 1, bottom + 1), true);
        
        // Check slightly outside region boundaries (8 cases):
        passed = logInBoxCase(left, right, bottom, top, new Point(left - 1, bottom - 1), false);
        passed = logInBoxCase(left, right, bottom, top, new Point(left - 1, bottom + 10), false);
        passed = logInBoxCase(left, right, bottom, top, new Point(left - 1, top + 1), false);
        passed = logInBoxCase(left, right, bottom, top, new Point(left + 10, top + 1), false);
        passed = logInBoxCase(left, right, bottom, top, new Point(right + 1, top + 1), false);
        passed = logInBoxCase(left, right, bottom, top, new Point(right + 1, bottom + 10), false);
        passed = logInBoxCase(left, right, bottom, top, new Point(right + 1, bottom - 1), false);
        passed = logInBoxCase(left, right, bottom, top, new Point(left + 10, bottom - 1), false);
        
        return passed;
    }
    
    private static boolean logInBoxCase(long left, long right, long bottom, long top, Point pt, boolean refAnswer) {
        
        boolean passed = true;
        boolean stuAnswer = pt.inBox(left, right, bottom, top);
        
        if ( stuAnswer == refAnswer ) {
            System.out.println("  Good, said " + stuAnswer + " for " + pt.toString());
            pointsEarned += 10;
        }
        else {
            System.out.println("  Error, said " + stuAnswer + " for " + pt.toString());
            passed = false;
        }
        return passed;
    }
    
    private static void logScore() {
        
        System.out.println("1 >> Score: " + pointsEarned + " / " + 330);
    }
}
