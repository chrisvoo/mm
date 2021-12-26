package services;

import models.files.MusicFile;
import routes.utils.PaginatedResponse;
import routes.utils.Pagination;

import java.util.List;

public interface MusicFileService {
    /**
     * Return a file by its primary key
     * @param id MusicFile's primary key
     * @return MusicFile
     */
    MusicFile getById(long id);

    /**
     * Upsert of a music file into the database
     * @param file The music file.
     * @return The music file's primary key on success, an exception otherwise
     */
    MusicFile save(MusicFile file);

    /**
     * Insert multiple files in the database or update the records in case there's a constraint error.
     * @param files a list of files.
     * @return an array of update counts
     */
    long bulkSave(List<MusicFile> files);

    /**
     * Delete a music file.
     * @param id The file's id.
     * @return true if the operation was successful, false otherwise.
     */
    boolean delete(long id);

    /**
     * A pagination-enabled list of music files.
     * @param pagination How to retrieve the results.
     * @return The response with pagination info.
     */
    PaginatedResponse<MusicFile> getAll(Pagination pagination);
}