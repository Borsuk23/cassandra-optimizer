package eu.asyroka.msc.model;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asyroka on 4/23/2017.
 */
@Data
public class Schema {
	private List<Table> tables = new ArrayList<>();

}
