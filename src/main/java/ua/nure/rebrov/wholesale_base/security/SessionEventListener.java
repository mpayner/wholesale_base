package ua.nure.rebrov.wholesale_base.security;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import ua.nure.rebrov.wholesale_base.model.Good;

import javax.servlet.http.HttpSessionEvent;
import java.util.HashMap;
import java.util.Map;

public class SessionEventListener extends HttpSessionEventPublisher {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        event.getSession().setAttribute("cart", new HashMap<>());
        super.sessionCreated(event);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        // БД движения
        super.sessionDestroyed(event);
    }

}
