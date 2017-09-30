package eu.asyroka.msc.process;

import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;
import eu.asyroka.msc.model.SchemaProjection;
import eu.asyroka.msc.service.ProjectionService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("GenerateProjections")
public class GenerateProjectionsTask implements JavaDelegate {

	@Autowired
	private ProjectionService projectionService;


	@Override
	public void execute(DelegateExecution delegateExecution) throws Exception {
		System.out.println("GenerateProjectionsTask starting --->");
		Schema schema = (Schema) delegateExecution.getVariable("schema");
		List<Query> queries = (List<Query>) delegateExecution.getVariable("queries");

		List<SchemaProjection> schemaProjections = projectionService.generateSchemas(schema, queries);

		delegateExecution.setVariable("baseProjections", schemaProjections);
		System.out.println("---> GenerateProjectionsTask ended.");
	}
}
