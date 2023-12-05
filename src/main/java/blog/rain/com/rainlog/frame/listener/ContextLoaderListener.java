package blog.rain.com.rainlog.frame.listener;

import blog.rain.com.rainlog.frame.ioc.BeanFactory;
import blog.rain.com.rainlog.frame.ioc.ClassPathXMLApplicationContext;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextLoaderListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        String contextConfigLocation = servletContext.getInitParameter("contextConfigLocation");
        contextConfigLocation = "ApplicationContext.xml";
        BeanFactory beanFactory = new ClassPathXMLApplicationContext(contextConfigLocation);
        servletContext.setAttribute("beanFactory", beanFactory);
    }
}
