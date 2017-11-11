package eu.asyroka.msc.exception;

/**
 * Created by asyroka on 4/23/2017.
 */
public class CassandraQueryParseException extends RuntimeException {
	public CassandraQueryParseException(String message) {
		super(message);
	}

	public CassandraQueryParseException() {
	}
}
