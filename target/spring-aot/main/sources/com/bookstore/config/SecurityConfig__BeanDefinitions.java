package com.bookstore.config;

import com.bookstore.security.LoginSuccessHandler;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ConfigurationClassUtils;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Bean definitions for {@link SecurityConfig}.
 */
@Generated
public class SecurityConfig__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'securityConfig'.
   */
  private static BeanInstanceSupplier<SecurityConfig> getSecurityConfigInstanceSupplier() {
    return BeanInstanceSupplier.<SecurityConfig>forConstructor(LoginSuccessHandler.class)
            .withGenerator((registeredBean, args) -> new SecurityConfig$$SpringCGLIB$$0(args.get(0)));
  }

  /**
   * Get the bean definition for 'securityConfig'.
   */
  public static BeanDefinition getSecurityConfigBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SecurityConfig.class);
    beanDefinition.setTargetType(SecurityConfig.class);
    ConfigurationClassUtils.initializeConfigurationClass(SecurityConfig.class);
    beanDefinition.setInstanceSupplier(getSecurityConfigInstanceSupplier());
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'securityFilterChain'.
   */
  private static BeanInstanceSupplier<SecurityFilterChain> getSecurityFilterChainInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<SecurityFilterChain>forFactoryMethod(SecurityConfig$$SpringCGLIB$$0.class, "securityFilterChain", HttpSecurity.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("securityConfig", SecurityConfig.class).securityFilterChain(args.get(0)));
  }

  /**
   * Get the bean definition for 'securityFilterChain'.
   */
  public static BeanDefinition getSecurityFilterChainBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SecurityFilterChain.class);
    beanDefinition.setFactoryBeanName("securityConfig");
    beanDefinition.setInstanceSupplier(getSecurityFilterChainInstanceSupplier());
    return beanDefinition;
  }
}
