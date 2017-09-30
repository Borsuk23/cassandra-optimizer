package eu.asyroka.msc.model;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asyroka on 4/23/2017.
 */
@Data
public class Schema implements Serializable {
	private List<Table> tables = new ArrayList<>();

	public Schema() {
	}

	public Schema(Schema inputSchema) {
		for (Table table : inputSchema.getTables()) {
			Table clonedTable = new Table(table.getName());
			PrimaryKey primaryKey = null;
			if (table.getPrimaryKey() != null) {
				primaryKey.setClusteringKey(table.getPrimaryKey().getClusteringKey());
				primaryKey.getPartitioningKey().addAll(table.getPrimaryKey().getPartitioningKey());
			}
			clonedTable.setPrimaryKey(primaryKey);
			clonedTable.getColumns().addAll(table.getColumns());
			this.getTables().add(clonedTable);
		}
	}
}
