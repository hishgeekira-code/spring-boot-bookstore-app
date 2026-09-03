package com.bookstore.controller;

import com.bookstore.service.AuthorService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link AuthorController}.
 */
@Generated
public class AuthorController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'authorController'.
   */
  private static BeanInstanceSupplier<AuthorController> getAuthorControllerInstanceSupplier() {
    return BeanInstanceSupplier.<AuthorController>forConstructor(AuthorService.class)
            .withGenerator((registeredBean, args) -> new AuthorController(args.get(0)));
  }

  /**
   * Get the bean definition for 'authorController'.
   */
  public static BeanDefinition getAuthorControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(AuthorController.class);
    beanDefinition.setInstanceSupplier(getAuthorControllerInstanceSupplier());
    return beanDefinition;
  }
}
