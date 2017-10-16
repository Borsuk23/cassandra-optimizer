package eu.asyroka.msc.generator;

import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;
import eu.asyroka.msc.model.SchemaProjection;

import java.util.List;
import java.util.Optional;

public interface ProjectionGeneratorRule {
    Optional<SchemaProjection> generateProjection(Schema schema, Query query);
}
