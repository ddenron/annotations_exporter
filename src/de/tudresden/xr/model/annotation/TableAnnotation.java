/**
 * 
 */
package de.tudresden.xr.model.annotation;

import java.util.HashMap;

import de.tudresden.xr.model.enums.AnnotationLabel;

/**
 *
 */
public class TableAnnotation extends ContainerAnnotation<RangeAnnotation> implements DependentAnnotation<WorksheetAnnotation>{
	
	private String sheetName;
	private int sheetIndex;
	private String tableName;
	private String rangeAddress;
	private WorksheetAnnotation parent;
	
	private HashMap <String, RangeAnnotation> headerRegions;
	private HashMap <String, RangeAnnotation> dataRegions;
	
	
	/**
	 * @param sheetName
	 * @param sheetIndex
	 * @param tableName
	 * @param rangeAddress
	 */
	public TableAnnotation(WorksheetAnnotation parentSheet, String tableName, String rangeAddress) {
		super();
		this.sheetName = parentSheet.getSheetName();
		this.sheetIndex = parentSheet.getSheetIndex();
		this.tableName = tableName;
		this.rangeAddress = rangeAddress;
		this.parent = parentSheet;
		
		this.dataRegions = new HashMap <String, RangeAnnotation>();
		this.headerRegions = new HashMap <String, RangeAnnotation>();
	}
	

	/**
	 * Add a new dependent annotation
	 * @param key a string that is used as an id (key) for the annotation object
	 * @param annotation the annotation object to add
	 * @param isImmediate when true the given annotation is considered an immediate children, otherwise not.
	 * This is to differentiate from cases with multiple levels of hierarchy (containment). In other words,
	 * this ContainerAnnotation might contain children that themselves contain other annotations. 
	 */
	@Override
	protected void addAnnotation(String key, RangeAnnotation annotation, boolean isImmediate){
		super.addAnnotation(key, annotation, isImmediate);
		
		if(annotation.getAnnotationLabel()==AnnotationLabel.Header){
			headerRegions.put(annotation.getKey(), annotation);
		}else if(annotation.getAnnotationLabel()==AnnotationLabel.Data){
			dataRegions.put(annotation.getKey(), annotation);
		}
	}
	
	
	/**
	 * Add a new dependent annotation
	 * @param key a string that is used as an id (key) for the annotation object
	 * @param annotation the annotation object to add
	 */
	@Override
	protected void addImmediateChildren(String key, RangeAnnotation annotation){
		super.addImmediateChildren(key, annotation);
				
		if(annotation.getAnnotationLabel()==AnnotationLabel.Header){
			headerRegions.put(annotation.getKey(), annotation);
		}else if(annotation.getAnnotationLabel()==AnnotationLabel.Data){
			dataRegions.put(annotation.getKey(), annotation);
		}
	}
	
	
	/**
	 * Remove the dependent annotation that has the given key
	 * @param key a string that is used as an id (key) for the annotation object
	 */
	@Override
	protected void removeAnnotation(String key){
		super.removeAnnotation(key);
		
		if(headerRegions.containsKey(key)){
			headerRegions.remove(key);
		}else if (dataRegions.containsKey(key)){
			dataRegions.remove(key);
		}
	}
	
	/**
	 * Remove all annotations depending on (contained by) this
	 */
	@Override
	protected void removeAllDependentAnnotations(){
		super.removeAllDependentAnnotations();
		
		this.dataRegions.clear();
		this.headerRegions.clear();
	}
	
	
	/**
	 * @return the name
	 */
	public String getTableName() {
		return this.tableName;
	}

	
	/**
	 * @return the sheetName
	 */
	public String getSheetName() {
		return sheetName;
	}


	/**
	 * @return the sheetIndex
	 */
	public int getSheetIndex() {
		return sheetIndex;
	}


	/**
	 * @return the rangeAddress
	 */
	public String getRangeAddress() {
		return rangeAddress;
	}


	/**
	 * @return the headerRegions
	 */
	public HashMap<String, RangeAnnotation> getHeaderRegions() {
		return headerRegions;
	}

	/**
	 * @return the dataRegions
	 */
	public HashMap<String, RangeAnnotation> getDataRegions() {
		return dataRegions;
	}

	
	@Override
	public boolean equals(ContainerAnnotation<RangeAnnotation> annotation) {
		
		if(!(annotation instanceof TableAnnotation))
			return false;
		
		TableAnnotation ta = (TableAnnotation) annotation;
				
		if(this.tableName.compareTo(ta.getTableName())!=0)
			return false;
		
		if(!(this.getAllDependentAsMap().equals(ta.getAllDependentAsMap())))
			return false;
		
		return true;
	}

	
	@Override
	public int hashCode() {
		int hash = this.tableName.hashCode();
		
		for (RangeAnnotation val : this.getAllDependentAsList()) {
			hash = hash + val.hashCode();
		}
		
		return hash;
	}


	/**
	 * Get the worksheet that contains this table
	 */
	@Override
	public WorksheetAnnotation getParent() {
		return this.parent;
	}


	/**
	 * parent the worksheet annotation to set as parent (container) of this table annotation 	 
	 */
	@Override
	public void setParent(WorksheetAnnotation parent) {
		this.parent = parent;
	}


	/**
	 * The unique key for this table annotation
	 */
	@Override
	public String getKey() {
		return this.getTableName();
	}

}
