/**
 * 
 */
package de.tudresden.xr.model.annotation;

/**
 *
 */
public interface DependentAnnotation<P extends ContainerAnnotation<?>>{
	
	P getParent();
	
	void setParent(P parent);
		
	String getKey();
}
