package blog.rain.com.rainlog.frame.filter;

import blog.rain.com.rainlog.frame.transaction.TransactionManager;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;
import java.sql.SQLException;

@WebFilter("/*")
public class OpenSessionViewFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            TransactionManager.beginTransaction();
            filterChain.doFilter(servletRequest, servletResponse);
            TransactionManager.commit();
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                TransactionManager.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
