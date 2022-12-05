package ua.nure.rebrov.wholesale_base;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import ua.nure.rebrov.wholesale_base.security.SessionEventListener;

import javax.servlet.ServletContext;


public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected void registerDispatcherServlet(ServletContext servletContext) {
        servletContext.addListener(new SessionEventListener());
        super.registerDispatcherServlet(servletContext);
    }

    @Override
    protected String[] getServletMappings() {
        return new String[0];
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[0];
    }
}
