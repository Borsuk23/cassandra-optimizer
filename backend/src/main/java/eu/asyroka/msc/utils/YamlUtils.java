package eu.asyroka.msc.utils;

import eu.asyroka.msc.model.Column;
import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.SecondaryIndex;
import eu.asyroka.msc.model.Table;
import eu.asyroka.msc.service.impl.BenchmarkServiceImpl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class YamlUtils {
	private static final String YAML_FOLDER = "benchmark_config/";
	private static final String EXTENSION_YAML = ".yaml";

	public YamlUtils() {
	}

	public static Path prepareYamlFile(Table table, int i, List<Query> queries) throws IOException {
		Path filePath = Paths.get(YAML_FOLDER +"projection_"+i+"_"+table.getName() + "_"+ Calendar.getInstance().getTimeInMillis() + EXTENSION_YAML);

		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));

		writer.append("keyspace: userskeyspace");
		writer.newLine();

		prepareYamlForKeyspace(writer);
		prepareYamlForTableDefinition(table, writer);
		prepareYamlForIndexes(table, writer);

		prepareYamlForColumnSpec(table, writer);
		prepareYamlForInsert(table, writer);
		prepareYamlForQueries(queries, writer);

		writer.flush();
		writer.close();

		return filePath;
	}

	private static void prepareYamlForIndexes(Table table, BufferedWriter writer) throws IOException {
		writer.append("extra_definitions: ");
		writer.newLine();
		for (SecondaryIndex secondaryIndex : table.getIndexes()) {
			writer.append("  - ").append(secondaryIndex.toString());
			writer.newLine();
		}
	}

	private static void prepareYamlForKeyspace(BufferedWriter writer) throws IOException {
		writer.append("keyspace_definition: |");
		writer.newLine();
		writer.append("  CREATE KEYSPACE userskeyspace WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};");
		writer.newLine();
	}

	private static void prepareYamlForTableDefinition(Table table, BufferedWriter writer) throws IOException {
		writer.append("table: ").append(table.getName());
		writer.newLine();
		writer.append("table_definition: |");
		writer.newLine();
		writer.append("  ").append(table.toString());
		writer.newLine();
	}

	private static void prepareYamlForColumnSpec(Table table, BufferedWriter writer) throws IOException {
		writer.append("columnspec:");
		writer.newLine();
		for (Column column : table.getColumns()) {
			writer.append("  - name: ").append(column.getName());
			writer.newLine();
			prepareYamlForColumnSize(column, writer);
			if (table.getPrimaryKey() != null) {
				if (table.getPrimaryKey().getPartitioningKey().contains(column.getName())) {
					writer.append("    population: uniform(1..10M)");
					writer.newLine();
				}
				if (column.getName().equalsIgnoreCase(table.getPrimaryKey().getClusteringKey())) {
					prepareYamlForClusteringKey(table, writer);
				}
			}
		}
	}

	private static void prepareYamlForClusteringKey(Table table, BufferedWriter writer) throws IOException {
		writer.append("    cluster: ");
		switch (table.getDataDistribution()) {
			case GAUSSIAN:
				writer.append("gaussian(1..200)");
				break;
			case UNIFORM:
				writer.append("uniform(1..200)");
				break;
			case FIXED:
				writer.append("fixed(100)");
				break;
			case EXTREME:
				writer.append("extreme(1..200)");
				break;
			case EXP:
				writer.append("exp(1..200)");
				break;
			default:
				writer.append("uniform(1..200)");
		}
		writer.newLine();
	}

	private static void prepareYamlForColumnSize(Column column, BufferedWriter writer) throws IOException {
		switch (column.getType()) {
			case TYPE_BLOB:
				writer.append("    size: gaussian(5..5000)");
				writer.newLine();
				break;
			case TYPE_FROZEN:
			case TYPE_LIST:
			case TYPE_MAP:
			case TYPE_SET:
			case TYPE_TUPLE:
				break;
			case TYPE_ASCII:
			case TYPE_TEXT:
			case TYPE_VARCHAR:
			case TYPE_VARINT:
			case TYPE_DECIMAL:
				writer.append("    size: gaussian(5..50)");
				writer.newLine();
				break;
			case TYPE_BIGINT:
			case TYPE_BOOLEAN:
			case TYPE_COUNTER:
			case TYPE_DATE:
			case TYPE_TIME:
			case TYPE_TIMESTAMP:
			case TYPE_DOUBLE:
			case TYPE_FLOAT:
			case TYPE_INET:
			case TYPE_INT:
			case TYPE_SMALLINT:
			case TYPE_TIMEUUID:
			case TYPE_TINYINT:
			case TYPE_UUID:
			default:
				break;
		}

	}

	private static void prepareYamlForInsert(Table table, BufferedWriter writer) throws IOException {
		writer.append("insert:");
		writer.newLine();
		writer.append("  batchtype: UNLOGGED");
		writer.newLine();
		writer.append("  partitions: fixed(1)");
		writer.newLine();
		writer.append("  select: fixed(1)/1");
		writer.newLine();
	}

	private static void prepareYamlForQueries(List<Query> queries, BufferedWriter writer) throws IOException {
		writer.append("queries:");
		writer.newLine();
		for (Query query : queries) {
			writer.append("  ").append(query.getQueryName()).append(":");
			writer.newLine();
			writer.append("    cql: ").append(query.toString()).append(" ALLOW FILTERING");
			writer.newLine();
			writer.append("    fields: samerow");
			writer.newLine();
		}
	}
}