package eu.asyroka.msc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by asyroka on 4/23/2017.
 */
@Data
public class OrderClause implements Serializable {
	private String column;
	private OrderDirection direction;

	public OrderClause(String column, OrderDirection direction) {
		this.column = column;
		this.direction = direction;
	}
}
