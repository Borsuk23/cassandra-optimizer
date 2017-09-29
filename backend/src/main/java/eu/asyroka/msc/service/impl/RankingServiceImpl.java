package eu.asyroka.msc.service.impl;

import eu.asyroka.msc.model.SchemaProjection;
import eu.asyroka.msc.service.RankingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingServiceImpl implements RankingService {
    @Override
    public List<SchemaProjection> prioritizeProjections(List<SchemaProjection> inputProjections) {
        return inputProjections;
    }
}
