# Annotations Exporter
The code in this repository illustrates how to read annotations from spreadsheet files, created by this [annotation tool](https://github.com/ddenron/annotation_tool).

In the ./jar folder you can find two executables. The RangeAnnotationsExporter.jar will export the annotations as were recorded by the [annotation tool](https://github.com/ddenron/annotation_tool). The CellAnnotationsExporter.jar exports the annotations at cell granularity. It ommits cells that are  hidden, empty, or blank (i.e., containing only white space characters).
Both executables export annotations in a CSV format. An example usage is shown below:

$ java -jar CellAnnotationsExporter.jar -inPath "/path/to/annotated/files" -outPath "/path/to/export/file" [-asMerged]

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -inPath INPATH &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  The path to the directory that holds the annotated Excel files.
                    
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -outPath OUTPATH &nbsp;&nbsp;&nbsp; The path to the file where the exported annotations will be written

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -asMerged &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; (Optional) When this flag is used, the merged areas of the sheet <br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; will be treated as single cell annotations. Otherwise, if not used, <br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; each cell in the merged area will be exported individually. <br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; This option is not available for RangeExportAnnotations.jar

**For more details** on the implementation of these scripts, refer to the CellAnnotationExporter.java and RangeAnnotationExporter.java in ./src/de/tudresden/xr/main. As well as check the the ./src/de/tudresden/xr/utils/WorkbookUtils.java


**Dependencies**: 
* Java 8
* Apache POI v3.17
* XMLBeans v2.6.0
