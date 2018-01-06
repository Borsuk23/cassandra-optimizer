package eu.asyroka.msc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by asyroka on 4/23/2017.
 */
@Data
public class Column implements Serializable {

	private String name;
	private Type type;

	public Column(String name, Type type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String toString() {
		return name +" "+  type.value();
	}
}
