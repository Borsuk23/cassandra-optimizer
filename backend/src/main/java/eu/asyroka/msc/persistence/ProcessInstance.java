package eu.asyroka.msc.persistence;

import eu.asyroka.msc.model.BenchmarkResult;
import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;
import eu.asyroka.msc.model.SchemaProjection;
import eu.asyroka.msc.model.input.InputQuery;
import eu.asyroka.msc.model.input.InputTable;
import lombok.Data;

import java.util.List;

@Data
public class ProcessInstance {
	private String processId;
	private ProcessStatus status;
	List<InputQuery> inputQueries;
	List<InputTable> inputTables;
	List<Query> queries;
	Schema schema;
	List<SchemaProjection> schemaProjections;
	List<SchemaProjection> mergedProjections;
	List<SchemaProjection> prioritizedProjections;
	List<BenchmarkResult> benchmarkResults;

}
