package eu.asyroka.msc.model.input;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OptimizeRequest implements Serializable {
private List<InputQuery> queries;
private List<InputTable> tables;
}
