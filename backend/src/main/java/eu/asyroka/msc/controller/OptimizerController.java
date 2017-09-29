package eu.asyroka.msc.controller;

import eu.asyroka.msc.model.input.OptimizeRequest;
import eu.asyroka.msc.model.output.OptimizeResponse;
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


	@ResponseBody
	@PostMapping(value = "/api/optimize", produces = "application/json", consumes = "application/json")
	public OptimizeResponse optimize(@RequestBody OptimizeRequest request) {

		Map<String, Object> processVariables = new HashMap<>();
		processVariables.put("inputTables", request.getTables());
		processVariables.put("inputQueries", request.getQueries());

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("optimizeCassandra", processVariables);

		OptimizeResponse response = new OptimizeResponse();
		response.setProcessId(processInstance.getProcessInstanceId());
		return response;
	}

}
