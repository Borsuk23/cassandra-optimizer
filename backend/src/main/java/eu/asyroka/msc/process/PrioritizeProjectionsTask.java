package eu.asyroka.msc.process;

import eu.asyroka.msc.model.SchemaProjection;
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


	@Override
	public void execute(DelegateExecution delegateExecution) throws Exception {
		System.out.println("PrioritizeProjectionsTask starting --->");
		List<SchemaProjection> mergeProjections = (List<SchemaProjection>) delegateExecution.getVariable("mergedProjections");

		List<SchemaProjection> prioritizedProjections = rankingService.prioritizeProjections(mergeProjections);

		delegateExecution.setVariable("prioritizedProjections", prioritizedProjections);
		System.out.println("---> PrioritizeProjectionsTask ended.");
	}
}