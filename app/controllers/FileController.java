package controllers;

import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import forms.FileRecordDeleteForm;
import models.FileRecord;
import play.data.Form;
import play.data.FormFactory;
import play.http.HttpEntity;
import play.libs.Files;
import play.mvc.*;
import services.FileService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class FileController extends Controller {

    private final Config config;
    private final FileService fileService;
    private final FormFactory formFactory;

    @Inject
    public FileController(Config config, FileService fileService, FormFactory formFactory) {
        this.config = config;
        this.fileService = fileService;
        this.formFactory = formFactory;
    }

    public Result upload(Http.Request request) {
        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> file = body.getFile("picture");
        if (file != null) {
            String filename = file.getFilename();
            long fileSize = file.getFileSize();
            String contentType = file.getContentType();
            fileService.add(new FileRecord(filename));
            Files.TemporaryFile tempFile = file.getRef();
            tempFile.copyTo(Paths.get(config.getString("archive.storage") + filename), true);
            return (ok("file uploaded")); //todo: think about it
        } else {
            return badRequest().flashing("error", "Missing file");
        }
    }

    public CompletionStage<Result> download(Long id) {
        String storagePath = config.getString("archive.storage");
        return fileService.get(id).thenApply(
                r -> {
                    String name = r.getName();
                    Path path = Paths.get(storagePath + name);
                    Source<ByteString, ?> source = FileIO.fromPath(path);
                    response().setHeader("Content-disposition", "attachment; filename=" + r.getName());


                    return new Result(
                            new ResponseHeader(200, Collections.emptyMap(), "todo"),
                            new HttpEntity.Streamed(source, Optional.empty(), Optional.of("application/octet-stream"))
                    );
                }
        );
    }

    @BodyParser.Of(BodyParser.FormUrlEncoded.class)
    public Result delete() {
        final Form<FileRecordDeleteForm> form = formFactory.form(FileRecordDeleteForm.class) .bindFromRequest("id");
        fileService.delete(form.get().id);
        return redirect(routes.HomeController.index());
    }


}
