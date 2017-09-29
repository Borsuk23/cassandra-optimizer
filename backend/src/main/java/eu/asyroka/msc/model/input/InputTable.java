package eu.asyroka.msc.model.input;


import lombok.Data;

@Data
public class InputTable {
	private String table;
	private int recordsNo;
	private DataDistribution dataDistribution;
}
