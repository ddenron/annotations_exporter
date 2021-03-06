/**
 * 
 */
package de.tudresden.xr.model.annotation;

import de.tudresden.xr.model.enums.AnnotationLabel;

/**
 */
@Deprecated
public class CompositeRangeAnnotation extends ContainerAnnotation<CompositeRangeAnnotation> implements DependentAnnotation<ContainerAnnotation<CompositeRangeAnnotation>>  {
	
	
	private String sheetName;
	private int sheetIndex;
	private AnnotationLabel annotationLabel; 
	private String name;
	private String rangeAddress;
	private ContainerAnnotation<CompositeRangeAnnotation> parent;
	
	private int cells;
	private int emptyCells;
	private int constantCells;
	private int formulaCells;
	private int rows;
	private int nonEmptyRows;
	private int columns; 
	private int nonEmptyColumns;
	
	private boolean containsMergedCells;
	
	/**
	 * Create a new RangeAnnotation
	 * @param sheetName the name of the sheet where the RangeAnnotation is located 
	 * @param sheetIndex the index of the sheet where the RangeAnnotation is located 
	 * @param label the AnnotationLabel that this RangeAnnotation was given
	 * @param name a string that represents the name (unique id) of the RangeAnnotation
	 * @param address the range address in A1 format (E.g., C1:F2, $G$3)
	 */
	public CompositeRangeAnnotation(String sheetName, int sheetIndex, AnnotationLabel label, String name, String address) {
		this.annotationLabel = label;
		this.name = name;
		this.rangeAddress = address;
		this.sheetName = sheetName;
		this.sheetIndex = sheetIndex;	
	}
	
	
	/**
	 * Create a new RangeAnnotation
	 * @param sheetName the name of the sheet where the RangeAnnotation is located 
	 * @param sheetIndex the index of the sheet where the RangeAnnotation is located 
	 * @param label the AnnotationLabel that this RangeAnnotation was given
	 * @param name a string that represents the name (unique id) of the RangeAnnotation
	 * @param address the range address in A1 format (E.g., C1:F2, $G$3)
	 * @param parent the parent annotation for this range annotation (i.e., the one that contains this annotation)
	 */
	public CompositeRangeAnnotation(String sheetName, int sheetIndex, AnnotationLabel label, String name, String address, ContainerAnnotation<CompositeRangeAnnotation> parent) {
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
	public ContainerAnnotation<CompositeRangeAnnotation> getParent() {
		return parent;
	}

	/**
	 * @param parent the annotation to set as parent for this range annotation
	 */
	@Override
	public void setParent(ContainerAnnotation<CompositeRangeAnnotation> parent) {
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
	public void setSheetName(String sheetName) {
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
	public void setSheetIndex(int sheetIndex) {
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
	public void setAnnotationLabel(AnnotationLabel label) {
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
	public void setName(String name) {
		this.name = name;
	}

	
	/**
	 * @return the key
	 */
	@Override
	public String getKey() {
		return this.name;
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
	public void setRangeAddress(String rangeAddress) {
		this.rangeAddress = rangeAddress;
	}

	/**
	 * @return the cells
	 */
	public int getCells() {
		return cells;
	}

	/**
	 * @param cells the cells to set
	 */
	public void setCells(int cells) {
		this.cells = cells;
	}

	/**
	 * @return the emptyCells
	 */
	public int getEmptyCells() {
		return emptyCells;
	}

	/**
	 * @param emptyCells the emptyCells to set
	 */
	public void setEmptyCells(int emptyCells) {
		this.emptyCells = emptyCells;
	}

	/**
	 * @return the constantCells
	 */
	public int getConstantCells() {
		return constantCells;
	}

	/**
	 * @param constantCells the constantCells to set
	 */
	public void setConstantCells(int constantCells) {
		this.constantCells = constantCells;
	}

	/**
	 * @return the formulaCells
	 */
	public int getFormulaCells() {
		return formulaCells;
	}

	/**
	 * @param formulaCells the formulaCells to set
	 */
	public void setFormulaCells(int formulaCells) {
		this.formulaCells = formulaCells;
	}

	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * @return the nonEmptyRows
	 */
	public int getNonEmptyRows() {
		return nonEmptyRows;
	}

	/**
	 * @param nonEmptyRows the nonEmptyRows to set
	 */
	public void setNonEmptyRows(int nonEmptyRows) {
		this.nonEmptyRows = nonEmptyRows;
	}

	/**
	 * @return the columns
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}

	/**
	 * @return the nonEmptyColumns
	 */
	public int getNonEmptyColumns() {
		return nonEmptyColumns;
	}

	/**
	 * @param nonEmptyColumns the nonEmptyColumns to set
	 */
	public void setNonEmptyColumns(int nonEmptyColumns) {
		this.nonEmptyColumns = nonEmptyColumns;
	}

	/**
	 * @return the hasMergedCells
	 */
	public boolean containsMergedCells() {
		return containsMergedCells;
	}

	/**
	 * @param hasMergedCells the hasMergedCells to set
	 */
	public void setContainsMergedCells(boolean hasMergedCells) {
		this.containsMergedCells = hasMergedCells;
	}

	@Override 
	public String toString() {
			return this.name+" = "+this.getAllDependentAsList().toString();
	}

	@Override
	public boolean equals(ContainerAnnotation<CompositeRangeAnnotation> annotation) {
		
		if(!(annotation instanceof CompositeRangeAnnotation))
			return false;
		
		CompositeRangeAnnotation ra = (CompositeRangeAnnotation) annotation;
				
		if(this.name.compareTo(ra.getName())!=0)
			return false;
		
		if(!(this.getAllDependentAsMap().equals(ra.getAllDependentAsMap())))
			return false;
		
		return true;	
	}

	
	@Override
	public int hashCode() {
		
		int hash = this.name.hashCode();
		
		for (CompositeRangeAnnotation val : this.getAllDependentAsList()) {
			hash = hash + val.hashCode();
		}
		
		return hash;
	}

}
