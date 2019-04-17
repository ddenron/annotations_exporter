/**
 * 
 */
package de.tudresden.xr.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import de.tudresden.xr.model.annotation.RangeAnnotation;
import de.tudresden.xr.model.annotation.TableAnnotation;
import de.tudresden.xr.model.annotation.WorkbookAnnotation;
import de.tudresden.xr.model.annotation.WorksheetAnnotation;
import de.tudresden.xr.model.enums.AnnotationLabel;
import de.tudresden.xr.utils.CellUtils;
import de.tudresden.xr.utils.FileUtils;
import de.tudresden.xr.utils.WorkbookUtils;


/**
 * Export the annotation for each individual cell in the sheets 
 */
public class CellAnnotationsExporter {
	

	/**
	 * Get a list of valid cells inside the specified range
	 * A cell is valid when: 
	 * 	a) it is filled with a value (i.e., it is not empty)
	 *  b) it contains characters other then just white spaces
	 * 
	 * @param range its a rectangular area of the sheet, covering one or more cells
	 * @param sheet an object that represents the worksheet where the range is located
	 * @param eval a formula evaluator, which provides methods to excess the value of formula cells
	 * @return the list of CellRangeAddresses, pointing to the valid cells of the given range
	 */
	public static List<CellRangeAddress> getValidCellAddressesInRange(CellRangeAddress range, Sheet sheet, FormulaEvaluator eval, boolean asMerged, boolean withHidden){
		
		// identify merged areas of the sheet
		HashMap<CellRangeAddress, Cell> mergedAreas = new HashMap<CellRangeAddress, Cell>();
		for (int i=0; i<sheet.getNumMergedRegions(); i++){
			CellRangeAddress nextMerged = sheet.getMergedRegion(i);
			if(nextMerged.intersects(range)){
				
				Row mergedRow = null;
				for (int j = nextMerged.getFirstRow(); j <= nextMerged.getLastRow(); j++) {
					Row sheetRow = sheet.getRow(j);
					if (sheetRow != null && (withHidden || !(sheetRow.getZeroHeight() || sheetRow.getHeight() == 0))){
						mergedRow = sheetRow;
						break;
					}
				}
				
				Cell firstMergedCell = mergedRow.getCell(nextMerged.getFirstColumn());
				if(firstMergedCell!=null){		
					String cellValue = CellUtils.getCellValue(firstMergedCell, eval);
					if(firstMergedCell.getCellTypeEnum()==CellType.FORMULA || firstMergedCell.getCellTypeEnum()==CellType.ERROR ||
							(cellValue.length()>0 && !cellValue.matches("^\\s+$"))){		
						mergedAreas.put(nextMerged, firstMergedCell);
					}else{
						mergedAreas.put(nextMerged, null);
					}
			 	}							
			}
		}

		// identify valid cells
		List<CellRangeAddress> cells = new ArrayList<CellRangeAddress>();
		for (int m = range.getFirstRow(); m <= range.getLastRow(); m++) {
			Row row = sheet.getRow(m);
			if(row==null || (!withHidden && (row.getZeroHeight() || row.getHeight() == 0)))
				 continue;
			
			for (int n = range.getFirstColumn(); n <= range.getLastColumn(); n++) {	
					
				CellRangeAddress thisMerged = null;
				for(CellRangeAddress merged: mergedAreas.keySet()){
					if(merged.isInRange(m,n)){
						thisMerged = merged;
						break;
					}
				}
		
				if(thisMerged==null){
					Cell cell = row.getCell(n);
					if(cell!=null && (withHidden || !(sheet.isColumnHidden(n) || sheet.getColumnWidth(n)==0))){
						String cellValue = CellUtils.getCellValue(cell, eval);

						if(cell.getCellTypeEnum()==CellType.FORMULA || cell.getCellTypeEnum()==CellType.ERROR ||
								(cellValue.length()>0 && !cellValue.matches("^\\s+$"))){ // cell is not empty or blank (i.e., containing only white spaces) 
							cells.add(new CellRangeAddress(m, m, n, n));
						}
					}
				}else{
					if(asMerged){				
						// if true export the whole merged area, 
						Cell topLeftCell = mergedAreas.get(thisMerged);
						if(topLeftCell!=null && !cells.contains(thisMerged)){
							cells.add(thisMerged);
						}
						
					}
					else{
						// otherwise export the individual cells of the merged area
						Cell topLeftCell = mergedAreas.get(thisMerged);
						if(topLeftCell!=null)
							cells.add(new CellRangeAddress(m, m, n, n));
					}
					
					if(thisMerged.getLastRow()==m && thisMerged.getLastColumn()==n)
						mergedAreas.remove(thisMerged);
				}				
			}
		}		
		return cells;
	}
	
	
	/**
	 * Export annotations from all excel files in the specified directory
	 * 
	 * @param dirPath the directory where the annotated excel files are found
	 * @param exportPath the path to the file where the annotations will be exported (written)
	 * @param exportAsMerged whether to treat merged areas as a single cell or as multiple individual cells
	 */
	public static void exportAnnotations(String dirPath, String exportPath, boolean exportAsMerged){
		
		
		File annotations_dir = new File(dirPath);
		if(!annotations_dir.exists()){
			System.err.println("The specified inPath does not exists!\n"+dirPath);
			System.exit(1);
		}
			
		File[] annotatedFiles = FileUtils.getExcelFiles(new File(dirPath));
		if(annotatedFiles.length==0){			
			System.err.println("There are no annotated excel files in the specified directory!!!");
			System.exit(1);
		}
		System.out.println("There are "+annotatedFiles.length+" files in the directory\n");
		
		
		// create the export file. if it already exists delete and re-create it
		File exportFile = new File(exportPath);
		try{
			if(exportFile.isDirectory()){
				throw new IllegalArgumentException("The specified export path is a directory. Please, provide a path to a file, instead.");
			}
			
			if (!exportFile.exists()){
				exportFile.createNewFile();
			}else{
				exportFile.delete();
				exportFile.createNewFile();
			}
			
		}catch(IOException ioEx){
			System.err.println("An i/o exception occurred while attemting to create the export file:\n"+ioEx.getMessage());
			System.exit(1);
		}catch(SecurityException secEx){
			System.err.println("A security exception occurred while attemting to create the export file:\n"+secEx.getMessage());
			System.exit(1);
		}catch(IllegalArgumentException argEx){
			System.err.println(argEx.getMessage());
			System.exit(1);
		}

		
		// create a string builder to collect the annotation info 
		StringBuilder sb = new StringBuilder();
		
		// create the header row for the csv file
		sb.append("FileName");
		sb.append(",").append("SheetName");
		sb.append(",").append("SheetIndex");
		sb.append(",").append("TableName");
		sb.append(",").append("AnnotationLabel");
		sb.append(",").append("AnnotationAddress");
		sb.append(",").append(String.valueOf("FirstColumn"));
		sb.append(",").append(String.valueOf("FirstRow"));
		sb.append(",").append(String.valueOf("LastColumn"));
		sb.append(",").append(String.valueOf("LastRow"));
		sb.append("\n");
			
		
		for (File file : annotatedFiles) {	// for each excel file in the directory	
			System.out.println(file.getName());
			
			try{									
				// read annotations from the excel workbook
				Workbook wb = WorkbookUtils.createWorkbook(file);
				WorkbookAnnotation wa = WorkbookUtils.readAnnotationData(wb, file.getName());
				
				// ensure that there is at least one annotated sheet per file
				if (wa==null || wa.getWorksheetsMap().isEmpty()){
					System.err.println("\nError: There are no annotations in the file \""+file.getName()+"\". ");
					System.exit(1);
				}
				
				// identify the annotated sheet. if multiple, consider the first one, from left to right (following the order of the tabs). 
				WorksheetAnnotation firstAnnotated = null;
				for(WorksheetAnnotation sha: wa.getWorksheetsMap().values()){
					if(firstAnnotated==null || wb.getSheetIndex(firstAnnotated.getSheetName()) > wb.getSheetIndex(sha.getSheetName())){
						firstAnnotated = sha;
					}
				}
						
				// get all range annotations from the selected sheet
				// these ranges might have empty and or hidden cells, among those with a value
				ArrayList<RangeAnnotation> allAnnotations = 
						new ArrayList<RangeAnnotation>(wa.getRangeAnnotationsForSheet(firstAnnotated.getKey()));
				String fileName = file.getName();
				String sheetName = firstAnnotated.getSheetName();
				int sheetIndex = firstAnnotated.getSheetIndex();
				
				// create Apache POI objects, used to identify valid (non-empty) cells
				Sheet sheet = wb.getSheet(firstAnnotated.getSheetName());
				FormulaEvaluator frmlEval = wb.getCreationHelper().createFormulaEvaluator(); 
				
				// for each range annotation identify the non-empty cells, and append their annotation-info
				for(RangeAnnotation ra: allAnnotations){					
					if(ra.getAnnotationLabel() != AnnotationLabel.Table){ // table annotations are treated separately
						
						String label = ra.getAnnotationLabel().name();	
						
						// if the cell is part of table annotation, get the table name.
						// otherwise export an empty string.
						String tableName = "";
						if(ra.getParent() instanceof TableAnnotation){
							tableName = ((TableAnnotation) ra.getParent()).getTableName();		
						}
						
						String shortTableName = "";
						if(tableName!=null && tableName.length()>0){
							int str_ix = tableName.lastIndexOf("TABLE");
							shortTableName = "Table".concat(tableName.substring(str_ix+5));
						}else{
							shortTableName = tableName;
						}
						
						// identify valid (non-empty) cells
						CellRangeAddress cra = CellRangeAddress.valueOf(ra.getRangeAddress());						
						List<CellRangeAddress> validCellAddresses = getValidCellAddressesInRange(cra, sheet, frmlEval, exportAsMerged, false); // omit hidden cells
						for(CellRangeAddress cellAddress: validCellAddresses){
								// export the annotation for each valid cell 
								sb.append("\"").append(fileName).append("\"").append(",");
								sb.append("\"").append(sheetName).append("\"").append(",");
								sb.append(sheetIndex).append(",");
								sb.append("\"").append(shortTableName).append("\"").append(",");
								
								sb.append(label).append(",");
								sb.append(cellAddress.formatAsString()).append(",");		
								sb.append(String.valueOf(cellAddress.getFirstColumn())).append(",");
								sb.append(String.valueOf(cellAddress.getFirstRow())).append(",");
								sb.append(String.valueOf(cellAddress.getLastColumn())).append(",");
								sb.append(String.valueOf(cellAddress.getLastRow()));
								sb.append("\n");
						}			
					}		
				}			
				
				FileUtils.appendToFile(exportFile, sb.toString());
				sb.setLength(0);
				
			}catch(Exception ex){				
				ex.printStackTrace();
			}
		}
		
		System.out.println("\nDone!");
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String inPath = null;
		String outPath = null;
		boolean asMerged = false;
		
		if(args.length<4 || args.length>5){
			System.err.println("Expected 4 + 1 optional arguments, instead was given " + args.length);
			System.exit(1);
		}		

        for (int j=0; j<args.length; j++) {
        	if(args[j].compareToIgnoreCase("-inPath")==0){
        		if (j+1<args.length)
        			inPath = args[j+1];
        	}
        		
        	if(args[j].compareToIgnoreCase("-outPath")==0){
        		if (j+1<args.length)
        			outPath = args[j+1];
        	}
        	
        	if(args[j].compareToIgnoreCase("-asMerged")==0){
        		asMerged = true;
        	}
        }
        
        if(inPath==null){
        	System.err.println("Missing argument -inPath!");
			System.exit(1);
        }
        
        if(outPath==null){
        	System.err.println("Missing argument -outPath!");
			System.exit(1);
        }
             
		exportAnnotations(inPath, outPath, asMerged);		
	}
}
