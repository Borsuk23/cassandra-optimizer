package eu.asyroka.msc.service.impl;

import eu.asyroka.msc.model.BenchmarkResult;
import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.SchemaProjection;
import eu.asyroka.msc.service.BenchmarkService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BenchmarkServiceImpl implements BenchmarkService {
    @Override
    public List<BenchmarkResult> benchmarkSchemas(List<SchemaProjection> generatedSchemas, List<Query> inputQueries) {
        List<BenchmarkResult> results = new ArrayList<>();
        for (SchemaProjection generatedSchema : generatedSchemas) {
            BenchmarkResult benchmarkResult = new BenchmarkResult();
            //TODO: run stress tool and save results here
            benchmarkResult.setSchema(generatedSchema.getSchema());
            results.add(benchmarkResult);
        }
        return results;
    }
}
