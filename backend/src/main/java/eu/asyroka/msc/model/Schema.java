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
			Table clonedTable = new Table(table.getName(), table.getRecordsNo(), table.getDataDistribution());

			PrimaryKey primaryKey = null;
			if (table.getPrimaryKey() != null) {
				primaryKey = new PrimaryKey(table.getPrimaryKey());
			}
			clonedTable.setPrimaryKey(primaryKey);

			ClusteringOrder clusteringOrder = null;
			if (table.getClusteringOrder() != null) {
				clusteringOrder = new ClusteringOrder(table.getClusteringOrder());
			}
			clonedTable.setClusteringOrder(clusteringOrder);

			clonedTable.getColumns().addAll(table.getColumns());
			clonedTable.getIndexes().addAll(table.getIndexes());
			this.getTables().add(clonedTable);
		}
	}
}
