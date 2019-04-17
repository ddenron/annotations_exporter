/**
 * 
 */
package de.tudresden.xr.utils;

import org.apache.poi.hssf.usermodel.HSSFEvaluationWorkbook;
import org.apache.poi.hssf.usermodel.HSSFName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalSheet;
import org.apache.poi.ss.formula.ptg.Area2DPtgBase;
import org.apache.poi.ss.formula.ptg.Area3DPtg;
import org.apache.poi.ss.formula.ptg.Area3DPxg;
import org.apache.poi.ss.formula.ptg.AreaPtgBase;
import org.apache.poi.ss.formula.ptg.NamePtg;
import org.apache.poi.ss.formula.ptg.NameXPtg;
import org.apache.poi.ss.formula.ptg.NameXPxg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.Ref3DPtg;
import org.apache.poi.ss.formula.ptg.Ref3DPxg;
import org.apache.poi.ss.formula.ptg.RefPtg;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFName;

/**
 */
public class ReferenceUtils {
	
	/**
	 * 
	 * @param thing
	 * @param wb
	 * @return
	 */
	public static XReference getReferenceToCell(Ptg thing, Workbook wb){
		
		XReference ref = null;
		
		if(thing instanceof RefPtg ){
			RefPtg cellRef = (RefPtg) thing;
			String address = cellRef.toFormulaString();
			CellRangeAddress cra = CellRangeAddress.valueOf(address);
			ref = new XReference(cra);
			
			//System.out.println("RefPtg: "+cellRef.toFormulaString()+" Ref: "+ref);
			
			return ref;
		}

		if(thing instanceof Ref3DPtg ){
			HSSFEvaluationWorkbook  evalWb =  HSSFEvaluationWorkbook.create((HSSFWorkbook) wb);
			
			Ref3DPtg cellRef = (Ref3DPtg) thing;
			String address = cellRef.format2DRefAsString();
			int sheetIndex = cellRef.getExternSheetIndex();
			
			ExternalSheet exSh = evalWb.getExternalSheet(sheetIndex);
			String sheetName = null; 
			String externWbName = null;		
			if(exSh!=null){
				sheetName = exSh.getSheetName();
				externWbName = exSh.getWorkbookName();
			}
			
			CellRangeAddress cra = CellRangeAddress.valueOf(address);	
			ref = new XReference(cra);
			ref.setSheetIndex(sheetIndex);
			
			if(sheetName!=null && sheetName.compareTo("null")!=0)
				ref.setSheetName(sheetName);
			
			if(externWbName!=null && externWbName.compareTo("null")!=0)
				ref.setWorkbookName(externWbName);
			
			return ref;
		}
		
		
		if(thing instanceof Ref3DPxg ){
			
			Ref3DPxg cellRef = (Ref3DPxg) thing;
			String address = cellRef.format2DRefAsString();
			String sheetName = cellRef.getSheetName();
			int externWbNr = cellRef.getExternalWorkbookNumber();
			
			CellRangeAddress cra = CellRangeAddress.valueOf(address);
			ref = new XReference(cra);
			ref.setSheetName(sheetName);
			ref.setWorkbookIndex(externWbNr);
			
			return ref;
		}
		
		return ref;
	}
	

