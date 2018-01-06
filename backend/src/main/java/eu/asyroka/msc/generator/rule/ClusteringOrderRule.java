package eu.asyroka.msc.generator.rule;

import eu.asyroka.msc.generator.ProjectionGeneratorRule;
import eu.asyroka.msc.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class ClusteringOrderRule implements ProjectionGeneratorRule {
	@Override
	public Optional<SchemaProjection> generateProjection(Schema schema, Query query) {
		if (!query.getOrderClauses().isEmpty()) {
			for (Table table : schema.getTables()) {
				if (table.getName().equalsIgnoreCase(query.getFromTableName())) {
					for (Column column : table.getColumns()) {
						if (column.getName().equalsIgnoreCase(query.getOrderClauses().get(0).getColumn())) {
							ClusteringOrder clusteringOrder = new ClusteringOrder();
							clusteringOrder.setColumnName(query.getOrderClauses().get(0).getColumn());
							clusteringOrder.setDirection(query.getOrderClauses().get(0).getDirection());
							table.setClusteringOrder(clusteringOrder);
							SchemaProjection projection = new SchemaProjection();
							projection.setQueries(new ArrayList<>());
							projection.getQueries().add(query);
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
