package eu.asyroka.msc.rules;

import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;

import java.util.List;

public interface SchemaGenerator {
    List<Schema> generateSchemas(List<Schema> schemas, List<Query> queries);
}
