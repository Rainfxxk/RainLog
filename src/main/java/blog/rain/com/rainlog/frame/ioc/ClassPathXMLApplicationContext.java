package blog.rain.com.rainlog.frame.ioc;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ClassPathXMLApplicationContext implements BeanFactory {

    private Map<String, Object> beanMap = new HashMap<>();

    public ClassPathXMLApplicationContext(String classPath) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("ApplicationContext.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            NodeList beanNodeList = document.getElementsByTagName("bean");

            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                Element beanElement = (Element) beanNode;
                String beanId = beanElement.getAttribute("id");
                String beanClass = beanElement.getAttribute("class");
                Object beanObject = Class.forName(beanClass).newInstance();
                beanMap.put(beanId, beanObject);
            }

            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                Element beanElement = (Element) beanNode;
                String beanId = beanElement.getAttribute("id");
                Object beanObject = beanMap.get(beanId);
                NodeList childNodes = beanElement.getChildNodes();

                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node node = childNodes.item(j);
                    if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName() == "property") {
                        Element propertyElement = (Element) node;
                        String propertyName = propertyElement.getAttribute("name");
                        String propertyRef = propertyElement.getAttribute("ref");
                        Object propertyObject = beanMap.get(propertyRef);
                        Class<?> beanClass = beanObject.getClass();
                        Field propertyField = beanClass.getDeclaredField(propertyName);
                        propertyField.setAccessible(true);
                        propertyField.set(beanObject, propertyObject);
                        System.out.println(propertyRef);
                    }
                }
            }

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getBean(String beanName) {
        return beanMap.get(beanName);
    }
}
