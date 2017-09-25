package eu.asyroka.msc.generator;

import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;
import eu.asyroka.msc.model.SchemaProjection;

import java.util.Optional;

public class AbstractSchemaGenerator implements SchemaGenerator {
    @Override
    public Optional<SchemaProjection> generateSchema(Schema schema, Query query) {
        return null;
    }


}
