package eu.asyroka.msc.model;

import java.io.Serializable;

/**
 * Created by asyroka on 4/23/2017.
 */
public enum Operator implements Serializable {
	EQUAL("="),
	LESSER("<"),
	GREATER(">"),
	NOT_EQUAL("<>"),
	LESSER_OR_EQUAL("<="),
	GREATER_OR_EQUAL(">=");

	String operator;

	Operator(String operator) {
		this.operator = operator;
	}

	public String value() {
		return operator;
	}

	public static Operator getByString(String operator) throws IllegalArgumentException {
		for (Operator value : Operator.values()) {
			if (value.value().equals(operator))
				return value;
		}
		throw new IllegalArgumentException();
	}


}
