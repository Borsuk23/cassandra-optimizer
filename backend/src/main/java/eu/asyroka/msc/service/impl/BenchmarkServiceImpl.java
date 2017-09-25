package eu.asyroka.msc.service.impl;

import eu.asyroka.msc.model.BenchmarkResult;
import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.SchemaProjection;
import eu.asyroka.msc.service.BenchmarkService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BenchmarkServiceImpl implements BenchmarkService {
    @Override
    public List<BenchmarkResult> benchmarkSchemas(List<SchemaProjection> generatedSchemas, List<Query> inputQueries) {
        return Collections.emptyList();
    }
}
