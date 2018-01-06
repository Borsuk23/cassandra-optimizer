package eu.asyroka.msc.service.impl;

import eu.asyroka.msc.model.Query;
import eu.asyroka.msc.model.SchemaProjection;
import eu.asyroka.msc.service.RankingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RankingServiceImpl implements RankingService {
	@Override
	public List<SchemaProjection> prioritizeProjections(List<SchemaProjection> inputProjections) {
		return inputProjections.stream().sorted((one, another) -> {
			Integer oneResult = 0;
			for (Query query : one.getQueries()) {
				oneResult += query.getFrequency();
			}
			one.setImportance(oneResult);
			Integer anotherResult = 0;
			for (Query query : another.getQueries()) {
				anotherResult += query.getFrequency();
			}
			another.setImportance(anotherResult);
			return anotherResult.compareTo(oneResult);
		}).collect(Collectors.toList());

	}


}
