package eu.asyroka.msc.process;

import eu.asyroka.msc.model.SchemaProjection;
import eu.asyroka.msc.persistence.CassandraProcessInstance;
import eu.asyroka.msc.persistence.CassandraProcessInstanceRepository;
import eu.asyroka.msc.persistence.ProcessStatus;
import eu.asyroka.msc.service.ProjectionService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("MergeProjections")
public class MergeProjectionsTask implements JavaDelegate {

	@Autowired
	private ProjectionService projectionService;

	@Autowired
	private CassandraProcessInstanceRepository repository;

	@Override
	public void execute(DelegateExecution delegateExecution) throws Exception {
		System.out.println("MergeProjectionsTask starting --->");

		CassandraProcessInstance instance = repository.getInstance(delegateExecution.getProcessInstanceId());
		instance.setStatus(ProcessStatus.MERGING_PROJECTIONS);

		List<SchemaProjection> baseProjections = (List<SchemaProjection>) delegateExecution.getVariable("baseProjections");

		List<SchemaProjection> mergedProjections = projectionService.mergeProjections(baseProjections);
		instance.setMergedProjections(mergedProjections);
		instance.setStatus(ProcessStatus.PROJECTIONS_MERGED);

		delegateExecution.setVariable("mergedProjections", mergedProjections);
		System.out.println("---> MergeProjectionsTask ended.");
	}
}
