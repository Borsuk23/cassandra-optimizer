package eu.asyroka.msc.model.input;

import lombok.Data;

import java.util.List;

@Data
public class OptimizeRequest {
private List<InputQuery> queries;
private List<InputTable> tables;
}
