package auth;

import mypackage.CookieService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class AuthenticationFilter implements Filter {

    private final static Logger LOG = Logger.getLogger(AuthenticationFilter.class.getName());

    private final CookieService _cookieService;

    public AuthenticationFilter() {
        _cookieService = CookieService.INSTANCE;
    }

    public AuthenticationFilter(
            CookieService cookieService
    ) {
        _cookieService = cookieService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        // TODO Make cookie service throw an exception with message if
        // request is not authenticated. And make the method void.
        if (_cookieService.isRequestUnauthenticated(req)) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "The request is not authenticated");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
