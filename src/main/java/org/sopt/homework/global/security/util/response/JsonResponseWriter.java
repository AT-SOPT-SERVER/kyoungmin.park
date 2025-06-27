package org.sopt.homework.global.security.util.response;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JsonResponseWriter {
	private final ObjectMapper objectMapper;

	public void writeJsonResponse(HttpServletResponse response, int statusCode, Object body) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(statusCode);
		objectMapper.writeValue(response.getWriter(), body);
	}
}
