package eu.asyroka.msc.generator;

import eu.asyroka.msc.generator.rule.MultiColumnPartitioningKey;
import eu.asyroka.msc.generator.rule.PartitioningAndClusteringKey;
import eu.asyroka.msc.generator.rule.SinglePartitioningKey;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Getter
public class ProjectionGeneratorRulesFactory {
    private List<ProjectionGeneratorRule> projectionGeneratorRules;

    public ProjectionGeneratorRulesFactory() {
        projectionGeneratorRules = Arrays.asList(
                new SinglePartitioningKey(),
                new PartitioningAndClusteringKey(),
                new MultiColumnPartitioningKey()
        );
    }

}

