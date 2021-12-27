package routes.utils;

import java.util.Base64;

public class PaginationMetadata {
  private String nextCursor;
  private boolean hasMoreData;
  /**
   * This is calculated only when cursor is null, so for the first request. The frontend
   * stores the value, and it will be recalculated when cursor will be null again (because it
   * changed the ordering or used a different filter).
   */
  private Long totalCount;

  public String getNextCursor() {
    return nextCursor;
  }

  public PaginationMetadata setNextCursor(String nextCursor) {
    this.nextCursor = Base64.getEncoder().encodeToString(nextCursor.getBytes());
    return this;
  }

  public boolean hasMoreData() {
    return hasMoreData;
  }

  public PaginationMetadata setHasMoreData(boolean hasMoreData) {
    this.hasMoreData = hasMoreData;
    return this;
  }

  public Long getTotalCount() {
    return totalCount;
  }

  public PaginationMetadata setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
    return this;
  }
}