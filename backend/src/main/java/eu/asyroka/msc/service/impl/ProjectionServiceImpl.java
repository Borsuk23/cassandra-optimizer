package eu.asyroka.msc.service.impl;

import eu.asyroka.msc.generator.ProjectionGeneratorRule;
import eu.asyroka.msc.generator.ProjectionGeneratorRulesFactory;
import eu.asyroka.msc.model.*;
import eu.asyroka.msc.service.ProjectionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectionServiceImpl implements ProjectionService {

	@Autowired
	private ProjectionGeneratorRulesFactory projectionGeneratorRulesFactory;

	@Override
	public List<SchemaProjection> generateProjections(Schema inputSchema, List<Query> inputQueries) {
		List<SchemaProjection> schemaProjections = new ArrayList<>();
		for (int i = 0, inputQueriesSize = inputQueries.size(); i < inputQueriesSize; i++) {
			Query inputQuery = inputQueries.get(i);
			schemaProjections.addAll(generateProjections(inputSchema, inputQuery));
		}
		return createIndexes(schemaProjections, inputQueries);
	}

	@Override
	public List<SchemaProjection> mergeProjections(List<SchemaProjection> schemaProjections) {
		List<SchemaProjection> merged = new ArrayList<>();
		merged.addAll(merge(schemaProjections, 0, null));
		merged = clearSubsetMerges(merged);
		return merged;
	}

	@Override
	public List<SchemaProjection> createIndexes(List<SchemaProjection> schemaProjections, List<Query> inputQueries) {
		for (SchemaProjection schemaProjection : schemaProjections) {
			createIndexes(schemaProjection, inputQueries);
		}
		return schemaProjections;
	}


	private void createIndexes(SchemaProjection schemaProjection, List<Query> inputQueries) {
		for (Table table : schemaProjection.getSchema().getTables()) {
			createMissingIndexes(table, inputQueries);
		}
	}

	private void createMissingIndexes(Table table, List<Query> inputQueries) {
		PrimaryKey primaryKey = table.getPrimaryKey();
		if (primaryKey != null) {
			for (Query query : inputQueries) {
				if (query.getWhereClauses().stream().allMatch(whereClause ->
						matchClusteringKey(primaryKey, whereClause) ||
								primaryKey.getPartitioningKey().stream().anyMatch(key -> key.equalsIgnoreCase(whereClause.getColumn()))))
					continue;

				List<WhereClause> notMatchedClauses = query.getWhereClauses().stream().filter(whereClause ->
						!matchClusteringKey(primaryKey, whereClause) &&
								primaryKey.getPartitioningKey().stream().noneMatch(key -> key.equalsIgnoreCase(whereClause.getColumn()))).collect(Collectors.toList());

				for (WhereClause notMatchedClause : notMatchedClauses) {
					if (table.getIndexes().stream().noneMatch(index -> index.getColumnName().equalsIgnoreCase(notMatchedClause.getColumn()))) {
						table.getIndexes().add(new SecondaryIndex(table.getName(), table.getName() + "_" + notMatchedClause.getColumn() + "_idx", notMatchedClause.getColumn()));
					}
				}

			}
		} else {
			for (Query query : inputQueries) {
				for (WhereClause whereClause : query.getWhereClauses()) {
					if (table.getIndexes().stream().noneMatch(index -> index.getColumnName().equalsIgnoreCase(whereClause.getColumn()))) {
						table.getIndexes().add(new SecondaryIndex(table.getName(), table.getName() + "_" + whereClause.getColumn() + "_idx", whereClause.getColumn()));
					}
				}
			}
		}


	}

	private boolean matchClusteringKey(PrimaryKey primaryKey, WhereClause whereClause) {
		return primaryKey.getClusteringKey() != null && whereClause.getColumn().equalsIgnoreCase(primaryKey.getClusteringKey());
	}


	private List<SchemaProjection> clearSubsetMerges(List<SchemaProjection> merged) {
		return merged.stream().filter(schemaProjection -> !checkIsSubset(schemaProjection, merged)).collect(Collectors.toList());
	}

	private boolean checkIsSubset(SchemaProjection inputSchema, List<SchemaProjection> comparingSchema) {
		for (SchemaProjection projection : comparingSchema) {
			if (!inputSchema.equals(projection)) {
				boolean isSubset = true;
				for (Table input : inputSchema.getSchema().getTables()) {
					for (Table comparing : projection.getSchema().getTables()) {
						if (input.getName().equalsIgnoreCase(comparing.getName())) {
							PrimaryKey inputPrimaryKey = input.getPrimaryKey();
							PrimaryKey comparingPrimaryKey = comparing.getPrimaryKey();
							isSubset &= inputPrimaryKey == null && comparingPrimaryKey != null || comparePrimaryKey(inputPrimaryKey, comparingPrimaryKey);
							ClusteringOrder inputClusteringOrder = input.getClusteringOrder();
							ClusteringOrder comparingClusteringOrder = comparing.getClusteringOrder();
							isSubset &= inputClusteringOrder == null && comparingClusteringOrder != null || compareClusteringOrder(inputClusteringOrder, comparingClusteringOrder);
						}
					}
				}
				if (isSubset) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean compareClusteringOrder(ClusteringOrder inputClusteringOrder, ClusteringOrder comparingClusteringOrder) {
		if (inputClusteringOrder == null && comparingClusteringOrder == null)
			return true;
		if (inputClusteringOrder != null && comparingClusteringOrder == null)
			return false;
		if (inputClusteringOrder == null)
			return true;
		return inputClusteringOrder.getColumnName().equals(comparingClusteringOrder.getColumnName()) && inputClusteringOrder.getDirection().equals(comparingClusteringOrder.getDirection());

	}

	private boolean comparePrimaryKey(PrimaryKey inputPrimaryKey, PrimaryKey comparingPrimaryKey) {
		if (inputPrimaryKey == null && comparingPrimaryKey == null)
			return true;
		if (inputPrimaryKey == null || comparingPrimaryKey == null)
			return false;
		if (inputPrimaryKey.getPartitioningKey().size() == comparingPrimaryKey.getPartitioningKey().size()) {
			if ((inputPrimaryKey.getClusteringKey() != null && comparingPrimaryKey.getClusteringKey() == null)
					|| (inputPrimaryKey.getClusteringKey() == null && comparingPrimaryKey.getClusteringKey() != null)) {
				return false;
			}
			boolean isEqual = true;
			if (inputPrimaryKey.getClusteringKey() != null && comparingPrimaryKey.getClusteringKey() != null) {
				isEqual = inputPrimaryKey.getClusteringKey().equalsIgnoreCase(comparingPrimaryKey.getClusteringKey());
			}


			for (int i = 0; i < inputPrimaryKey.getPartitioningKey().size(); i++) {
				isEqual &= inputPrimaryKey.getPartitioningKey().get(i).equalsIgnoreCase(comparingPrimaryKey.getPartitioningKey().get(i));
			}
			return isEqual;
		} else {
			return false;
		}
	}

	private List<SchemaProjection> merge(List<SchemaProjection> schemaProjections, int index, SchemaProjection partiallyMerged) {
		List<SchemaProjection> merged = new ArrayList<>();
		for (int i = index; i < schemaProjections.size(); i++) {
			Optional<SchemaProjection> schemaProjection = mergeProjections(partiallyMerged, schemaProjections.get(i));
			if (schemaProjection.isPresent()) {
				merged.add(schemaProjection.get());
				merged.addAll(merge(schemaProjections, i + 1, schemaProjection.get()));
			}
		}
		return merged;
	}

	private Optional<SchemaProjection> mergeProjections(SchemaProjection partiallyMerged, SchemaProjection schemaProjection) {
		if (partiallyMerged == null) {
			return Optional.of(new SchemaProjection(schemaProjection));
		}
		Boolean mergedFlag = false;
		SchemaProjection merged = new SchemaProjection(partiallyMerged);

		for (Table table : merged.getSchema().getTables()) {
			mergedFlag |= mergePrimaryKey(schemaProjection, mergedFlag, table);
			mergedFlag |= mergeClusteringOrder(schemaProjection, mergedFlag, table);
		}
		if (mergedFlag) {
			for (Query query : schemaProjection.getQueries()) {
				if (!merged.getQueries().contains(query)) {
					merged.getQueries().add(query);
				}
			}
			return Optional.of(merged);
		} else {
			return Optional.empty();
		}
	}

	private Boolean mergeClusteringOrder(SchemaProjection schemaProjection, Boolean mergedFlag, Table table) {
		if (table.getClusteringOrder() == null) {
			if (table.getPrimaryKey() != null) {
				if (StringUtils.isNotBlank(table.getPrimaryKey().getClusteringKey())) {
					for (Table tableToMerge : schemaProjection.getSchema().getTables()) {
						if (tableToMerge.getName().equalsIgnoreCase(table.getName()) && tableToMerge.getClusteringOrder() != null && tableToMerge.getClusteringOrder().getColumnName().equalsIgnoreCase(table.getPrimaryKey().getClusteringKey())) {
							table.setClusteringOrder(tableToMerge.getClusteringOrder());
							mergedFlag = true;
						}
					}
				} else {
					for (Table tableToMerge : schemaProjection.getSchema().getTables()) {
						if (tableToMerge.getName().equalsIgnoreCase(table.getName()) && tableToMerge.getClusteringOrder() != null) {
							table.getPrimaryKey().setClusteringKey(tableToMerge.getClusteringOrder().getColumnName());
							table.setClusteringOrder(tableToMerge.getClusteringOrder());
							mergedFlag = true;
						}
					}
				}
			}
		}
		return mergedFlag;
	}

	private Boolean mergePrimaryKey(SchemaProjection schemaProjection, Boolean mergedFlag, Table table) {
		if (table.getPrimaryKey() == null) {
			for (Table tableToMerge : schemaProjection.getSchema().getTables()) {
				if (tableToMerge.getName().equalsIgnoreCase(table.getName()) && tableToMerge.getPrimaryKey() != null) {
					table.setPrimaryKey(tableToMerge.getPrimaryKey());
					mergedFlag = true;
				}
			}
		}
		return mergedFlag;
	}

	private List<SchemaProjection> generateProjections(Schema inputSchema, Query inputQuery) {
		List<SchemaProjection> projectionList = new ArrayList<>();
		List<ProjectionGeneratorRule> projectionGeneratorRules = projectionGeneratorRulesFactory.getProjectionGeneratorRules();
		for (int i = 0; i < projectionGeneratorRules.size(); i++) {
			Schema schema = new Schema(inputSchema);
			Optional<SchemaProjection> schemaProjection = projectionGeneratorRules.get(i).generateProjection(schema, inputQuery);
			if (schemaProjection.isPresent()) {
				projectionList.add(schemaProjection.get());
			}
		}
		return projectionList;
	}


}
