package com.flexcil.flexc.core.model;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class ExpenseManager_Factory implements Factory<ExpenseManager> {
  @Override
  public ExpenseManager get() {
    return newInstance();
  }

  public static ExpenseManager_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ExpenseManager newInstance() {
    return new ExpenseManager();
  }

  private static final class InstanceHolder {
    static final ExpenseManager_Factory INSTANCE = new ExpenseManager_Factory();
  }
}
