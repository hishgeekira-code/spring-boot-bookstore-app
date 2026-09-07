package com.bookstore.security;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link LoginSuccessHandler}.
 */
@Generated
public class LoginSuccessHandler__BeanDefinitions {
  /**
   * Get the bean definition for 'loginSuccessHandler'.
   */
  public static BeanDefinition getLoginSuccessHandlerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(LoginSuccessHandler.class);
    beanDefinition.setInstanceSupplier(LoginSuccessHandler::new);
    return beanDefinition;
  }
}
