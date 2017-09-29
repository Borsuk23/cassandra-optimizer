package eu.asyroka.msc.service;

import eu.asyroka.msc.exception.CassandraQueryParseException;
import eu.asyroka.msc.exception.CassandraSchemaParseException;
import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;
import eu.asyroka.msc.model.input.InputQuery;
import eu.asyroka.msc.model.input.InputTable;

import java.io.IOException;
import java.util.List;

/**
 * Created by asyroka on 4/23/2017.
 */
public interface ParseDataService {

	Schema parseSchema(List<InputTable> tables) throws IOException, CassandraSchemaParseException;


	List<Query> parseQueries(List<InputQuery> queries) throws IOException, CassandraSchemaParseException, CassandraQueryParseException;

	Schema parseSchemaFromFile(String path) throws IOException, CassandraSchemaParseException;


	List<Query> parseQueriesFromFile(String path) throws IOException, CassandraSchemaParseException, CassandraQueryParseException;
}
