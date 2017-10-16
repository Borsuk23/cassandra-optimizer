package eu.asyroka.msc.service;

import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;
import eu.asyroka.msc.model.SchemaProjection;

import java.util.List;

public interface ProjectionService {

    List<SchemaProjection> generateProjections(Schema inputSchema, List<Query> inputQueries);

    List<SchemaProjection> mergeProjections(List<SchemaProjection> schemaProjections);
}
