package eu.asyroka.msc.generator;

import eu.asyroka.msc.generator.rule.ClusteringOrderRule;
import eu.asyroka.msc.generator.rule.MultiColumnPartitioningKeyRule;
import eu.asyroka.msc.generator.rule.PartitioningAndClusteringKeyRule;
import eu.asyroka.msc.generator.rule.SinglePartitioningKeyRule;
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
                new SinglePartitioningKeyRule(),
                new PartitioningAndClusteringKeyRule(),
                new MultiColumnPartitioningKeyRule(),
                new ClusteringOrderRule()
        );
    }

}

