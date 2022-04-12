import java.util.ArrayList;

public class prQuadTree<T extends Compare2D<? super T>> {
	// Inner classes for nodes (public so test harness has access)
	public abstract class prQuadNode {

	}

	public class prQuadLeaf extends prQuadNode {
		// Use an ArrayList to support a bucketed implementation later.
		ArrayList<T> Elements;

		public prQuadLeaf(T elem) {
			Elements = new ArrayList<T>();
			Elements.add(elem);
		}

		public prQuadLeaf() {
			Elements = new ArrayList<T>();
		}

//		public T getElement(T elem) {
//			return Elements.get(Elements.indexOf(elem));
//		}
	}

	public class prQuadInternal extends prQuadNode {
		// Use base-type pointers since children can be either leaf nodes
		// or internal nodes.
		prQuadNode NW, NE, SE, SW;
		long xLo, xHi, yLo, yHi;

		public prQuadInternal() {
		}

		public prQuadInternal(long xLo, long xHi, long yLo, long yHi) {
			this.xLo = xLo;
			this.xHi = xHi;
			this.yLo = yLo;
			this.yHi = yHi;
		}

		public void setNW(prQuadNode node) {
			NW = node;
		}

		public void setNE(prQuadNode node) {
			NE = node;
		}

		public void setSE(prQuadNode node) {
			SE = node;
		}

		public void setSW(prQuadNode node) {
			SW = node;
		}

		public prQuadNode getNW() {
			return NW;
		}

		public prQuadNode getNE() {
			return NE;
		}

		public prQuadNode getSW() {
			return SW;
		}

		public prQuadNode getSE() {
			return SE;
		}
	}

	// prQuadTree elements (public so test harness has access)
	public prQuadNode root;
	public long xMin, xMax, yMin, yMax;
	private int load;

	// Initialize quadtree to empty state, representing the specified region.
	// Pre: xMin < xMax and yMin < yMax
	public prQuadTree(long xMin, long xMax, long yMin, long yMax) {
		this.root = null;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		load = 0;
	}

	public prQuadNode getRoot() {
		return this.root;
	}

	// Pre: elem != null
	// Post: If elem lies within the tree's region, and elem is not already
	// present in the tree, elem has been inserted into the tree.
	// Return true iff elem is inserted into the tree.
	public boolean insert(T elem) {

		if (elem == null) {
			return false;
		}
		if (!elem.inBox(xMin, xMax, yMin, yMax))
			return false;

		if (insertHelper(root, elem, xMin, xMax, yMin, yMax)) {
			load++;
			return true;
		}
		return false;

	}

	public void setRoot(prQuadNode node) {
		this.root = node;
	}

