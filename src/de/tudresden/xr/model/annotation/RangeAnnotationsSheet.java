/**
 * 
 */
package de.tudresden.xr.model.annotation;


import java.util.LinkedHashMap;



/**
 */
public class RangeAnnotationsSheet {
	
	protected static final String name = "Range_Annotations_Data";
	private static String startColumn = "A";
	private static int startRow = 1; 
	
	/**
	 * This linked hash map stores the names of the fields used in the Annotation Data Sheet and their default order. 
	 */
	private static final LinkedHashMap<String, Integer> fields;
	static
    {
		fields = new LinkedHashMap<String,Integer>();
		fields.put("Sheet.Name", 0); // required 
		fields.put("Sheet.Index", 1); // required
		fields.put("Annotation.Label", 2); // required
		fields.put("Annotation.Name", 3); // required
		fields.put("Annotation.Range", 4); // required
		fields.put("Annotation.Parent", 5); // required
		fields.put("TotalCells", 6); // optional
		fields.put("EmptyCells", 7); // optional
		fields.put("ConstantCells", 8); // optional
		fields.put("FormulaCells", 9); // optional
		fields.put("HasMergedCells", 10); // optional
		fields.put("Rows", 11); // optional
		fields.put("Columns", 12); // optional
    }
	

	/**
	 * @return the name
	 */
	public static String getName() {
		return name;
	}


	/**
	 * @return the startColumn
	 */
	public static String getStartColumn() {
		return startColumn;
	}


	/**
	 * @return the startRow
	 */
	public static int getStartRow() {
		return startRow;
	}


	/**
	 * @return the fields
	 */
	public static LinkedHashMap<String, Integer> getFields() {
		return fields;
	}
	
}
