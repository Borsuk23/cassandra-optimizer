package eu.asyroka.msc.model;

import lombok.Data;

/**
 * Created by asyroka on 4/23/2017.
 */
@Data
public class WhereClause {

	private String column;
	private Operator operator;
	private String value;

	public WhereClause(String column, Operator operator, String value) {
		this.column = column;
		this.operator = operator;
		this.value = value;
	}
}