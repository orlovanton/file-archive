package services;

import com.google.inject.Inject;
import models.FileRecord;
import models.FileRepository;


import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

public class FileService {

    private final FileRepository fileRepository;

    @Inject
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public CompletionStage<FileRecord> add(FileRecord fileRecord) {
        return fileRepository.add(fileRecord);
    }

    public CompletionStage<FileRecord> update(FileRecord fileRecord) {
        return fileRepository.update(fileRecord);
    }

    public CompletionStage<FileRecord> get(Long id) {
        return fileRepository.get(id);
    }

    public CompletionStage<Stream<FileRecord>> list() {
        return fileRepository.list();
    }

    public void delete(Long id) {
        //todo: handle properly
        fileRepository.remove(id);
    }
}
