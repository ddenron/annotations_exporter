/**
 * 
 */
package de.tudresden.xr.model.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.tudresden.xr.model.enums.AnnotationLabel;

/**
 *
 */
public class WorkbookAnnotation extends ContainerAnnotation<WorksheetAnnotation> {
	
	/**
	 * The name of the workbook (i.e., Excel file name) 
	 */
	private String workbookName;
	

	/**
	 * @param workbookName
	 */
	public WorkbookAnnotation(String workbookName) {
		this.workbookName = workbookName;
	}

	/**
	 * @return the workbookName
	 */
	public String getWorkbookName() {
		return this.workbookName;
	}

	/**
	 * @param workbookName the workbookName to set
	 */
	protected void setWorkbookName(String workbookName) {
		this.workbookName = workbookName;
	}
	
	
	/**
	 * @param sheetKey a string that uniquely identifies the requested worksheet annotation
	 * @return the worksheet annotation, or null if it is not contained by this workbook.
	 */
	public void addWorksheetAnnotation(String sheetKey, WorksheetAnnotation sha){
		this.addAnnotation(sheetKey, sha, true);
	}
	
	
	/**
	 * @return a copy of the WorksheetAnnotations contained by this workbook as a map (sheetKey->WorksheetAnnotation) 
	 */
	public HashMap<String, WorksheetAnnotation> getWorksheetsMap(){
		return new HashMap<String,WorksheetAnnotation>(this.getImmediateChildrenAsMap());
	}
	
	
	/**
	 * @return a (copy) list of the WorksheetAnnotations contained by this workbook
	 */
	public List<WorksheetAnnotation> getWorksheetsList(){
		return new ArrayList<WorksheetAnnotation>(this.getImmediateChildrenAsMap().values());
	}
	
	
	/**
	 * @param sheetKey a string that uniquely identifies the requested worksheet annotation
	 * @return the worksheet annotation, or null if it is not contained by this workbook.
	 */
	public WorksheetAnnotation getWorksheet(String sheetKey){
		return this.getWorksheetsMap().get(sheetKey);
	}
	
			
	/**
	 * Add a RangeAnnotation 
	 * @param ra an object that represents a RangeAnnotation
	 */
	public void addRangeAnnotation(String sheetKey, RangeAnnotation ra){
		
		if(!this.getWorksheetsMap().containsKey(sheetKey)){
			throw new IllegalArgumentException("The specified sheetKey cannot be found for this workbook!");
		}		
		
		ContainerAnnotation<RangeAnnotation> parent = ra.getParent();
		if(parent instanceof WorksheetAnnotation){
			WorksheetAnnotation sheetParent = (WorksheetAnnotation) parent;
			if(sheetParent.getKey().compareTo(sheetKey)!=0){
				throw new IllegalArgumentException("The given RangeAnnotation has a worksheet as parent. "
						+ "However, the provided sheetKey does not match the (parent) worksheet key!");
			}
			
			if(!sheetParent.equals(this.getWorksheet(sheetKey))){
				throw new IllegalArgumentException("The parentKey and sheetKey match. However, the state of the objects differs!!!");
			}
			
			// add range annotations as immediate child
			WorksheetAnnotation sha = this.getAnnotation(sheetKey);
			sha.addAnnotation(ra.getKey(), ra, true);

		}else if (parent instanceof TableAnnotation){	
			TableAnnotation table = (TableAnnotation) parent;
						
			String tableParentKey = table.getParent().getKey();	
			if(tableParentKey.compareTo(sheetKey)!=0){
				throw new IllegalArgumentException("The given RangeAnnotation has a table as parent. "
						+ "These two annotations, range and table, are expected to be part of the same worksheet."
						+ "Instead, found that the worksheets do not match!");
			}
			
			WorksheetAnnotation sha = this.getAnnotation(sheetKey);	
			if(!table.getParent().equals(this.getWorksheet(sheetKey))){
				throw new IllegalArgumentException("The tableParentKey and sheetKey match. However, the state of the objects differs!!!");
			}
			
			if(!sha.containsTable(table.getKey())){
				throw new IllegalArgumentException("The table annotation, which is parent for the given range annotation, "
						+ "must already be a child of the worksheet annotation (referenced by the sheetKey)! "
						+ "Instead, found that it is not present in the specified worksheet");
			}
			
			
			if(!table.containsAnnotation(ra.getKey())){
				table.addAnnotation(ra.getKey(), ra, true);
				// throw new IllegalArgumentException("The given range annotation has a table as parent. "
				//		+ "However, this table does not contain the range annotation as child."
				//		+ "The parent child relationship most hold in both directions!");
			}
	
			// add range annotations as dependent child, but not immediate!
			sha.addAnnotation(ra.getKey(), ra, false);
			
		}else{
			throw new IllegalArgumentException("The parent for the range annotation must be either a WorksheetAnnotation or TableAnnotation! "
					+ "Instead, got an instance of  \""+parent.getClass().getName()+"\"");
		}
	}
	
	
	/**
	 * Get the RangeAnnotation based on the worksheet key and annotation key
	 * @param sheetKey a string that represents the id (key) of the worksheet where the RangeAnnotation is located 
	 * @param raKey a string that is used as key for the range annotation object 
	 * @return the RangeAnnotation object that corresponds to the given arguments  
	 */
	public RangeAnnotation getRangeAnnotation(String sheetKey, String raKey){	
		WorksheetAnnotation sheetAnnotation= this.getAnnotation(sheetKey);
		
		if(sheetAnnotation==null)
			return null;
		
		return sheetAnnotation.getAnnotation(raKey);
	}
	
	
	/**
	 * @param parentKey a string the represents the key for the parent of the requested range annotation
	 * @return an ContainerAnnotation object that represents the parent of this RangeAnnotation
	 */
	public ContainerAnnotation<RangeAnnotation> getParentOfRangeAnnotation(String parentKey){	
		
		if(this.getWorksheetsMap().containsKey(parentKey)){
			return this.getWorksheetsMap().get(parentKey);
		}
		
		for(WorksheetAnnotation wa: this.getWorksheetsMap().values()){
			if(wa.containsTable(parentKey)){
				return wa.getTable(parentKey);
			}
		}
		
		return null;
	}
	
	
	/**
	 * Get the list of RangeAnnotations for the given worksheet key, and given annotation label
	 * @param sheetKey a string that represents the id (key) of the worksheet where the RangeAnnotations are located
	 * @param label one of the AnnotationLabel values
	 * @return a list of RangeAnnotations that correspond to the given arguments
	 */
	public List<RangeAnnotation> getRangeAnnotationsForSheetByLabel(String sheetKey, AnnotationLabel label){
		
		WorksheetAnnotation sheetAnnotation = this.getAnnotation(sheetKey);
		
		if(sheetAnnotation==null)
			return null;
		
		List<RangeAnnotation> results = new ArrayList<RangeAnnotation>();
		for (RangeAnnotation ra: sheetAnnotation.getAllDependentAsList()){
			if(ra.getAnnotationLabel()==label)
				results.add(ra);
		}
		
		return results;	
	}
	
	
	/**
	 * Get all RangeAnnotation objects for the specified worksheet
	 * @param sheetKey a string that represents the key (id) of the worksheet 
	 * @return a list of annotations objects
	 */
	public List<RangeAnnotation> getRangeAnnotationsForSheet(String sheetKey){
		
		WorksheetAnnotation sheetAnnotation = this.getAnnotation(sheetKey);
		
		if(sheetAnnotation==null)
			return null;
		
		return sheetAnnotation.getAllDependentAsList();
	}
	
	
	/**
	 * Get all RangeAnnotation objects that depend directly to the specified worksheet (i.e.,they are not contained by any other annotation object)
	 * @param sheetKey a string that represents the key (id) of the worksheet annotation
	 * @return a list of annotations objects (i.e., the immediate children)
	 */
	public List<RangeAnnotation> getImmediateChildrenForSheet(String sheetKey){
		
		WorksheetAnnotation sheetAnnotation = this.getAnnotation(sheetKey);
		
		if(sheetAnnotation==null)
			return null;
		
		return sheetAnnotation.getImmediateChildrenAsList();
	}
	
	
	/**
	 * Remove a RangeAnnotation
	 * @param sheetKey a string that represents the id (key) of the worksheet where the RangeAnnotation is located
	 * @param raKey a string that is used as key for the range annotation object 
	 */
	public void removeRangeAnnotation(String sheetKey, String raKey){
		WorksheetAnnotation sheetAnnotation = this.getAnnotation(sheetKey);
		
		if(sheetAnnotation==null)
			return;
		
		ContainerAnnotation<RangeAnnotation> parent = sheetAnnotation.getAnnotation(raKey).getParent();		
		if(!(parent instanceof WorksheetAnnotation)){	
			parent.removeAnnotation(raKey);
		}
		
		sheetAnnotation.removeAnnotation(raKey);
	}
	
	
	/**
	 * Remove all RangeAnnotations belonging to the specified worksheet, and having the specified AnnotationLabel
	 * @param sheetKey a string that represents the id (key) of the worksheet where the RangeAnnotations are located
	 * @param label one of the AnnotationLabel values
	 * 
	 */
	public void removeRangeAnnotationsForSheetByLabel(String sheetKey, AnnotationLabel label){
		
		WorksheetAnnotation sheetAnnotation = this.getAnnotation(sheetKey);
		
		if(sheetAnnotation==null)
			return;
			
		for (RangeAnnotation ra : sheetAnnotation.getAllDependentAsList()) {
			if(ra.getAnnotationLabel()==label){
				this.removeRangeAnnotation(sheetKey, ra.getKey());
			}	
		}
	}
	
	
	/**
	 * Remove all RangeAnnotations belonging to the specified worksheet
	 * @param sheetKey a string that represents the id (key) of the worksheet where the RangeAnnotations are located
	 * 
	 */
	public void removeAllRangeAnnotationsForSheet(String sheetKey){
		
		WorksheetAnnotation sheetAnnotation = this.getAnnotation(sheetKey);
		
		if(sheetAnnotation==null)
			return;
			
		sheetAnnotation.removeAllDependentAnnotations();
	}
	
	
	/**
	 * Remove all Annotations, both Range and Tables, belonging to the specified worksheet
	 * @param sheetKey a string that represents the id (key) of the worksheet where the annotations are located
	 * 
	 */
	public void removeAllAnnotationsForSheet(String sheetKey){
		
		WorksheetAnnotation sheetAnnotation = this.getAnnotation(sheetKey);
		
		if(sheetAnnotation==null)
			return;
			
		sheetAnnotation.removeAllDependentAnnotations();
		sheetAnnotation.removeAllTables();
	}
	
	
	/**
	 * Remove all Table annotations in the specified worksheet
	 * @param sheetKey a string that represents the id (key) of the worksheet where the annotations are located
	 */
	public void removeAllTablesForSheet(String sheetKey){
		
		WorksheetAnnotation sheetAnnotation = this.getAnnotation(sheetKey);
		
		if(sheetAnnotation==null)
			return;

		sheetAnnotation.removeAllTables();
	}
	
