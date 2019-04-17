
/**
 * 
 */
package de.tudresden.xr.model.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class WorksheetAnnotation  extends ContainerAnnotation<RangeAnnotation> implements DependentAnnotation<WorkbookAnnotation> {

	private String workbookName;
	private String sheetName;
	private int sheetIndex;
	private WorkbookAnnotation parent;
	
	
	private HashMap<String, TableAnnotation> tableRegions;

	
	/**
	 * @param sheetName the name assigned (given) to the worksheet 
	 * @param sheetIndex the index of the worksheet (as specified by the Excel application)
	 * @param parentWorkbook an object that represents the workbook, which contains the worksheet
	 */
	public WorksheetAnnotation(String sheetName, int sheetIndex, WorkbookAnnotation parentWorkbook) {
		this.workbookName = parentWorkbook.getWorkbookName();
		this.parent = parentWorkbook;
		this.sheetIndex = sheetIndex;
		this.sheetName = sheetName;
		this.tableRegions = new HashMap<String, TableAnnotation>();
	}
	
	
	/** 
	 * @param sheetName the name assigned (given) to the worksheet 
	 * @param sheetIndex the index of the worksheet (as specified by the Excel application)
	 * @param parentWorkbook an object that represents the workbook, which contains the worksheet
	 * @param tables the list of table annotations present in this worksheet
	 */
	protected WorksheetAnnotation(String sheetName, int sheetIndex, WorkbookAnnotation parentWorkbook, List<TableAnnotation> tables) {
		super();
		this.sheetName = sheetName;
		this.sheetIndex = sheetIndex;
		this.parent = parentWorkbook;
		this.tableRegions = new HashMap<String, TableAnnotation>();
		for(TableAnnotation table: tables){
			this.addTableAnnotation(table);
		}
	}


	/**
	 * @return the workbookName
	 */
	public String getWorkbookName() {
		return workbookName;
	}

	/**
	 * @param workbookName the workbookName to set
	 */
	protected void setWorkbookName(String workbookName) {
		this.workbookName = workbookName;
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
	 * 
	 * @param table
	 * @return
	 */
	protected boolean addTableAnnotation(TableAnnotation table){
		if(table.getParent().getKey().compareTo(this.getKey())==0){ 
			this.tableRegions.put(table.getKey(), table);
			for(RangeAnnotation ra: table.getAllDependentAsList()){
				this.addAnnotation(ra.getKey(), ra, false);
			}
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 */
	protected TableAnnotation getTable(String tblKey){	
		if(!tableRegions.containsKey(tblKey))
			return null;	
		return tableRegions.get(tblKey);
	}

	
	/**
	 * @return a copy of the table list
	 */
	protected List<TableAnnotation> getTableList(){		
		return new ArrayList<TableAnnotation>(tableRegions.values());
	}
	
	
	/**
	 * @return a copy of the table map
	 */
	protected Map<String, TableAnnotation> getTableMap(){		
		return new HashMap<String, TableAnnotation>(tableRegions);
	}
	
	/**
	 * 
	 * @param tblKey
	 */
	protected void removeTable(String tblKey){	
		if(!tableRegions.containsKey(tblKey))
			return;
		
		for(RangeAnnotation ra: tableRegions.get(tblKey).getAllDependentAsList()){
			this.removeAnnotation(ra.getKey());
		}		
		this.tableRegions.remove(tblKey);
	}
	
	/**
	 * 
	 */
	protected void removeAllTables(){	
		for(TableAnnotation ta: tableRegions.values()){
			for(RangeAnnotation ra: ta.getAllDependentAsList()){
				this.removeAnnotation(ra.getKey());
			}
		}		
		this.tableRegions.clear();
	}

	
	/**
	 * @param tblKey the unique key identifier for the table annotation object
	 * @return true if this worksheet contains the specified table, false otherwise
	 */
	protected boolean containsTable(String tblKey){
		return this.tableRegions.containsKey(tblKey);
	}
	
	/**
	 * @return the parent workbook annotation
	 */
	@Override
	public WorkbookAnnotation getParent() {
		return parent;
	}

	
	/**
	 * @param the workbook annotation to set as parent
	 */
	@Override
	public void setParent(WorkbookAnnotation parent) {
		this.parent = parent;
	}
	
	/**
	 * @return the key
	 */
	@Override
	public String getKey() {
		return WorksheetAnnotation.getWorksheetKey(this.sheetName, this.sheetIndex, this.workbookName);
	}
	
	/**
	 * @return string representation for this object
	 */
	@Override 
	public String toString() {
		return this.getSheetName()+" = "+this.getAllDependentAsList(); 
	}
	
	/**
	 * 
	 */
	@Override
	public boolean equals(ContainerAnnotation<RangeAnnotation> annotation) {
		
		if(!(annotation instanceof WorksheetAnnotation))
			return false;
		
		WorksheetAnnotation sa = (WorksheetAnnotation) annotation;
		
		if(sa.getSheetName().compareTo(this.sheetName)!=0)
			return false;
		
		if(!(sa.getAllDependentAsMap().equals(this.getAllDependentAsMap())))
			return false;

		return true;
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		int hash = this.getSheetName().hashCode();
		
		for (RangeAnnotation val : this.getAllDependentAsList()) {
			hash = hash + val.hashCode();
		}
		
		return hash;
	}
	
	/**
	 * 
	 * @param sheetName the name assigned to the worksheet 
	 * @param sheetIndex the index of the worksheet (as specified by the Excel application)
	 * @param workbookName the name of the workbook where this worksheet is located
	 * @return key a string that uniquely identifies the worksheet
	 */
	public static String getWorksheetKey(String sheetName, int sheetIndex, String workbookName){
		return sheetName;
	}

}
