package controllers;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import play.libs.Files.TemporaryFile;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.FileService;

import java.nio.file.Paths;

public class UploadController extends Controller {

    private final FileService fileService;
    private final Config config;

    @Inject
    public UploadController(FileService fileService, Config config) {
        this.fileService = fileService;
        this.config = config;
    }

    public Result upload(Http.Request request) {
        Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<TemporaryFile> file = body.getFile("picture");
        if (file != null) {
            String filename = file.getFilename();
            long fileSize = file.getFileSize();
            String contentType = file.getContentType();
//            todo: save to db
            TemporaryFile tempFile = file.getRef();
            tempFile.copyTo(Paths.get(config.getString("archive.storage")), true);
            return (ok("file uploaded")); //todo: think about it
        } else {
            return badRequest().flashing("error", "Missing file");
        }


    }
}
