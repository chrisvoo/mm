package routes;

import utils.FileUtils;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.Properties;

import static spark.Spark.get;
import static spark.Spark.path;

public class InfoRoutes extends routes.Route implements Router {
  /**

   */

  private String getProjectVersion() {
    final Properties properties = new Properties();
    try {
      properties.load(this.getClass().getClassLoader().getResourceAsStream("project.properties"));
      return properties.getProperty("version");
    } catch (IOException e) {
      return "N/A";
    }
  }

  /**
   * For memory usage
   * @see <a href="https://docs.oracle.com/javase/9/docs/api/java/lang/management/MemoryUsage.html">MemoryUsage</a>
   * @return The system info
   */
  private spark.Route getInfo() {
    MemoryUsage memoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
    String xmx = FileUtils.formatBytes(memoryUsage.getMax());             // max memory allowed for jvm -Xmx flag (-1 if isn't specified)
    String used = FileUtils.formatBytes(memoryUsage.getUsed());           // used now by your heap
    String committed = FileUtils.formatBytes(memoryUsage.getCommitted()); // given memory to JVM by OS ( may fail to reach getMax, if there isn't more memory)
    String xms = FileUtils.formatBytes(memoryUsage.getInit());            // -Xms flag

    return (req, res) -> String.format("""
        {
          "memory": {
             "max": "%s",
             "used": "%s",
             "committed": "%s",
             "initial": "%s"
          },
          "version": "%s"
        }
      """, xmx, used, committed, xms, this.getProjectVersion());
  }

  /**
   * All the routes definition
   */
  @Override
  public void routes() {
    path("/info", () -> {
      get(
        "",
        "application/json",
        this.getInfo()
      );
    });
  }
}