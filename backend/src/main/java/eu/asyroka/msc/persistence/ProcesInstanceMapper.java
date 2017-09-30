package eu.asyroka.msc.persistence;

import org.springframework.stereotype.Repository;

@Repository
public interface ProcesInstanceMapper {

	Integer save(ProcessInstance instance);

	ProcessInstance getInstance(String processId);



}
