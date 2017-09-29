package eu.asyroka.msc.model.input;


import lombok.Data;

@Data
public class InputQuery {
	private String query;
	private double frequency;
	private double executionTime;
	private int importance;
}
