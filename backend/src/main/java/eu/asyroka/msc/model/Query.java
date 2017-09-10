package eu.asyroka.msc.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asyroka on 4/23/2017.
 */
@Data
public class Query {

	private List<String> selectColumns = new ArrayList<>();
	private String fromTableName;
	private List<JoinClause> joinClauses = new ArrayList<>();
	private List<WhereClause> whereClauses = new ArrayList<>();
	private List<OrderClause> orderClauses = new ArrayList<>();
	private Integer limit;
}
