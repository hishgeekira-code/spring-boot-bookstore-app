package com.bookstore.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link BookPageController}.
 */
@Generated
public class BookPageController__BeanDefinitions {
  /**
   * Get the bean definition for 'bookPageController'.
   */
  public static BeanDefinition getBookPageControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(BookPageController.class);
    beanDefinition.setInstanceSupplier(BookPageController::new);
    return beanDefinition;
  }
}
