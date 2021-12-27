package routes.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class PaginatedResponse<T> {
  private List<T> items;
  private PaginationMetadata metadata;

  public List<T> getItems() {
    return items;
  }

  public PaginatedResponse<T> setItems(List<T> items) {
    this.items = items;
    return this;
  }

  public PaginationMetadata getMetadata() {
    return metadata;
  }

  public PaginatedResponse<T> setMetadata(PaginationMetadata metadata) {
    this.metadata = metadata;
    return this;
  }

  public static <T> PaginatedResponse<T> fromJson(String json, Class<T> classOfT) {
    Type typeOfT = TypeToken.getParameterized(PaginatedResponse.class, classOfT).getType();
    return new Gson().fromJson(json, typeOfT);
  }
}