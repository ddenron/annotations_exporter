/**
 * 
 */
package de.tudresden.xr.model.annotation;

import de.tudresden.xr.model.enums.AnnotationLabel;

/**
 */
public class RangeAnnotation extends ContainerAnnotation<CellAnnotation> implements DependentAnnotation<ContainerAnnotation<RangeAnnotation>>  {
		
	private String sheetName;
	private int sheetIndex;
	private AnnotationLabel annotationLabel; 
	private String name;
	private String rangeAddress;
	private ContainerAnnotation<RangeAnnotation> parent;

	
	/**
	 * Create a new RangeAnnotation
	 * @param sheetName the name of the sheet where the RangeAnnotation is located 
	 * @param sheetIndex the index of the sheet where the RangeAnnotation is located 
	 * @param parent the immediate parent annotation for this range annotation (i.e., the one that contains it). Can be a TableAnnotation or a WorksheetAnnotation.
	 * @param label the AnnotationLabel that this RangeAnnotation was given
	 * @param name a string that represents the name (unique id) of the RangeAnnotation
	 * @param address the range address in A1 format (E.g., C1:F2, $G$3)
	 */
	public RangeAnnotation(String sheetName, int sheetIndex, ContainerAnnotation<RangeAnnotation> parent, AnnotationLabel label, String name, String address) {
		this.annotationLabel = label;
		this.name = name;
		this.rangeAddress = address;
		this.sheetName = sheetName;
		this.sheetIndex = sheetIndex;
		this.parent = parent;
	}
	
	
	/**
	 * @return the parent of this range annotation (i.e., the one that contains this annotation)
	 */
	@Override
	public ContainerAnnotation<RangeAnnotation> getParent() {
		return parent;
	}

	/**
	 * @param parent the annotation to set as parent for this range annotation
	 */
	@Override
	public void setParent(ContainerAnnotation<RangeAnnotation> parent) {
		this.parent=parent;
	}
	
	
	/**
	 * @return the sheetName
	 */
	public String getSheetName() {
		return sheetName;
	}

	/**
	 * @param sheetName the sheetName to set
	 */
	protected void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	/**
	 * @return the sheetIndex
	 */
	public int getSheetIndex() {
		return sheetIndex;
	}

	/**
	 * @param sheetIndex the sheetIndex to set
	 */
	protected void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	/**
	 * @return the annotation label
	 */
	public AnnotationLabel getAnnotationLabel() {
		return annotationLabel;
	}

	/**
	 * @param label the annotation label to set
	 */
	protected void setAnnotationLabel(AnnotationLabel label) {
		this.annotationLabel = label;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	protected void setName(String name) {
		this.name = name;
	}	
	
	/**
	 * @return the rangeAddress
	 */
	public String getRangeAddress() {
		return rangeAddress;
	}

	/**
	 * @param rangeAddress the rangeAddress to set
	 */
	protected void setRangeAddress(String rangeAddress) {
		this.rangeAddress = rangeAddress;
	}

	/**
	 * @return the key
	 */
	@Override
	public String getKey() {
		return this.name;
	}
	
	/**
	 * 
	 */
	@Override 
	public String toString() {
			return this.name+" = "+this.getAllDependentAsList().toString();
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(ContainerAnnotation<CellAnnotation> annotation) {
		
		if(!(annotation instanceof RangeAnnotation))
			return false;
		
		RangeAnnotation ra = (RangeAnnotation) annotation;
				
		if(this.name.compareTo(ra.getName())!=0)
			return false;
		
		if(this.getKey().compareTo(ra.getKey())!=0)
			return false;
		
		if(!(this.getAllDependentAsMap().equals(ra.getAllDependentAsMap())))
			return false;
		
		return true;	
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		
		int hash = this.name.hashCode();
		
		for (CellAnnotation val : this.getAllDependentAsList()) {
			hash = hash + val.hashCode();
		}	
		return hash;
	}
}
