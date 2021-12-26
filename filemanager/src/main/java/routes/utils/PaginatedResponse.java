package routes.utils;

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
}