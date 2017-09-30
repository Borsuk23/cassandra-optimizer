package eu.asyroka.msc.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asyroka on 4/23/2017.
 */
@Data
public class Table implements Serializable {
	private String name;
	private List<Column> columns = new ArrayList<>();

	private PrimaryKey primaryKey;

	public Table(String name) {
		this.name = name;
	}
}
