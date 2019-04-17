/**
 * 
 */
package de.tudresden.xr.main;

import java.io.File;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;


import de.tudresden.xr.model.annotation.RangeAnnotation;
import de.tudresden.xr.model.annotation.TableAnnotation;
import de.tudresden.xr.model.annotation.WorkbookAnnotation;
import de.tudresden.xr.model.annotation.WorksheetAnnotation;
import de.tudresden.xr.model.enums.AnnotationLabel;
import de.tudresden.xr.utils.FileUtils;
import de.tudresden.xr.utils.WorkbookUtils;



public class RangeAnnotationsExporter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String dirPath = null;
		String outputPath = null;
		
		if(args.length!=4){
			System.err.println("Expected 4 arguments, instead was given " + args.length);
			System.exit(1);
		}		

        for (int j=0; j<args.length; j++) {
        	if(args[j].compareToIgnoreCase("-inPath")==0){
        		if (j+1<args.length)
        			dirPath = args[j+1];
        	}
        		
        	if(args[j].compareToIgnoreCase("-outPath")==0){
        		if (j+1<args.length)
        			outputPath = args[j+1];
        	}
        }
        
		
        if(dirPath==null){
        	System.err.println("Missing argument -inPath!");
			System.exit(1);
        }
        
        if(outputPath==null){
        	System.err.println("Missing argument -outPath!");
			System.exit(1);
        }
        
	
		File annotations_dir = new File(dirPath);
		if(!annotations_dir.exists()){
			System.err.println("The specified inPath does not exists!\n"+dirPath);
			System.exit(1);
		}
		
		
		File[] annotatedFiles = FileUtils.getExcelFiles(annotations_dir);
		
		if(annotatedFiles.length==0){			
			System.err.println("There are no annotated excel files in the specified directory!!!");
			System.exit(1);
		}
		System.out.println("There are "+annotatedFiles.length+" files in the directory\n");
			
		StringBuilder sb = new StringBuilder();
		sb.append("FileName");
		sb.append(",").append("SheetName");
		sb.append(",").append("SheetIndex");
		sb.append(",").append("AnnotationName");
		sb.append(",").append("AnnotationLabel");
		sb.append(",").append("AnnotationParent");
		sb.append(",").append("AnnotationAddress");
		sb.append(",").append(String.valueOf("FirstColumn"));
		sb.append(",").append(String.valueOf("FirstRow"));
		sb.append(",").append(String.valueOf("LastColumn"));
		sb.append(",").append(String.valueOf("LastRow"));
		sb.append("\n");
		
		for (int i=0; i<annotatedFiles.length; i++) {
			
			File file = annotatedFiles[i];		
			System.out.println(file.getName());
			
			try{
				
				Workbook wb = WorkbookUtils.createWorkbook(file);
				WorkbookAnnotation wa = WorkbookUtils.readAnnotationData(wb, file.getName());
				
				if(wa==null)
					throw new Exception("The file \""+file.getName()+"\" does not contain annotations!!!");
				
				for (String sheetKey : wa.getWorksheetsMap().keySet()){	
					
					// export table annotations from the sheet
					for(TableAnnotation ta: wa.getTablesForSheet(sheetKey)){
						sb.append("\"").append(file.getName()).append("\"");
						sb.append(",").append("\"").append(ta.getSheetName()).append("\"");
						sb.append(",").append(ta.getSheetIndex());
						sb.append(",").append("\"").append(ta.getTableName()).append("\"");
						sb.append(",").append(AnnotationLabel.Table.name());	
						
						if (ta.getParent() instanceof WorksheetAnnotation){
							sb.append(",").append("\"").append(ta.getSheetName()).append("\"");
						}else{
							throw new Exception("TableAnnotations can only have WorksheetAnnotations as parents!!!");
						}
				
						sb.append(",").append(ta.getRangeAddress());
						CellRangeAddress cra = CellRangeAddress.valueOf(ta.getRangeAddress());
						sb.append(",").append(String.valueOf(cra.getFirstColumn()));
						sb.append(",").append(String.valueOf(cra.getFirstRow()));
						sb.append(",").append(String.valueOf(cra.getLastColumn()));
						sb.append(",").append(String.valueOf(cra.getLastRow()));
						sb.append("\n");
						
					}
										
					
					// export range annotations from the sheet
					for (RangeAnnotation ra : wa.getRangeAnnotationsForSheet(sheetKey)){				
						sb.append("\"").append(file.getName()).append("\"");
						sb.append(",").append("\"").append(ra.getSheetName()).append("\"");
						sb.append(",").append(ra.getSheetIndex());
						sb.append(",").append("\"").append(ra.getName()).append("\"");
						sb.append(",").append(ra.getAnnotationLabel().name());
							
						// the parent can be either a table or a worksheet
						if (ra.getParent() instanceof TableAnnotation){
							sb.append(",").append("\"").append(((TableAnnotation) ra.getParent()).getTableName()).append("\"");
						}else if (ra.getParent() instanceof WorksheetAnnotation){
							sb.append(",").append("\"").append(ra.getSheetName()).append("\"");
						}else{
							throw new Exception("RangeAnnotations can have as parent either a WorksheetAnnotation or TableAnnotation!!!");
						}
						
						sb.append(",").append(ra.getRangeAddress());
						CellRangeAddress cra = CellRangeAddress.valueOf(ra.getRangeAddress());
						sb.append(",").append(String.valueOf(cra.getFirstColumn()));
						sb.append(",").append(String.valueOf(cra.getFirstRow()));
						sb.append(",").append(String.valueOf(cra.getLastColumn()));
						sb.append(",").append(String.valueOf(cra.getLastRow()));
						sb.append("\n");
					}
							
				}
			}catch(Exception ex){				
				ex.printStackTrace();
			}
		}
		
		File output = new File(outputPath);
		FileUtils.writeToFile(output, sb.toString());
		
		System.out.println("\nDone!");
	}
}