	/**
	 * Get list of table annotations in the specified worksheet
	 * @param sheetKey the key that uniquely identifies the worksheet annotation
	 * @return a list of TableAnnotation object, or null if worksheet annotation does exist
	 */
	public List<TableAnnotation> getTablesForSheet(String sheetKey){
		
		WorksheetAnnotation sheetAnnotation = this.getAnnotation(sheetKey);
		
		if(sheetAnnotation==null)
			return null;
		
		return sheetAnnotation.getTableList();
	}
	
	
	/**
	 * Add the given TableAnnotations into the specified worksheet.
	 * @param sheetKey the key that uniquely identifies the worksheet annotation
	 * @return true if the annotations were successfully added, false otherwise.
	 */
	public boolean addTableAnnotation(String sheetKey, TableAnnotation table){
		
		WorksheetAnnotation sheetAnnotation = this.getAnnotation(sheetKey);
		
		if(sheetAnnotation==null)
			return false;
		
		if(table.getParent().getKey().compareTo(sheetKey)==0){
			sheetAnnotation.addTableAnnotation(table);
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * Add the given TableAnnotations into the specified worksheet.
	 * @param sheetKey the key that uniquely identifies the worksheet annotation
	 * @return true if the annotations were successfully added, false otherwise.
	 */
	public boolean addMultipleTables(String sheetKey, List<TableAnnotation> tables){
		
		WorksheetAnnotation sheetAnnotation = this.getAnnotation(sheetKey);
		
		if(sheetAnnotation==null)
			return false;
		
		for (TableAnnotation table: tables){
			if(table.getParent().getKey().compareTo(sheetKey)==0){
				sheetAnnotation.addTableAnnotation(table);
			}else{
				String breakTable = table.getKey();
				for (TableAnnotation t: tables){
					if(breakTable.compareTo(t.getKey())==0){
						break;
					}
					sheetAnnotation.removeTable(t.getKey());
				}
				return false;
			}
		}
		return true;
	}
	
	/**
	 *  Check if this workbook annotation is equal to the given annotation. 
	 */
	@Override
	public boolean equals(ContainerAnnotation<WorksheetAnnotation> annotation) {
		
		if(!(annotation instanceof WorkbookAnnotation))
			return false;
		
		WorkbookAnnotation wa = (WorkbookAnnotation) annotation;
				
		if(this.workbookName.compareTo(wa.getWorkbookName())!=0)
			return false;
		
		if(!this.getWorksheetsMap().equals(wa.getWorksheetsMap()))
			return false;
		
		return true;	
	}
	
	/**
	 * Get hash code for this workbook annotation
	 */
	@Override
	public int hashCode() {	
		int hash = this.getWorkbookName().hashCode();
		
		for (WorksheetAnnotation val : this.getWorksheetsMap().values()) {
			hash = hash + val.hashCode();
		}		
		return hash;
	}
	
	/**
	 * Get the string representation for this workbook annotation.
	 */
	@Override
	public String toString() {
		return this.workbookName;
	}
}
