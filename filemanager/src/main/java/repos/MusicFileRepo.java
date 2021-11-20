package repos;

import models.MusicFile;
import services.MusicFileService;

public class MusicFileRepo implements MusicFileService {
    /**
     * Return a file by its primary key
     *
     * @param id MusicFile's primary key
     * @return MusicFile
     */
    @Override
    public MusicFile getById(long id) {
        return null;
    }

    /**
     * Saves a new music file into the database
     *
     * @param file The music file.
     * @return The music file's primary key on success, an exception otherwise
     */
    @Override
    public long create(MusicFile file) {
        return 0;
    }

    /**
     * Updates a music file into the database
     *
     * @param file The music file.
     * @return The result of the operation
     */
    @Override
    public boolean update(MusicFile file) {
        return false;
    }
}
