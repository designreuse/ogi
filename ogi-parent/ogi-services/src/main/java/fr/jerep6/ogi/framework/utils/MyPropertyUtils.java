package fr.jerep6.ogi.framework.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

public final class MyPropertyUtils {
	/**
	 * Retourne la valeur de la propriété spécifiée dans l'argument path
	 * Si l'avant dernier élément du path est une liste alors l'objet retourné est une List contenant les valeurs
	 *
	 * @param o
	 *            object to retrieve property
	 * @param path
	 *            property path
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object getAdvancedProperty(Object o, String path) throws IllegalAccessException,
	InvocationTargetException, NoSuchMethodException {
		Object value = null;
		try {
			value = PropertyUtils.getNestedProperty(o, path);
		} catch (NoSuchMethodException nse) {
			// La propriété à récupérer n'existe pas. C'est peut être une liste => assayons
			List<String> split = new ArrayList<>(Arrays.asList(path.split("\\.")));

			// Ne traite que l'avant dernier niveau
			String last = split.get(split.size() - 1);
			split.remove(last);
			String newPath = Joiner.on(".").join(split);

			Object newO = PropertyUtils.getSimpleProperty(o, newPath);
			if (newO instanceof Collection) {
				Collection c = (Collection) newO;
				List<Object> s = new ArrayList<>();
				for (Object object : c) {
					s.add(PropertyUtils.getSimpleProperty(object, last));
				}
				value = s;
			}
		}
		return value;
	}

	private static Logger	LOGGER	= LoggerFactory.getLogger(MyPropertyUtils.class);

	/**
	 * Private constructor : don't allow instantiation of this class
	 */
	private MyPropertyUtils() {}

}