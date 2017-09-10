package eu.asyroka.msc.rules;

import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;

import java.util.Collections;
import java.util.List;

public class RuleContainter {

    private Rule rule;
    private SchemaGenerator generator;

    public RuleContainter(Rule rule, SchemaGenerator generator) {
        this.rule = rule;
        this.generator = generator;
    }

    public List<Schema> generateIfApply(List<Schema> schemas, List<Query> queries) {
        if (rule.check(schemas, queries)) {
            return generator.generateSchemas(schemas, queries);
        } else {
            return Collections.emptyList();
        }
    }
}
