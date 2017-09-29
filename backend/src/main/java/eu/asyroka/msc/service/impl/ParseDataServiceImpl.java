package eu.asyroka.msc.service.impl;

import eu.asyroka.msc.exception.CassandraQueryParseException;
import eu.asyroka.msc.exception.CassandraSchemaParseException;
import eu.asyroka.msc.model.*;
import eu.asyroka.msc.model.input.InputQuery;
import eu.asyroka.msc.model.input.InputTable;
import eu.asyroka.msc.service.ParseDataService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by asyroka on 4/23/2017.
 */
@Service
public class ParseDataServiceImpl implements ParseDataService {

	@Override
	public Schema parseSchema(List<InputTable> tables) throws IOException, CassandraSchemaParseException {
		return null;
	}

	@Override
	public List<Query> parseQueries(List<InputQuery> queries) throws IOException, CassandraSchemaParseException, CassandraQueryParseException {
		return null;
	}

	@Override
	public Schema parseSchemaFromFile(String path) throws IOException, CassandraSchemaParseException {
		String schemaString = new String(Files.readAllBytes(Paths.get(path)));

		schemaString = schemaString.replace(",", " , ");
		schemaString = schemaString.replace(")", " ) ");
		schemaString = schemaString.replace("(", " ( ");

		schemaString = schemaString.replaceAll("[^a-zA-z0-9,()\\s]", "");

		String[] words = schemaString.split("\\s+");

		SchemaParseStatus schemaParseStatus = SchemaParseStatus.NONE;
		Schema schema = new Schema();


		Table currentTable = null;
		String columnNameValue = null;

		for (String word : words) {
			switch (schemaParseStatus) {
				case NONE:
					if (word.toUpperCase().equals("CREATE")) {
						schemaParseStatus = SchemaParseStatus.CREATE;
					} else {
						throw new CassandraSchemaParseException();
					}
					break;
				case CREATE:
					if (word.toUpperCase().equals("TABLE")) {
						schemaParseStatus = SchemaParseStatus.TABLE_NAME;
					} else {
						throw new CassandraSchemaParseException();
					}
					break;
				case TABLE_NAME:
					if (word.matches("[A-Za-z]\\w+")) {
						currentTable = new Table(word.toLowerCase());
						schemaParseStatus = SchemaParseStatus.TABLE_BEGIN;
					} else {
						throw new CassandraSchemaParseException();
					}
					break;
				case TABLE_BEGIN:
					if (word.equals("(")) {
						schemaParseStatus = SchemaParseStatus.COLUMN_NAME;
					}
					break;
				case TABLE:
					switch (word) {
						case ",":
							schemaParseStatus = SchemaParseStatus.COLUMN_NAME;
							break;
						case ")":
							schemaParseStatus = SchemaParseStatus.NONE;
							schema.getTables().add(currentTable);
							currentTable = null;
							break;
						default:
							throw new CassandraSchemaParseException();
					}
					break;
				case COLUMN_NAME:
					if (word.matches("[A-Za-z]\\w+")) {
						columnNameValue = word.toLowerCase();
						schemaParseStatus = SchemaParseStatus.COLUMN_TYPE;
					} else {
						throw new CassandraSchemaParseException();
					}
					break;
				case COLUMN_TYPE:
					try {
						currentTable.getColumns().add(new Column(columnNameValue, Type.valueOf(word.toUpperCase())));
						columnNameValue = null;
						schemaParseStatus = SchemaParseStatus.TABLE;
					} catch (IllegalArgumentException ex) {
						throw new CassandraSchemaParseException();
					}
					break;
			}
		}


		return schema;
	}

