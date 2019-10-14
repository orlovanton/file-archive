package controllers;

import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import forms.FileRecordDeleteForm;
import forms.FileRecordDescriptionForm;
import models.FileRecord;
import play.data.Form;
import play.data.FormFactory;
import play.http.HttpEntity;
import play.libs.Files;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;
import services.FileService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class FileController extends Controller {

    private final Config config;
    private final FileService fileService;
    private final FormFactory formFactory;
    private final HttpExecutionContext executionContext;

    @Inject
    public FileController(Config config, FileService fileService, FormFactory formFactory, HttpExecutionContext executionContext) {
        this.config = config;
        this.fileService = fileService;
        this.formFactory = formFactory;
        this.executionContext = executionContext;
    }

    public CompletionStage<Result> saveDesctiption(Http.Request request) {
        final Form<FileRecordDescriptionForm> form = formFactory.form(FileRecordDescriptionForm.class).bindFromRequest("id", "description");
        FileRecord fileRecord = new FileRecord();
        fileRecord.setId(form.get().getId());
        fileRecord.setDescription(form.get().getDescription());
        if (fileRecord.getId() == null) {
            return fileService.add(fileRecord).thenApply(r -> {
                return ok();
            });
        } else {
            return fileService.update(fileRecord).thenApply(r -> ok());
        }
    }

    public Result upload(Http.Request request) {
        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> file = body.getFile("picture");
        if (file != null) {
            String filename = file.getFilename();
            long fileSize = file.getFileSize();
            String contentType = file.getContentType();
            Files.TemporaryFile tempFile = file.getRef();
            try {
                return fileService.add(new FileRecord(filename)).thenApply(r -> {
                    tempFile.copyTo(Paths.get(config.getString("archive.storage") + filename), true);
                    return ok(Json.toJson(r));
//                    return redirect(routes.HomeController.index()).flashing("success", "File uploaded");
                }).toCompletableFuture().get();
            } catch (InterruptedException | ExecutionException ex) {
                return redirect(routes.HomeController.index()).flashing("error", "Internal error");
            }

        } else {
            return redirect(routes.HomeController.index()).flashing("error", "Missing file");
        }
    }

    public CompletionStage<Result> download(Long id) {
        String storagePath = config.getString("archive.storage");
        return fileService.get(id).thenApplyAsync(
                r -> {
                    String name = r.getName();
                    Path path = Paths.get(storagePath + name);
                    Source<ByteString, ?> source = FileIO.fromPath(path);
                    response().setHeader("Content-disposition", "attachment; filename=" + r.getName());

                    return new Result(
                            new ResponseHeader(200, Collections.emptyMap(), "todo"),
                            new HttpEntity.Streamed(source, Optional.empty(), Optional.of("application/octet-stream"))
                    );
                }, executionContext.current()
        );
    }

    @BodyParser.Of(BodyParser.FormUrlEncoded.class)
    public Result delete() {
        final Form<FileRecordDeleteForm> form = formFactory.form(FileRecordDeleteForm.class).bindFromRequest("id");
        fileService.delete(form.get().id);
        return redirect(routes.HomeController.index());
    }

}
