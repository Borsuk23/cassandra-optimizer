package eu.asyroka.msc.generator.rule;

import eu.asyroka.msc.generator.SchemaGenerator;
import eu.asyroka.msc.model.*;

import java.util.Collections;
import java.util.Optional;

public class SinglePartitioningKey implements SchemaGenerator {


    @Override
    public Optional<SchemaProjection> generateSchema(Schema schema, Query query) {
        if (query.getWhereClauses().size() == 1) {
            for (Table table : schema.getTables()) {
                if (table.getName().equalsIgnoreCase(query.getFromTableName())) {
                    for (Column column : table.getColumns()) {
                        if (column.getName().equalsIgnoreCase(query.getWhereClauses().get(0).getColumn())) {
                            PrimaryKey primaryKey = new PrimaryKey();
                            primaryKey.setPartitioningKey(Collections.singletonList(column.getName()));
                            primaryKey.setClusteringKey(null);
                            table.setPrimaryKey(primaryKey);
                            SchemaProjection projection = new SchemaProjection();
                            projection.setQuery(query);
                            projection.setSchema(schema);
                            return Optional.of(projection);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }
}