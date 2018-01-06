package eu.asyroka.msc.model.input;


import lombok.Data;

import java.io.Serializable;

@Data
public class InputQuery implements Serializable {
	private String query;
	private int frequency;
}
