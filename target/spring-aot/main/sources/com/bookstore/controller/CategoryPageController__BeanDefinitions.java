package com.bookstore.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link CategoryPageController}.
 */
@Generated
public class CategoryPageController__BeanDefinitions {
  /**
   * Get the bean definition for 'categoryPageController'.
   */
  public static BeanDefinition getCategoryPageControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CategoryPageController.class);
    beanDefinition.setInstanceSupplier(CategoryPageController::new);
    return beanDefinition;
  }
}
