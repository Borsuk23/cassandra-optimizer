package eu.asyroka.msc.model;

import eu.asyroka.msc.model.input.DataDistribution;
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
	private ClusteringOrder clusteringOrder;
	private int recordsNo;
	private DataDistribution dataDistribution;
	private List<SecondaryIndex> indexes = new ArrayList<>();

	public Table(String name, int recordsNo, DataDistribution dataDistribution) {
		this.name = name;
		this.recordsNo = recordsNo;
		this.dataDistribution = dataDistribution;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE ");
		builder.append(name);
		builder.append(" (");
		builder.append(columns.get(0).toString());
		for (int i = 1; i < columns.size(); i++) {
			builder.append(", ").append(columns.get(i).toString());
		}

		if (primaryKey != null) {
			builder.append(", ").append(primaryKey.toString());
		}

		builder.append(")");
		if (clusteringOrder != null) {
			builder.append(" ").append(clusteringOrder.toString());
		}
		return builder.toString();
	}

}
