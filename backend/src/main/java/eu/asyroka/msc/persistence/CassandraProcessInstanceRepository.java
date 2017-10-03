package eu.asyroka.msc.persistence;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CassandraProcessInstanceRepository {

	private Map<String, CassandraProcessInstance> processInstances = new HashMap<>();

	public void save(CassandraProcessInstance instance) {
		processInstances.put(instance.getProcessId(), instance);
	}

	public CassandraProcessInstance getInstance(String processId) {
		return processInstances.get(processId);
	}


}
