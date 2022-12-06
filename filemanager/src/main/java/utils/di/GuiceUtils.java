package utils.di;

import com.google.inject.Guice;
import com.google.inject.Injector;
import utils.FileManagerModule;

public class GuiceUtils {

  public static <T> T getInstance(Class<T> theClass){
    Injector injector = Guice.createInjector(new FileManagerModule());
    return injector.getInstance(theClass);
  }
}