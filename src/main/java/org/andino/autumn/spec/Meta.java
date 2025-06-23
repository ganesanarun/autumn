package org.andino.autumn.spec;

import io.qameta.allure.Allure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static io.qameta.allure.util.ResultsUtils.SEVERITY_LABEL_NAME;
import static io.qameta.allure.util.ResultsUtils.TAG_LABEL_NAME;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Meta {
    private String epic;
    private String feature;
    private String story;
    private String severity;
    private List<String> tags;
    private String description;

    public void annotate() {
        annotateFeature();
    }

    private void annotateFeature() {
        if (getEpic() != null) {
            Allure.epic(getEpic());
        }
        if (getFeature() != null) {
            Allure.feature(getFeature());
        }
        if (getStory() != null) {
            Allure.story(getStory());
        }
        if (getTags() != null) {
            getTags().forEach(tag -> Allure.label(TAG_LABEL_NAME, tag));
        }
        if (getDescription() != null) {
            Allure.description(getDescription());
        }
        if (getSeverity() != null) {
            Allure.label(SEVERITY_LABEL_NAME, getSeverity());
        }
    }
}
