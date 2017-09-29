package eu.asyroka.msc.process;

import eu.asyroka.msc.model.SchemaProjection;
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

	@Override
	public void execute(DelegateExecution delegateExecution) throws Exception {
		System.out.println("MergeProjectionsTask starting --->");
		List<SchemaProjection> baseProjections = (List<SchemaProjection>) delegateExecution.getVariable("baseProjections");

		List<SchemaProjection> mergedProjections = projectionService.mergeProjections(baseProjections);

		delegateExecution.setVariable("mergedProjections", mergedProjections);
		System.out.println("---> MergeProjectionsTask ended.");
	}
}