	@SuppressWarnings("unchecked")
	private boolean insertHelper(prQuadNode sRoot, T elem, long xLo, long xHi, long yLo, long yHi) {
		// if ( sRoot.getClass().getName().equals("prQuadtree$prQuadLeaf") ) {}
		Direction direction = elem.inQuadrant(xLo, xHi, yLo, yHi);
		if (Math.abs(xHi - xLo) <= 1 &&

				Math.abs(yHi - yLo) <= 1) {
			return false;
		}
		// System.out.println(this.toString());
		// insert into an empty tree
		if (sRoot == null) {
			sRoot = new prQuadLeaf();
			((prQuadLeaf)sRoot).Elements.add(elem);

			setRoot((prQuadTree<T>.prQuadLeaf) sRoot);
			return true;
			
		}
		else if (sRoot.getClass().equals(prQuadLeaf.class)) 
			
		
		{
			prQuadLeaf temp = (prQuadTree<T>.prQuadLeaf) sRoot;
			setRoot(sRoot = new prQuadInternal(xLo, xHi, yLo, yHi));
			
			 insertHelper(sRoot, temp.Elements.get(0), xLo, xHi, yLo, yHi);
			return insertHelper(sRoot, elem, xLo, xHi, yLo, yHi);
		}	
			
			
			
//			if (direction.equals(Direction.NE)) {
//				((prQuadInternal) sRoot).setNE(new prQuadLeaf(elem));
//				return true;
//			} else if (direction.equals(Direction.NW)) {
//				((prQuadInternal) sRoot).setNW(new prQuadLeaf(elem));
//				return true;
//			} else if (direction.equals(Direction.SE)) {
//				((prQuadInternal) sRoot).setSE(new prQuadLeaf(elem));
//				return true;
//			} else if (direction.equals(Direction.SW)) {
//				((prQuadInternal) sRoot).setSW(new prQuadLeaf(elem));
//				return true;
//			} else {
//				return false;
//			}

		

		else if (sRoot.getClass().equals(prQuadInternal.class)) {
			// sRoot = prQuadInternal.class.cast(sRoot);

			if (elem.inQuadrant(xLo, xHi, yLo, yHi).equals(Direction.NW)) {
				if (((prQuadInternal) sRoot).getNW() == null) {
					((prQuadInternal) sRoot).setNW(new prQuadLeaf(elem));
					return true;

				}
				if (((prQuadInternal) sRoot).getNW().getClass().equals(prQuadLeaf.class)) {

					prQuadLeaf temp = (prQuadTree<T>.prQuadLeaf) ((prQuadInternal) sRoot).getNW();
					if (temp.Elements.get(0).equals(elem)) {
						return false;
					}
					((prQuadInternal) sRoot).setNW(new prQuadInternal(xLo, (xHi + xLo)/2, (yHi+yLo) / 2, yHi));
					insertHelper(((prQuadInternal) sRoot).getNW(), temp.Elements.get(0), xLo, (xHi + xLo)/2, (yHi+yLo) / 2, yHi);
					return insertHelper(((prQuadInternal) sRoot).getNW(), elem,xLo, (xHi + xLo)/2, (yHi+yLo) / 2, yHi);
				}

				else {
					return insertHelper(((prQuadInternal) sRoot).getNW(), elem, xLo, (xHi + xLo)/2, (yHi+yLo) / 2, yHi);
				}
			} else if (elem.inQuadrant(xLo, xHi, yLo, yHi).equals(Direction.NE)) {
				if (((prQuadInternal) sRoot).getNE() == null) {
					((prQuadInternal) sRoot).setNE(new prQuadLeaf(elem));
					return true;
				}
				if (((prQuadInternal) sRoot).getNE().getClass().equals(prQuadLeaf.class)) {

					prQuadLeaf temp = (prQuadTree<T>.prQuadLeaf) ((prQuadInternal) sRoot).getNE();
					if (temp.Elements.get(0).equals(elem)) {
						return false;
					}
					((prQuadInternal) sRoot).setNE(new prQuadInternal((xHi + xLo)/2, xHi , (yHi + yLo) / 2, yHi));
					 insertHelper(((prQuadInternal) sRoot).getNE(), temp.Elements.get(0), (xHi + xLo)/2, xHi , (yHi + yLo) / 2, yHi);
					return insertHelper(((prQuadInternal) sRoot).getNE(), elem,(xHi + xLo)/2, xHi , (yHi + yLo) / 2, yHi);
					
				} else {
					return insertHelper(((prQuadInternal) sRoot).getNE(), elem, (xHi + xLo)/2, xHi , (yHi + yLo) / 2, yHi);
				}
			} else if (elem.inQuadrant(xLo, xHi, yLo, yHi).equals(Direction.SW)) {
				if (((prQuadInternal) sRoot).getSW() == null) {
					((prQuadInternal) sRoot).setSW(new prQuadLeaf(elem));
					return true;
				}
				if (((prQuadInternal) sRoot).getSW().getClass().equals(prQuadLeaf.class)) {
					
					prQuadLeaf temp = (prQuadTree<T>.prQuadLeaf) ((prQuadInternal) sRoot).getSW();
					
					if (temp.Elements.get(0).equals(elem)) {
						return false;
					}
					
					((prQuadInternal) sRoot).setSW(new prQuadInternal(xLo, (xHi + xLo)/ 2, yLo, (yHi + yLo)/2));
					insertHelper(((prQuadInternal) sRoot).getSW(), temp.Elements.get(0), xLo, (xHi + xLo)/ 2, yLo, (yHi + yLo)/2);
					return insertHelper(((prQuadInternal) sRoot).getSW(), elem, xLo, (xHi + xLo)/ 2, yLo, (yHi + yLo)/2);
					
				} else {
					return insertHelper(((prQuadInternal) sRoot).getSW(), elem, xLo, (xHi + xLo)/ 2, yLo, (yHi + yLo)/2);
				}
			} else if (elem.inQuadrant(xLo, xHi, yLo, yHi).equals(Direction.SE)) {
				if (((prQuadInternal) sRoot).getSE() == null) {
					((prQuadInternal) sRoot).setSE(new prQuadLeaf(elem));
					return true;
				}
				if (((prQuadInternal) sRoot).getSE().getClass().equals(prQuadLeaf.class)) {

					prQuadLeaf temp = (prQuadTree<T>.prQuadLeaf) ((prQuadInternal) sRoot).getSE();
					if (temp.Elements.get(0).equals(elem)) {
						return false;
					}
					((prQuadInternal) sRoot).setSE(new prQuadInternal((xHi + xLo)/2, xHi, yLo, (yHi+yLo)/2));
					insertHelper(((prQuadInternal) sRoot).getSE(), temp.Elements.get(0), (xHi + xLo)/2, xHi, yLo, (yHi+yLo)/2);
					return insertHelper(((prQuadInternal) sRoot).getSE(), elem, (xHi + xLo)/2, xHi, yLo, (yHi+yLo)/2);
				} else {
					return insertHelper(((prQuadInternal) sRoot).getSE(), elem, (xHi + xLo)/2, xHi, yLo, (yHi+yLo)/2);
				}
			} else {
				System.out.print("Invalid quadrant");
				return false;
			}
		} else {
			System.out.print("leaf");
			return false;
		}


	}

