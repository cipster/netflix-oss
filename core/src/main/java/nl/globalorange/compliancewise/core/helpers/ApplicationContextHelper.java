package nl.globalorange.compliancewise.core.helpers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Optional;

public class ApplicationContextHelper implements ApplicationContextAware {

    public static final String BEAN_NAME = "applicationContextHelper";
    private static final ApplicationContextHelper INSTANCE = new ApplicationContextHelper();
    private static ApplicationContext applicationContext;

    private ApplicationContextHelper() {
    }

    /**
     * Returns an {@link Optional} containing the bean with the given name, if present in the {@link ApplicationContext}
     */
    public static Object getBean(String beanName) {
        if (applicationContext != null) {
            return applicationContext.getBean(beanName);
        }
        throw new IllegalArgumentException(String.format("Cannot get bean %s from context", beanName));
    }

    /**
     * Returns an {@link Optional} containing the bean with the given name, if present in the {@link ApplicationContext}
     */
    public static <T> Optional<T> getBean(Class<T> className) {
        if (applicationContext != null) {
            return Optional.of(applicationContext.getBean(className));
        }
        return Optional.empty();
    }

    /**
     * @return the singleton instance.
     */
    public static ApplicationContextHelper getInstance() {
        return INSTANCE;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        ApplicationContextHelper.applicationContext = applicationContext;
    }

}
