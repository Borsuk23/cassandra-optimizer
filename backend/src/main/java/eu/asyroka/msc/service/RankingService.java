package eu.asyroka.msc.service;

import eu.asyroka.msc.model.Schema;

import java.util.List;

public interface RankingService {
    List<Schema> prioritizeSchemas(List<Schema> inputSchemas);
}
