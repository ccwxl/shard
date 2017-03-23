package org.gpstable.config;

import org.gpstable.shared.StrategyManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author worke
 * @email xielong.wang@nvr-china.com
 * @date 1/20/17
 */
@Component
public class ContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ContextHelper.applicationContext == null) {
            ContextHelper.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }


    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }


    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    public static StrategyManager getStrategyManager(){  
        return applicationContext.getBean(StrategyManager.class);  
    } 
}

