package com.example.bootweb.aspect;

import com.example.bootweb.jft.event.RequestEvent;
import org.apache.tika.metadata.HttpHeaders;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
public class EventAspect {

    @Around("@annotation(EventLogging)")
    Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest httpServletRequest = (HttpServletRequest) Arrays.stream(joinPoint.getArgs())
                .filter(o -> o instanceof HttpServletRequest)
                .findAny().orElse(null);
        RequestEvent event = null;
        if (httpServletRequest != null) {
            event = new RequestEvent();
            event.method = httpServletRequest.getMethod();
            event.uri = httpServletRequest.getRequestURI();
            event.size = Optional.ofNullable(httpServletRequest.getHeader(HttpHeaders.CONTENT_LENGTH))
                    .map(Long::parseLong)
                    .orElse(0L);
            event.begin();
        }
        try {
            return joinPoint.proceed();
        } finally {
            if (event != null) {
                event.end();
                event.commit();
            }
        }
    }
}