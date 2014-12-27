package org.jd.cfb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Collections {

	public static <T> Collection<T> newCollection(final T[] elements) {
		return newList(elements);
	}

	public static <T> List<T> newList(final T[] elements) {
		final List<T> list = new ArrayList<T>(elements.length);
		for (final T element : elements) {
			list.add(element);
		}
		return list;
	}

}
