package eu.asyroka.msc.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asyroka on 4/23/2017.
 */
@Data
public class Query implements Serializable {

	private List<String> selectColumns = new ArrayList<>();
	private String fromTableName;
	private List<JoinClause> joinClauses = new ArrayList<>();
	private List<WhereClause> whereClauses = new ArrayList<>();
	private List<OrderClause> orderClauses = new ArrayList<>();
	private Integer limit;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT ");
		builder.append(selectColumns.get(0));
		for (int i = 1; i < selectColumns.size(); i++) {
			builder.append(", ").append(selectColumns.get(i));
		}
		builder.append(" FROM ");
		builder.append(fromTableName);

		if (whereClauses.size() > 0) {
			WhereClause firstClause = whereClauses.get(0);
			builder.append(" WHERE ").append(firstClause.toString());
			for (int i = 1; i < whereClauses.size(); i++) {
				builder.append(" AND ").append(whereClauses.get(i).toString());
			}
		}

		if (orderClauses.size() > 0) {
			OrderClause firstClause = orderClauses.get(0);
			builder.append(" ORDER BY ").append(firstClause.toString());
			for (int i = 1; i < orderClauses.size(); i++) {
				builder.append(" , ").append(orderClauses.get(i).toString());
			}
		}
		if (limit != null) {
			builder.append(" limit ").append(limit);
		}

		return builder.toString();
	}
}
