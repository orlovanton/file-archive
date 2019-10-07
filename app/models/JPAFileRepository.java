package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPAFileRepository implements FileRepository {
    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;


    @Inject
    public JPAFileRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }


    @Override
    public CompletionStage<FileRecord> add(FileRecord person) {
        return supplyAsync(() -> wrap(em -> insert(em, person)), executionContext);
    }

    private FileRecord insert(EntityManager em, FileRecord fileRecord) {
        em.persist(fileRecord);
        return fileRecord;
    }

    @Override
    public CompletionStage<FileRecord> get(Long id) {
        return supplyAsync(() -> wrap(em -> em.find(FileRecord.class, id)), executionContext);
    }

    @Override
    public void remove(Long id) {
        wrap(em -> {
            FileRecord fileRecord = em.find(FileRecord.class, id);
            em.remove(fileRecord);
            return null;
        });
    }

    @Override
    public CompletionStage<Stream<FileRecord>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    private Stream<FileRecord> list(EntityManager em) {
        List<FileRecord> persons = em.createQuery("select p from file_record p", FileRecord.class).getResultList();
        return persons.stream();
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }
}
