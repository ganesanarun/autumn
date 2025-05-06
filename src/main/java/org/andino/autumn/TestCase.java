package org.andino.autumn;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import io.qameta.allure.Allure;
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

import static io.qameta.allure.util.ResultsUtils.PARENT_SUITE_LABEL_NAME;
import static io.qameta.allure.util.ResultsUtils.SEVERITY_LABEL_NAME;
import static io.qameta.allure.util.ResultsUtils.SUB_SUITE_LABEL_NAME;
import static io.qameta.allure.util.ResultsUtils.TAG_LABEL_NAME;
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
    private Meta meta;

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
    public static class Meta {
        private String epic;
        private String feature;
        private String story;
        private String severity;
        private List<String> tags;
        private String description;
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


    void annotateFor(TestInfo testInfo) {
        removePackage();
        if (getMeta().getEpic() != null) {
            Allure.epic(getMeta().getEpic());
        }
        if (getMeta().getFeature() != null) {
            Allure.feature(getMeta().getFeature());
        }
        if (getMeta().getStory() != null) {
            Allure.story(getMeta().getStory());
        }
        if (getMeta().getTags() != null) {
            getMeta().getTags().forEach(tag -> Allure.label(TAG_LABEL_NAME, tag));
        }
        if (getMeta().getDescription() != null) {
            Allure.description(getMeta().getDescription());
        }
        if (getMeta().getSeverity() != null) {
            Allure.label(SEVERITY_LABEL_NAME, getMeta().getSeverity());
        }
        Allure.step("Request Details", () -> {
            Allure.parameter("URL", getAct().getUrl());
            Allure.parameter("Method", getAct().getMethod().toString());
            if (getAct().getBody() != null) {
                Allure.attachment("Request Body", getAct().getBody().toPrettyString());
            }
        });
        if (isDisabled()) {
            Allure.step("Test is disabled: " + getDisabledReason());
        }
        String folderPath = testInfo.folderPath(); // Get the path relative to the base
        String[] folders = folderPath.isEmpty() ? new String[0] : folderPath.split("/");

        if (folders.length > 0) {
            Allure.label(PARENT_SUITE_LABEL_NAME, folders[0]);
        }

        if (folders.length > 1) {
            Allure.suite(folders[1]);
        }

        if (folders.length > 2) {
            Allure.label(SUB_SUITE_LABEL_NAME, folders[2]);
        }
    }

    public Meta getMeta() {
        return meta != null ? meta : new Meta();
    }

    private void removePackage() {
        Allure.getLifecycle().updateTestCase(testResult -> testResult.getLabels().removeIf(label -> label.getName().equals("suite")));
    }
}
