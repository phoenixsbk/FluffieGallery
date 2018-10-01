package com.fluffynx.fluffiegallery.config;

import com.fluffynx.fluffiegallery.entity.Painter;
import com.fluffynx.fluffiegallery.entity.PainterToken;
import com.fluffynx.fluffiegallery.repos.PainterRepository;
import com.fluffynx.fluffiegallery.repos.PainterTokenRepository;
import com.fluffynx.fluffiegallery.utils.DateUtil;
import com.fluffynx.fluffiegallery.utils.SHAUtil;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@WebFilter("/*")
public class AuthFilter implements ContainerRequestFilter, Filter {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

  private static final String[] SKIP_PATHS = new String[] {"/css/", "/js/", "/fonts/", "/images/"};

  @Autowired
  private PainterRepository painterRepository;

  @Autowired
  private PainterTokenRepository painterTokenRepository;

  @Autowired
  private SHAUtil shaUtil;

  @Override
  public void filter(ContainerRequestContext ctx) throws IOException {
    String path = ctx.getUriInfo().getPath();
    if (!path.equals("painter/login") && !path.equals("painter/logout")) {
      if (path.equals("painter/create")) {
        String decrypted = shaUtil.decrypt(ctx.getHeaderString("atk"));
        if (decrypted != null && decrypted.startsWith("AUTHORIZED_CREATOR_")) {
          String author = decrypted.substring("AUTHORIZED_CREATOR_".length());
          LOGGER.warn("Author " + author + " access create painter from " + ((HttpServletRequest) ctx.getRequest()).getRemoteAddr());
          return;
        }
      } else {
        Map<String, javax.ws.rs.core.Cookie> ckmap = ctx.getCookies();
        if (ckmap != null && ckmap.size() >= 2) {
          javax.ws.rs.core.Cookie pidck = ckmap.get("pid");
          javax.ws.rs.core.Cookie ptkck = ckmap.get("ptk");
          if (pidck != null && ptkck != null) {
            Painter painter = painterRepository.findByUserId(pidck.getValue());
            if (painter != null) {
              PainterToken token = painterTokenRepository.findByPainter(painter);
              String ckToken = ptkck.getValue();
              if (isTokenValid(token, ckToken)) {
                return;
              }
            }
          }
        }
      }

      ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }
  }

  private boolean isTokenValid(PainterToken token, String cookieToken) {
    if (token != null && token.getToken() != null && token.getToken().equals(cookieToken)
        && !DateUtil.isTokenExpired(token.getGenTime())) {
      token.setGenTime(LocalDateTime.now());
      painterTokenRepository.save(token);
      return true;
    } else {
      return false;
  }
}

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    String uri = ((HttpServletRequest) servletRequest).getRequestURI();
    if (uri != null && (uri.equals("/login.html") || uri.equals("/logout.html") || Arrays.stream(SKIP_PATHS).anyMatch(uri::startsWith))) {
      filterChain.doFilter(servletRequest, servletResponse);
    } else {
      Cookie[] cookies = ((HttpServletRequest) servletRequest).getCookies();
      if (cookies != null && cookies.length > 0) {
        List<Cookie> found = Arrays.stream(cookies)
            .filter(ck -> "pid".equals(ck.getName()) || "ptk".equals(ck.getName()))
            .collect(Collectors.toList());
        if (found.size() == 2) {
          String pid = found.stream().filter(c -> "pid".equals(c.getName()))
              .map(Cookie::getValue).collect(Collectors.toList()).get(0);
          Painter p = painterRepository.findByUserId(pid);
          if (p != null) {
            PainterToken token = painterTokenRepository.findByPainter(p);
            String ckToken = found.stream().filter(c -> "ptk".equals(c.getName())).map(Cookie::getValue).findFirst().orElse(null);
            if (isTokenValid(token, ckToken)) {
              filterChain.doFilter(servletRequest, servletResponse);
              return;
            }
          }
        }
      }
      HttpServletResponse resp = (HttpServletResponse) servletResponse;
      resp.sendRedirect("/login.html");
    }
  }

  @Override
  public void destroy() {
  }
}
