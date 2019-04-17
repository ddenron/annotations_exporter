/**
 * 
 */
package de.tudresden.xr.utils;

import java.io.File;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import de.tudresden.xr.model.annotation.ContainerAnnotation;
import de.tudresden.xr.model.annotation.RangeAnnotation;
import de.tudresden.xr.model.annotation.RangeAnnotationsSheet;
import de.tudresden.xr.model.annotation.TableAnnotation;
import de.tudresden.xr.model.annotation.WorkbookAnnotation;
import de.tudresden.xr.model.annotation.WorksheetAnnotation;
import de.tudresden.xr.model.enums.AnnotationLabel;



/**
 * Provides various utility functions about Annotated Workbooks
 * 
 */
public class WorkbookUtils {
		
	
	/**
	 * Read the annotation data for the given workbook.
	 * 
	 * @param wb the excel workbook as an Apache POI object
	 * @param fileName a string that represents the name of the excel file
	 * @return a WorkbookAnnotation object that provides access to all the RangeAnnotations for the given Workbook.
	 * @throws Exception a generic exception, containing a string message with clarifications
	 */
	public static WorkbookAnnotation readAnnotationData(Workbook wb, String fileName) throws Exception{
		
		WorkbookAnnotation wa = null;
		
		try {								
			Sheet sheet = wb.getSheet(RangeAnnotationsSheet.getName());
			if(sheet==null){
				throw new Exception("RangeAnnotationData sheet was not found in the file \""+fileName+"\".");
			}
			
			int firstRowNum = sheet.getFirstRowNum();
			int lastRowNum = sheet.getLastRowNum();
			
			Row firstRow = sheet.getRow(firstRowNum);		
			int firstColumnNum = firstRow.getFirstCellNum();
			int lastColumnNum = firstRow.getLastCellNum();
					
			LinkedHashMap<String,Integer> expectedFields = RangeAnnotationsSheet.getFields();
			if(expectedFields.size() != (lastColumnNum - firstColumnNum)){
				throw new Exception("The sheet holding the annotation data for the file \""+fileName+"\" has less or more fields than expected");
			}
			
			// check if the format is correct
			boolean isCorrectFormat = true;
			for (int j = firstColumnNum; j <lastColumnNum; j++) {
				Cell cell = firstRow.getCell(j);
				String value = cell.getStringCellValue();
				
				if(expectedFields.get(value)==null){
					isCorrectFormat = false;
					break;
				}
				
				int fieldIndex = expectedFields.get(value); 
				if(fieldIndex!=j){
					isCorrectFormat = false;
					break;
				}
			}
				
			if(!isCorrectFormat){
				throw new Exception("The sheet holding the annotation data for the file \""+fileName+"\" it is not in the expected format");
			}
				
			wa = new WorkbookAnnotation(fileName);				
			for (int i = firstRowNum+1; i <=lastRowNum; i++) {
				
				// read the annotations data
				Row row= sheet.getRow(i);	
				String[] annotationData = new String[lastColumnNum - firstColumnNum];   				
				for (int k = firstColumnNum; k <lastColumnNum; k++){
					
					Cell cell = row.getCell(k);
					int n = cell.getColumnIndex() - firstColumnNum;
					
					annotationData[n] = cell.getStringCellValue();
				}
				
				// the following fields are obligatory for the re-creation of the annotations
				String sheetName = annotationData[0];
				int sheetIndex = Integer.valueOf(annotationData[1]);
				String strLabel = annotationData[2];
				String annotationName = annotationData[3];
				String strRangeAdress = annotationData[4];
				
				// ensure that the range address is valid
				try{
					CellRangeAddress.valueOf(strRangeAdress);
				}catch(Exception ex){
					throw new Exception("The range address is not valid (A1 format expected!). "+strRangeAdress);
				}
				String parentName = annotationData[5];
				
				// check if the worksheet annotation exists. 
				// if not, create new and appended to the workbook annotation.
				String sheetKey = WorksheetAnnotation.getWorksheetKey(sheetName, sheetIndex, fileName);
				if(!wa.getWorksheetsMap().containsKey(sheetKey)){
					WorksheetAnnotation sha = new WorksheetAnnotation(sheetName, sheetIndex, wa);
					wa.addWorksheetAnnotation(sheetKey, sha);
				}	
				
				
				// get the parent annotation if it exists
				ContainerAnnotation<RangeAnnotation> parentAnnotation = wa.getParentOfRangeAnnotation(parentName);
				if (parentAnnotation==null){
					throw new Exception("Child annotation was created before parent annotation!!!"
							+ "\nParent =\""+parentName+"\", Child=\""+annotationName+"\"");
				}
				
				// determine the annotation label
				AnnotationLabel annotationLabel = AnnotationLabel.valueOf(strLabel);	
				
				// if table annotation, treat separately
				if(annotationLabel == AnnotationLabel.Table){
					if(parentAnnotation instanceof WorksheetAnnotation){
						// re-create the table annotation object 
						TableAnnotation ta = new TableAnnotation((WorksheetAnnotation) parentAnnotation, annotationName, strRangeAdress);
						wa.addTableAnnotation(((WorksheetAnnotation) parentAnnotation).getKey(), ta);
					}else{
						throw new Exception("For TableAnnotations expects a WorksheetAnnotation as parent."
								+ "Instead got "+parentAnnotation.getClass().getName());
					}				
				}else{
					if(parentAnnotation instanceof WorksheetAnnotation ||  parentAnnotation instanceof TableAnnotation){
						// re-create the range annotation object 
						RangeAnnotation ra = new RangeAnnotation(sheetName, sheetIndex, parentAnnotation, annotationLabel, 
								annotationName, strRangeAdress); 
							
						wa.addRangeAnnotation(sheetKey, ra);
					}else{
						throw new Exception("For RangeAnnotation expects either WorksheetAnnotation or TableAnnotation as parent."
								+ "Instead got "+parentAnnotation.getClass().getName());
					}
				}				
			}
								
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();

		} catch (OldExcelFormatException oefEx){
			oefEx.printStackTrace();
		}		
		return wa;
	}
	
	
	
	/**
	 * Create a new Workbook object (Apache POI) using the given file
	 * 
	 * @param excelFile a generic File object that provides access to the Excel file
	 * @return a Workbook (Apache POI) object
	 * @throws Exception a generic exception, containing a string message with clarifications
	 */
	public static Workbook createWorkbook(File excelFile) throws Exception {

		if (!excelFile.exists())
			return null;

		Workbook wb = null;

		int index = excelFile.getAbsolutePath().lastIndexOf(".");
		String extension = excelFile.getAbsolutePath().substring(index);
		if (extension.compareToIgnoreCase(".xls") == 0 || extension.compareToIgnoreCase(".xlsx") == 0) {

			try {
				wb = WorkbookFactory.create(excelFile);
			} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
				e.printStackTrace();
			}

		} else {
			throw new Exception("The file extension is not recognized by this application: " + extension + " !");
		}
		return wb;
	}	
}
