// Runs tests on a PR quadtree generic.
//
// Used with a file seed.txt which was created by prQuadGenerator.jar.
// Uses the saved seed value in that file to initialize a random generator.
// If there is no such seed file, the use of this driver is fairly useless.
//
// Assuming no runtime errors occur, creates the output files:
//    - TestTreeInitialization.txt
//    - TestInsertion.txt
//    - TestRegionSearch.txt
//
// Use LogComparator.jar to compare those files to the corresponding files
// that were created by prQuadGenerator.jar, using the same seed value.
//
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.Vector;
import java.util.Random;

public class testQuadTree {
	
	static int xMin = 0;          // world is [0, 2^15] on all sides
	static int xMax = 1 << 15;
	static int yMin = 0;
	static int yMax = 1 << 15;
	static Vector<Point> data;
	static Vector<Long> seps;     // holds subregion boundaries 
	static long randSeed = -1;
    
	public static void main(String[] args) throws IOException {
		
		Long randSeed = 0L;
        File sFile = new File("seed.txt");
        if ( sFile.exists() ) {
			RandomAccessFile seedFile = new RandomAccessFile(sFile, "r");
			randSeed = Long.parseLong(seedFile.readLine());
			seedFile.close();
		}
		else {
		   randSeed = System.currentTimeMillis();
           FileWriter sfile = new FileWriter("seed.txt");
           sfile.write( randSeed.toString() + "\n" );
           sfile.close();
	   }
        
		seps = new Vector<Long>();
		computePartition(10);
		data = new Vector<Point>();
		generatePoints();
		
		checkScatterAll();
		
		Lewis robbie = new Lewis(xMin, xMax, yMin, yMax, data, randSeed, false);
		
		try {
			try {
			   checkTreeInitialization(robbie);
			   System.out.print(" Completed test of quadtree initialization.\n");			}
			catch ( Exception e ) {
			   System.out.print("Exception caught while testing tree initialization: " + e + "\n");
			   System.out.print("Aborting remaining tests.\n");
			   return;
			}

			try {
			   checkInsertion(robbie);
			   System.out.print(" Completed test of quadtree insertion.\n");
			}
			catch ( Exception e ) {
			   System.out.print("Exception caught while testing insertion: " + e + "\n");
			   System.out.print("Aborting remaining tests.\n");
			   return;
			}

			try {
			   checkRegionSearch(robbie);
			   System.out.print(" Completed test of quadtree region search.\n");
			}
			catch ( Exception e ) {
				System.out.print("Exception caught while testing region search: " + e + "\n");
			}
		} 
		catch (Exception e) {
			System.out.print("Exception caught in main: " + e.getMessage() + "\n");
		}
	}
	
	private static void checkTreeInitialization(Lewis robbie) throws IOException {
		
	    robbie.checkTreeInitialization();
	}
	
	private static void checkInsertion(Lewis robbie) throws IOException {
		
       robbie.checkInsertion();
	}
	
	private static void checkRegionSearch(Lewis robbie) throws IOException {
		
       robbie.checkRegionSearch();
	}

	private static void generatePoints() throws IOException {
		
		Random source = new Random( randSeed );
		int numPts =  30;// + Math.abs(source.nextInt()) % 6;
		
		int pt = 0;
		while ( pt < numPts ) {
			long x = Math.abs(source.nextInt()) % xMax;
			long y = Math.abs(source.nextInt()) % yMax;
			
			if ( seps.contains(x) ) {
				++x;
			}
			if ( seps.contains(y) ) {
				++y;
			}
			
			Point nxt = new Point(x, y);
			//System.out.println(nxt);
			if ( checkScatterOK( nxt, 4L) ) {
			   if ( !data.contains(nxt) ) {
				   ++pt;
			      data.add(nxt);
			   }
			   else {
				   System.out.println("too close");
			   }
			}
         else {
            System.out.println("checkScatterOK() said no");
         }
		}
	}
	
	private static boolean checkScatterOK(Point A, long Min) {
		
		for (int i = 0; i < data.size(); i++) {
			Point N = data.get(i);
			if ( taxiDistance(A, N) < Min )
				return false;
		}
		return true;
	}
	
	private static long taxiDistance(Point A, Point B) {
		
		return Math.abs(A.getX() - B.getX()) + Math.abs(A.getY() - B.getY());
	}
	
	private static long checkScatterAll() {
		
		long minimumSeparation = (1 << 20);
		for (int i = 0; i < data.size(); i++) {
			Point A = data.get(i);
			for (int j = 0; j < data.size(); j++) {
				if ( j != i ) {
					Point B = data.get(j);
					long currSeparation = taxiDistance(A, B);
					if ( currSeparation < minimumSeparation )
						minimumSeparation = currSeparation;
				}
			}
		}
		return minimumSeparation;
	}
	
	private static void computePartition(int Divisions) {
		
		int numParts = 1 << Divisions;
		int Step = (xMax - xMin) / numParts;
		for (int lvl = 0; lvl <= numParts; lvl++) {
			
			long x = xMin + lvl * Step;
			seps.add( x );
		}
	}
}
