package org.andino.autumn.executors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.andino.autumn.spec.Act;
import org.andino.autumn.spec.Response;
import org.andino.autumn.spec.TestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestClientException;

@AllArgsConstructor
@Slf4j
public class HttpRequestExecutor {

	final RestClient restClient;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public Response execute(TestCase testCase) {
		log.debug("Making a call for {}", testCase.getAct());
		return invoke(testCase.getAct(), testCase.getAct().getHeaders());
	}

	public Response execute(Act act, MultiValueMap<String, String> headers) {
		log.debug("Making a call for {}", act);
		return invoke(act, headers);
	}

	private Response invoke(Act request, MultiValueMap<String, String> headers) {
		return restClient.method(request.getMethod()).uri(request.getUri()).body(request.getBody()).headers(h -> {
			if (headers != null && !headers.isEmpty()) {
				h.addAll(headers);
			}
		}).exchange((req, res) -> {
			log.debug("Received response with code {}", res.getStatusCode());
			return new Response(res.getStatusCode(), objectMapper.readTree(res.getBody()), res.getHeaders());
		});
	}

}
