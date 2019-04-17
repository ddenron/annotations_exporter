/**
 * 
 */
package de.tudresden.xr.model.annotation;

import de.tudresden.xr.model.enums.AnnotationLabel;

/**
 *
 */
public class CellAnnotation extends Annotation<CellAnnotation> implements DependentAnnotation<RangeAnnotation> {
	
	private String sheetName;
	private int sheetIndex;
	private AnnotationLabel annotationLabel;
	private String cellAddress;
	private int rowNum;
	private int columnNum;
	private RangeAnnotation parent;
	
	
	/**
	 * @param sheetName
	 * @param sheetIndex
	 * @param annotationLabel
	 * @param cellAddress
	 * @param parent
	 */
	public CellAnnotation(String sheetName, int sheetIndex, AnnotationLabel annotationLabel, String cellAddress,
			RangeAnnotation parent) {
		super();
		this.sheetName = sheetName;
		this.sheetIndex = sheetIndex;
		this.annotationLabel = annotationLabel;
		this.cellAddress = cellAddress;
		this.parent = parent;
	}


	/**
	 * @param sheetName
	 * @param sheetIndex
	 * @param annotationLabel
	 * @param cellAddress
	 */
	public CellAnnotation(String sheetName, int sheetIndex, AnnotationLabel annotationLabel, String cellAddress) {
		super();
		this.sheetName = sheetName;
		this.sheetIndex = sheetIndex;
		this.annotationLabel = annotationLabel;
		this.cellAddress = cellAddress;
	}

	
	/**
	 * @param sheetName
	 * @param sheetIndex
	 * @param annotationLabel
	 * @param rowNum
	 * @param columnNum
	 * @param parent
	 */
	public CellAnnotation(String sheetName, int sheetIndex, AnnotationLabel annotationLabel, int rowNum, int columnNum,
			RangeAnnotation parent) {
		super();
		this.sheetName = sheetName;
		this.sheetIndex = sheetIndex;
		this.annotationLabel = annotationLabel;
		this.rowNum = rowNum;
		this.columnNum = columnNum;
		this.parent = parent;
	}

	
	/**
	 * @param sheetName
	 * @param sheetIndex
	 * @param annotationLabel
	 * @param rowNum
	 * @param columnNum
	 */
	public CellAnnotation(String sheetName, int sheetIndex, AnnotationLabel annotationLabel, int rowNum,
			int columnNum) {
		super();
		this.sheetName = sheetName;
		this.sheetIndex = sheetIndex;
		this.annotationLabel = annotationLabel;
		this.rowNum = rowNum;
		this.columnNum = columnNum;
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
	 * @return the annotationLabel
	 */
	public AnnotationLabel getAnnotationLabel() {
		return annotationLabel;
	}


	/**
	 * @param annotationLabel the annotationLabel to set
	 */
	protected void setAnnotationLabel(AnnotationLabel annotationLabel) {
		this.annotationLabel = annotationLabel;
	}


	/**
	 * @return the cellAddress
	 */
	public String getCellAddress() {
		return cellAddress;
	}


	/**
	 * @param cellAddress the cellAddress to set
	 */
	protected void setCellAddress(String cellAddress) {
		this.cellAddress = cellAddress;
	}

	
	/**
	 * @return the rowNum
	 */
	public int getRowNum() {
		return rowNum;
	}


	/**
	 * @param rowNum the rowNum to set
	 */
	protected void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}


	/**
	 * @return the columnNum
	 */
	public int getColumnNum() {
		return columnNum;
	}


	/**
	 * @param columnNum the columnNum to set
	 */
	protected void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}

	
	/**
	 * @return the parent annotation 
	 */
	@Override
	public RangeAnnotation getParent() {
		return parent;
	}

	
	/**
	 * @param parent the annotation to set as parent
	 */
	@Override
	public void setParent(RangeAnnotation parent) {
		this.parent = parent;
	}

	
	/**
	 * @return the key
	 */
	@Override
	public String getKey(){
		return this.sheetName+"_"+this.annotationLabel.name()+"_"+this.cellAddress;
	}


	/**
	 *  Check if this cell annotation is equal to the given annotation. 
	 */
	@Override
	public boolean equals(CellAnnotation annotation) {
		
		if(!(annotation instanceof CellAnnotation))
			return false;
		
		CellAnnotation ca = (CellAnnotation) annotation;
		
		if(this.columnNum!=ca.getColumnNum())
			return false;
		
		if(this.rowNum!=ca.getRowNum())
			return false;
		
		if(this.annotationLabel!=ca.getAnnotationLabel())
			return false;
		
		if(this.parent!=ca.getParent())
			return false;
					
		if(this.sheetName.compareTo(ca.getSheetName())!=0)
			return false;

		if(this.sheetIndex!=ca.getSheetIndex())
			return false;
				
		return true;	
	}


	/**
	 * Get hash code for this cell annotation
	 */
	@Override
	public int hashCode() {
		int hash = this.getKey().hashCode();
		hash = hash + this.parent.hashCode();
			
		return hash;
	}	
}
