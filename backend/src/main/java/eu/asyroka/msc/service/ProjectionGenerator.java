package eu.asyroka.msc.service;

import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;

import java.util.List;

public interface ProjectionGenerator {

    List<Schema> generateSchemas(List<Schema> inputSchemas, List<Query> inputQueries);
}
