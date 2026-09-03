package com.bookstore;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link BookstoreApplication}.
 */
@Generated
public class BookstoreApplication__BeanDefinitions {
  /**
   * Get the bean definition for 'bookstoreApplication'.
   */
  public static BeanDefinition getBookstoreApplicationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(BookstoreApplication.class);
    beanDefinition.setInstanceSupplier(BookstoreApplication::new);
    return beanDefinition;
  }
}