	@Override
	public List<Query> parseQueriesFromFile(String path) throws IOException, CassandraSchemaParseException, CassandraQueryParseException {

		String stringToParse = new String(Files.readAllBytes(Paths.get(path)));

		stringToParse = stringToParse.replace(",", " , ");
		stringToParse = stringToParse.replace(";", " ; ");
		stringToParse = stringToParse.replace("=", " = ");
		stringToParse = stringToParse.replace("<", " < ");
		stringToParse = stringToParse.replace(">", " > ");
		stringToParse = stringToParse.replace("<>", " <> ");
		stringToParse = stringToParse.replace("<=", " <= ");
		stringToParse = stringToParse.replace(">=", " >= ");
		stringToParse = stringToParse.replaceAll("<\\s+=", "<=");
		stringToParse = stringToParse.replaceAll(">\\s+=", ">=");
		stringToParse = stringToParse.replaceAll("<\\s+>", "<>");


		String[] queriesString = stringToParse.split(";");


		List<Query> queries = new ArrayList<>();

		Arrays.stream(queriesString).forEach(queryString ->
		{
			String[] words = queryString.trim().split("\\s+");
			QueriesParseStatus queryParseStatus = QueriesParseStatus.NONE;
			Query query = new Query();
			queries.add(query);

			String whereColumn = null;
			String orderColumn = null;
			Operator whereOperator = null;

			for (String word : words) {
				switch (queryParseStatus) {
					case NONE:
						if (word.toUpperCase().equals("SELECT")) {
							queryParseStatus = QueriesParseStatus.SELECT_COLUMNS;
						}
						break;
					case SELECT_COLUMNS:
						if (word.matches("[A-Za-z]\\w+|\\*")) {
							query.getSelectColumns().add(word);
							queryParseStatus = QueriesParseStatus.COLUMN_DELIMITER_OR_FROM;
						}
						break;
					case COLUMN_DELIMITER_OR_FROM:
						if (word.equals(",")) {
							queryParseStatus = QueriesParseStatus.SELECT_COLUMNS;
						} else if (word.toUpperCase().equals("FROM")) {
							queryParseStatus = QueriesParseStatus.FROM_TABLE;
						}
						break;
					case FROM_TABLE:
						if (word.matches("[A-Za-z]\\w+")) {
							query.setFromTableName(word);
							queryParseStatus = QueriesParseStatus.JOIN_OR_WHERE_CLAUSE;
						}
						break;
					case JOIN_OR_WHERE_CLAUSE:
						if (word.toUpperCase().equals("WHERE")) {
							queryParseStatus = QueriesParseStatus.WHERE_CLAUSE_COLUMN;
						}
						break;
					case WHERE_CLAUSE_COLUMN:
						if (word.matches("[A-Za-z]\\w+")) {
							whereColumn = word;
							queryParseStatus = QueriesParseStatus.WHERE_CLAUSE_OPERATOR;
						}
						break;
					case WHERE_CLAUSE_OPERATOR:
						try {
							whereOperator = Operator.getByString(word);
							queryParseStatus = QueriesParseStatus.WHERE_CLAUSE_VALUE;
							break;
						} catch (IllegalArgumentException ex) {
							throw new CassandraQueryParseException();
						}
					case WHERE_CLAUSE_VALUE:
						if (whereColumn != null && whereOperator != null) {
							query.getWhereClauses().add(new WhereClause(whereColumn, whereOperator, word));
							queryParseStatus = QueriesParseStatus.AND_OR_ORDER_LIMIT;
							whereColumn = null;
							whereOperator = null;
						}
						break;
					case AND_OR_ORDER_LIMIT:
						switch (word.toUpperCase()) {
							case "AND":
							case "OR":
								queryParseStatus = QueriesParseStatus.WHERE_CLAUSE_COLUMN;
								break;
							case "ORDER":
								queryParseStatus = QueriesParseStatus.ORDER_BY;
								break;
							case "LIMIT":
								queryParseStatus = QueriesParseStatus.LIMIT;
								break;
						}
						break;
					case ORDER_BY:
						if (word.toUpperCase().equals("BY")) {
							queryParseStatus = QueriesParseStatus.ORDER_BY_COLUMN;
						}
						break;
					case LIMIT:
						if (word.matches("\\d+")) {
							query.setLimit(Integer.parseInt(word));
						} else throw new CassandraQueryParseException();
						break;
					case ORDER_BY_DIRECTION:
						try {
							query.getOrderClauses().add(new OrderClause(orderColumn, OrderDirection.valueOf(word)));
							orderColumn = null;
							queryParseStatus = QueriesParseStatus.AND_LIMIT;
						} catch (IllegalArgumentException ex) {
							throw new CassandraQueryParseException();
						}
						break;
					case AND_LIMIT:
						switch (word.toUpperCase()) {
							case "AND":
								queryParseStatus = QueriesParseStatus.ORDER_BY_COLUMN;
								break;
							case "LIMIT":
								queryParseStatus = QueriesParseStatus.LIMIT;
								break;
						}
						break;
					case ORDER_BY_COLUMN:
						if (word.matches("[A-Za-z]\\w+")) {
							orderColumn = word;
							queryParseStatus = QueriesParseStatus.ORDER_BY_DIRECTION;
						}
						break;
				}
			}
		});

		return queries;
	}

	private enum SchemaParseStatus {
		NONE,
		CREATE,
		TABLE_NAME,
		TABLE,
		COLUMN_NAME,
		COLUMN_TYPE,
		TABLE_BEGIN
	}

	private enum QueriesParseStatus {
		NONE,
		SELECT_COLUMNS,
		COLUMN_DELIMITER_OR_FROM,
		FROM_TABLE,
		JOIN_OR_WHERE_CLAUSE,
		WHERE_CLAUSE_COLUMN,
		WHERE_CLAUSE_OPERATOR,
		WHERE_CLAUSE_VALUE,
		AND_OR_ORDER_LIMIT,
		ORDER_BY,
		LIMIT,
		ORDER_BY_DIRECTION, AND_LIMIT, ORDER_BY_COLUMN
	}
}
