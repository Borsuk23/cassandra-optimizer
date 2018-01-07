package eu.asyroka.msc.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SecondaryIndex implements Serializable {

	private String tableName;
	private String indexName;
	private String columnName;

	public SecondaryIndex(String tableName, String indexName, String columnName) {
		this.tableName = tableName;
		this.indexName = indexName;
		this.columnName = columnName;
	}

	@Override
	public String toString() {
		return "CREATE INDEX " + indexName + " ON userskeyspace." + tableName + "(" + columnName + ");";
	}
}
