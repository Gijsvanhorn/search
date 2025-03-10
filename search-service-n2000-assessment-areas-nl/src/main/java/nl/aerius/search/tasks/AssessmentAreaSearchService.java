package nl.aerius.search.tasks;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.reactivex.rxjava3.core.Single;

import nl.aerius.search.domain.SearchCapability;
import nl.aerius.search.domain.SearchRegion;
import nl.aerius.search.domain.SearchSuggestion;
import nl.aerius.search.domain.SearchSuggestionBuilder;
import nl.aerius.search.domain.SearchSuggestionType;
import nl.aerius.search.domain.SearchTaskResult;

@Component
@ImplementsCapability(value = SearchCapability.ASSESSMENT_AREA, region = SearchRegion.NL)
public class AssessmentAreaSearchService implements SearchTaskService {
  private static final Logger LOG = LoggerFactory.getLogger(AssessmentAreaSearchService.class);

  private Map<String, Nature2000Area> areas;

  @Autowired Natura2000WfsInterpreter interpreter;

  @PostConstruct
  public void init() {
    areas = interpreter.retrieveAreas();
  }

  @Override
  public Single<SearchTaskResult> retrieveSearchResults(final String query) {
    final String normalizedQuery = interpreter.normalize(query);

    final String[] normalizedQueryParts = normalizedQuery.split(" ");

    final List<SearchSuggestion> sugs = areas.entrySet().stream()
        .filter(area -> Stream.of(normalizedQueryParts)
            .anyMatch(part -> area.getKey().contains(part)))
        .map(v -> v.getValue())
        .map(v -> areaToSuggestion(normalizedQuery, v))
        .collect(Collectors.toList());

    final SearchTaskResult result = new SearchTaskResult();
    result.setSuggestions(sugs);
    return Single.just(result);
  }

  public SearchSuggestion areaToSuggestion(final String normalizedQuery, final Nature2000Area area) {
    // Start with a score of 20, increment with 10 for each character in the query,
    // then reduce by size of match
    // It's not pretty but it'll do fine in most cases
    final int score = Math.max(20, Math.min(100, normalizedQuery.length() * 10 - Math.min(area.getNormalizedName().length(), 20)));

    return SearchSuggestionBuilder.create(area.getName(), score, SearchSuggestionType.ASSESSMENT_AREA, area.getWktCentroid(), area.getWktGeometry());
  }
}
