package com.github.mehmetsahinnn.onlineordertrackingsystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public abstract class BaseController {

    protected <T> ResponseEntity<T> handleRequest(RequestHandler<T> handler, String... logMessages) {
        try {
            T result = handler.handle();
            logInfo(logMessages);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Error occurred while handling request", e);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error occurred while handling request", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    protected void logInfo(String... messages) throws Exception {
        Map<String, String> logMap = new HashMap<>();
        for (String message : messages) {
            logMap.put("message", message);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonLogMessage = objectMapper.writeValueAsString(logMap);
        log.info(jsonLogMessage);
    }

    @FunctionalInterface
    protected interface RequestHandler<T> {
        T handle() throws Exception;
    }
}