package eu.asyroka.msc.model;

import lombok.Data;

/**
 * Created by asyroka on 4/23/2017.
 */
@Data
public class Column {

	private String name;
	private Type type;

	public Column(String name, Type type) {
		this.name = name;
		this.type = type;
	}
}
