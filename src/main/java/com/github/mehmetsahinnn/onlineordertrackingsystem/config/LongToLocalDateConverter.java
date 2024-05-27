package com.github.mehmetsahinnn.onlineordertrackingsystem.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class LongToLocalDateConverter implements Converter<Long, LocalDate> {

    @Override
    public LocalDate convert(Long source) {
        return LocalDate.ofInstant(Instant.ofEpochMilli(source), ZoneId.systemDefault());
    }
}