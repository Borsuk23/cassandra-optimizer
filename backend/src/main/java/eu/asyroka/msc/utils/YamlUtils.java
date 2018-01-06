package eu.asyroka.msc.utils;

import eu.asyroka.msc.model.Column;
import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.Table;
import eu.asyroka.msc.service.impl.BenchmarkServiceImpl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class YamlUtils {
	private static final String YAML_FOLDER = "benchmark_config/";
	private static final String EXTENSION_YAML = ".yaml";

	public YamlUtils() {
	}

	public static Path prepareYamlFile(Table table, List<Query> queries) throws IOException {
		Path filePath = Paths.get(YAML_FOLDER + UUID.randomUUID().toString() + EXTENSION_YAML);

		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));

		writer.append("keyspace: userskeyspace");
		writer.newLine();

		prepareYamlForKeyspace(writer);
		prepareYamlForTableDefinition(table, writer);

		prepareYamlForColumnSpec(table, writer);
		prepareYamlForInsert(table, writer);
		prepareYamlForQueries(queries, writer);

		writer.flush();
		writer.close();

		return filePath;
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
			writer.append("  - name:").append(column.getName());
			writer.newLine();
			writer.append("    size: gaussian(5..50)");
			writer.newLine();
			if (table.getPrimaryKey() != null) {
				if (table.getPrimaryKey().getPartitioningKey().contains(column.getName())) {
					writer.append("    population: uniform(1..10000)");
					writer.newLine();
				}
				if (column.getName().equalsIgnoreCase(table.getPrimaryKey().getClusteringKey())) {
					writer.append("    population: uniform(1..10000)");
					writer.newLine();
				}
			}
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
		for (int i = 0; i < queries.size(); i++) {
			Query query = queries.get(i);
			writer.append("  query_").append(String.valueOf(i));
			writer.newLine();
			writer.append("    cql: ").append(query.toString()).append(" ALLOW FILTERING");
			writer.newLine();
			writer.append("    fields: samerow");
			writer.newLine();
		}
	}
}