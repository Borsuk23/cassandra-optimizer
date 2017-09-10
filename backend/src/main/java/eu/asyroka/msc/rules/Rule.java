package eu.asyroka.msc.rules;

import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;

import java.util.List;

public interface Rule {
    boolean check(List<Schema> schemas, List<Query> queries);
}
