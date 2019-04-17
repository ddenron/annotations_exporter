/**
 * 
 */
package de.tudresden.xr.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.EvaluationCell;
import org.apache.poi.ss.formula.EvaluationSheet;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.formula.ptg.AbstractFunctionPtg;
import org.apache.poi.ss.formula.ptg.AreaPtgBase;
import org.apache.poi.ss.formula.ptg.AttrPtg;
import org.apache.poi.ss.formula.ptg.BoolPtg;
import org.apache.poi.ss.formula.ptg.IntPtg;
import org.apache.poi.ss.formula.ptg.NamePtg;
import org.apache.poi.ss.formula.ptg.NameXPtg;
import org.apache.poi.ss.formula.ptg.NameXPxg;
import org.apache.poi.ss.formula.ptg.NumberPtg;
import org.apache.poi.ss.formula.ptg.OperandPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.RangePtg;
import org.apache.poi.ss.formula.ptg.RefPtgBase;
import org.apache.poi.ss.formula.ptg.StringPtg;
import org.apache.poi.ss.formula.ptg.UnaryMinusPtg;
import org.apache.poi.ss.formula.ptg.UnaryPlusPtg;
import org.apache.poi.ss.formula.ptg.ValueOperatorPtg;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFEvaluationWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 */
public class CellUtils {
	
