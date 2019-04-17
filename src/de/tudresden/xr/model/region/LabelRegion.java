/**
 * 
 */
package de.tudresden.xr.model.region;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

import org.apache.poi.ss.util.CellRangeAddress;

import copy.edu.princeton.cs.algs4.Interval2D;
import de.tudresden.xr.model.enums.AnnotationLabel;


/**
 */
public class LabelRegion {
	
	private final AnnotationLabel label;
	private final Interval2D region;
	
	
	/**
	 * A map that contains all the row intervals that form this LabelRegion   
	 */
	private TreeMap<Integer, ArrayList<LabelRegion>> rowIntervals;
		
	/**
     * Compares two label regions by the polar distance of their diagonals (minimum and maximum point).
     */
    public static final Comparator<LabelRegion> POLAR_ORDER = new PolarOrder();

	/**
     * Compares two label regions by the minimum row number (minimum y coordinate)
     */
    public static final Comparator<LabelRegion> MIN_ROW_ORDER = new MinRowOrder();
    
	/**
     * Compares two label regions by the minimum column number (minimum x coordinate)
     */
    public static final Comparator<LabelRegion> MIN_COLUMN_ORDER = new MinColumnOrder();
    
    /**
     * Compares two label regions by their area
     */
    public static final Comparator<LabelRegion> AREA_ORDER = new AreaOrder();
       
    /**
     * Compares two label regions by their width
     */
    public static final Comparator<LabelRegion> WIDTH_ORDER = new WidthOrder();
    
    /**
     * Compares two label regions by their height
     */
    public static final Comparator<LabelRegion> HEIGHT_ORDER = new HeightOrder();
    
	/**
	 * 
	 * @param label
	 * @param cellRegion
	 */
	public LabelRegion(AnnotationLabel label, Interval2D cellRegion){	
		this.label = label;
		this.region = cellRegion;
	}
	
	/**
	 * @return the label
	 */
	public AnnotationLabel getLabel() {
		return label;
	}

	/**
	 * @return the region
	 */
	public Interval2D getRegion() {
		return region;
	}


	/**
	 * @return the rowIntervals
	 */
	public TreeMap<Integer, ArrayList<LabelRegion>> getRowIntervals() {
		return rowIntervals;
	}

	/**
	 * @param intervals the rowIntervals to set
	 */
	public void setRowIntervals(TreeMap<Integer, ArrayList<LabelRegion>> intervals) {
		this.rowIntervals = intervals;
	}

	/**
	 * Check if the label region is in a single column
	 * @return
	 */
	public boolean isSingleColumn(){
		Interval2D intv = this.getRegion();
		double width = intv.getIntervalX().length();
		
		return width == 1;
	}
	
	/**
	 * Check if the label region is in single row
	 * @return
	 */
	public boolean isSingleRow(){
		Interval2D intv = this.getRegion();
		double height = intv.getIntervalY().length();
		
		return height == 1;
	}

	/**
	 * Check if the label region has more than one row and column
	 * @return
	 */
	public boolean isMatrix(){
		Interval2D intv = this.getRegion();
		double height = intv.getIntervalY().length();
		double width = intv.getIntervalX().length();
		
		return height > 1 && width > 1;
	}
		
