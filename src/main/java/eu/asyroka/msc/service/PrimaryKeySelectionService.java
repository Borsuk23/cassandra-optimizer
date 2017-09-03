package eu.asyroka.msc.service;

import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;
import eu.asyroka.msc.model.SchemaProjectionRanking;

import java.util.List;

/**
 * Created by asyroka on 4/24/2017.
 */
public interface PrimaryKeySelectionService {

	SchemaProjectionRanking calculatePrimaryKeys(Schema schema, List<Query> queries);
}
