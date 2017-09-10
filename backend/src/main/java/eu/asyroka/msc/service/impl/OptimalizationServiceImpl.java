package eu.asyroka.msc.service.impl;

import eu.asyroka.msc.model.BenchmarkResult;
import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;
import eu.asyroka.msc.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OptimalizationServiceImpl implements OptimalizationService {


    @Autowired
    private CassandraInputValidator cassandraInputValidator;

    @Autowired
    private DataParserService dataParserService;

    @Autowired
    private ProjectionGenerator projectionGenerator;

    @Autowired
    private RankingService rankingService;

    @Autowired
    private BenchmarkService benchmarkService;


    @Override
    public List<BenchmarkResult> generateOptimizedSchemas(String schemasPath, String queriesPath) throws Exception {
        List<Query> inputQueries;
        List<Schema> inputSchemas = new ArrayList<>();

        List<Schema> generatedSchemas;

        try {

            if (cassandraInputValidator.validateInput(schemasPath, queriesPath)) {
                inputSchemas.add(dataParserService.parseSchemaFromFile(schemasPath));
                inputQueries = dataParserService.parseQueriesFromFile(queriesPath);

                generatedSchemas = projectionGenerator.generateSchemas(inputSchemas, inputQueries);

                generatedSchemas = rankingService.prioritizeSchemas(generatedSchemas);

                List<BenchmarkResult> benchmarkResults = benchmarkService.benchmarkSchemas(generatedSchemas, inputQueries);

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
