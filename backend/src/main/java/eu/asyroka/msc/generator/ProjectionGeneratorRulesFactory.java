package eu.asyroka.msc.generator;

import eu.asyroka.msc.generator.rule.SinglePartitioningKey;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Getter
public class ProjectionGeneratorRulesFactory {
    private List<SchemaGenerator> schemaGenerators;

    public ProjectionGeneratorRulesFactory() {
        schemaGenerators = Arrays.asList(
                new SinglePartitioningKey()
        );
    }

}

