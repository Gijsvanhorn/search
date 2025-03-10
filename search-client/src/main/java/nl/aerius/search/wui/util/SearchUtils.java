/*
 * Copyright the State of the Netherlands
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package nl.aerius.search.wui.util;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.aerius.search.domain.SearchCapability;

public final class SearchUtils {
  private SearchUtils() {}

  /**
   * Return the given capabilities as a Set.
   * 
   * Set.of() is not emulated in GWT. 
   */
  public static Set<SearchCapability> of(final SearchCapability... capabilities) {
    return Stream.of(capabilities)
        .collect(Collectors.toSet());
  }
}
