package eu.asyroka.msc.generator.rule;

import eu.asyroka.msc.generator.ProjectionGeneratorRule;
import eu.asyroka.msc.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MultiColumnPartitioningKeyRule implements ProjectionGeneratorRule {
	@Override
	public Optional<SchemaProjection> generateProjection(Schema schema, Query query) {
		if (query.getWhereClauses().size() > 2) {
			for (Table table : schema.getTables()) {
				if (table.getName().equalsIgnoreCase(query.getFromTableName())) {
					PrimaryKey primaryKey = new PrimaryKey();
					List<String> partitioningKeys = new ArrayList<>();
					for (int i = 0; i < query.getWhereClauses().size() - 1; i++) {
						if (!choosePartitioningKeyPart(query, table, partitioningKeys, i)) {
							return Optional.empty();
						}
					}
					primaryKey.setPartitioningKey(partitioningKeys);

					if (chooseClusteringKey(query, table, primaryKey)) {
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
		WhereClause clusteringClause = query.getWhereClauses().get(query.getWhereClauses().size() - 1);
		for (Column column : table.getColumns()) {
			if (column.getName().equalsIgnoreCase(clusteringClause.getColumn())) {
				primaryKey.setClusteringKey(column.getName());
				return true;
			}
		}
		return false;
	}

	private boolean choosePartitioningKeyPart(Query query, Table table, List<String> partitioningKeys, int i) {
		for (Column column : table.getColumns()) {
			if (column.getName().equalsIgnoreCase(query.getWhereClauses().get(i).getColumn())) {
				partitioningKeys.add(column.getName());
				return true;
			}
		}
		return false;
	}
}
