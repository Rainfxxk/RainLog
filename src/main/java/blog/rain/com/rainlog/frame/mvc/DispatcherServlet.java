package blog.rain.com.rainlog.frame.mvc;

import blog.rain.com.rainlog.frame.ioc.BeanFactory;
import blog.rain.com.rainlog.frame.ioc.ClassPathXMLApplicationContext;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@MultipartConfig
@WebServlet(name = "DispatcherServlet", value = "/*")
public class DispatcherServlet extends ViewBaseServlet {

    private BeanFactory beanFactory = null;

    @Override
    public void init() throws ServletException {
        super.init();

        ServletContext servletContext = getServletContext();
        beanFactory = (BeanFactory) servletContext.getAttribute("beanFactory");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp){
        String pathInfo = req.getPathInfo();
        System.out.println(pathInfo);
        String beanId = "user";
        String motion = "index";
        String[] pathInfoSplited = pathInfo.split("/");

        if (pathInfoSplited.length > 2) {
            beanId = pathInfoSplited[1];
            motion = pathInfoSplited[2];
        }
        else if (pathInfoSplited.length > 0) {
            beanId = pathInfoSplited[1];
        }
        System.out.println(beanId + motion);

        Object controllerBean = beanFactory.getBean(beanId);
        try {
            Class<?> beanClass = controllerBean.getClass();
            Method[] methods = beanClass.getMethods();
            Method method = null;

            for (int index = 0; index < methods.length; index++) {
                if (methods[index].getName().equals(motion)) {
                    method = methods[index];
                    break;
                }
            }

            Parameter[] parameters = method.getParameters();
            Object[] parameterValues = new Object[parameters.length];

            for (int index = 0; index < parameters.length; index++) {
                Parameter parameter = parameters[index];
                String parameterName = parameter.getName();

                if (parameterName.equals("request")) {
                    parameterValues[index] = req;
                }
                else  if (parameterName.equals("response")) {
                    parameterValues[index] = resp;
                }
                else if (parameterName.equals("session")) {
                    parameterValues[index] = req.getSession();
                }
                else {
                    if (parameter.getType() == String[].class) {
                        String[] parameterValue = req.getParameterValues(parameterName);
                        System.out.println(parameterName + "[" + parameterValue.length + "]: " + parameterValue);
                        parameterValues[index] = parameterValue;
                    }
                    else if (parameter.getType() == int.class) {
                        int parameterValue = Integer.parseInt(req.getParameter(parameterName));
                        System.out.println(parameterName + ": " + parameterValue);
                        parameterValues[index] = parameterValue;
                    }
                    else {
                        Object parameterValue = req.getParameter(parameterName);
                        System.out.println(parameterName + ": " + parameterValue);
                        parameterValues[index] = parameterValue;
                    }
                }
            }

            method.setAccessible(true);
            String result = (String) method.invoke(controllerBean, parameterValues);

            processControllerResult(result, req, resp);

        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void processControllerResult(String result, HttpServletRequest request, HttpServletResponse response) {
        String resultType = result.substring(0, result.indexOf(":"));
        String resultContent = result.substring(result.indexOf(":") + 1);

        if (resultType.equals("page")) {
            try {
                super.processTemplate(resultContent, request, response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if (resultType.equals("redirect")) {
            try {
                response.sendRedirect(resultContent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if (resultType.equals("json")) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            try {
                PrintWriter writer = response.getWriter();
                writer.println(resultContent);
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
