package com.bookstore.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link LoginController}.
 */
@Generated
public class LoginController__BeanDefinitions {
  /**
   * Get the bean definition for 'loginController'.
   */
  public static BeanDefinition getLoginControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(LoginController.class);
    beanDefinition.setInstanceSupplier(LoginController::new);
    return beanDefinition;
  }
}
