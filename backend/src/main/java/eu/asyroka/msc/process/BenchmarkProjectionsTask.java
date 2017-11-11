package eu.asyroka.msc.process;

import eu.asyroka.msc.model.BenchmarkResult;
import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.SchemaProjection;
import eu.asyroka.msc.persistence.CassandraProcessInstance;
import eu.asyroka.msc.persistence.CassandraProcessInstanceRepository;
import eu.asyroka.msc.persistence.ProcessStatus;
import eu.asyroka.msc.service.BenchmarkService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("BenchmarkProjections")
public class BenchmarkProjectionsTask implements JavaDelegate {

	@Autowired
	private BenchmarkService benchmarkService;

	@Autowired
	private CassandraProcessInstanceRepository repository;

	@Override
	public void execute(DelegateExecution delegateExecution) throws Exception {
		System.out.println("BenchmarkProjectionsTask starting --->");
		CassandraProcessInstance instance = repository.getInstance(delegateExecution.getProcessInstanceId());
		instance.setStatus(ProcessStatus.BENCHMARKING);

		List<SchemaProjection> prioritizedProjections = (List<SchemaProjection>) delegateExecution.getVariable("prioritizedProjections");
		List<Query> queries = (List<Query>) delegateExecution.getVariable("queries");

		try {
			List<BenchmarkResult> benchmarkResults = benchmarkService.benchmarkSchemas(prioritizedProjections, queries);
			instance.setBenchmarkResults(benchmarkResults);
			instance.setStatus(ProcessStatus.FINISHED);

			delegateExecution.setVariable("benchmarkResults", benchmarkResults);
			System.out.println("---> BenchmarkProjectionsTask ended.");
		} catch (Exception ex) {
			instance.setErrorMessage(ex.getMessage());
			instance.setStatus(ProcessStatus.ERROR);
			throw ex;
		}


	}
}