	/**
	 * Get the type of the cell
	 * @param cell an object that represents the spreadsheet cell in consideration
	 * @return a number that represents the (value) type of the cell
	 */
	public static short getCellValueType(Cell cell){
		
		short cellValueType = 0;
		
		// augmenting the available options, with one more types 
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {	
			if (DateUtil.isCellDateFormatted(cell)) {
				cellValueType = 6; // when DATE custom type = 6
		    }
		}else{
			cellValueType = (short) cell.getCellType();
		}
			
		return cellValueType;
	}
	
	
	/**
	 * Get the value of the cell
	 * @param cell an object that provides access to the cell in consideration
	 * @param evaluator a FormulaEvaluator object, to be used for retrieving the value of the formula (if any) in the cell.
	 * @return a string that represents the value of the cell
	 */
	public static String getCellValue(Cell cell, FormulaEvaluator evaluator){
		
		short type= getCellValueType(cell);
		
		String value = ""; 		
		if(type == Cell.CELL_TYPE_NUMERIC){
            value = String.valueOf(cell.getNumericCellValue());
		
		}else if(type == Cell.CELL_TYPE_STRING){
			
			value = cell.getStringCellValue();
//			try {
//				value = new String(cell.getStringCellValue().getBytes(),"UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			} 
		
		}else if(type == Cell.CELL_TYPE_BOOLEAN){
			value = String.valueOf(cell.getBooleanCellValue());
			
		}else if(type == Cell.CELL_TYPE_FORMULA){
			value = getFormulaValue(cell, evaluator);
			
		}else if(type == 6){ // Additional CellType.Date = 6
			
			DataFormatter df = new DataFormatter();
			value = df.formatCellValue(cell);
		}
						
		return value;
	}

	
	/**
	 * Get the type of the formula result
	 * @param cell an object that represents the cell that contains the formula
	 * @param evaluator a FormulaEvaluator object, to be used for evaluating the formula in the given cell
	 * @return an short that represents the type of the formula result (value)
	 */
	public static short getFormulaValueType(Cell cell, FormulaEvaluator evaluator){
			
		CellValue cv = null;
		try {
			// evaluator.setIgnoreMissingWorkbooks(true);
			
			if(evaluator instanceof XSSFFormulaEvaluator){			
				cv = ((XSSFFormulaEvaluator) evaluator).evaluate(cell);
			}
			
			if(evaluator instanceof HSSFFormulaEvaluator){
				cv = ((HSSFFormulaEvaluator) evaluator).evaluate(cell);
			}			
		} catch (Exception e) {
		}
		
		short formulaValueType = 9; 	
		if(cv==null){
			formulaValueType = (short) cell.getCachedFormulaResultType();
		}else{
			formulaValueType = (short) cv.getCellType();
		}
			
		// augmenting the available options, with two more types 
		if (formulaValueType == Cell.CELL_TYPE_NUMERIC) {
			
			if (DateUtil.isCellDateFormatted(cell)) {
				formulaValueType = 6; // when DATE custom type = 6
		    }
			
		}else if(formulaValueType == Cell.CELL_TYPE_ERROR){
			
			if(cell.isPartOfArrayFormulaGroup()){ // arrays formulas are not handled by POI, thus they return always error
				formulaValueType = 7; // when ArrayGroupFormula custom type = 7
			}		
		}
			
		return formulaValueType;
	}
		
	
	/**
	 * Get the type of the formula result. This method is preferred when the cell formula is already evaluated
	 * @param cell an object that represents the cell that contains the formula
	 * @param cv a CellValue object, holding the result from the formula evaluation
	 * @return an short that represents the type of the formula result (value)
	 */
	public static short getFormulaValueType(Cell cell, CellValue cv){
		
		short formulaValueType; 	
		if(cv==null){
			formulaValueType = (short) cell.getCachedFormulaResultType();
		}else{
			formulaValueType = (short) cv.getCellType();
		}
				
		// augmenting the available options, with two more types 
		if (formulaValueType == Cell.CELL_TYPE_NUMERIC) {
			
			if (DateUtil.isCellDateFormatted(cell)) {
				formulaValueType = 6; // when DATE custom type = 6
		    }
			
		}else if(formulaValueType == Cell.CELL_TYPE_ERROR){
			
			if(cell.isPartOfArrayFormulaGroup()){ // arrays formulas are not handled by POI, thus they return always error
				formulaValueType = 7; // when ArrayGroupFormula custom type = 7
			}		
		}
			
		return formulaValueType;
	}
	
	
	/**
	 * Get the value of the formula in the cell
	 * @param cell an object that represents the cell where the formula is placed
	 * @param evaluator a FormulaEvaluator object, to be used for calculating the value of the formula
	 * @return a string that represents the result from the formula evaluation
	 */
	public static String getFormulaValue(Cell cell, FormulaEvaluator evaluator){
		
		CellValue cv = null;
		try {
			if(evaluator instanceof XSSFFormulaEvaluator){			
				cv = ((XSSFFormulaEvaluator) evaluator).evaluate(cell);
			}
			
			if(evaluator instanceof HSSFFormulaEvaluator){
				cv = ((HSSFFormulaEvaluator) evaluator).evaluate(cell);
			}			
		} catch (Exception e) {
		}
		
		
		short type = getFormulaValueType(cell, cv);
		String formulaValue = "";
		if (type == Cell.CELL_TYPE_NUMERIC) {
		
			if(cv!=null){
				formulaValue = String.valueOf(cv.getNumberValue());
			}else{
				formulaValue = String.valueOf(cell.getNumericCellValue());
			}
			
		}else if(type == Cell.CELL_TYPE_STRING){
			
			if(cv!=null){
				formulaValue = cv.getStringValue();
			}else{
				formulaValue = cell.getStringCellValue();
			}	
			
		}else if(type == Cell.CELL_TYPE_BOOLEAN){
			
			if(cv!=null){
				formulaValue = String.valueOf(cv.getBooleanValue());
			}else{
				formulaValue =  String.valueOf(cell.getBooleanCellValue());
			}
			
		}else if(type == 6){ // Custom type when cell is Date = 6
			
			DataFormatter df = new DataFormatter();
			try {
				formulaValue = df.formatCellValue(cell, evaluator);
			} catch (Exception e) {
			}
			
			if(formulaValue.compareTo("")==0){
				formulaValue = String.valueOf(cell.getDateCellValue());
			}
		}
					
		return formulaValue;
	}
	
