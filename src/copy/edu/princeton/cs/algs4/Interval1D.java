
/******************************************************************************
*  Compilation:  javac Interval1D.java
 *  Execution:    java Interval1D
 *  Dependencies: StdOut.java
 *  
 *  1-dimensional interval data type.
 *
 ******************************************************************************/

package copy.edu.princeton.cs.algs4;

import java.util.ArrayList;
import java.util.Comparator;

/**
 *  The {@code Interval1D} class represents a one-dimensional interval.
 *  The interval is <em>closed</em>—it contains both endpoints.
 *  Intervals are immutable: their values cannot be changed after they are created.
 *  The class {@code Interval1D} includes methods for checking whether
 *  an interval contains a point and determining whether two intervals intersect.
 *  <p>
 *  For additional documentation, 
 *  see <a href="http://algs4.cs.princeton.edu/12oop">Section 1.2</a> of 
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne. 
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Interval1D {

    /**
     * Compares two intervals by min endpoint.
     */
    public static final Comparator<Interval1D> MIN_ENDPOINT_ORDER  = new MinEndpointComparator();

    /**
     * Compares two intervals by max endpoint.
     */
    public static final Comparator<Interval1D> MAX_ENDPOINT_ORDER = new MaxEndpointComparator();

    /**
     * Compares two intervals by length.
     */
    public static final Comparator<Interval1D> LENGTH_ORDER = new LengthComparator();

    private final double min;
    private final double max;

    /**
     * Initializes a closed interval [min, max].
     *
     * @param  min the smaller endpoint
     * @param  max the larger endpoint
     * @throws IllegalArgumentException if the min endpoint is greater than the max endpoint
     * @throws IllegalArgumentException if either {@code min} or {@code max}
     *         is {@code Double.NaN}, {@code Double.POSITIVE_INFINITY} or
     *         {@code Double.NEGATIVE_INFINITY}

     */
    public Interval1D(double min, double max) {
        if (Double.isInfinite(min) || Double.isInfinite(max))
            throw new IllegalArgumentException("Endpoints must be finite");
        if (Double.isNaN(min) || Double.isNaN(max))
            throw new IllegalArgumentException("Endpoints cannot be NaN");

        // convert -0.0 to +0.0
        if (min == 0.0) min = 0.0;
        if (max == 0.0) max = 0.0;

        if (min <= max) {
            this.min = min;
            this.max = max;
        }
        else throw new IllegalArgumentException("Illegal interval");
    }

    /**
     * Returns the left endpoint of this interval.
     *
     * @return the left endpoint of this interval
     * @deprecated Replaced by {@link #min()}.
     */
    @Deprecated
    public double left() { 
        return min;
    }

    /**
     * Returns the right endpoint of this interval.
     * @return the right endpoint of this interval
     * @deprecated Replaced by {@link #max()}.
     */
    @Deprecated
    public double right() { 
        return max;
    }

    /**
     * Returns the min endpoint of this interval.
     *
     * @return the min endpoint of this interval
     */
    public double min() { 
        return min;
    }

    /**
     * Returns the max endpoint of this interval.
     *
     * @return the max endpoint of this interval
     */
    public double max() { 
        return max;
    }

    
    /**
     * Returns the center point of this interval.
     *
     * @return the center point of this interval
     * 
     * @author Elvis Koci
     */
    public double center(){
    	return ((max-min)/2) + min;
    }
    
    /**
     * Returns true if this interval intersects the specified interval.
     *
     * @param  that the other interval
     * @return {@code true} if this interval intersects the argument interval;
     *         {@code false} otherwise
     */
    public boolean intersects(Interval1D that) {
        if (this.max < that.min) return false;
        if (that.max < this.min) return false;
        return true;
    }

    /**
     * Returns true if this interval contains the specified value.
     *
     * @param x the value
     * @return {@code true} if this interval contains the value {@code x};
     *         {@code false} otherwise
     */
    public boolean contains(double x) {
        return (min <= x) && (x <= max);
    }

    /**
     * Return the length of the intersection between this interval and the given one (that interval)
     * 
     * @param that the other interval
     * @return a double value that represents the length of the intersection between the two intervals
     * A negative value is returned when intervals do not intersect.  
     * 
     * @author Elvis Koci
     */
    public double getLengthOfIntersection(Interval1D that){
    	
    	if(!this.intersects(that)) return -1.0;
    	
    	double min = this.contains(that.min())? that.min(): this.min();
    	double max = this.contains(that.max())? that.max(): this.max();
    	
    	return max - min;
    }
    
    
    /**
     * Get the intersection of this interval with the given (that) interval.
     * 
     * @param that the other interval
     * @return an 1DInterval, which represents the intersection between this and the given interval.   
     * 
     * @author Elvis Koci
     */
    public Interval1D getIntersection(Interval1D that){
    	
    	if(!this.intersects(that)) return null; // no intersection
    	
    	double min = this.contains(that.min())? that.min(): this.min();
    	double max = this.contains(that.max())? that.max(): this.max();
    	
    	return new Interval1D(min,max);
    }
    
    
    /**
     * Get the symmetric difference of this interval with the
     * intersection between this interval and that interval (the given interval).
     * 
     * @param that the other interval
     * @return a list of 1DIntervals, representing the symmetric difference   
     * 
     * @author Elvis Koci
     */
    public ArrayList<Interval1D> getNegativeIntersection(Interval1D that){
    	
    	ArrayList<Interval1D> negatives = new ArrayList<Interval1D>(); 
    	
    	// no intersection
    	if(!this.intersects(that)) {
    		negatives.add(this);
    		return negatives; 
    	}
    	
    	double min = this.contains(that.min())? that.min(): this.min();
    	double max = this.contains(that.max())? that.max(): this.max();
    	
    	if(this.min()<min){
    		negatives.add(new Interval1D(this.min(),min));
    	}
    	
    	if(this.max()>max){
    		negatives.add(new Interval1D(max,this.max()));
    	}
    	
    	return negatives;
    }
   
    
    /**
     * Get the minimum distance between the given interval and this interval.
     * 
     * @param that the other 1D interval
     * @return A double value that represent the distance between the intervals. 
     * When the intervals intersect, the distance is 0 (zero);
     * 
     * @author Elvis Koci
     */
    public double getMinDistanceFrom(Interval1D that){
    	
    	if(this.intersects(that)) return 0.0;
    	
    	double minDistance = this.max() - that.min(); 
    	
    	if(minDistance > (that.max() - this.min())){
    		minDistance = (that.max() - this.min()); 
    	}
    	
    	return minDistance;
    }
    
    /**
     * Returns the length of this interval.
     *
     * @return the length of this interval (max - min)
     */
    public double length() {
        return max - min;
    }

    /**
     * Returns a string representation of this interval.
     *
     * @return a string representation of this interval in the form [min, max]
     */
    public String toString() {
        return "[" + min + ", " + max + "]";
    }

    /**
     * Compares this transaction to the specified object.
     *
     * @param  other the other interval
     * @return {@code true} if this interval equals the other interval;
     *         {@code false} otherwise
     */
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Interval1D that = (Interval1D) other;
        return this.min == that.min && this.max == that.max;
    }

    /**
     * Returns an integer hash code for this interval.
     *
     * @return an integer hash code for this interval
     */
    public int hashCode() {
        int hash1 = ((Double) min).hashCode();
        int hash2 = ((Double) max).hashCode();
        return 31*hash1 + hash2;
    }

    // ascending order of min endpoint, breaking ties by max endpoint
    private static class MinEndpointComparator implements Comparator<Interval1D> {
        public int compare(Interval1D a, Interval1D b) {
            if      (a.min < b.min) return -1;
            else if (a.min > b.min) return +1;
            else if (a.max < b.max) return -1;
            else if (a.max > b.max) return +1;
            else                    return  0;
        }
    }

    // ascending order of max endpoint, breaking ties by min endpoint
    private static class MaxEndpointComparator implements Comparator<Interval1D> {
        public int compare(Interval1D a, Interval1D b) {
            if      (a.min < b.max) return -1;
            else if (a.min > b.max) return +1;
            else if (a.min < b.min) return -1;
            else if (a.min > b.min) return +1;
            else                    return  0;
        }
    }

    // ascending order of length
    private static class LengthComparator implements Comparator<Interval1D> {
        public int compare(Interval1D a, Interval1D b) {
            double alen = a.length();
            double blen = b.length();
            if      (alen < blen) return -1;
            else if (alen > blen) return +1;
            else                  return  0;
        }
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

