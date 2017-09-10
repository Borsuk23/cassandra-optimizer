package eu.asyroka.msc.service.impl;

import eu.asyroka.msc.model.*;
import eu.asyroka.msc.service.DataParserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asyroka on 4/23/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataParserServiceImplTest {


	@Autowired
	private
	DataParserService service;

	@Test
	public void parseSchemaFromFile() throws Exception {
		Schema schema = service.parseSchemaFromFile("schema.txt");

		Schema expectedSchema = new Schema();
		Table table1 = new Table("test");
		table1.getColumns().add(new Column("column1", Type.STRING));
		table1.getColumns().add(new Column("column2", Type.NUMERIC));
		table1.getColumns().add(new Column("column3", Type.DATE));

		expectedSchema.getTables().add(table1);

		Table table2 = new Table("test2");
		table2.getColumns().add(new Column("column11", Type.STRING));
		table2.getColumns().add(new Column("column22", Type.NUMERIC));
		table2.getColumns().add(new Column("column33", Type.DATE));

		expectedSchema.getTables().add(table2);

		Assert.assertEquals(expectedSchema, schema);


	}

	@Test
	public void parseQueriesFromFile() throws Exception {

		List<Query> queries = service.parseQueriesFromFile("queries.txt");

		List<Query> expectedQueries = new ArrayList<>();
		Query query1 = new Query();
		query1.getSelectColumns().add("*");
		query1.setFromTableName("group_join_dates");
		query1.getWhereClauses().add(new WhereClause("groupname", Operator.EQUAL, "?"));
		query1.getOrderClauses().add(new OrderClause("joined", OrderDirection.DESC));
		query1.setLimit(10);
		expectedQueries.add(query1);


		Query query2 = new Query();
		query2.getSelectColumns().add("*");
		query2.setFromTableName("test");
		query2.getWhereClauses().add(new WhereClause("groupname", Operator.NOT_EQUAL, "?"));
		query2.getWhereClauses().add(new WhereClause("test", Operator.LESSER, "2"));
		query2.getWhereClauses().add(new WhereClause("sia", Operator.GREATER_OR_EQUAL, "1"));
		query2.getOrderClauses().add(new OrderClause("joined", OrderDirection.DESC));
		query2.getOrderClauses().add(new OrderClause("test", OrderDirection.ASC));

		expectedQueries.add(query2);

		Assert.assertEquals(expectedQueries, queries);

	}

}