	/**
	 * Check if cell is blank
	 * @param cell an object representing the cell in consideration
	 * @return true if the cell is blank, false otherwise
	 */
	public static boolean isBlankCell(Cell cell, FormulaEvaluator evaluator){
				
		DataFormatter df = new DataFormatter();
		
		String val = ""; 		
		try {
			evaluator.setIgnoreMissingWorkbooks(true);
			val = df.formatCellValue(cell,evaluator );
		} catch (Exception e) {
		}
		
		if(val.length()>0 && !val.matches("^[\\s]+$")){
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Get the number of formatting runs for the given cell
	 * @param cell an object representing for accessing the functionalities of the cell in consideration
	 * @return an integer representing the number of formatting runs
	 */
	public static int getNumberOfFormattingRuns(Cell cell){
		
		int count = 0; 
		if(cell.getCellType() == Cell.CELL_TYPE_STRING){
			RichTextString rts = cell.getRichStringCellValue();
		    count = rts.numFormattingRuns(); 	
		}
		
		return count;
	}
	
	
	/**
	 * Checks if the given cell is in one of the merged regions of the sheet. If yes, return the merged region.
	 * @param sheet an object that represents the sheet that contains the cell 
	 * @param cell an object that represents the cell in consideration
	 * @return a CellRangeAddress object that represents the merged region that the cell is part of,
	 * null if the cell is not merged
	 */
	public static CellRangeAddress getMergedRegion(Sheet sheet, Cell cell){
		
		if(sheet!=null && cell!=null){
			
			List<CellRangeAddress> mergedRegions = null;
			try {
				mergedRegions = sheet.getMergedRegions();
			} catch (IllegalStateException e) {
			}
			
			if(mergedRegions!=null){
			
				// check if this cell is inside a merged region
				for (CellRangeAddress cellRangeAddress :  sheet.getMergedRegions()) {
					 if(cellRangeAddress.isInRange(cell.getRowIndex(), cell.getColumnIndex())){
						 return cellRangeAddress;
					 }
				}		
			}
		}			
		
		return null;				
	}
	
	
	/**
	 * Compare the styles of two cells
	 * @param cell1 the first cell
	 * @param cell2 the second cell
	 * @return 1 if the styles match, 0 otherwise
	 */
	public static short compareCellStyles(Cell cell1, Cell cell2){		
		return ((short) (cell1.getCellStyle().equals(cell2.getCellStyle())?1:0));
	}
	
	/**
	 * Compare the types of two cells
	 * @param cell1 the first cell
	 * @param cell2 the second cell
	 * @return 1 if the types match, 0 otherwise
	 */
	public static short compareCellTypes(Cell cell1, Cell cell2){		
		return ((short) (cell1.getCellType()==cell2.getCellType()?1:0));
	}
	
	/**
	 * 
	 * @param cell
	 * @return
	 */
	public static ArrayList<XReference> findPrecedents(Cell cell) {
		
		Sheet sheet = cell.getSheet();
		Workbook wb = sheet.getWorkbook();
		
		Ptg[] things = null;
		if (wb instanceof HSSFWorkbook) {
			things = HSSFFormulaParser.parse(cell.getCellFormula(), (HSSFWorkbook) wb);

		} else {
			
			XSSFEvaluationWorkbook evalWb = XSSFEvaluationWorkbook.create((XSSFWorkbook) wb);
			
			int sheetIndex = wb.getSheetIndex(sheet);
			EvaluationSheet evalSheet = evalWb.getSheet(sheetIndex);
			
			EvaluationCell evalCell = evalSheet.getCell(cell.getRowIndex(), cell.getColumnIndex());
			try {
				things = evalWb.getFormulaTokens(evalCell);
			} catch (FormulaParseException e) {
				// TODO: handle exception
			}
		}
		
		if (things == null)
			return null;
		
		if(Ptg.doesFormulaReferToDeletedCell(things)){
			return null;
		}
		
		ArrayList<String> operationsList = new ArrayList<String>();
		ArrayList<XReference> refList = new ArrayList<XReference>();
		// when true reference, when  false scalar param;
		ArrayList<Boolean> params =  new ArrayList<Boolean>();
//		System.out.println("\n\nOriginal Formula: "+cell.toString());
//		System.out.println("Things List: "+Arrays.toString(things));
		int listIdx = 0; 
		int countOperationsAsParams = 0;
		int updatedOperands = 0;
		for (int i=0; i<things.length; i++) {
			
			Ptg thing = things[i]; 
			
			if(thing instanceof OperandPtg){
				
				XReference ref = null;
				if(thing instanceof NamePtg || thing instanceof NameXPtg || thing instanceof NameXPxg ){ // NameXPxg throws exception			
					ref = ReferenceUtils.getReferenceToNamedRange(thing, wb);
				}
				
				if(thing instanceof AreaPtgBase ){			
					ref = ReferenceUtils.getReferenceToArea(thing, wb);
				}
				
				if(thing instanceof RefPtgBase ){		
					ref = ReferenceUtils.getReferenceToCell(thing, wb);
				}
				
				// TODO: Handle MemFuncPtg and MemAreaPtg
				if(ref!=null){
					params.add(true);
					refList.add(ref);
				}
			}		
			
			if(thing instanceof  AbstractFunctionPtg || thing instanceof  ValueOperatorPtg 
					|| thing instanceof  RangePtg || thing instanceof AttrPtg){
				
				String operationName = null;
				int numOfOperants = -1;
				if(thing instanceof  AbstractFunctionPtg){
					AbstractFunctionPtg func = ( AbstractFunctionPtg) thing;
					operationName = func.getName();			
					numOfOperants = func.getNumberOfOperands();
				
				}else if(thing instanceof  ValueOperatorPtg){
					
					if(thing instanceof UnaryPlusPtg || thing instanceof UnaryMinusPtg){
						continue;
					}
					
					ValueOperatorPtg op = ( ValueOperatorPtg) thing;
					operationName = op.getClass().getSimpleName().replace("Ptg","");
					numOfOperants = op.getNumberOfOperands();
				}else if(thing instanceof AttrPtg){
					
					AttrPtg attr = (AttrPtg) thing; 
					if(attr.isSum()){
						operationName = "SUM";
						numOfOperants = attr.getNumberOfOperands();
					}else{
						continue;
					}
				}
			
				if(thing instanceof  RangePtg){
					XReference first = refList.get(listIdx);
					String firstCell = first.getRange().formatAsString();
					String lastCell =  refList.get(refList.size() -1).getRange().formatAsString();
					CellRangeAddress cra = CellRangeAddress.valueOf(firstCell.concat(":").concat(lastCell));
					
					XReference newRef = new XReference(cra);
					newRef.setSheetIndex(first.getSheetIndex());
					newRef.setSheetName(first.getSheetName());
					newRef.setWorkbookIndex(first.getWorkbookIndex());
					newRef.setWorkbookName(first.getWorkbookName());
					
					while(!refList.isEmpty() && refList.size()>listIdx){
						refList.remove(listIdx);
					}			
					refList.add(newRef);
				
				}else if(operationName!=null){
					
					int numOfRef =  numOfOperants - countOperationsAsParams;
					
					// do not consider scalar operands, only references to cells and areas
					int scalar = 0;
					int t = 0;
					int k = 0;
					int l = refList.size() -1;
					for(int m=(params.size()-1); m>=0; m--){
						
						if( k >= numOfRef){
							while(!params.isEmpty() && params.size()>m ){
								params.remove(m);
							}
							break;
						}
						
						if(!params.get(m)){
							t++;
						}else{
							if(refList.get(l--).getOperationName()==null){
								scalar = scalar+t;
								k =  t + 1;	
							}
							else{
								while(!params.isEmpty() && params.size()>m ){
									params.remove(m);
								}
							}			
							t = 0;
						}					
					}
					
					// update references to cells and areas 
					numOfRef = numOfRef - scalar;
					int j = 0;
					for(int n=(refList.size()-1); n>=0; n--){
						
						if(j >= numOfRef){
							break;
						}
						
						if(refList.get(n).getOperationName()==null){
							refList.get(n).setOperationName(operationName);
							j++;
						}
					}
					
					// when all the references are updated reset to zero
					// either 1 or 0, is this correct
					updatedOperands = updatedOperands+numOfRef;
					if(updatedOperands>=refList.size()){
						countOperationsAsParams = 0;
					}else{
						countOperationsAsParams = 1;
					}
					
					operationsList.add(operationName);
					listIdx = refList.size();
				}		
			}
				
			if(thing instanceof BoolPtg || thing instanceof IntPtg || thing instanceof NumberPtg || thing instanceof StringPtg ){
				params.add(false);
			}
		}
		
		for(XReference r: refList){
			r.setOperationsList(operationsList);
		}
		
//		System.out.println("RefList: "+refList);
		return refList;
	}

	
	
//	/**
//	 * 
//	 * @param rgb
//	 * @return
//	 */
//	public static int rgbArraytoInt(byte[] rgb){
//		
//		if(rgb == null || rgb.length==0 ){
//			return -1;
//		}
//			
//		int val = 0; 		
//		for (int i=rgb.length-1; i<0; i--){		
//			
//			if(val<0){
//				val = (rgb[0] & 0xFF) << (i * 8) ;
//			}else{
//				val = val | (rgb[0] & 0xFF) << (i * 8);
//			}
//		}
//	
//		return  val;
//	}
//	
//	
//	/**
//	 * 		
//	 * @param triplet
//	 * @return
//	 */
//	public static int tripletRgbToInt(short[] triplet){
//		
//		int rgb = triplet[0];
//		rgb = rgb << 8;
//		rgb |= triplet[1];
//		rgb = rgb << 8;
//		rgb |= triplet[2];
//		
//		return rgb;
//	}
}
