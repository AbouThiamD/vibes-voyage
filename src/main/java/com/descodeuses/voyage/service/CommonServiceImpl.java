package com.descodeuses.voyage.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;

import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class CommonServiceImpl implements CommonService {

    @Override
    public void removeSessionMessage() {
     HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes()))
     .getRequest();
     HttpSession session = request.getSession();
     session.removeAttribute("succMsg");
     session.removeAttribute("errorMsg");
    }

}
