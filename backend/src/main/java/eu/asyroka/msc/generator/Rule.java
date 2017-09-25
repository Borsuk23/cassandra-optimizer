package eu.asyroka.msc.generator;

import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;

import java.util.List;

public interface Rule {
    boolean check(Schema schema, Query query);
}
