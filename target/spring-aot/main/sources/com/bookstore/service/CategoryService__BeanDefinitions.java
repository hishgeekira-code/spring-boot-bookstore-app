package com.bookstore.service;

import com.bookstore.repository.CategoryRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link CategoryService}.
 */
@Generated
public class CategoryService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'categoryService'.
   */
  private static BeanInstanceSupplier<CategoryService> getCategoryServiceInstanceSupplier() {
    return BeanInstanceSupplier.<CategoryService>forConstructor(CategoryRepository.class)
            .withGenerator((registeredBean, args) -> new CategoryService(args.get(0)));
  }

  /**
   * Get the bean definition for 'categoryService'.
   */
  public static BeanDefinition getCategoryServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CategoryService.class);
    beanDefinition.setInstanceSupplier(getCategoryServiceInstanceSupplier());
    return beanDefinition;
  }
}
