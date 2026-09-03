package com.bookstore.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link AuthorPageController}.
 */
@Generated
public class AuthorPageController__BeanDefinitions {
  /**
   * Get the bean definition for 'authorPageController'.
   */
  public static BeanDefinition getAuthorPageControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(AuthorPageController.class);
    beanDefinition.setInstanceSupplier(AuthorPageController::new);
    return beanDefinition;
  }
}
