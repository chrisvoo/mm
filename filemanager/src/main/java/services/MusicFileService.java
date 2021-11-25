package services;

import models.files.MusicFile;

public interface MusicFileService {
    /**
     * Return a file by its primary key
     * @param id MusicFile's primary key
     * @return MusicFile
     */
    public MusicFile getById(long id);

    /**
     * Saves a new music file into the database
     * @param file The music file.
     * @return The music file's primary key on success, an exception otherwise
     */
    public long create(MusicFile file);

    /**
     * Updates a music file into the database
     * @param file The music file.
     * @return The result of the operation
     */
    public boolean update(MusicFile file);

    // TODO: cursor-based pagination
}
