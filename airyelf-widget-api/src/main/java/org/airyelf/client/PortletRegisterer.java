package org.airyelf.client;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.ServletContextAware;

/**
 *
 */
public class PortletRegisterer implements InitializingBean, DisposableBean, ServletContextAware {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final int RETRY_INTERVAL_IN_SEC = 60000;

    private String serverUrl = defaultServerUrl();
    private String myHost = defaultHost();

    private ServletContext servletContext;

    private List<PortletDefinition> portletDefinitions;

    private boolean registered = false;

    private String defaultServerUrl() {
        String url = System.getProperty("org.airyelf.serverUrl");
        if (url == null) {
            url = "http://localhost:8080/airyelf";
        }
        return url;
    }

    private String defaultHost() {
        String url = System.getProperty("org.airyelf.myHost");
        if (url == null) {
            url = "http://localhost:8080";
        }
        return url;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final RestTemplate rest = new RestTemplate();
        final Timer timer = new Timer(true);
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            rest.postForObject(actionUrl(), portletDefinitions, String.class, encodeUrl(myUrl()));
                            log.info("Registered widgets at '{}' on server at '{}'", myUrl(), serverUrl);
                            registered = true;
                            timer.cancel();
                        } catch (RestClientException e) {
                            log.warn("Could not register widgets on server at '{}', retry in {}s", serverUrl, RETRY_INTERVAL_IN_SEC, e);
                        }
                    }
                }, 5000, RETRY_INTERVAL_IN_SEC);
    }

    @Override
    public void destroy() {
        final RestTemplate rest = new RestTemplate();
        rest.delete(actionUrl(), encodeUrl(myUrl()));
    }

    private String encodeUrl(String url) {
        return url.replace("/", "%2F");
    }

    private String myUrl() {
        return myHost + servletContext.getContextPath();
    }

    private String actionUrl() {
        return serverUrl + "/action/portlet-definition/{url}";
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public List<PortletDefinition> getPortletDefinitions() {
        return portletDefinitions;
    }

    public void setPortletDefinitions(List<PortletDefinition> portletDefinitions) {
        this.portletDefinitions = portletDefinitions;
    }

    public boolean isRegistered() {
        return registered;
    }
}
