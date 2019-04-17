/******************************************************************************
 *  Compilation:  javac Interval2D.java
 *  Execution:    java Interval2D
 *  Dependencies: StdOut.java Interval1D.java StdDraw.java
 *  
 *  2-dimensional interval data type.
 *
 ******************************************************************************/

package copy.edu.princeton.cs.algs4;

/**
 *  The {@code Interval2D} class represents a closed two-dimensional interval,
 *  which represents all points (x, y) with both {@code xmin <= x <= xmax} and
 *  {@code ymin <= y <= ymax}.
 *  Two-dimensional intervals are immutable: their values cannot be changed
 *  after they are created.
 *  The class {@code Interval2D} includes methods for checking whether
 *  a two-dimensional interval contains a point and determining whether
 *  two two-dimensional intervals intersect.
 *  <p>
 *  For additional documentation, 
 *  see <a href="http://algs4.cs.princeton.edu/12oop">Section 1.2</a> of 
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne. 
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Interval2D {
    private final Interval1D x;
    private final Interval1D y;

    
    /**
     * Initializes a two-dimensional interval.
     * 
     * @param x the one-dimensional interval of x-coordinates
     * @param y the one-dimensional interval of y-coordinates
     */
    public Interval2D(Interval1D x, Interval1D y) {
        this.x = x;
        this.y = y;
    }
    
    
    /**
     * Is the interval longer horizontally (wider) rather than vertically (taller)?
     * 
     * @return true if the horizontal length is longer than the vertical length; false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isHorizontal(){
    	
    	double xLength = this.getIntervalX().length();
    	double yLength = this.getIntervalY().length();
    	
    	return xLength>yLength;
    }
    
    
    /**
     * Is the interval longer vertically (taller) rather than horizontally (wider) ?
     * 
     * @return true if the vertical length is longer than the horizontal length; false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isVertical(){
    	
    	double xLength = this.getIntervalX().length();
    	double yLength = this.getIntervalY().length();
    	
    	return yLength>xLength;
    }
    
    
    /**
     * Is the interval a square ?
     * 
     * @return true if the vertical length and horizontal length are equal; false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isSquare(){
    	
    	double xLength = this.getIntervalX().length();
    	double yLength = this.getIntervalY().length();
    	
    	return yLength==xLength;
    }
    
    
    /**
     * Is the interval a unit square ?
     * 
     * @return true if the vertical length and horizontal length are equal to 1; false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isUnitSquare(){
    	
    	double xLength = this.getIntervalX().length();
    	double yLength = this.getIntervalY().length();
    	
    	return (yLength == 1 && xLength == 1);
    }
    
    
    /**
     * Does this two-dimensional interval contain the point p?
     * 
     * @param p the two-dimensional point
     * @return true if this two-dimensional interval contains the point p; false otherwise
     */
    public boolean contains(double pX, double pY) {
        return x.contains(pX) && y.contains(pY);
    }
    
    
    /**
     * Does this two-dimensional interval contain the given (that) two-dimensional interval
     * 
     * @param that the other two-dimensional interval
     * @return true if this two-dimensional interval contains that; false otherwise
     
     * @author Elvis Koci
     */
    public boolean contains(Interval2D that) {
    	
    	return  this.contains(that.getIntervalX().min(),that.getIntervalY().min()) && 
    			this.contains(that.getIntervalX().max(),that.getIntervalY().max());
    }
    
    
    /**
     * Does this two-dimensional interval intersect that two-dimensional interval?
     * 
     * @param that the other two-dimensional interval
     * @return true if this two-dimensional interval intersects
     *    that two-dimensional interval; false otherwise
     */
    public boolean intersects(Interval2D that) { 
        if (!this.x.intersects(that.x)) return false;
        if (!this.y.intersects(that.y)) return false;
        return true;
    }
    
    
    /**
     * Checks if the two intervals intersect (meet) only at a vertex. 
     * In other words, the edges of these intervals do not intersect, except of one vertex
     * @param that the other 2D interval
     * @return true if the intervals intersect at single point that is one of the vertexes; false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isVertexIntersection(Interval2D that){
    	
    	double xIntersection = this.getIntervalX().getLengthOfIntersection(that.getIntervalX());
		double yIntersection = this.getIntervalY().getLengthOfIntersection(that.getIntervalY());
    	
		return xIntersection==0 && yIntersection==0;
    }
    
    
    /**
     * Check if the two intervals cut(intersect) each other edges multiple times, but there 
     * is no full containment of one interval by the other.
     * @param that the other 2D interval
     * @return true if the intervals intersect each others edges multiple times; false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isMultiEdgeIntersection(Interval2D that){ //TODO: rename to overlap
    		
    	if(this.contains(that) || that.contains(this)) return false;
    	
    	double xIntersection = this.getIntervalX().getLengthOfIntersection(that.getIntervalX());
    	double yIntersection = this.getIntervalY().getLengthOfIntersection(that.getIntervalY());
    	
    	return xIntersection>0 && yIntersection>0;
    }
    
    
    /**
     * Check if the two intervals at least partially share one edge. 
     * @param that the other 2D interval
     * @return true if the intervals  at least partially share an edge; false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isSingleEdgeIntersection(Interval2D that){
    	
    	double xIntersection = this.getIntervalX().getLengthOfIntersection(that.getIntervalX());
    	double yIntersection = this.getIntervalY().getLengthOfIntersection(that.getIntervalY());
    	
    	return (xIntersection>0 && yIntersection==0) || (xIntersection==0 && yIntersection>0);
    }
    
    
    /**
     * Get the minimum 2D interval that bounds (contains) this and the given interval. 
     * In more formal terms this is the <a href="https://en.wikipedia.org/wiki/Minimum_bounding_rectangle"> 
     * smallest bounding rectangle</a> for these intervals.
     * 
     * @param that the other interval
     * @return a 2D interval that represents the union between this and the provided interval.
     * 
     * @author Elvis Koci
     */
    public Interval2D getMinimumBoundingRectangle(Interval2D that){ 
    	
    	Interval1D newXInteval = new Interval1D(Math.min(this.getIntervalX().min(), that.getIntervalX().min()), 
				Math.max(this.getIntervalX().max(), that.getIntervalX().max()));
		
		Interval1D newYInteval = new Interval1D(Math.min(this.getIntervalY().min(), that.getIntervalY().min()), 
				Math.max(this.getIntervalY().max(), that.getIntervalY().max()));
		
		return new Interval2D(newXInteval, newYInteval);
    }      
  
    
    /**
     * Check if this interval is located more to the right than the given interval.
     * In other words, check if the minimum_x coordinate for this interval is bigger than  
     * minimum_x coordinate for (that) interval
     * @param that the other interval 
     * @return true if this interval is further to the right than that interval, false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isMoreToTheRightThan(Interval2D that){
    	return that.getIntervalX().min() <  this.getIntervalX().min()? true : false;
    }
    
    
    /**
     * Check if this interval is located on the right of the given interval.
     * This is true when the intervals overlap with respect to the y-axis
     * and the minimum_x coordinate for this interval is bigger than  
     * maximum_x coordinate for (that) interval
     * @param that the other interval 
     * @return true if this interval is on the right of that interval, false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isOnTheRightOf(Interval2D that){
    	
    	if(this.getIntervalX().min() > that.getIntervalX().max()){
	    	if(this.getIntervalY().intersects(that.getIntervalY()) && 
		    	this.getIntervalY().getLengthOfIntersection(that.getIntervalY())>0){
	    		return true;
	    	}
    	}    	
    	return false;
    }
    
    
    /**
     * Check if this interval is located strictly on the right of the given interval.
     * This is true when (i) the minimum_x coordinate for this interval is bigger 
     * than maximum_x coordinate for (that) interval, (ii)  this Y interval
     * is within (enclosed by) that Y interval.
     * @param that the other interval 
     * @return true if this interval is strictly on the right of that interval, false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isStrictlyOnTheRightOf(Interval2D that){
    	
    	if(this.getIntervalX().min() > that.getIntervalX().max()){
	    	if(this.getIntervalY().min() >= that.getIntervalY().min() && 
	    			this.getIntervalY().max() <= that.getIntervalY().max()){
	    		
	    		return true;
	    	}
    	}    	
    	return false;
    }
    
    
    /**
     * Check if this interval's left edge overlaps with that interval's right edge.
     * In other words the minimum x coordinate of this interval equals the maximum
     * x coordinate of that interval, and the vertical edges overlap once projected
     * to the y-axis. The size of the y overlap must be 1 or more.    
     * 
     * @author Elvis Koci
     */
    public boolean isRightAdjacentWith(Interval2D that){
    	
    	if(this.getIntervalX().min()==that.getIntervalX().max()){
    		if(this.getIntervalY().intersects(that.getIntervalY()) && 
		    	this.getIntervalY().getLengthOfIntersection(that.getIntervalY())>0){
	    		return true;
	    	}
    	}
    	
    	return false;
    }
    
    
    /**
     * Check if the minimum_x coordinate for this interval is smaller than the 
     * minimum_x coordinate for the given (that) interval
     * @param that the other interval 
     * @return true if this interval is further to the left than that interval, false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isMoreToTheLeftThan(Interval2D that){
    	return this.getIntervalX().min() <  that.getIntervalX().min()? true : false;
    }
    
    
    /**
     * Check if this interval is located on the left of the given interval.
     * This is true when the intervals intersects with respect to the y-axis
     * and the maximum_x coordinate for this interval is smaller than the 
     * minimum_x coordinate for the given (that) interval
     * @param that the other interval 
     * @return true if this interval is on the left of that interval, false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isOnTheLeftOf(Interval2D that){
    	
    	if(this.getIntervalX().max() < that.getIntervalX().min()){
	    	if(this.getIntervalY().intersects(that.getIntervalY()) && 
	    		this.getIntervalY().getLengthOfIntersection(that.getIntervalY())>0){
	    		return true;
	    	}
    	}    	
    	return false;
    }
    
    
    /**
     * Check if this interval is located strictly on the left of the given interval.
     * This is true when (i) the minimum_x coordinate for this interval is smaller 
     * than maximum_x coordinate for (that) interval, and (ii)  this Y interval
     * is within (enclosed by) that Y interval.
     * @param that the other interval 
     * @return true if this interval is strictly on the left of that interval, false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isStrictlyOnTheLeftOf(Interval2D that){
    	
    	if(this.getIntervalX().max() < that.getIntervalX().min()){
	    	if(this.getIntervalY().min() >= that.getIntervalY().min() && 
	    			this.getIntervalY().max() <= that.getIntervalY().max()){
	    		
	    		return true;
	    	}
    	}    	
    	return false;
    }
    
    
    /**
     * Check if this interval's right edge overlaps with that interval's left edge.
     * In other words the maximum x coordinate of this interval equals the minimum
     * x coordinate of that interval, and the vertical edges overlap once projected
     * to the y-axis. The size of the y overlap must be 1 or more.    
     * 
     * @author Elvis Koci
     */
    public boolean isLeftAdjacentWith(Interval2D that){
    	
    	if(this.getIntervalX().max()==that.getIntervalX().min()){
    		if(this.getIntervalY().intersects(that.getIntervalY()) && 
		    	this.getIntervalY().getLengthOfIntersection(that.getIntervalY())>0){
	    		return true;
	    	}
    	}
    	
    	return false;
    }
    
    
    /**
     * Check if the minimum_y coordinate for this interval is smaller than the 
     * minimum_y coordinate for the given (that) interval
     * @param that the other interval 
     * @return true if this interval is located higher than that interval, false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isHigherThan(Interval2D that){
    	return this.getIntervalY().min() <  that.getIntervalY().min()? true: false;
    }
    
    
    /**
     * Check if this interval in above the given interval
     * Additionally, the method looks for intervals that do not intersect
     * (distance between them is more than 0).
     * However, they must overlap when projected to the x-axis
     * @param that the other interval 
     * @return true if this interval is located above of that interval, false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isAboveOf(Interval2D that){ // TODO: rename isOnTopOf()
    	
    	if( this.getIntervalY().max() < that.getIntervalY().min()){
    		if( this.getIntervalX().intersects(that.getIntervalX()) &&
    			this.getIntervalX().getLengthOfIntersection(that.getIntervalX())>0){
    			return true;
    		}
    	}  	
    	return false;
    }
    
    /**
     * Check if this interval in above the given interval
     * Additionally, the method looks for intervals that overlap when projected to the x-axis,
     * and this X Interval is within (enclosed by) that X interval.   
     * @param that the other interval 
     * @return true if this interval is located strictly on top of that interval, false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isStrictlyAboveOf(Interval2D that){
    	
    	if( this.getIntervalY().max() < that.getIntervalY().min()){
    		if( this.getIntervalX().min() >= that.getIntervalX().min() && 
    				this.getIntervalX().max() <= that.getIntervalX().max() ){
    			
    			return true;
    		}
    	}  	
    	return false;
    }
    
    
    /**
     * Check if this interval's bottom edge overlaps with that interval's top edge.
     * In other words the maximum y coordinate of this interval equals the minimum
     * y coordinate of that interval, and the horizontal edges overlap once projected
     * to the x-axis. The size of the x overlap must be 1 or more.    
     * 
     * @author Elvis Koci
     */
    public boolean isTopAdjacentWith(Interval2D that){
    	
    	if(this.getIntervalY().max()==that.getIntervalY().min()){
    		if(this.getIntervalX().intersects(that.getIntervalX()) && 
		    	this.getIntervalX().getLengthOfIntersection(that.getIntervalX())>0){
	    		return true;
	    	}
    	}
    	
    	return false;
    }
    
        
    /**
     * Check if the minimum_y coordinate for this interval is bigger than the 
     * minimum_y coordinate for the given (that) interval
     * @param that the other interval 
     * @return true if this interval is located lower than that interval, false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isLowerThan(Interval2D that){
    	return that.getIntervalY().min() <  this.getIntervalY().min()? true: false;
    }
    
    
    /**
     * Check if this interval in bellow the given interval
     * Additionally, the method looks for intervals that do not intersect 
     * (distance between them is more than 0).
     * However, they must overlap when projected to the x-axis
     * @param that the other interval 
     * @return true if this interval is located below of that interval, false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isBelowOf(Interval2D that){ //TODO: rename isUnder()
    	
    	if( this.getIntervalY().min() > that.getIntervalY().max()){
    		if( this.getIntervalX().intersects(that.getIntervalX()) &&
    			this.getIntervalX().getLengthOfIntersection(that.getIntervalX())>0){
    			return true;
    		}
    	}   	
    	return false;
    }
    
    
    /**
     * Check if this interval in bellow the given interval.
     * Additionally, the method looks for intervals that overlap when projected to the x-axis,
     * and this X Interval is within (enclosed by) that X interval.  
     * @param that the other interval 
     * @return true if this interval is located strictly below of that interval, false otherwise
     * 
     * @author Elvis Koci
     */
    public boolean isStrictlyBelowOf(Interval2D that){ 
    	
    	if( this.getIntervalY().min() > that.getIntervalY().max()){
    		if( this.getIntervalX().min() >= that.getIntervalX().min() && 
    				this.getIntervalX().max() <= that.getIntervalX().max() ){
    			
    			return true;
    		}
    	}   	
    	return false;
    }
   
    
    /**
     * Check if this interval's top edge overlaps with that interval's bottom edge.
     * In other words the minimum y coordinate of this interval equals the maximum
     * y coordinate of that interval, and the horizontal edges overlap once projected
     * to the x-axis. The size of the x overlap must be 1 or more.    
     * 
     * @author Elvis Koci
     */
    public boolean isBottomAdjacentWith(Interval2D that){
    	
    	if(this.getIntervalY().min()==that.getIntervalY().max()){
    		if(this.getIntervalX().intersects(that.getIntervalX()) && 
		    	this.getIntervalX().getLengthOfIntersection(that.getIntervalX())>0){
	    		return true;
	    	}
    	}
    	
    	return false;
    }
    
    
    /**
     * Get the distance between this interval and the given one 
     * 
     * @param that the other interval 
     * @return the distance from the two interval as a double value
     * 
     * @author Elvis Koci
     */
    public double distanceFrom(Interval2D that){
    	
    	if(this.intersects(that)) return 0.0;
    	
    	Interval2D mostLeft = this.getIntervalX().min() <  that.getIntervalX().min()? this: that;
       	Interval2D mostRight = that.getIntervalX().min() <  this.getIntervalX().min()? this: that;
       	
       	double xDifference = mostLeft.getIntervalX().min() == mostRight.getIntervalX().min() ? 0 : 
       		mostRight.getIntervalX().min() - mostLeft.getIntervalX().max();
       	
       	xDifference = Math.max(0, xDifference);
       	
       	Interval2D upper = this.getIntervalY().min() <  that.getIntervalY().min()? this: that;
       	Interval2D lower = that.getIntervalY().min() <  this.getIntervalY().min()? this: that;
       	
       	double yDifference = upper.getIntervalY().min() == lower.getIntervalY().min() ? 0 : 
       		lower.getIntervalY().min() - upper.getIntervalY().max();
       	
    	yDifference = Math.max(0, yDifference);
        
    	return Math.sqrt(xDifference*xDifference + yDifference*yDifference);
    }
    
    
    /**
     * Get the distance from the ceiling of the given interval
     * @param that the other interval 
     * @return the distance of this interval from ceiling that interval as double value
     * 
     * @author Elvis Koci
     */
    public double distanceFromTheCeilingOf(Interval2D that){

    	double yMin = that.getIntervalY().min();
    	Interval1D ySegment = new Interval1D(yMin, yMin);
    	
    	// def "ceiling": the line that contains all points from 2D interval at yMin
    	Interval2D ceiling = new Interval2D(that.getIntervalX(), ySegment); 
    	
    	return distanceFrom(ceiling);
    }
    
    
    /**
     * Get the distance from the floor of the given interval
     * @param that the other interval 
     * @return the distance of this interval from floor that interval as double value
     * 
     * @author Elvis Koci
     */
    public double distanceFromTheFloorOf(Interval2D that){

    	double yMax = that.getIntervalY().max();
    	Interval1D ySegment = new Interval1D(yMax, yMax);
    	
    	// def "floor": the line that contains all points from 2D interval at yMax
    	Interval2D floor = new Interval2D(that.getIntervalX(), ySegment); 
    	
    	return distanceFrom(floor);
    }
    
    
    /**
     * Get the distance from the right edge of the given interval
     * @param that the other interval 
     * @return the distance of this interval from right edge of that interval as double value
     * 
     * @author Elvis Koci
     */
    public double distanceFromTheRightEdgeOf(Interval2D that){

    	double xMax = that.getIntervalX().max();
    	Interval1D xSegment = new Interval1D(xMax, xMax);
    	
    	// def "rightEdge": the line that contains all points from 2D interval at xMax
    	Interval2D rightEdge = new Interval2D(xSegment, that.getIntervalY()); 
    	
    	return distanceFrom(rightEdge);
    }
    
    
    /**
     * Get the distance from the left most part of the given interval
     * @param that the other interval 
     * @return the distance of this interval from left edge of that interval as double value
     * 
     * @author Elvis Koci
     */
    public double distanceFromTheLeftEdgeOf(Interval2D that){

    	double xMin = that.getIntervalX().min();
    	Interval1D xSegment = new Interval1D(xMin, xMin);
    	
    	// def "leftEdge": the line that contains all points from 2D interval at xMin
    	Interval2D leftEdge = new Interval2D(xSegment, that.getIntervalY()); 
    	
    	return distanceFrom(leftEdge);
    }
    
    
    /**
     * Returns the area of this two-dimensional interval.
     * 
     * @return the area of this two-dimensional interval
     */
    public double area() {
        return x.length() * y.length();
    }
    
    /**
     * Returns the area of this two-dimensional interval.
     * 
     * @return the area of this two-dimensional interval
     * 
     * @author Elvis Koci
     */
    public double perimeter() {
        return (x.length() + y.length()) * 2;
    }
    
    
    /**
     * Returns the width of this two-dimensional interval.
     * 
     * @return the width of this two-dimensional interval
     * 
     * @author Elvis Koci 
     */
    public double width() {
        return x.length();
    }
    
    
    /**
     * Returns the height of this two-dimensional interval.
     * 
     * @return the height of this two-dimensional interval
     */
    public double height() {
        return y.length();
    }
    
        
    /**
     * Returns a string representation of this two-dimensional interval.
     * 
     * @return a string representation of this two-dimensional interval
     *    in the form [xmin, xmax] x [ymin, ymax]
     */
    public String toString() {
        return x + " x " + y;
    }

    
    /**
     * Does this interval equal the other interval?
     * 
     * @param other the other interval
     * @return true if this interval equals the other interval; false otherwise
     */
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Interval2D that = (Interval2D) other;
        return this.x.equals(that.x) && this.y.equals(that.y);
    }

 
    /**
     * Returns an integer hash code for this interval.  
     * 
     * @return an integer hash code for this interval 
     */
    public int hashCode() {
        int hash1 = x.hashCode();
        int hash2 = y.hashCode();
        return 31*hash1 + hash2;
    }
    
    
    /**
	 * @return the x interval
	 */
	public Interval1D getIntervalX() {
		return x;
	}

	
	/**
	 * @return the y interval
	 */
	public Interval1D getIntervalY() {
		return y;
	}

}

/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
