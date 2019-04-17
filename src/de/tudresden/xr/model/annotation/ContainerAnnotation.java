/**
 * 
 */
package de.tudresden.xr.model.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a spreadsheet annotation that contains other annotations. 
 * @param <T> The Class of the objects that act as children (dependent) Annotations  
 */
public abstract class ContainerAnnotation <T extends DependentAnnotation<?>> extends Annotation<ContainerAnnotation<T>>{
	

	/*
	 * A LinkedHashMap that stores all the annotations that are contained by this annotation
	 * In other words, all annotations in the Map depend on this container annotation object.  
	 */
	private HashMap<String, T> dependentAnnotations;
	private HashMap<String, T> immediateChildren;
	
	
	protected ContainerAnnotation(){
		this.dependentAnnotations =  new HashMap<String, T>();
		this.immediateChildren =  new HashMap<String, T>();
	}
	
	/**
	 * Add a new dependent annotation
	 * @param key a string that is used as an id (key) for the annotation object
	 * @param annotation the annotation object to add
	 * @param isImmediate when true the given annotation is considered an immediate children, otherwise not.
	 * This is to differentiate from cases with multiple levels of hierarchy (containment). In other words,
	 * this ContainerAnnotation might contain children that themselves contain other annotations. 
	 */
	protected void addAnnotation(String key, T annotation, boolean isImmediate){
		if(isImmediate){
			this.immediateChildren.put(key, annotation);
			this.dependentAnnotations.put(key, annotation);
		}else{
			this.dependentAnnotations.put(key, annotation);
		}	
	}
	
	
	/**
	 * Add a new dependent annotation
	 * @param key a string that is used as an id (key) for the annotation object
	 * @param annotation the annotation object to add
	 */
	protected void addImmediateChildren(String key, T annotation){
		this.immediateChildren.put(key, annotation);
		this.dependentAnnotations.put(key, annotation);
	}
	
	
	/**
	 * Remove the dependent annotation that has the given key
	 * @param key a string that is used as an id (key) for the annotation object
	 */
	protected void removeAnnotation(String key){
		
		if(this.dependentAnnotations.containsKey(key))
			this.dependentAnnotations.remove(key);
		
		if(this.immediateChildren.containsKey(key))
			this.immediateChildren.remove(key);
	}
	
	/**
	 * Remove all annotations depending on (contained by) this
	 */
	protected void removeAllDependentAnnotations(){
		this.dependentAnnotations.clear();
		this.immediateChildren.clear();
	}
	
	
	/**
	 * Get dependent annotation by id (key)
	 * @param key a string that is used as an id (key) for the annotation object
	 * @return the annotation object
	 */
	protected T getAnnotation(String key){
		return this.dependentAnnotations.get(key);
	}
	
	
	/**
	 * Get list of all dependent annotations 
	 * @return a list of annotation objects
	 */
	protected List<T> getAllDependentAsList(){
		return new ArrayList<T>(this.dependentAnnotations.values());
	}
	
	
	/**
	 * Get list of all immediate children annotations 
	 * @return a list of annotation objects
	 */
	protected List<T> getImmediateChildrenAsList(){
		return new ArrayList<T>(this.immediateChildren.values());
	}
	
	
	/**
	 * Get map of all dependent annotations 
	 * @return a list of annotation objects
	 */
	protected Map<String, T> getAllDependentAsMap(){
		return new HashMap <String, T>(this.dependentAnnotations);
	}
	
	
	/**
	 * Get map of all immediate children annotations 
	 * @return a list of annotation objects
	 */
	protected Map<String, T> getImmediateChildrenAsMap(){
		return new HashMap <String, T>(this.immediateChildren);
	}
	
	
	/**
	 * Check if an annotation is contained based on the given key
	 * @param key a string that is used as an id (key) for the annotation object
	 * @return true if it is contained, false otherwise
	 */
	protected boolean containsAnnotation(String key){
		return this.dependentAnnotations.containsKey(key);
	}
	
	
	/**
	 * Check if an annotation is an immediate children based on the given key
	 * @param key a string that is used as an id (key) for the annotation object
	 * @return true if the annotation object is an immediate child, false otherwise
	 */
	protected boolean isImmediateChild(String key){
		return this.immediateChildren.containsKey(key);
	}
}
