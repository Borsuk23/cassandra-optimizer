package eu.asyroka.msc.controller;

import eu.asyroka.msc.model.output.OptimizeProcessResults;
import eu.asyroka.msc.model.input.OptimizeRequest;
import eu.asyroka.msc.model.output.OptimizeResponse;
import eu.asyroka.msc.persistence.CassandraProcessInstance;
import eu.asyroka.msc.persistence.CassandraProcessInstanceRepository;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OptimizerController {
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private CassandraProcessInstanceRepository repository;

// 	receive http request
	@ResponseBody
	@PostMapping(value = "/api/optimize", produces = "application/json", consumes = "application/json")
	public OptimizeResponse optimize(@RequestBody OptimizeRequest request) {

		Map<String, Object> processVariables = new HashMap<>();
		processVariables.put("inputTables", request.getTables());
		processVariables.put("inputQueries", request.getQueries());

//		start activiti process
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("optimizeCassandra", processVariables);

//		send back process id to frontend
		OptimizeResponse response = new OptimizeResponse();
		response.setProcessId(processInstance.getProcessInstanceId());
		return response;
	}

//	ask about process status
	@ResponseBody
	@GetMapping(value = "/api/status", produces = "application/json")
	public OptimizeProcessResults getOptimizeProcessResults(@RequestParam(name = "processId") String processId) {
		CassandraProcessInstance instance = repository.getInstance(processId);
		return instance != null ? new OptimizeProcessResults(instance) : new OptimizeProcessResults();
	}
}
