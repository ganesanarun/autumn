package org.andino.autumn.executors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.andino.autumn.spec.Act;
import org.andino.autumn.spec.Response;
import org.andino.autumn.spec.TestCase;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;


@AllArgsConstructor
@Slf4j
public class HttpRequestExecutor {
    final RestClient restClient;
    final ObjectMapper objectMapper;

    public Response execute(TestCase testCase) {
        log.debug("Making a call for {}", testCase.getAct());
        final var httpHeaders = new HttpHeaders();
        return invoke(testCase.getAct(), httpHeaders);
    }

    private Response invoke(Act request, MultiValueMap<String, String> headers) {
        final var response = restClient.method(request.getMethod()).uri(request.getUri()).body(request.getBody()).retrieve().toEntity(JsonNode.class);
        return new Response(response.getStatusCode(), response.getBody(), response.getHeaders());
    }
}
