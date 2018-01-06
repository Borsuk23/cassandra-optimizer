package eu.asyroka.msc.generator.rule;

import eu.asyroka.msc.generator.ProjectionGeneratorRule;
import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;
import eu.asyroka.msc.model.SchemaProjection;

import java.util.Optional;

public class ClusteringOrder implements ProjectionGeneratorRule {
	@Override
	public Optional<SchemaProjection> generateProjection(Schema schema, Query query) {
		return Optional.empty();
	}
}
