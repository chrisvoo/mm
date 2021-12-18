package scanner;

import com.mpatric.mp3agic.Mp3File;
import models.files.MusicFile;
import models.scanner.ScanOp;
import models.scanner.ScanOpError;
import utils.Db;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ScanTask extends RecursiveTask<ScanOp> {
  private List<Path> paths;
  private Db db;

  public ScanTask(List<Path> paths, Db db) {
    this.paths = paths;
    this.db = db;
  }

  private void parsePath(Path path, List<MusicFile> docs, ScanOp result) {
    MusicFile audioFile;
    boolean hasErrors = false;
    try {
      // If for some reasons, metadata aren't readable, we just store the file path
      audioFile = new MusicFile(new Mp3File(path));
    } catch (Exception e) {
      String filePath = path.normalize().toAbsolutePath().toString();
      audioFile = new MusicFile()
        .setAbsolutePath(filePath)
        .calculateSize(path);

      result
        .joinError(
            new ScanOpError()
              .setMessage(e.getMessage())
              .setAbsolutePath(filePath)
        );
    }

    docs.add(audioFile);

    result
      .joinScannedFiles(1)
      .joinBytes(audioFile.getSize());
  }

  private void saveIntoDb(List<MusicFile> docs, ScanOp result) {

  }

  private ScanOp forkJoinComputation(ScanOp result) throws Throwable {
    // it directly parse the list...
    if (paths.size() < 500) {
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
      ScanTask subTaskOne = new ScanTask(subset1, db);

      List<Path> subset2 = paths.subList(paths.size() / 2, paths.size());
      ScanTask subTaskTwo = new ScanTask(subset2, db);

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