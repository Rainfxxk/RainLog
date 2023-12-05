package blog.rain.com.rainlog.frame.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;
import java.io.IOException;

public class ViewBaseServlet extends HttpServlet {

    TemplateEngine templateEngine;
    JakartaServletWebApplication jakartaServletWebApplication;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationTemplateResolver resolver = new WebApplicationTemplateResolver(JakartaServletWebApplication.buildApplication(getServletContext()));
        resolver.setTemplateMode(TemplateMode.HTML);
        System.out.println();
        resolver.setPrefix(getServletContext().getInitParameter("view-prefix"));
        resolver.setSuffix(getServletContext().getInitParameter("view-suffix"));
        resolver.setCacheTTLMs(60000L);
        resolver.setCacheable(true);
        resolver.setCharacterEncoding("UTF-8");
        templateEngine=new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
    }

    public void processTemplate(String templateName, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        jakartaServletWebApplication = JakartaServletWebApplication.buildApplication(getServletContext());
        WebContext ctx = new WebContext(jakartaServletWebApplication.buildExchange(req, resp), req.getLocale(), jakartaServletWebApplication.getAttributeMap());
        templateEngine.process(templateName, ctx, resp.getWriter());
    }
}
