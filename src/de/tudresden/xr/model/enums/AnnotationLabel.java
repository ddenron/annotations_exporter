/**
 * 
 */
package de.tudresden.xr.model.enums;

/**
 * An enumeration of the labels used for the annotation of regions in spreadsheets.
 * 
 */
public enum AnnotationLabel {
	

	Attributes("attributes"),
	Data("data"),
	Header("header"),
	Metadata("metadata"),
	Derived("derived"),
	Table("table"),
	MetaTitle("metatitle"),
	Notes("notes"),
	GroupHead("grouphead"),
	Other("other"),
	Mixed("mixed");
	
	
	/**
	 * The alternative name of annotation label
	 */
	private final String altName ;
	
	private AnnotationLabel(String strName){
		this.altName = strName;
	}

	/**
	 * @return the altName
	 */
	public String getAltName() {
		return altName;
	}
	
	/**
	 * Get the label of the cell based on the given alternative name.
	 * @param name the alternative name of the AnnotationLabel 
	 * @return the AnnotationLabel, or null if the the given alternative name
	 * does not match any of the defined AnnotationLabels
	 */
	public static AnnotationLabel getByAltName(String name){
		
		for (AnnotationLabel label:AnnotationLabel.values()) {
			
			if(label.getAltName().compareTo(name)==0){
				return label;
			}
		}		
		return null;
	}
}
