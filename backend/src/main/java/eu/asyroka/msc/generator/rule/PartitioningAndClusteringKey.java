package eu.asyroka.msc.generator.rule;

import eu.asyroka.msc.generator.ProjectionGeneratorRule;
import eu.asyroka.msc.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class PartitioningAndClusteringKey implements ProjectionGeneratorRule {
	@Override
	public Optional<SchemaProjection> generateProjection(Schema schema, Query query) {
		if (query.getWhereClauses().size() == 2) {
			for (Table table : schema.getTables()) {
				if (table.getName().equalsIgnoreCase(query.getFromTableName())) {
					PrimaryKey primaryKey = new PrimaryKey();

					boolean done = choosePartitioningKey(query, table, primaryKey);
					if (done) {
						done = chooseClusteringKey(query, table, primaryKey);
					}
					if (done) {
						table.setPrimaryKey(primaryKey);
						SchemaProjection projection = new SchemaProjection();
						projection.setQueries(new ArrayList<>());
						projection.getQueries().add(query);
						projection.setSchema(schema);
						return Optional.of(projection);
					}
				}
			}
		}
		return Optional.empty();
	}

	private boolean chooseClusteringKey(Query query, Table table, PrimaryKey primaryKey) {
		WhereClause whereClause2 = query.getWhereClauses().get(1);
		for (Column column : table.getColumns()) {
			if (column.getName().equalsIgnoreCase(whereClause2.getColumn())) {
				primaryKey.setClusteringKey(column.getName());
				return true;
			}
		}
		return false;
	}

	private boolean choosePartitioningKey(Query query, Table table, PrimaryKey primaryKey) {
		WhereClause whereClause1 = query.getWhereClauses().get(0);
		for (Column column : table.getColumns()) {
			if (column.getName().equalsIgnoreCase(whereClause1.getColumn())) {
				primaryKey.setPartitioningKey(Collections.singletonList(column.getName()));
				return true;
			}
		}
		return false;
	}
}
