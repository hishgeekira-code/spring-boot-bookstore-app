package com.bookstore.config;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ConfigurationClassUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Bean definitions for {@link PasswordConfig}.
 */
@Generated
public class PasswordConfig__BeanDefinitions {
  /**
   * Get the bean definition for 'passwordConfig'.
   */
  public static BeanDefinition getPasswordConfigBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PasswordConfig.class);
    beanDefinition.setTargetType(PasswordConfig.class);
    ConfigurationClassUtils.initializeConfigurationClass(PasswordConfig.class);
    beanDefinition.setInstanceSupplier(PasswordConfig$$SpringCGLIB$$0::new);
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'passwordEncoder'.
   */
  private static BeanInstanceSupplier<PasswordEncoder> getPasswordEncoderInstanceSupplier() {
    return BeanInstanceSupplier.<PasswordEncoder>forFactoryMethod(PasswordConfig$$SpringCGLIB$$0.class, "passwordEncoder")
            .withGenerator((registeredBean) -> registeredBean.getBeanFactory().getBean("passwordConfig", PasswordConfig.class).passwordEncoder());
  }

  /**
   * Get the bean definition for 'passwordEncoder'.
   */
  public static BeanDefinition getPasswordEncoderBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PasswordEncoder.class);
    beanDefinition.setFactoryBeanName("passwordConfig");
    beanDefinition.setInstanceSupplier(getPasswordEncoderInstanceSupplier());
    return beanDefinition;
  }
}
