package org.andino.autumn.spec;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Allure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.javacrumbs.jsonunit.assertj.JsonAssert;
import org.andino.autumn.FileMeta;
import org.assertj.core.api.AbstractIntegerAssert;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.StringUtils;

import static io.qameta.allure.util.ResultsUtils.PARENT_SUITE_LABEL_NAME;
import static io.qameta.allure.util.ResultsUtils.SUB_SUITE_LABEL_NAME;

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
    private TimeWindow schedule;

    public JsonAssert assertThis(JsonNode actual) {
        return asserts.assertBody(actual);
    }

    public AbstractIntegerAssert<?> assertThis(HttpStatusCode actual) {
        return org.assertj.core.api.Assertions.assertThat(actual.value()).isEqualTo(asserts.getStatus());
    }

    public String getDisabledReason() {
        if (isOutsideScheduledTime()) {
            return "Test is outside of the execution window: " + schedule.getActiveAfter() + " - " + schedule.getActiveBefore();
        }

        return StringUtils.hasText(disabledReason) ? disabledReason : "Test is disabled in YAML configuration";
    }

    public void annotateFor(FileMeta testInfo) {
        removePackage();
        annotateSuite(testInfo);
        getMeta().annotate();
        annotateDisabled();
        annotateRequest();
    }

    private void annotateDisabled() {
        if (shouldBeSkipped()) {
            Allure.step("Test is disabled: " + getDisabledReason());
        }
    }

    private void annotateSuite(FileMeta testInfo) {
        String folderPath = testInfo.folderPath();
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

    private void annotateRequest() {
        if (!shouldBeSkipped()) {
            getAct().annotate();
        }
    }

    public Meta getMeta() {
        return meta != null ? meta : new Meta();
    }

    private void removePackage() {
        Allure.getLifecycle().updateTestCase(testResult -> testResult.getLabels().removeIf(label -> label.getName().equals("suite")));
    }

    private boolean isOutsideScheduledTime() {
        return schedule != null && schedule.isOutsideExecutionWindow();
    }

    public boolean shouldBeSkipped() {
        return isDisabled() || isOutsideScheduledTime();
    }

}

