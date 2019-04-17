/**
 * 
 */
package de.tudresden.xr.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import copy.edu.princeton.cs.algs4.Interval1D;
import copy.edu.princeton.cs.algs4.Interval2D;
import de.tudresden.xr.model.annotation.CellAnnotation;
import de.tudresden.xr.model.enums.AnnotationLabel;
import de.tudresden.xr.model.region.LabelRegion;

/**
 *
 */
public class PreprocessingUtils {
	
		
	/**
	 * Get intervals per label per row in the cell matrix
	 * 
	 * @param cellMatrix
	 * @param usePredictedLabels
	 * @param useOriginalIndices
	 * 
	 * @return
	 * @throws Exception
	 */
	public static TreeMap<Integer, HashMap<AnnotationLabel, ArrayList<LabelRegion>>> createLabelRowIntervals(
			List<List<CellAnnotation>> cellMatrix, boolean usePredictedLabels, boolean useOriginalIndices) {

		// maintain list of row intervals per row per label. 
		TreeMap<Integer, HashMap<AnnotationLabel, ArrayList<LabelRegion>>> intervalMap = 
				new TreeMap<Integer, HashMap<AnnotationLabel, ArrayList<LabelRegion>>>();
		
		for (int i = 0; i < cellMatrix.size(); i++) {
			
			AnnotationLabel prevLabel = null;
			Interval2D prevRowInterval = null;
			
			// create a hash map holding all the intervals per label for the current row
			// Row Interval Map (RIM)
			HashMap<AnnotationLabel, ArrayList<LabelRegion>> rim = new HashMap<AnnotationLabel, ArrayList<LabelRegion>>();
						
			// find all intervals per label in the row
			for (int j = 0; j < cellMatrix.get(i).size(); j++) {
				
				CellAnnotation ca = cellMatrix.get(i).get(j);
				AnnotationLabel currentLabel  = null;
				
				int colNum = i;
				int rowNum = j;	
				if(ca!=null){
									
					currentLabel = ca.getAnnotationLabel();				
					if(useOriginalIndices){
						colNum = ca.getColumnNum();
						rowNum = ca.getRowNum();
					}
				}
			
				
				if (prevRowInterval == null && ca != null) {				
					Interval1D xInteval = new Interval1D(colNum, colNum + 1);
					Interval1D yInteval = new Interval1D(rowNum, rowNum + 1);

					prevRowInterval = new Interval2D(xInteval, yInteval);
					prevLabel = currentLabel;
					
				} else {
					
					if (ca == null) {
						if (prevRowInterval != null) {

							if (!rim.containsKey(prevLabel)) {
								rim.put(prevLabel, new ArrayList<LabelRegion>());
							}

							ArrayList<LabelRegion> intervalList = rim.get(prevLabel);
							intervalList.add(new LabelRegion(prevLabel, prevRowInterval));
							// rim.put(prevLabel, intervalList);

							prevRowInterval = null;
							prevLabel = null;
						}
					}else if (prevLabel.compareTo(currentLabel) != 0) {

						if (!rim.containsKey(prevLabel)) {
							rim.put(prevLabel, new ArrayList<LabelRegion>());
						}

						ArrayList<LabelRegion> intervalList = rim.get(prevLabel);
						intervalList.add(new LabelRegion(prevLabel, prevRowInterval));
						// rim.put(prevLabel, intervalList);

						Interval1D newXInteval = new Interval1D(colNum, colNum + 1);
						Interval1D newYInteval = new Interval1D(rowNum, rowNum + 1);

						prevRowInterval = new Interval2D(newXInteval, newYInteval);
						prevLabel = currentLabel;
						
					}else {
						
						Interval1D newXInteval = new Interval1D(prevRowInterval.getIntervalX().min(), colNum + 1);
						Interval1D newYInteval = new Interval1D(rowNum, rowNum + 1);

						prevRowInterval = new Interval2D(newXInteval, newYInteval);
					}
				}
				
				if (ca != null && j == (cellMatrix.get(i).size() - 1)) {

					if (!rim.containsKey(prevLabel)) {
						rim.put(prevLabel, new ArrayList<LabelRegion>());
					}

					ArrayList<LabelRegion> intervalList = rim.get(prevLabel);
					intervalList.add(new LabelRegion(prevLabel, prevRowInterval));
					//rim.put(prevLabel, intervalList);
				}
			} 
			
			intervalMap.put(i, rim);
		}
		
		return intervalMap;
	}
}
