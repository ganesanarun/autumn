package org.andino.autumn.spec;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.javacrumbs.jsonunit.assertj.JsonAssert;
import net.javacrumbs.jsonunit.core.Option;
import org.andino.autumn.matchers.AnyUUIDMatcher;
import org.andino.autumn.matchers.AnyZonedDateTimeMatcher;
import org.springframework.http.HttpHeaders;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Assert {
    private int status;
    private JsonNode body;
    private HttpHeaders headers;
    private Customization customization;

    public JsonAssert assertBody(JsonNode actual) {
        if (customization == null || customization.getBody() == null || customization.getBody().getOptions().isEmpty()) {
            return assertThatJson(actual).withMatcher("any-zoned-date-time", new AnyZonedDateTimeMatcher()).withMatcher("any-uuid", new AnyUUIDMatcher()).isEqualTo(body);
        }

        return assertThatJson(actual).withMatcher("any-zoned-date-time", new AnyZonedDateTimeMatcher()).withMatcher("any-uuid", new AnyUUIDMatcher()).when(customization.getBody().getOptions().getFirst(), customization.getBody().getOptions().toArray(Option[]::new)).isEqualTo(body);
    }
}
