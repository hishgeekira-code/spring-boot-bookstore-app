package com.bookstore.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link CustomerController}.
 */
@Generated
public class CustomerController__BeanDefinitions {
  /**
   * Get the bean definition for 'customerController'.
   */
  public static BeanDefinition getCustomerControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CustomerController.class);
    beanDefinition.setInstanceSupplier(CustomerController::new);
    return beanDefinition;
  }
}
