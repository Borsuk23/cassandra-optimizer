package eu.asyroka.msc.service;

import eu.asyroka.msc.model.BenchmarkResult;
import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;
import eu.asyroka.msc.model.SchemaProjection;

import java.util.List;

public interface BenchmarkService {
    List<BenchmarkResult> benchmarkSchemas(List<SchemaProjection> generatedSchemas, List<Query> inputQueries);
}
