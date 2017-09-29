package eu.asyroka.msc.service.impl;

import eu.asyroka.msc.model.BenchmarkResult;
import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;
import eu.asyroka.msc.model.SchemaProjection;
import eu.asyroka.msc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class OptimalizationServiceImpl implements OptimalizationService {


    @Autowired
    private CassandraInputValidator cassandraInputValidator;

    @Autowired
    private ParseDataService parseDataService;

    @Autowired
    private ProjectionService projectionService;

    @Autowired
    private RankingService rankingService;

    @Autowired
    private BenchmarkService benchmarkService;


    @Override
    public List<BenchmarkResult> generateOptimizedSchemas(String schemasPath, String queriesPath) throws Exception {

        try {
            if (cassandraInputValidator.validateInput(schemasPath, queriesPath)) {
                Schema inputSchema = parseDataService.parseSchemaFromFile(schemasPath);
                List<Query> inputQueries = parseDataService.parseQueriesFromFile(queriesPath);

                List<SchemaProjection> schemaProjections = projectionService.generateSchemas(inputSchema, inputQueries);
                List<SchemaProjection> mergedProjections = projectionService.mergeProjections(schemaProjections);

                mergedProjections = rankingService.prioritizeProjections(mergedProjections);

                List<BenchmarkResult> benchmarkResults = benchmarkService.benchmarkSchemas(mergedProjections, inputQueries);

                return benchmarkResults;
            } else {
                throw new Exception("There are errors in input data");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}