	/**
	 * Check if this label region contains the given label region
	 * @param that the other label region
	 * @return true if this label region contains with the given label region; false otherwise
	 */
	public boolean contains(LabelRegion that){
		
		Interval2D thisIntv = this.getRegion(); 
		Interval2D thatIntv = that.getRegion();
		
		return thisIntv.contains(thatIntv);
	}
	
	
	/**
	 * Check if this label region intersects the given label region
	 * @param that the other label region
	 * @return true if this label region intersects with the given label region; false otherwise
	 */
	public boolean intersects(LabelRegion that){
		
		Interval2D thisIntv = this.getRegion(); 
		Interval2D thatIntv = that.getRegion();
		
		return thisIntv.intersects(thatIntv);
	}
	
	
	/**
	 * Get the distance from this label region to the given one
	 * @param that the other label region
	 * @return the distance between the two label regions as double value
	 */
	public double getDistanceFromRegion(LabelRegion that){
		
		Interval2D thisIntv = this.getRegion(); 
		Interval2D thatIntv = that.getRegion();
	
		return thisIntv.distanceFrom(thatIntv);
	}
	
	
	
	
	/**
	 * Does this label region equal the given object
	 * @param other the other label region
	 * @return true if this label region equals the other label region; false otherwise
	 */
	public boolean equals(Object other) {
		
		if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
		LabelRegion that = (LabelRegion) other;
        
        if(this.getLabel()!= that.getLabel()) return false;
        			
		if(!this.region.equals(that.getRegion())){
			return false;
		}
		
		if(this.getRowIntervals()!=null && !this.getRowIntervals().isEmpty()){
			return this.getRowIntervals().equals(that.getRowIntervals());
		}else{
			return that.getRowIntervals()==null || that.getRowIntervals().isEmpty();
		}
	}
	
	
	/**
     * Returns an integer hash code for this label region.  
     * @return an integer hash code for this label region 
     */
	public int hashCode() {
		
		int labelHash = this.label.ordinal(); 
		
		int intervalsHash = 0;
		if(this.getRowIntervals()!=null && !this.getRowIntervals().isEmpty()){
			intervalsHash = this.getRowIntervals().hashCode(); 
		}
		return 31 * labelHash + intervalsHash + this.region.hashCode();
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		CellRangeAddress cra  = this.getAsCellRangeAddress();
		
		String label = this.label==null? "null" : this.label.name(); 
		
		return "("+label+", ["+cra.formatAsString()+"])"; //this.getLabel().name()+", "+ cra.formatAsString();
	}
	
	
	public String getAddressStringFormatted(){
		
		CellRangeAddress cra  = this.getAsCellRangeAddress();
		
		return cra.formatAsString();
	}
	
	public CellRangeAddress getAsCellRangeAddress(){
		
		CellRangeAddress cra  = new CellRangeAddress( 
				(int) this.region.getIntervalY().min(), 
				(int) (this.region.getIntervalY().max() - 1),
				(int) this.region.getIntervalX().min(), 
				(int) (this.region.getIntervalX().max() - 1));
		
		return cra;
	}
	
	/**
	 *  compare label regions according to the polar radius of their diagonal (min and max point)
	 */
    private static class PolarOrder implements Comparator<LabelRegion> {
        public int compare(LabelRegion lbIntv1, LabelRegion lbIntv2) {
        	
        	double xMinIntv1 = lbIntv1.getRegion().getIntervalX().min();
        	double yMinIntv1 = lbIntv1.getRegion().getIntervalY().min();
        	
        	double xMinIntv2 = lbIntv2.getRegion().getIntervalX().min();
        	double yMinIntv2 = lbIntv2.getRegion().getIntervalY().min();
        	
            double xDelta = (xMinIntv1*xMinIntv1 + yMinIntv1*yMinIntv1) - 
            		(xMinIntv2*xMinIntv2 + yMinIntv2*yMinIntv2);
            
            if (xDelta < 0) return -1;
            if (xDelta > 0) return +1;
               
            
            double xMaxIntv1 = lbIntv1.getRegion().getIntervalX().max();
        	double yMaxIntv1 = lbIntv1.getRegion().getIntervalY().max();
        	
        	double xMaxIntv2 = lbIntv2.getRegion().getIntervalX().max();
        	double yMaxIntv2 = lbIntv2.getRegion().getIntervalY().max();
        	
            double yDelta = (xMaxIntv1*xMaxIntv1 + yMaxIntv1*yMaxIntv1) - 
            		(xMaxIntv2*xMaxIntv2 + yMaxIntv2*yMaxIntv2);
            
            if (yDelta < 0) return -1;
            if (yDelta > 0) return +1;
            
            return 0;
        }
    }
    
    
    /**
     *  compare label regions according to their min row number (min y coordinate)
     */
    private static class MinRowOrder implements Comparator<LabelRegion> {
        public int compare(LabelRegion lbIntv1, LabelRegion lbIntv2) {
        	
        	double yMinIntv1 = lbIntv1.getRegion().getIntervalY().min();
        	double yMinIntv2 = lbIntv2.getRegion().getIntervalY().min();
        	
        	if(yMinIntv1<yMinIntv2) return -1; 
        	if(yMinIntv1>yMinIntv2) return +1;
                   
        	double xMinIntv1 = lbIntv1.getRegion().getIntervalX().min();
        	double xMinIntv2 = lbIntv2.getRegion().getIntervalX().min();
        	
        	if(xMinIntv1<xMinIntv2) return -1; 
        	if(xMinIntv1>xMinIntv2) return +1;
        	
          	double yMaxIntv1 = lbIntv1.getRegion().getIntervalY().max();
          	double yMaxIntv2 = lbIntv2.getRegion().getIntervalY().max();
          	
          	if(yMaxIntv1<yMaxIntv2) return -1; 
        	if(yMaxIntv1>yMaxIntv2) return +1;
        	
        	double xMaxIntv1 = lbIntv1.getRegion().getIntervalX().max();
          	double xMaxIntv2 = lbIntv2.getRegion().getIntervalX().max();
          	
          	if(xMaxIntv1<xMaxIntv2) return -1; 
        	if(xMaxIntv1>xMaxIntv2) return +1;
          	
            return 0;
        }
    }
    
