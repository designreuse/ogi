package fr.jerep6.ogi.framework.test;

import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Classe abstraite définissant un TU ne nécessitant pas la BD.
 * 
 * La classe AbstractJUnit4SpringContextTests définie le lanceur de TU (SpringJUnit4ClassRunner) et exécute l'écouteur
 * DependencyInjectionTestExecutionListener qui définie active notamment les annotations
 * 
 * @author jerep6
 * 
 */
// @ContextConfiguration(locations = { "classpath:META-INF/spring/spring-context.xml", "classpath:tu-context.xml" })
public abstract class AbstractTest extends AbstractJUnit4SpringContextTests {

}