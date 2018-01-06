package eu.asyroka.msc.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryTableResult   implements Serializable {
	private String tableName;
	private String query;
	private String result;
}
