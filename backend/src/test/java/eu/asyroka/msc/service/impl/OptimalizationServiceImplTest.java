package eu.asyroka.msc.service.impl;

import eu.asyroka.msc.model.BenchmarkResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class OptimalizationServiceImplTest {

    @Autowired
    private OptimalizationServiceImpl optimalizationService;

    @Test
    public void generateOptimizedSchemas() throws Exception {
        List<BenchmarkResult> benchmarkResults = optimalizationService.generateOptimizedSchemas("schema.txt", "queries.txt");
        System.out.println(benchmarkResults.size());
    }

}