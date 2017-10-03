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
public class CassandraProcessInstance {
	private String processId;
	private ProcessStatus status;
	private String errorMessage;
	private List<InputQuery> inputQueries;
	private List<InputTable> inputTables;
	private List<Query> queries;
	private Schema schema;
	private List<SchemaProjection> schemaProjections;
	private List<SchemaProjection> mergedProjections;
	private List<SchemaProjection> prioritizedProjections;
	private List<BenchmarkResult> benchmarkResults;

	public CassandraProcessInstance() {
	}

	public CassandraProcessInstance(String processId, ProcessStatus status, String errorMessage, List<InputQuery> inputQueries, List<InputTable> inputTables, List<Query> queries, Schema schema, List<SchemaProjection> schemaProjections, List<SchemaProjection> mergedProjections, List<SchemaProjection> prioritizedProjections, List<BenchmarkResult> benchmarkResults) {
		this.processId = processId;
		this.status = status;
		this.errorMessage = errorMessage;
		this.inputQueries = inputQueries;
		this.inputTables = inputTables;
		this.queries = queries;
		this.schema = schema;
		this.schemaProjections = schemaProjections;
		this.mergedProjections = mergedProjections;
		this.prioritizedProjections = prioritizedProjections;
		this.benchmarkResults = benchmarkResults;
	}
}