	// Pre: elem != null
	// Returns reference to an element x within the tree such that
	// elem.equals(x)is true, provided such a matching element occurs within
	// the tree; returns null otherwise.
	public T find(T Elem) {
		return findHelper(this.getRoot(), Elem);
	}
	
	@SuppressWarnings("unchecked")
	private T findHelper(prQuadNode sRoot, T elem) {
		
		if(sRoot == null) {
			return null;
		}
		else if(sRoot.getClass().equals(prQuadLeaf.class)) {
			if( ((prQuadLeaf)sRoot).Elements.get(0).equals(elem)) {
				return ((prQuadLeaf)sRoot).Elements.get(0);
			}
			else { return null;}
		}
		else {
			Direction direction = elem.inQuadrant(((prQuadInternal) sRoot).xLo, ((prQuadInternal) sRoot).xHi, ((prQuadInternal) sRoot).yLo, ((prQuadInternal) sRoot).yHi);

			if (direction.equals(Direction.NE)) {
				return findHelper(((prQuadInternal) sRoot).getNE(), elem);
				
			} else if (direction.equals(Direction.NW)) {
				return findHelper(((prQuadInternal) sRoot).getNW(), elem);
			} else if (direction.equals(Direction.SE)) {
				return findHelper(((prQuadInternal) sRoot).getSE(), elem);
			} else if (direction.equals(Direction.SW)) {
				return findHelper(((prQuadInternal) sRoot).getSW(), elem);
			} else {
				return null;
			}
		}
		
	}
	// Pre: xLo < xHi and yLo < yHi
	// Returns a collection of (references to) all elements x such that x is
	// in the tree and x lies at coordinates within the defined rectangular
	// region, including the boundary of the region.
	public ArrayList<T> find(long xLo, long xHi, long yLo, long yHi) {
		ArrayList<T> array= new ArrayList<T>();
		findHelper(array, this.getRoot(), xLo, xHi, yLo, yHi);
		return array;
	}
	private void findHelper(ArrayList<T> array,prQuadNode sRoot, long xLo, long xHi, long yLo, long yHi){
		
		if(sRoot == null) {
			return;
		}
		if(sRoot.getClass().equals(prQuadLeaf.class)) {
			long x = ((prQuadLeaf)sRoot).Elements.get(0).getX();
			long y = ((prQuadLeaf)sRoot).Elements.get(0).getY();
			if(x >= xLo && x<=xHi && y <= yHi && y>=yLo ) {
				array.add(((prQuadLeaf)sRoot).Elements.get(0));
				 
			}
			
		}
		else if (sRoot.getClass().equals(prQuadInternal.class)) {
			 findHelper(array, ((prQuadInternal) sRoot).getNW(), xLo, xHi, yLo, yHi);
			 findHelper(array, ((prQuadInternal) sRoot).getNE(), xLo, xHi, yLo, yHi);
			 findHelper(array, ((prQuadInternal) sRoot).getSE(), xLo, xHi, yLo, yHi);
			 findHelper(array, ((prQuadInternal) sRoot).getSW(), xLo, xHi, yLo, yHi);
		}
	}
}
