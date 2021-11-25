package repos;

import com.google.inject.Inject;
import models.files.MusicFile;
import services.MusicFileService;
import utils.EnvVars;

public class MusicFileRepo implements MusicFileService {
    @Inject
    private EnvVars envVars;

    /**
     * Return a file by its primary key
     *
     * @param id MusicFile's primary key
     * @return MusicFile
     */
    @Override
    public MusicFile getById(long id) {
        System.out.println(envVars.getMysqlUser());
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
