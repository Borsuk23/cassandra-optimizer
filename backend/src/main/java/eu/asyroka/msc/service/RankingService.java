package eu.asyroka.msc.service;

import eu.asyroka.msc.model.SchemaProjection;

import java.util.List;

public interface RankingService {
    List<SchemaProjection> prioritizeSchemas(List<SchemaProjection> inputProjections);
}
