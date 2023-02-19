package scanner;

import com.google.inject.Inject;
import models.files.MusicFile;
import models.scanner.ScanOp;
import services.MusicFileService;
import utils.di.GuiceUtils;
import utils.eyeD3.EyeD3;
import utils.logging.LoggerInterface;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ScanTask extends RecursiveTask<ScanOp> {
  @Inject private LoggerInterface logger;
  private List<Path> paths;
  @Inject private MusicFileService musicFileService;

  public ScanTask setPaths(List<Path> paths) {
    this.paths = paths;
    return this;
  }

  private MusicFile parseFailback(Path path) {
    String filePath = path.normalize().toAbsolutePath().toString();
    return new MusicFile()
      .setAbsolutePath(filePath)
      .calculateSize(path);
  }

  private void parsePath(Path path, List<MusicFile> docs, ScanOp result) {
    MusicFile audioFile;

    try {
      // If for some reasons, metadata aren't readable, we just store the file path
      audioFile = EyeD3.parse(path); // new MusicFile(path);
      if (audioFile == null) {
        audioFile = parseFailback(path);
      }
    } catch (Exception e) {
      String filePath = path.normalize().toAbsolutePath().toString();
      audioFile = parseFailback(path);

      logger.severe(String.format("Error for %s: %s", filePath, e.getMessage()));
    }

    docs.add(audioFile);

    result
      .joinScannedFiles(1)
      .joinBytes(audioFile.getSize());
  }

  synchronized private void saveIntoDb(List<MusicFile> docs, ScanOp result) {
    try {
      long upserts = this.musicFileService.bulkSave(docs);
      result.joinInsertedFiles(upserts);
    } catch (Exception e) {
      logger.severe(String.format("Error for saveIntoDb: %s", e.getMessage()));
    }
  }

  private ScanOp forkJoinComputation(ScanOp result) throws Throwable {
    // it directly parse the list...
    if (paths.size() <= 100) {
      List<MusicFile> docs = new ArrayList<>();

      for (Path path : paths) {
        parsePath(path, docs, result);
      }

      if (!docs.isEmpty()) {
        this.saveIntoDb(docs, result);
      }
    } else {
      // otherwise it split the job in two tasks
      List<Path> subset1 = paths.subList(0, paths.size() / 2);
      ScanTask subTaskOne = GuiceUtils.getInstance(ScanTask.class).setPaths(subset1);

      List<Path> subset2 = paths.subList(paths.size() / 2, paths.size());
      ScanTask subTaskTwo = GuiceUtils.getInstance(ScanTask.class).setPaths(subset2);

      invokeAll(subTaskOne, subTaskTwo);

      result.joinResult(subTaskOne.join());
      result.joinResult(subTaskTwo.join());
    }

    return result;
  }

  /**
   * The main computation performed by this task.
   *
   * @return the result of the computation
   */
  @Override
  protected ScanOp compute() {
    ScanOp scanOp = new ScanOp();
    try {
      return forkJoinComputation(scanOp);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      return scanOp;
    }
  }
}