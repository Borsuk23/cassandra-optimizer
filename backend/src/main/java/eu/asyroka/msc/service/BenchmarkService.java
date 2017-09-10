package eu.asyroka.msc.service;

import eu.asyroka.msc.model.BenchmarkResult;
import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;

import java.util.List;

public interface BenchmarkService {
    List<BenchmarkResult> benchmarkSchemas(List<Schema> generatedSchemas, List<Query> inputQueries);
}
