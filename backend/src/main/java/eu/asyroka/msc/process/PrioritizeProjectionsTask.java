package eu.asyroka.msc.process;

import eu.asyroka.msc.model.SchemaProjection;
import eu.asyroka.msc.persistence.CassandraProcessInstance;
import eu.asyroka.msc.persistence.CassandraProcessInstanceRepository;
import eu.asyroka.msc.persistence.ProcessStatus;
import eu.asyroka.msc.service.RankingService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("PrioritizeProjections")
public class PrioritizeProjectionsTask implements JavaDelegate {

	@Autowired
	private RankingService rankingService;

	@Autowired
	private CassandraProcessInstanceRepository repository;

	@Override
	public void execute(DelegateExecution delegateExecution) throws Exception {
		System.out.println("PrioritizeProjectionsTask starting --->");
		CassandraProcessInstance instance = repository.getInstance(delegateExecution.getProcessInstanceId());
		instance.setStatus(ProcessStatus.PRIORITIZING_PROJECTIONS);

		List<SchemaProjection> mergedProjections = (List<SchemaProjection>) delegateExecution.getVariable("mergedProjections");
		try {
			List<SchemaProjection> prioritizedProjections = rankingService.prioritizeProjections(mergedProjections);

			instance.setPrioritizedProjections(prioritizedProjections);
			instance.setStatus(ProcessStatus.PROJECTIONS_PRIORITIZED);

			delegateExecution.setVariable("prioritizedProjections", prioritizedProjections);
			System.out.println("---> PrioritizeProjectionsTask ended.");
		} catch (Exception ex) {
			instance.setErrorMessage(ex.getMessage());
			instance.setStatus(ProcessStatus.ERROR);
			throw ex;
		}
	}
}
