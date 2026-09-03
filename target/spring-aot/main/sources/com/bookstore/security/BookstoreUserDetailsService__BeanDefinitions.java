package com.bookstore.security;

import com.bookstore.repository.UserRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link BookstoreUserDetailsService}.
 */
@Generated
public class BookstoreUserDetailsService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'bookstoreUserDetailsService'.
   */
  private static BeanInstanceSupplier<BookstoreUserDetailsService> getBookstoreUserDetailsServiceInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<BookstoreUserDetailsService>forConstructor(UserRepository.class)
            .withGenerator((registeredBean, args) -> new BookstoreUserDetailsService(args.get(0)));
  }

  /**
   * Get the bean definition for 'bookstoreUserDetailsService'.
   */
  public static BeanDefinition getBookstoreUserDetailsServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(BookstoreUserDetailsService.class);
    beanDefinition.setInstanceSupplier(getBookstoreUserDetailsServiceInstanceSupplier());
    return beanDefinition;
  }
}
