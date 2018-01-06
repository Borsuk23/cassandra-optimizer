package eu.asyroka.msc.service.impl;

import eu.asyroka.msc.model.*;
import eu.asyroka.msc.service.BenchmarkService;
import eu.asyroka.msc.utils.YamlUtils;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BenchmarkServiceImpl implements BenchmarkService {

	private static final String EXTENSION_YAML = ".yaml";
	private static final String RESULTS_FOLDER = "benchmark_results/";
	private static final int LIMIT = 3;

	@Override
	public List<BenchmarkResult> benchmarkSchemas(List<SchemaProjection> generatedSchemas, List<Query> inputQueries) throws IOException, InterruptedException {
		List<BenchmarkResult> results = new ArrayList<>();

		int processed = 0;
		for (SchemaProjection generatedSchema : generatedSchemas) {
			if (processed++ < LIMIT) {
				BenchmarkResult result = new BenchmarkResult();
				result.setSchema(generatedSchema.getSchema());
				results.add(result);
				for (Table table : generatedSchema.getSchema().getTables()) {
					List<Query> filteredQueries = inputQueries.stream().filter(query ->
							query.getFromTableName().equalsIgnoreCase(table.getName())
					).collect(Collectors.toList());
					if (!filteredQueries.isEmpty()) {
						Path yamlPath = YamlUtils.prepareYamlFile(table, filteredQueries);
						Path resultHtml = runTests(yamlPath, result);
						BenchmarkTableResult benchmarkTableResult = new BenchmarkTableResult();

						benchmarkTableResult.setResultFile(resultHtml.toString());
						benchmarkTableResult.setTableName(table.getName());
						benchmarkTableResult.setResultsPerQuery(readResults(resultHtml, table.getName()));
						result.getResults().add(benchmarkTableResult);
					}
				}
			}
		}
		return results;
	}


	private Path runTests(Path yamlPath, BenchmarkResult result) throws IOException, InterruptedException {

		ProcessBuilder processBuilder = new ProcessBuilder();
		List<String> commands = new ArrayList<>();
		commands.add("cassandra-stress");
		commands.add("user");
		commands.add("profile=/home/stresstooltest/cassandra_optimizer/users_test.yaml");
		commands.add("n=100");
		commands.add("ops(insert=1,age=1)");
		commands.add("no-warmup");
		processBuilder.command(commands);

		return Paths.get(UUID.randomUUID().toString() + EXTENSION_YAML);
//		Process process = processBuilder.start();
//
//		InputStream inputStream = process.getInputStream();
//		InputStream errorStream = process.getErrorStream();
//
//		process.waitFor();
//
//		String input = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
//		String error = IOUtils.toString(errorStream, StandardCharsets.UTF_8.name());
//		System.out.println("Results from cassandra stress tool");
//		System.out.println("Results:");
//		System.out.println(input);
//		System.out.println("Errors:");
//		System.out.println(error);
//
//		if (StringUtils.isBlank(input)) {
//			return error;
//		}
//		return input;

	}

	private List<QueryTableResult> readResults(Path path, String tableName) throws IOException {
		StringBuilder data = new StringBuilder();
		Stream<String> lines = Files.lines(Paths.get(RESULTS_FOLDER + "results.html"));
		lines.forEach(line -> data.append(line).append("\n"));
		lines.close();

		String statsString = StringUtils.substringBetween(data.toString(), "/* stats start */", "/* stats end */").replace("\n", "").replace("stats =", "");
		JSONObject jsonObj = new JSONObject(statsString.trim());
		JSONArray stats = jsonObj.getJSONArray("stats");

		List<QueryTableResult> results = new ArrayList<>();
		for (int i = 0; i < stats.length(); i++) {
			JSONObject jsonObject = stats.getJSONObject(i);
			QueryTableResult result = new QueryTableResult();
			JSONObject resultJson = new JSONObject();
			resultJson.put("revision", jsonObject.getString("revision"));
			resultJson.put("row rate", jsonObject.getString("row rate"));
			resultJson.put("latency median", jsonObject.getString("latency median"));
			resultJson.put("latency 95th percentile", jsonObject.getString("latency 95th percentile"));
			resultJson.put("total partitions", jsonObject.getString("total partitions"));
			resultJson.put("op rate", jsonObject.getString("op rate"));
			resultJson.put("latency mean", jsonObject.getString("latency mean"));
			resultJson.put("latency max", jsonObject.getString("latency max"));
			resultJson.put("partition rate", jsonObject.getString("partition rate"));

			result.setQuery(jsonObject.getString("revision"));
			result.setTableName(tableName);
			result.setResult(resultJson.toString());
			results.add(result);
		}
		return results;
	}
}
