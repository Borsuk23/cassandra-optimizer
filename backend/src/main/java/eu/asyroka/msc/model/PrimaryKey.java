package eu.asyroka.msc.model;

import lombok.Data;

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
}
