package eu.asyroka.msc.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asyroka on 4/24/2017.
 */
@Data
public class PrimaryKey implements Serializable {
	private List<String> partitioningKey = new ArrayList<>();
	private String clusteringKey;

	public PrimaryKey(PrimaryKey objectToClone) {
		this.partitioningKey.addAll(objectToClone.getPartitioningKey());
		this.clusteringKey = objectToClone.getClusteringKey();
	}


	public PrimaryKey() {
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PRIMARY KEY (");
		if (partitioningKey.size() == 1) {
			builder.append(partitioningKey.get(0));
		} else {
			builder.append("(").append(partitioningKey.get(0));
			for (int i = 1; i < partitioningKey.size(); i++) {
				builder.append(",").append(partitioningKey.get(i));
			}
			builder.append(")");
		}
		if (StringUtils.isNotBlank(clusteringKey)) {
			builder.append(", ").append(clusteringKey);
		}
		builder.append(")");

		return builder.toString();
	}
}
