package eu.asyroka.msc.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asyroka on 4/24/2017.
 */
@Data
public class PrimaryKey {
	private List<String> partitioningKey = new ArrayList<>();
	private String clusteringKey;

}
