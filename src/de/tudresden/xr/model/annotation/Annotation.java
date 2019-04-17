/**
 * 
 */
package de.tudresden.xr.model.annotation;

/**
 *
 */
public abstract class Annotation <T extends Annotation<?>> {
	
	/**
	 * Check if the given annotation object is equal to this one
	 * @param annotation the annotation object to compare this object to
	 * @return true if the objects are equal, false otherwise
	 */
	public abstract boolean equals(T annotation);

	
	/**
	 * Get the hashcode of this object
	 * @return the hashcode of the object
	 */
	@Override
	public abstract int hashCode();

}
