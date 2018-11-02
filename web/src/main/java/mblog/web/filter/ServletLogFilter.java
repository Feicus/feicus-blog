package mblog.web.filter;

import mblog.web.utils.MDCUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * @author feicus
 * @Description:
 * @date 13:25
 */
public class ServletLogFilter implements Filter{
    public ServletLogFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String traceId = request.getHeader("TRACE_ID");
        if (traceId == null || "".equals(traceId)) {
            traceId = UUID.randomUUID().toString();
        }

        MDCUtil.put(MDCUtil.Type.TRACE_ID, traceId);
        filterChain.doFilter(servletRequest, servletResponse);
        MDCUtil.clear();
    }

    public void destroy() {
    }
}
