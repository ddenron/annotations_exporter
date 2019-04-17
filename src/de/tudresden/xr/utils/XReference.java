/**
 * 
 */
package de.tudresden.xr.utils;

import java.util.ArrayList;

import org.apache.poi.ss.util.CellRangeAddress;

/**
 */
public class XReference {

	private String workbookName = null;
	private int workbookIndex = -1;
	private String sheetName = null;
	private int sheetIndex = -1;
	private CellRangeAddress range;
	private String operationName = null;
	private ArrayList<String> operationsList = null;

	/**
	 * @param range
	 */
	public XReference(CellRangeAddress range) {
		super();
		this.range = range;
	}

	/**
	 * @param range
	 * @param operationName
	 */
	public XReference(CellRangeAddress range, String operationName) {
		super();
		this.range = range;
		this.operationName = operationName;
	}

	/**
	 * @return the workbookName
	 */
	public String getWorkbookName() {
		return workbookName;
	}

	/**
	 * @param workbookName
	 *            the workbookName to set
	 */
	public void setWorkbookName(String workbookName) {
		if(workbookName!=null && workbookName.compareTo("")!=0)
			this.workbookName = workbookName;
	}

	/**
	 * @return the workbookIndex
	 */
	public int getWorkbookIndex() {
		return workbookIndex;
	}

	/**
	 * @param workbookIndex
	 *            the workbookIndex to set
	 */
	public void setWorkbookIndex(int workbookIndex) {
		this.workbookIndex = workbookIndex;
	}

	/**
	 * @return the sheetName
	 */
	public String getSheetName() {
		return sheetName;
	}

	/**
	 * @param sheetName
	 *            the sheetName to set
	 */
	public void setSheetName(String sheetName) {
		
		if(sheetName!=null && sheetName.compareTo("")!=0)
			this.sheetName = sheetName;
	}

	/**
	 * @return the sheetIndex
	 */
	public int getSheetIndex() {
		return sheetIndex;
	}

	/**
	 * @param sheetIndex
	 *            the sheetIndex to set
	 */
	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	/**
	 * @return the range
	 */
	public CellRangeAddress getRange() {
		return range;
	}

	/**
	 * @param range
	 *            the range to set
	 */
	public void setRange(CellRangeAddress range) {
		this.range = range;
	}

	/**
	 * @return the operandName
	 */
	public String getOperationName() {
		return operationName;
	}

	/**
	 * @param operationName
	 *            the operandName to set
	 */
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	/**
	 * @return the operandList
	 */
	public ArrayList<String> getOperationsList() {
		return operationsList;
	}

	/**
	 * @param operationsList the operandList to set
	 */
	public void setOperationsList(ArrayList<String> operationsList) {
		this.operationsList = operationsList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb =  new StringBuilder();
		if(this.workbookName!=null){
			sb.append("[ ").append(this.workbookName).append(" ] ");
			
		}else if(this.workbookIndex>=0){
			sb.append("[ ").append(""+this.workbookIndex).append(" ] ");
		}else{
			sb.append("[ ] ");
		}
		
		if(this.sheetName!=null){
			sb.append("sheet = ").append(this.sheetName).append(" ! ");
		}else if(this.sheetIndex>=0){
			sb.append("sheetIx = ").append(""+this.sheetIndex).append(" ! ");
		}else{
			sb.append("sheet = null").append(" ! ");
		}
				
		sb.append(this.range.formatAsString());
		
		if(this.operationName!=null){
			sb.append(", opr = ").append(this.operationName);
		}else{
			sb.append(", opr = null");
		}
		
//		if(this.operationsList!=null && !this.operationsList.isEmpty()){
//			sb.append(", last_opr = "+this.operationsList.get(this.operationsList.size()-1));
//			sb.append(", opr_list = "+this.operationsList);
//		}else{
//			sb.append(", last_opr = null");
//		}
//		
		return sb.toString();
	}
}
