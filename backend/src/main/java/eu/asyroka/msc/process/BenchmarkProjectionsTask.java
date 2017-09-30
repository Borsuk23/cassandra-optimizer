package eu.asyroka.msc.process;

import eu.asyroka.msc.model.BenchmarkResult;
import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.SchemaProjection;
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

	@Override
	public void execute(DelegateExecution delegateExecution) throws Exception {
		System.out.println("BenchmarkProjectionsTask starting --->");
		List<SchemaProjection> prioritizedProjections = (List<SchemaProjection>) delegateExecution.getVariable("prioritizedProjections");
		List<Query> queries = (List<Query>) delegateExecution.getVariable("queries");

		List<BenchmarkResult> benchmarkResults = benchmarkService.benchmarkSchemas(prioritizedProjections, queries);

		delegateExecution.setVariable("benchmarkResults", benchmarkResults);
		System.out.println("---> BenchmarkProjectionsTask ended.");
	}
}
