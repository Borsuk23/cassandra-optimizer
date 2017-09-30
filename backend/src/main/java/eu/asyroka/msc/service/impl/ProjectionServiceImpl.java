package eu.asyroka.msc.service.impl;

import eu.asyroka.msc.generator.ProjectionGeneratorRulesFactory;
import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;
import eu.asyroka.msc.model.SchemaProjection;
import eu.asyroka.msc.model.Table;
import eu.asyroka.msc.service.ProjectionService;
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
	public List<SchemaProjection> generateSchemas(Schema inputSchema, List<Query> inputQueries) {
		List<SchemaProjection> schemaProjections = new ArrayList<>();
		for (Query inputQuery : inputQueries) {
			Schema schema = new Schema(inputSchema);
			schemaProjections.addAll(generateSchemas(schema, inputQuery));
		}
		return schemaProjections;
	}

	@Override
	public List<SchemaProjection> mergeProjections(List<SchemaProjection> schemaProjections) {
		List<SchemaProjection> merged = new ArrayList<>();
		merged.addAll(merge(schemaProjections, 0, null));
		return merged;
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
			if (table.getPrimaryKey() == null) {
				for (Table tableToMerge : schemaProjection.getSchema().getTables()) {
					if (tableToMerge.getName().equalsIgnoreCase(table.getName()) && tableToMerge.getPrimaryKey() != null) {
						table = tableToMerge;
						mergedFlag = true;
					}
				}
			}
		}
		if (mergedFlag) {
			return Optional.of(merged);
		} else {
			return Optional.empty();
		}
	}

	private List<SchemaProjection> generateSchemas(Schema inputSchema, Query inputQuery) {
		return projectionGeneratorRulesFactory.getSchemaGenerators().stream()
				.map(rule -> rule.generateSchema(inputSchema, inputQuery))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());
	}


}
