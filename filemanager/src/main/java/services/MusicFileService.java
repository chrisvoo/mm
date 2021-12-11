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
     * Upsert of a music file into the database
     * @param file The music file.
     * @return The music file's primary key on success, an exception otherwise
     */
    public MusicFile save(MusicFile file);

    /**
     * Delete a music file.
     * @param id The file's id.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean delete(long id);

    // TODO: cursor-based pagination
}