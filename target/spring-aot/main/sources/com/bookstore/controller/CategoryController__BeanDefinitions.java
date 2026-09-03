package com.bookstore.controller;

import com.bookstore.service.CategoryService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link CategoryController}.
 */
@Generated
public class CategoryController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'categoryController'.
   */
  private static BeanInstanceSupplier<CategoryController> getCategoryControllerInstanceSupplier() {
    return BeanInstanceSupplier.<CategoryController>forConstructor(CategoryService.class)
            .withGenerator((registeredBean, args) -> new CategoryController(args.get(0)));
  }

  /**
   * Get the bean definition for 'categoryController'.
   */
  public static BeanDefinition getCategoryControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CategoryController.class);
    beanDefinition.setInstanceSupplier(getCategoryControllerInstanceSupplier());
    return beanDefinition;
  }
}
