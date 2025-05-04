package org.andino.autumn;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.net.URI;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TestCase {
    private Act act;
    private Assert asserts;

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
    }
}
