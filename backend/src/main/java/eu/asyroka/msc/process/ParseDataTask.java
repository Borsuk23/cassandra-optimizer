package eu.asyroka.msc.process;

import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;
import eu.asyroka.msc.model.input.InputQuery;
import eu.asyroka.msc.model.input.InputTable;
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

	@Override
	public void execute(DelegateExecution delegateExecution) throws Exception {
		System.out.println("ParseDataTask starting --->");
		List<InputQuery> inputQueries = (List<InputQuery>) delegateExecution.getVariable("inputQueries");
		List<InputTable> inputTables = (List<InputTable>) delegateExecution.getVariable("inputTables");

		List<Query> queries = parseDataService.parseQueries(inputQueries);
		Schema schema = parseDataService.parseSchema(inputTables);

		delegateExecution.setVariable("queries", queries);
		delegateExecution.setVariable("schema", schema);
		System.out.println("---> ParseDataTask ended.");
	}
}
