/**
 * 
 */
package de.tudresden.xr.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 */
public class FileUtils {
	
	public static final String ExportedCellsFeatures = "./cellsTrainingSet.txt";
	public static final String ExportedRowsFeatures = "./rowsTrainingSet.txt";
	public static final String ExportedColumnsFeatures = "./columnsTrainingSet.txt";
	
	/**
	 * 
	 * @param directory
	 * @return
	 */
	public static File[] getExcelFiles(File directory){
				
		if(!directory.exists()){
			System.err.println("The given directory does not exist!!!");
			System.exit(1);
		}
		
		return directory.listFiles(new FilenameFilter() {			
			@Override
			public boolean accept(File dir, String name) {
				 return name.toLowerCase().endsWith(".xls")  
						|| name.toLowerCase().endsWith(".xlsx"); 
				 		// || name.toLowerCase().endsWith(".xlsm");
			}
		});
	}
	 
	
	/**
	 * 
	 * @param directory
	 * @return
	 */
	public static File[] exploreForCompletedExcelFiles(File directory){
		
		ArrayList<File> fileList = new ArrayList<File>();	
		
		if(directory.getName().compareTo("completed")==0){		
			File[] currentDirFiles = getExcelFiles(directory);
			fileList.addAll(Arrays.asList(currentDirFiles));
		}
		
		File[] directories = directory.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory() && pathname.getName().compareTo(".svn")!=0;
			}
		});
		
		for (File dir : directories) {
			fileList.addAll(Arrays.asList(exploreForCompletedExcelFiles(dir)));
		}		
		
		return fileList.toArray(new File[fileList.size()]);
	}
	
	/**
	 * 
	 * @param text
	 */
	public static void appendCellsFeatures(String text){
		appendToFile(new File(ExportedCellsFeatures), text);
	}
	
	/**
	 * 
	 * @param text
	 */
	public static void appendRowsFeatures(String text){
		appendToFile(new File(ExportedRowsFeatures), text);
	}
	
	/**
	 * 
	 * @param text
	 */
	public static void appendColumnsFeatures(String text){
		appendToFile(new File(ExportedColumnsFeatures), text);
	}
	
	public static void deleteExistingFeatureDataFiles(){
		
		deleteFile(ExportedCellsFeatures);
		deleteFile(ExportedRowsFeatures);
		deleteFile(ExportedColumnsFeatures);
		
	}
	
	/**
	 * 
	 * @param file
	 * @param text
	 */	
	public static void writeToFile(File file, String text){
		
		try{
			
			if(!file.exists()){
    			file.createNewFile();
    		}
    		
    		FileWriter fileWritter = new FileWriter(file.getName());
	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	        bufferWritter.write(text);
	        bufferWritter.close();

		}catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param file
	 * @param text
	 */	
	public static void appendToFile(File file, String text){
		
		try{
			
			if(!file.exists()){
    			file.createNewFile();
    		}
    		
    		FileWriter fileWritter = new FileWriter(file.getName(), true);
	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	        bufferWritter.write(text);
	        bufferWritter.close();

		}catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param filePath
	 */
	public static void deleteFile(String filePath){
		
		File file = new File(filePath);
		
		if(file.exists())
			file.delete();
	}
}
