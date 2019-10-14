package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(JPAFileRepository.class)
public interface FileRepository {

    CompletionStage<FileRecord> add(FileRecord fileRecord);

    CompletionStage<FileRecord> update(FileRecord fileRecord);

    CompletionStage<FileRecord> get(Long id);

    void remove(Long id);

    CompletionStage<Stream<FileRecord>> list();


}
