package com.myproject.petcare.pet_diary.common.exception.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
public class FilterExceptionFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        this.doFilter((HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (RuntimeException e) {
            sendErrorResponse(response, e);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, RuntimeException e) {

        log.error("[exceptionHandler] ex", e);
        ResponseDto<HttpStatus> httpStatusResponseDto = new ResponseDto<>(false, e.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(httpStatusResponseDto);
            response.getWriter().write(json);
        }catch (IOException error){
            log.error(error.getMessage());
        }

    }
}
