package eu.asyroka.msc.model;

import lombok.Data;

/**
 * Created by asyroka on 4/23/2017.
 */
@Data
public class JoinClause {
	private String name;
	private String alias;
	private String joinColumn;
	private String originAlias;
	private String originJoinColumn;
}
