package eu.asyroka.msc.model;

import java.io.Serializable;

/**
 * Created by asyroka on 4/23/2017.
 */
public enum Type implements Serializable {

	TYPE_ASCII("ASCII"),
	TYPE_BIGINT("BIGINT"),
	TYPE_BLOB("BLOB"),
	TYPE_BOOLEAN("BOOLEAN"),
	TYPE_COUNTER("COUNTER"),
	TYPE_DATE("DATE"),
	TYPE_DECIMAL("DECIMAL"),
	TYPE_DOUBLE("DOUBLE"),
	TYPE_FLOAT("FLOAT"),
	TYPE_FROZEN("FROZEN"),
	TYPE_INET("INET"),
	TYPE_INT("INT"),
	TYPE_LIST("LIST"),
	TYPE_MAP("MAP"),
	TYPE_SET("SET"),
	TYPE_SMALLINT("SMALLINT"),
	TYPE_TEXT("TEXT"),
	TYPE_TIME("TIME"),
	TYPE_TIMESTAMP("TIMESTAMP"),
	TYPE_TIMEUUID("TIMEUUID"),
	TYPE_TINYINT("TINYINT"),
	TYPE_TUPLE("TUPLE"),
	TYPE_UUID("UUID"),
	TYPE_VARCHAR("VARCHAR"),
	TYPE_VARINT("VARINT");

	String type;

	Type(String type) {
		this.type = type;
	}

	public String value() {
		return type;
	}

	public static Type getByString(String type) throws IllegalArgumentException {
		for (Type value : Type.values()) {
			if (value.value().equals(type))
				return value;
		}
		throw new IllegalArgumentException();
	}


	}
