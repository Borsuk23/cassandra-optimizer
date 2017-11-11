package eu.asyroka.msc.service.impl;

import eu.asyroka.msc.model.BenchmarkResult;
import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.SchemaProjection;
import eu.asyroka.msc.model.Table;
import eu.asyroka.msc.service.BenchmarkService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BenchmarkServiceImpl implements BenchmarkService {

	private static final String EXTENSION_YAML = ".yaml";
	@Override
	public List<BenchmarkResult> benchmarkSchemas(List<SchemaProjection> generatedSchemas, List<Query> inputQueries) throws IOException, InterruptedException {

//		for (SchemaProjection generatedSchema : generatedSchemas) {
//			for (Table table : generatedSchema.getSchema().getTables()) {
//				List<Query> filteredQueries = inputQueries.stream().filter(query ->
//						query.getFromTableName().equalsIgnoreCase(table.getName())
//				).collect(Collectors.toList());
//				Path yamlPath = prepareYamlFile(table, filteredQueries);
//				Path resultsPath = runTests(yamlPath);
//				readResults(resultsPath);
//			}
//
//		}
//		Path yamlPath = prepareYamlFile();
		List<BenchmarkResult> results = new ArrayList<>();
		BenchmarkResult benchmarkResult = new BenchmarkResult();
		benchmarkResult.setSchema(generatedSchemas.get(0).getSchema());
		benchmarkResult.setResult(runTests());
		results.add(benchmarkResult);
//		readResults(resultsPath);

		return results;
	}


	private Path prepareYamlFile() throws IOException {
		Path filePath = Paths.get(UUID.randomUUID().toString() + EXTENSION_YAML);

		Map<String, Object> data = new HashMap<>();
		data.put("name", "Silenthand Olleander");
		data.put("race", "Human");
		data.put("traits", new String[]{"ONE_HAND", "ONE_EYE"});
		Yaml yaml = new Yaml();
		yaml.dump(data, new FileWriter(filePath.toFile()));

		return filePath;

	}

	private String runTests() throws IOException, InterruptedException {

		ProcessBuilder processBuilder = new ProcessBuilder();
		List<String> commands = new ArrayList<>();
		commands.add("cassandra-stress");
		commands.add("user");
		commands.add("profile=/home/stresstooltest/cassandra_optimizer/users_test.yaml");
		commands.add("n=100");
		commands.add("ops(insert=1,age=1)");
		commands.add("no-warmup");
		processBuilder.command(commands);

		Process process = processBuilder.start();

		InputStream inputStream = process.getInputStream();
		InputStream errorStream = process.getErrorStream();

		process.waitFor();

		String input = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
		String error = IOUtils.toString(errorStream, StandardCharsets.UTF_8.name());
		System.out.println("Results from cassandra stress tool");
		System.out.println("Results:");
		System.out.println(input);
		System.out.println("Errors:");
		System.out.println(error);

		if (StringUtils.isBlank(input)) {
			return error;
		}
		return input;

	}

	private void readResults(Path path) {

	}
}
