package eu.asyroka.msc.model;

import lombok.Data;

import java.awt.peer.ListPeer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zason on 4/24/2017.
 */
@Data
public class SchemaProjectionRanking implements Serializable {

    List<Schema> projectionRanking = new ArrayList<>();

}