    /**
     *  compare label regions according to their min col number (min x coordinate)
     */
    private static class MinColumnOrder implements Comparator<LabelRegion> {
        public int compare(LabelRegion lbIntv1, LabelRegion lbIntv2) {
        	
        	double xMinIntv1 = lbIntv1.getRegion().getIntervalX().min();
        	double xMinIntv2 = lbIntv2.getRegion().getIntervalX().min();
        	
        	if(xMinIntv1<xMinIntv2) return -1; 
        	if(xMinIntv1>xMinIntv2) return +1;
        	
        	double yMinIntv1 = lbIntv1.getRegion().getIntervalY().min();
        	double yMinIntv2 = lbIntv2.getRegion().getIntervalY().min();
        	
        	if(yMinIntv1<yMinIntv2) return -1; 
        	if(yMinIntv1>yMinIntv2) return +1;
                          	
        	double xMaxIntv1 = lbIntv1.getRegion().getIntervalX().max();
          	double xMaxIntv2 = lbIntv2.getRegion().getIntervalX().max();
          	
          	if(xMaxIntv1<xMaxIntv2) return -1; 
        	if(xMaxIntv1>xMaxIntv2) return +1;
          	
        	double yMaxIntv1 = lbIntv1.getRegion().getIntervalY().max();
          	double yMaxIntv2 = lbIntv2.getRegion().getIntervalY().max();
          	
          	if(yMaxIntv1<yMaxIntv2) return -1; 
        	if(yMaxIntv1>yMaxIntv2) return +1;
        	
            return 0;
        }
    }
    
    /**
     *  compare label regions according to their height
     */
    private static class HeightOrder implements Comparator<LabelRegion> {
        public int compare(LabelRegion lbIntv1, LabelRegion lbIntv2) {
        	
        	Interval2D intv1 = lbIntv1.getRegion();
        	Interval2D intv2 = lbIntv2.getRegion();
        	
        	if(intv2.getIntervalY().length()>intv1.getIntervalY().length()){
        		return -1;
        	}else if(intv2.getIntervalY().length()<intv1.getIntervalY().length()){
        		return 1;
        	}else{
        		return 0;
        	}
        }
    }
    
    /**
     *  compare label regions according to their width
     */
    private static class WidthOrder implements Comparator<LabelRegion> {
        public int compare(LabelRegion lbIntv1, LabelRegion lbIntv2) {
        	
        	Interval2D intv1 = lbIntv1.getRegion();
        	Interval2D intv2 = lbIntv2.getRegion();
        	
        	if(intv2.getIntervalX().length()>intv1.getIntervalX().length()){
        		return -1;
        	}else if(intv2.getIntervalX().length()<intv1.getIntervalX().length()){
        		return 1;
        	}else{
        		return 0;
        	}
        }
    }
    
    /**
     *  compare label regions according to their area
     */
    private static class AreaOrder implements Comparator<LabelRegion> {
        public int compare(LabelRegion lbIntv1, LabelRegion lbIntv2) {
        	
        	Interval2D intv1 = lbIntv1.getRegion();
        	Interval2D intv2 = lbIntv2.getRegion();
        	
        	if(intv2.area()>intv1.area()){
        		return -1;
        	}else if(intv2.area()<intv1.area()){
        		return 1;
        	}else{
        		return 0;
        	}
        }
    }
}
