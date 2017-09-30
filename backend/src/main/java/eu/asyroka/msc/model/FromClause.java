package eu.asyroka.msc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by asyroka on 4/23/2017.
 */
@Data
public class FromClause  implements Serializable {
	private String name;
	private String alias;
}
