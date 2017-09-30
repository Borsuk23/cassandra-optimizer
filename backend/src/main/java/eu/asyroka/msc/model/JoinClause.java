package eu.asyroka.msc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by asyroka on 4/23/2017.
 */
@Data
public class JoinClause implements Serializable {
	private String name;
	private String alias;
	private String joinColumn;
	private String originAlias;
	private String originJoinColumn;
}
