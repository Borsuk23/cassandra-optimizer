package eu.asyroka.msc.service.impl;

import eu.asyroka.msc.model.*;
import eu.asyroka.msc.service.PrimaryKeySelectionService;

import java.util.List;

/**
 * Created by zason on 4/24/2017.
 */
public class PrimaryKeySelectionServiceImpl implements PrimaryKeySelectionService {
	@Override
	public SchemaProjectionRanking calculatePrimaryKeys(Schema schema, List<Query> queries) {
		SchemaProjectionRanking ranking = new SchemaProjectionRanking();
		for (Query query : queries) {
			ranking.getProjectionRanking().add(calculateSchemaProjection(schema, query));
		}
		return ranking;
	}

	private Schema calculateSchemaProjection(Schema schema, Query query) {

		schema.getTables()
				.stream()
				.filter(table -> table.getName().equals(query.getFromTableName()))
				.forEach(table -> {
					PrimaryKey primaryKey = new PrimaryKey();
					for (WhereClause whereClause : query.getWhereClauses()) {
						for (Column column : table.getColumns()) {
							if (column.getName().equals(whereClause.getColumn())) {
								primaryKey.getPartitioningKey().add(column.getName());
							}
						}
					}
					table.setPrimaryKey(primaryKey);
				});

		return schema;


	}
}
