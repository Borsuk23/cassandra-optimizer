package eu.asyroka.msc.exception;

/**
 * Created by asyroka on 4/23/2017.
 */
public class CassandraSchemaParseException extends RuntimeException {
	public CassandraSchemaParseException() {
	}

	public CassandraSchemaParseException(String message) {
		super(message);
	}
}
