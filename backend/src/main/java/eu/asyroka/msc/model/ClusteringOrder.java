package eu.asyroka.msc.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClusteringOrder implements Serializable {
	private String columnName;
	private OrderDirection direction;


	public ClusteringOrder(String columnName, OrderDirection direction) {
		this.columnName = columnName;
		this.direction = direction;
	}


	public ClusteringOrder() {
	}

	@Override
	public String toString() {
		return "WITH CLUSTERING ORDER BY (" +
				columnName +
				" " +
				direction.toString() +
				")";
	}
}
