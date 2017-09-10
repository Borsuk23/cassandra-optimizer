package eu.asyroka.msc.service;

import eu.asyroka.msc.model.BenchmarkResult;
import eu.asyroka.msc.model.Schema;

import java.util.List;

public interface OptimalizationService {

    List<BenchmarkResult> generateOptimizedSchemas(String schemasPath, String queriesPath) throws Exception;

}
