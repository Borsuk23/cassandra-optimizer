package eu.asyroka.msc.model.output;

import eu.asyroka.msc.persistence.CassandraProcessInstance;
import lombok.Data;

@Data
public class OptimizeProcessResults extends CassandraProcessInstance {
	public OptimizeProcessResults() {

	}

	public OptimizeProcessResults(CassandraProcessInstance instance) {
		super(instance.getProcessId(), instance.getStatus(), instance.getErrorMessage(), instance.getInputQueries(), instance.getInputTables(), instance.getQueries(), instance.getSchema(), instance.getSchemaProjections(), instance.getMergedProjections(), instance.getPrioritizedProjections(), instance.getBenchmarkResults(), instance.getSubstatuses());
	}
}
