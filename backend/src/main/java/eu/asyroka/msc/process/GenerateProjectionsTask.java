package eu.asyroka.msc.process;

import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;
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

@Component("GenerateProjections")
public class GenerateProjectionsTask implements JavaDelegate {

	@Autowired
	private ProjectionService projectionService;

	@Autowired
	private CassandraProcessInstanceRepository repository;

	@Override
	public void execute(DelegateExecution delegateExecution) throws Exception {
		System.out.println("GenerateProjectionsTask starting --->");
//		read process instance
		CassandraProcessInstance instance = repository.getInstance(delegateExecution.getProcessInstanceId());
		instance.setStatus(ProcessStatus.GENERATING_PROJECTIONS);

//		get parsed schema and queries
		Schema schema = (Schema) delegateExecution.getVariable("schema");
		List<Query> queries = (List<Query>) delegateExecution.getVariable("queries");

		List<SchemaProjection> schemaProjections = projectionService.generateProjections(schema, queries);

		instance.setSchemaProjections(schemaProjections);
		instance.setStatus(ProcessStatus.PROJECTIONS_GENERATED);
		repository.save(instance);

		delegateExecution.setVariable("baseProjections", schemaProjections);
		System.out.println("---> GenerateProjectionsTask ended.");
	}
}
