package com.bookstore.service;

import com.bookstore.repository.AuthorRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link AuthorService}.
 */
@Generated
public class AuthorService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'authorService'.
   */
  private static BeanInstanceSupplier<AuthorService> getAuthorServiceInstanceSupplier() {
    return BeanInstanceSupplier.<AuthorService>forConstructor(AuthorRepository.class)
            .withGenerator((registeredBean, args) -> new AuthorService(args.get(0)));
  }

  /**
   * Get the bean definition for 'authorService'.
   */
  public static BeanDefinition getAuthorServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(AuthorService.class);
    beanDefinition.setInstanceSupplier(getAuthorServiceInstanceSupplier());
    return beanDefinition;
  }
}
