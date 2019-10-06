package controllers;

import com.google.inject.Inject;
import models.FileRecord;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;
import services.FileService;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private final FileService fileService;
    private final HttpExecutionContext ec;

    @Inject
    public HomeController(FileService fileService, HttpExecutionContext ec) {
        this.fileService = fileService;
        this.ec = ec;
    }

    public CompletionStage<Result> index() {
        return fileService.list()
                .thenApplyAsync(personStream ->
                        ok(views.html.index.render(personStream.collect(Collectors.toList()))), ec.current()
                );
    }

}
