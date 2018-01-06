package eu.asyroka.msc.service.impl;

import eu.asyroka.msc.model.*;
import eu.asyroka.msc.service.BenchmarkService;
import eu.asyroka.msc.utils.YamlUtils;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BenchmarkServiceImpl implements BenchmarkService {

	private static final String RESULTS_FOLDER = "benchmark_results/";
	private static final int LIMIT = 3;
	private static final String EXTENSION_HTML = ".html";

	@Override
	public List<BenchmarkResult> benchmarkSchemas(List<SchemaProjection> generatedSchemas, List<Query> inputQueries, String processId) throws IOException, InterruptedException {
		List<BenchmarkResult> results = new ArrayList<>();

		int projectionsToBenchmarkNo = Math.min(generatedSchemas.size(), LIMIT);

		for (int i = 0; i < projectionsToBenchmarkNo; i++) {
			SchemaProjection generatedSchema = generatedSchemas.get(i);
			BenchmarkResult result = new BenchmarkResult();
			result.setSchema(generatedSchema.getSchema());
			results.add(result);
			for (Table table : generatedSchema.getSchema().getTables()) {
				List<Query> filteredQueries = inputQueries.stream().filter(query ->
						table.getName().equalsIgnoreCase(query.getFromTableName())
				).collect(Collectors.toList());
				if (!filteredQueries.isEmpty()) {
					Path yamlPath = YamlUtils.prepareYamlFile(table,i,  filteredQueries);
					Path resultHtml = runTests(yamlPath, table, filteredQueries, result);
					BenchmarkTableResult benchmarkTableResult = new BenchmarkTableResult();

					benchmarkTableResult.setResultFile(resultHtml.toString());
					benchmarkTableResult.setTableName(table.getName());
					benchmarkTableResult.setResultsPerQuery(readResults(resultHtml, table.getName()));
					result.getResults().add(benchmarkTableResult);
				}
			}
		}
		return results;
	}


	private Path runTests(Path yamlPath, Table table, List<Query> filteredQueries, BenchmarkResult result) throws IOException, InterruptedException {
		return Paths.get(table.getName() + Calendar.getInstance().getTimeInMillis() + EXTENSION_HTML);

//		int numberOfRecords = table.getRecordsNo();
//		if (table.getPrimaryKey() != null && StringUtils.isNotBlank(table.getPrimaryKey().getClusteringKey())) {
//			runInserts(yamlPath, numberOfRecords / 100);
//		} else {
//			runInserts(yamlPath, numberOfRecords);
//		}
//
//		Path resultHtmlPath = Paths.get(table.getName() + Calendar.getInstance().getTimeInMillis() + EXTENSION_HTML);
//
//		for (Query filteredQuery : filteredQueries) {
//			runQueryTest(yamlPath, filteredQuery.getQueryName(), resultHtmlPath);
//
//		}
//
//		return resultHtmlPath;
	}

	private void runQueryTest(Path yamlPath, String queryName, Path resultHtmlPath) throws IOException, InterruptedException {

//		 -graph file=groups_test_3case.html title=user-stress revision=3case_t100_n10mln -node node1casmgr.eastus.cloudapp.azure.com
		ProcessBuilder processBuilder = new ProcessBuilder();
		List<String> commands = new ArrayList<>();
		commands.add("cassandra-stress");
		commands.add("user");
		commands.add("profile=" + yamlPath.toAbsolutePath().toString());
		commands.add("n=5000000");
		commands.add("truncate=ONCE");
		commands.add("ops(" + queryName + "=1)");
		commands.add("no-warmup");
		commands.add("-rate");
		commands.add("threads=100");
		commands.add("-graph");
		commands.add("file=" + resultHtmlPath.toAbsolutePath().toString());
		commands.add("revision=" + queryName);
		commands.add("-node");
		commands.add("node1casmgr.eastus.cloudapp.azure.com");


		processBuilder.command(commands);

		Process process = processBuilder.start();

		InputStream inputStream = process.getInputStream();
		InputStream errorStream = process.getErrorStream();

		process.waitFor();

		String input = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
		String error = IOUtils.toString(errorStream, StandardCharsets.UTF_8.name());

		if (StringUtils.isNotBlank(error)) {
			throw new IOException(error);
		}
	}

	private void runInserts(Path yamlPath, int numberOfRecords) throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder();
		List<String> commands = new ArrayList<>();
		commands.add("cassandra-stress");
		commands.add("user");
		commands.add("profile=" + yamlPath.toAbsolutePath().toString());
		commands.add("n=" + numberOfRecords);
		commands.add("ops(insert=1)");
		commands.add("no-warmup");
		commands.add("-rate");
		commands.add("threads=100");
		commands.add("-node");
		commands.add("node1casmgr.eastus.cloudapp.azure.com");

		processBuilder.command(commands);

		Process process = processBuilder.start();

		InputStream inputStream = process.getInputStream();
		InputStream errorStream = process.getErrorStream();

		process.waitFor();

		String input = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
		String error = IOUtils.toString(errorStream, StandardCharsets.UTF_8.name());

		if (StringUtils.isNotBlank(error)) {
			throw new IOException(error);
		}
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
