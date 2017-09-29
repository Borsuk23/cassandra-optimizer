package eu.asyroka.msc.service.impl;

import eu.asyroka.msc.generator.ProjectionGeneratorRulesFactory;
import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;
import eu.asyroka.msc.model.SchemaProjection;
import eu.asyroka.msc.service.ProjectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectionServiceImpl implements ProjectionService {

    @Autowired
    private ProjectionGeneratorRulesFactory projectionGeneratorRulesFactory;

    @Override
    public List<SchemaProjection> generateSchemas(Schema inputSchema, List<Query> inputQueries) {
        return inputQueries.parallelStream()
                .map(query -> generateSchemas(inputSchema, query))
                .flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public List<SchemaProjection> mergeProjections(List<SchemaProjection> schemaProjections) {
        return schemaProjections;
    }

    private List<SchemaProjection> generateSchemas(Schema inputSchema, Query inputQuery) {
        return projectionGeneratorRulesFactory.getSchemaGenerators().stream()
                .map(rule -> rule.generateSchema(inputSchema, inputQuery))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }


}
