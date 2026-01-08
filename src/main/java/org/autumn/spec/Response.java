package org.autumn.spec;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;

/**
 * Represents an HTTP response received from an API call.
 * <p>
 * This class encapsulates the complete HTTP response including status code, response body
 * as JSON, and headers.
 */
@AllArgsConstructor
@Setter
@Getter
public class Response {

	HttpStatusCode status;

	JsonNode responseBody;

	HttpHeaders headers;

}
