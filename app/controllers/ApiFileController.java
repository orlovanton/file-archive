package controllers;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.FileService;

import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class ApiFileController extends Controller {

    private final Config config;
    private final FileService fileService;

    @Inject
    public ApiFileController(Config config, FileService fileService) {
        this.config = config;
        this.fileService = fileService;
    }

    public CompletionStage<Result> list() {
        return fileService.list().thenApply(l -> ok(Json.toJson(l.collect(Collectors.toList()))).as("application/json"));

    }

}
