package com.bookstore.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link AdminController}.
 */
@Generated
public class AdminController__BeanDefinitions {
  /**
   * Get the bean definition for 'adminController'.
   */
  public static BeanDefinition getAdminControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(AdminController.class);
    beanDefinition.setInstanceSupplier(AdminController::new);
    return beanDefinition;
  }
}
