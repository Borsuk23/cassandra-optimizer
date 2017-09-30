package eu.asyroka.msc.model.input;


import lombok.Data;

import java.io.Serializable;

@Data
public class InputTable implements Serializable {
	private String table;
	private int recordsNo;
	private DataDistribution dataDistribution;
}
