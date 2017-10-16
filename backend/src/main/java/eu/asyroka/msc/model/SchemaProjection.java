package eu.asyroka.msc.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zason on 4/24/2017.
 */
@Data
public class SchemaProjection implements Serializable {

    private Schema schema;
    private List<Query> queries = new ArrayList<>();


    public SchemaProjection() {
    }

    public SchemaProjection(SchemaProjection objectToClone) {
        this.schema = new Schema(objectToClone.getSchema());
        this.queries.addAll(objectToClone.getQueries());
    }
}
