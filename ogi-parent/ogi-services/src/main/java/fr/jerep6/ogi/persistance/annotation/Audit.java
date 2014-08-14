package fr.jerep6.ogi.persistance.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Audit {

	/**
	 * List of properties to NOT audit. Take precedence over includes
	 * 
	 * @return
	 */
	String[] excludes() default {};

	/**
	 * List of properties to audit
	 * 
	 * @return
	 */
	String[] includes() default {};

}
