package org.andino.autumn.spec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import io.qameta.allure.Allure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.andino.autumn.serializers.PlaceHolderResolver;
import org.springframework.http.HttpMethod;

import java.net.URI;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Act {
	private String url;
	private HttpMethod method;
	private JsonNode body;

	public URI getUri() {
		return URI.create(url);
	}

	public JsonNode getBody() {
		return body == null ? JsonNodeFactory.instance.nullNode() : body;
	}

	public void setBody(JsonNode body) {
		if (body == null) {
			this.body = JsonNodeFactory.instance.nullNode();
		} else {
			this.body = PlaceHolderResolver.resolve(body);
		}
	}

	public void annotate() {
		Allure.step("Request Details", () -> {
			Allure.parameter("URL", getUrl());
			Allure.parameter("Method", getMethod().toString());
			if (getBody() != null) {
				Allure.attachment("Request Body", getBody().toPrettyString());
			}
		});
	}
}
