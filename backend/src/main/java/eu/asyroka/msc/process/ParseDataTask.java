package eu.asyroka.msc.process;

import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;
import eu.asyroka.msc.model.input.InputQuery;
import eu.asyroka.msc.model.input.InputTable;
import eu.asyroka.msc.persistence.CassandraProcessInstance;
import eu.asyroka.msc.persistence.CassandraProcessInstanceRepository;
import eu.asyroka.msc.persistence.ProcessStatus;
import eu.asyroka.msc.service.ParseDataService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ParseData")
public class ParseDataTask implements JavaDelegate {


	@Autowired
	private ParseDataService parseDataService;

	@Autowired
	private CassandraProcessInstanceRepository repository;

	//	execute ParseData task
	@Override
	public void execute(DelegateExecution delegateExecution) throws Exception {
		System.out.println("ParseDataTask starting --->");
		List<InputQuery> inputQueries = (List<InputQuery>) delegateExecution.getVariable("inputQueries");
		List<InputTable> inputTables = (List<InputTable>) delegateExecution.getVariable("inputTables");

		CassandraProcessInstance instance = new CassandraProcessInstance();
		instance.setProcessId(delegateExecution.getProcessInstanceId());
		instance.setInputQueries(inputQueries);
		instance.setInputTables(inputTables);
		instance.setStatus(ProcessStatus.PARSING_INPUT);
		repository.save(instance);

//		parse data
		try {
			List<Query> queries = parseDataService.parseQueries(inputQueries);
			Schema schema = parseDataService.parseSchema(inputTables);

//		save queries and schema to repository
			instance.setQueries(queries);
			instance.setSchema(schema);
			instance.setStatus(ProcessStatus.INPUT_PARSED);
			repository.save(instance);

//		save process variables
			delegateExecution.setVariable("queries", queries);
			delegateExecution.setVariable("schema", schema);
			System.out.println("---> ParseDataTask ended.");
		} catch (Exception ex) {
			instance.setErrorMessage(ex.getMessage());
			instance.setStatus(ProcessStatus.ERROR);
			throw ex;
		}
	}
}
