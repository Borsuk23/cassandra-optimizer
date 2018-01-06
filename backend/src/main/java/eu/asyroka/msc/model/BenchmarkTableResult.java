package eu.asyroka.msc.model;

import lombok.Data;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Data
public class BenchmarkTableResult  implements Serializable {
	private String tableName;
	private String resultFile;
	private List<QueryTableResult> resultsPerQuery = new ArrayList<>();
}
