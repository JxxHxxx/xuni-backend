package com.jxx.xuni.auth.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.support.JwtTokenManager;
import com.jxx.xuni.common.exception.NotPermissionException;
import com.jxx.xuni.auth.domain.Authority;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.*;

@Slf4j
@RequiredArgsConstructor
public class ActuatorFilter implements Filter {

    private final JwtTokenManager jwtTokenManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (isActuatorEndpoint(httpRequest)) {
            checkAdminAuthority(httpRequest);
            chain.doFilter(request, response);
            return;
        }

        chain.doFilter(request, response);
    }

    private void checkAdminAuthority(HttpServletRequest request) {
        MemberDetails memberDetails = jwtTokenManager.getMemberDetails(request.getHeader(AUTHORIZATION));

        if (!Authority.ADMIN.equals(memberDetails.getAuthority())) {
            throw new NotPermissionException("접근 권한이 존재하지 않습니다.");
        }

    }

    private static boolean isActuatorEndpoint(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/actuator");
    }
}