	/**
	 * 	
	 * @param thing
	 * @param wb
	 * @return
	 */
	public static XReference getReferenceToArea(Ptg thing, Workbook wb){
		
		XReference ref = null;
	
		if(thing instanceof AreaPtgBase ){

			if(thing instanceof Area2DPtgBase){
				Area2DPtgBase area = (Area2DPtgBase) thing;
				String rangeAddress = area.toFormulaString();
				
				CellRangeAddress cra = CellRangeAddress.valueOf(rangeAddress);
				ref = new XReference(cra);
				
				return ref;
			}
			
			if(thing instanceof Area3DPtg){
				
				HSSFEvaluationWorkbook  evalWb =  HSSFEvaluationWorkbook.create((HSSFWorkbook) wb);
				Area3DPtg area = (Area3DPtg) thing;
				int sheetIndex  = area.getExternSheetIndex();
				String rangeAddress = area.format2DRefAsString();
				
				ExternalSheet exSh = evalWb.getExternalSheet(sheetIndex);
				String sheetName = null; 
				String externWbName = null;		
				if(exSh!=null){
					sheetName = exSh.getSheetName();
					externWbName = exSh.getWorkbookName();
				}
				
				CellRangeAddress cra = CellRangeAddress.valueOf(rangeAddress);	
				ref = new XReference(cra);
				ref.setSheetIndex(sheetIndex);
				
				if(sheetName!=null && sheetName.compareTo("null")!=0)
					ref.setSheetName(sheetName);
				
				if(externWbName!=null && externWbName.compareToIgnoreCase("null")!=0)
					ref.setWorkbookName(externWbName);
				
				return ref;
			}
			
			if(thing instanceof Area3DPxg){
				
				Area3DPxg area = (Area3DPxg) thing;
				String rangeAddress = area.format2DRefAsString();
				String sheetName  = area.getSheetName();
				int externWb = area.getExternalWorkbookNumber();	
				
				CellRangeAddress cra = CellRangeAddress.valueOf(rangeAddress);
				ref = new XReference(cra);
				ref.setSheetName(sheetName);
				ref.setWorkbookIndex(externWb);
				
				return ref;
			}
		}	
		return ref;
	}
	
	
	/**
	 * 
	 * @param thing
	 * @param wb
	 * @return
	 */
	public static XReference getReferenceToNamedRange(Ptg thing, Workbook wb){
			
		
		XReference ref = null;
		
		if(thing instanceof NamePtg || thing instanceof NameXPtg || thing instanceof NameXPxg ){ // NameXPxg throws exception when invoking wb.getName()
			
			String sheetName = null;
			int sheetIndex = -1;
			Name namedRange = null;
			if(thing instanceof NamePtg){					
				int nameIx = ((NamePtg) thing).getIndex();
				namedRange = wb.getNameAt(nameIx);		
			}
			
			if(thing instanceof NameXPtg){
				int nameIx = ((NameXPtg) thing).getNameIndex();
				namedRange = wb.getNameAt(nameIx);						
			}
			
			if(thing instanceof NameXPxg){
				
				NameXPxg nx = (NameXPxg) thing;							
				int nameIx = wb.getNameIndex(nx.getNameName());	
				if(nameIx != -1)
					namedRange = wb.getNameAt(nameIx);
			}
			
			String strFormula = null;
			boolean isFunctionName = false;
			boolean isDeleted = false;
			
			if(namedRange instanceof HSSFName){					
				isFunctionName = ((HSSFName) namedRange).isFunctionName();
				isDeleted = ((HSSFName) namedRange).isFunctionName();
				sheetName = ((HSSFName) namedRange).getSheetName();
				sheetIndex = ((HSSFName) namedRange).getSheetIndex();
				strFormula = ((HSSFName) namedRange).getRefersToFormula();
				
			}else if(namedRange instanceof XSSFName){			
				isFunctionName = ((XSSFName) namedRange).isFunctionName();
				isDeleted = ((XSSFName) namedRange).isFunctionName();
				sheetName = ((XSSFName) namedRange).getSheetName();
				sheetIndex = ((XSSFName) namedRange).getSheetIndex();
				strFormula = ((XSSFName) namedRange).getRefersToFormula();		
			}
			
			if(strFormula!=null && !isDeleted && !isFunctionName){
				strFormula = strFormula.replace("$", "");						
				int k = strFormula.lastIndexOf("!");
				String rangeAddress = strFormula.substring(k+1);
				CellRangeAddress cra = CellRangeAddress.valueOf(rangeAddress);
				
				ref = new XReference(cra);
				ref.setSheetName(sheetName);
				ref.setSheetIndex(sheetIndex);
				
				return ref;
			}
		}
		
		return ref;
	}
}
