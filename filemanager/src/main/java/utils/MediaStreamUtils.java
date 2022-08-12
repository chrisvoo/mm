package utils;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @see <a href="https://github.com/dessalines/torrenttunes-client/blob/master/src/main/java/com/torrenttunes/client/webservice/Platform.java#L555">
 *      Original class
 *      </a> by <a href="https://github.com/dessalines">dessalines</a>
 */
public class MediaStreamUtils {
  public static final Set<String> NON_STREAMING_BROWSERS = new HashSet<>(Arrays.asList("Firefox", "Android"));
  static final Logger log = LoggerFactory.getLogger(MediaStreamUtils.class);
  private final Request req;
  private final Response res;
  private boolean nonStreamingBrowser = false;

  /**
   * You just pass the reference of the request and response and then call {@link #stream(File)}
   * @param req Request
   * @param res Response
   */
  public MediaStreamUtils(Request req, spark.Response res) {
    this.req = req;
    this.res = res;
  }

  /**
   * It sets the headers that don't depend on any object for their values.
   */
  private void setResponseHeaders() {
    res.status(HttpStatus.PARTIAL_CONTENT_206);
    res.type("audio/mpeg");
    res.header("Accept-Ranges",  "bytes");
    res.header("X-Content-Duration", "30");
    res.header("Content-Duration", "30");
    res.header("Connection", "Keep-Alive");
    res.header("Cache-Control", "no-cache, private");
    res.header("X-Pad","avoid browser bug");
    res.header("Expires", "0");
    res.header("Pragma", "no-cache");
    res.header("Content-Transfer-Encoding", "binary");
    res.header("Transfer-Encoding", "chunked");
    res.header("Keep-Alive", "timeout=15, max=100");
    res.header("If-None-Match", "webkit-no-cache");
    res.header("X-Stream", "true");
  }

  /**
   *
   * @param mp3 the file to stream
   * @return HttpServletResponse object
   * @throws IOException A stream exceptions occurs.
   */
  public HttpServletResponse stream(File mp3) throws IOException {
    HttpServletResponse raw = res.raw();

      String origin = req.headers("Origin");
      res.header("Access-Control-Allow-Credentials", "true");
      res.header("Access-Control-Allow-Origin", origin);

      String range = req.headers("Range");
      this.setStreamingBrowser();

      OutputStream os = raw.getOutputStream();
      BufferedOutputStream bos = new BufferedOutputStream(os);

      BasicFileAttributes basicFileAttributes = Files.readAttributes(mp3.toPath(), BasicFileAttributes.class);
      if (range == null || nonStreamingBrowser) {
        res.header("Content-Length", String.valueOf(basicFileAttributes.size()));
        Files.copy(mp3.toPath(), os);

        return res.raw();
      }

      this.setResponseHeaders();
      int[] fromTo = fromTo(mp3, range);
      int length = fromTo[1] - fromTo[0] + 1;


      res.header("Content-Range", "bytes " + fromTo[0] + "-" + fromTo[1] + "/" + fromTo[2]);
      res.header("Content-Length", String.valueOf(length));
      res.header("Date", new Date(basicFileAttributes.lastModifiedTime().toMillis()).toString());
      res.header("Last-Modified", new Date(basicFileAttributes.lastModifiedTime().toMillis()).toString());

      log.debug("writing random access file instead");
      final RandomAccessFile raf = new RandomAccessFile(mp3, "r");
      raf.seek(fromTo[0]);
      writeAudioToOS(length, raf, bos);

      raf.close();

      bos.flush();
      bos.close();

      return res.raw();
  }

  /**
   * It detects if it's a streaming browser or not.
   */
  private void setStreamingBrowser() {
    String userAgent = this.req.headers("User-Agent").toLowerCase();
    for (String browser : NON_STREAMING_BROWSERS) {
      if (userAgent.contains(browser.toLowerCase())) {
        this.nonStreamingBrowser = true;
        log.debug("Its a non-streaming browser.");
        break;
      }
    }
  }

  /**
   * It calculates the range limits depending on the range header.
   * @param mp3 The file to stream
   * @param range The Range header's value.
   * @return The bytes to stream.
   */
  private int[] fromTo(File mp3, String range) {
    int[] ret = new int[3];

    String[] ranges = range.split("=")[1].split("-");
    log.info(range);
    log.debug("ranges[] = " + Arrays.toString(ranges));

    Integer chunkSize = 512;
    Integer from = Integer.parseInt(ranges[0]);
    int to = chunkSize + from;
    if (to >= mp3.length()) {
      to = (int) (mp3.length() - 1);
    }
    if (ranges.length == 2) {
      to = Integer.parseInt(ranges[1]);
    }

    ret[0] = from;
    ret[1] = to;
    ret[2] = (int) mp3.length();

    return ret;
  }

  /**
   * It writes data on the output stream
   * @param length Total bytes to stream
   * @param raf Object use to read the range
   * @param os Output stream
   * @throws IOException if the connection is closed while writing to the stream.
   */
  private void writeAudioToOS(Integer length, RandomAccessFile raf, BufferedOutputStream os) throws IOException {
    byte[] buf = new byte[256];
    while(length != 0) {
      int read = raf.read(buf, 0, buf.length > length ? length : buf.length);
      os.write(buf, 0, read);
      length -= read;
    }
  }
}