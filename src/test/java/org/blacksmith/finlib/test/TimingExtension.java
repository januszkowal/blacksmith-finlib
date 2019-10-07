package org.blacksmith.finlib.test;

import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimingExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

  private static final Logger LOGGER = LoggerFactory.getLogger(TimingExtension.class);

  private static final String START_TIME = "start time";

  @Override
  public void beforeTestExecution(ExtensionContext context) throws Exception {
    getStore(context).put(START_TIME, System.currentTimeMillis());
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    Method testMethod = context.getRequiredTestMethod();
    Class<?> testClass = context.getRequiredTestClass();
    long startTime = getStore(context).remove(START_TIME, long.class);
    long duration = System.currentTimeMillis() - startTime;
    LOGGER.info("Method [{}.{}] took {} ms.", testClass.getSimpleName(),testMethod.getName(), duration);
  }

  private Store getStore(ExtensionContext context) {
    return context.getStore(Namespace.create(getClass(), context.getRequiredTestMethod()));
  }

}