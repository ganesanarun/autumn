package org.andino.autumn;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.javacrumbs.jsonunit.assertj.JsonAssert;
import net.javacrumbs.jsonunit.core.Option;
import org.andino.autumn.matchers.AnyUUIDMatcher;
import org.andino.autumn.matchers.AnyZonedDateTimeMatcher;
import org.assertj.core.api.AbstractIntegerAssert;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TestCase {
    private Act act;
    private Assert asserts;
    private boolean disabled;
    private String disabledReason;

    @AllArgsConstructor
    @Setter
    @Getter
    @NoArgsConstructor
    @ToString
    public static class Act {
        private String url;
        private HttpMethod method;
        private JsonNode body;

        public URI getUri() {
            return URI.create(url);
        }

        public JsonNode getBody() {
            return body == null ? JsonNodeFactory.instance.nullNode() : body;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Assert {
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

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Customization {
        private Body body;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Body {
        private List<Option> options;
    }

    public JsonAssert assertThis(JsonNode actual) {
        return asserts.assertBody(actual);
    }

    public AbstractIntegerAssert<?> assertThis(HttpStatusCode actual) {
        return org.assertj.core.api.Assertions.assertThat(actual.value()).isEqualTo(asserts.getStatus());
    }

    public String getDisabledReason() {
        return StringUtils.hasText(disabledReason) ? disabledReason : "Test is disabled in YAML configuration";
    }
}
