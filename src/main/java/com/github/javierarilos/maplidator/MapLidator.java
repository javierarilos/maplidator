package com.github.javierarilos.maplidator;

import java.util.List;
import java.util.Map;

/**
 * Utility class for easy Map validation.For example, for validating maps
 * resulting from JSON deserialization.
 * 
 */

public class MapLidator {
    public enum Validation {
	EXISTS, STRING, NUMBER, BOOLEAN, OBJECT, ARRAY, NULLABLE, STRING_NOT_EMPTY, NOT_NULL, REGEXP, LENGTH, EQUALS, MIN, MAX
    }

    public static void validate(Map<String, Object> map, String field, Object... args) {
	Object value = getValue(map, field);
	// TODO: implement check for Maplidator.Validation that require
	// parameters, eg. MIN, 0, MAX, 25, REGEXP, ".+@.+\\.[a-z]+"
	Validation currValidation;
	for (Object arg : args) {
	    if (arg instanceof Validation) {
		currValidation = (Validation) arg;
		switch (currValidation) {
		case NOT_NULL:
		    checkNotNull(field, value);
		    break;
		case STRING:
		    checkString(field, value);
		    break;
		case STRING_NOT_EMPTY:
		    checkNotEmpty(field, value);
		    break;
		case OBJECT:
		    checkObject(field, value);
		    break;
		case ARRAY:
		    checkArray(field, value);
		    break;

		default:
		    throw new UnsupportedOperationException(String.format(
			    "currently validation %s supported. pending implementation", currValidation));

		}

	    } else
		throw new UnsupportedOperationException(
			"currently non-Validation args are not supported. pending implementation");
	}
    }

    private static void checkArray(String field, Object value) {
	// Json array is mapped as a List
	if (value != null && !(value instanceof List))
	    throw new IllegalArgumentException(String.format("Object should be an Array for path: %s", field));
    }

    private static void checkObject(String field, Object value) {
	// Json object is mapped as a Map
	if (value != null && !(value instanceof Map))
	    throw new IllegalArgumentException(String.format("Object should be a Map for path: %s", field));
    }

    private static void checkNotEmpty(String field, Object value) {
	checkString(field, value);
	if (value != null && ((String) value).isEmpty())
	    throw new IllegalArgumentException(String.format("Object should not be a Empty String for path: %s", field));

    }

    private static void checkString(String field, Object value) {
	if (value != null && !(value instanceof String))
	    throw new IllegalArgumentException(String.format("Object should be a String for path: %s", field));
    }

    private static void checkNotNull(String field, Object value) {
	if (value == null)
	    throw new IllegalArgumentException(String.format("Object should not be null for path: %s", field));
    }

    private static Object getValue(Map<String, Object> map, String path) {
	// TODO: field should support javascript expressions, eg: "user.name" or
	// "user.addresses[0].zipCode"
	// REVISIT: naive implementation for ".". pending implementation for
	// arrays.
	Object currentObject = null;
	String[] fields = path.split("\\.");
	for (int i = 0; i < fields.length; i++) {
	    String currField = fields[i];
	    currentObject = map.get(currField);
	    if (i < fields.length-1)//not the last field in path. it should not be null and should be a map.
		if (currentObject != null && currentObject instanceof Map<?, ?>)
		    map = (Map<String, Object>) currentObject;
		else
		    throw new IllegalArgumentException(
			    String.format(
				    "Unexpected non Json Object (map) while evaluating field %s in path : %s : Object value is : %s",
				    currField, path, currentObject));
	}
	return currentObject;
    }

}
