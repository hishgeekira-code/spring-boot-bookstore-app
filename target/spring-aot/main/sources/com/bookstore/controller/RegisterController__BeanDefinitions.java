package com.bookstore.controller;

import com.bookstore.service.UserService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link RegisterController}.
 */
@Generated
public class RegisterController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'registerController'.
   */
  private static BeanInstanceSupplier<RegisterController> getRegisterControllerInstanceSupplier() {
    return BeanInstanceSupplier.<RegisterController>forConstructor(UserService.class)
            .withGenerator((registeredBean, args) -> new RegisterController(args.get(0)));
  }

  /**
   * Get the bean definition for 'registerController'.
   */
  public static BeanDefinition getRegisterControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(RegisterController.class);
    beanDefinition.setInstanceSupplier(getRegisterControllerInstanceSupplier());
    return beanDefinition;
  }
}
