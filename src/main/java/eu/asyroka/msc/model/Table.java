package eu.asyroka.msc.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asyroka on 4/23/2017.
 */
@Data
public class Table {
	private String name;
	private List<Column> columns = new ArrayList<>();

	public Table(String name) {
		this.name = name;
	}
}
