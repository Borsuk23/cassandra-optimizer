package eu.asyroka.msc.service;

import eu.asyroka.msc.exception.CassandraQueryParseException;
import eu.asyroka.msc.exception.CassandraSchemaParseException;
import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Schema;

import java.io.IOException;
import java.util.List;

/**
 * Created by asyroka on 4/23/2017.
 */
public interface DataParserService {

	Schema parseSchemaFromFile(String path) throws IOException, CassandraSchemaParseException;


	List<Query> parseQueriesFromFile(String path) throws IOException, CassandraSchemaParseException, CassandraQueryParseException;
}
