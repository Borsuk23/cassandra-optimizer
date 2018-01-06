package eu.asyroka.msc.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class BenchmarkResult implements Serializable {
	private Schema schema;
	private String result;

	private List<BenchmarkTableResult> results = new ArrayList<>();




}
