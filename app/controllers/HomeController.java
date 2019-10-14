package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import forms.FileRecordDescriptionForm;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import services.FileService;

import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private final FileService fileService;
    private final HttpExecutionContext ec;
    private final FormFactory formFactory;

    @Inject
    public HomeController(FileService fileService, HttpExecutionContext ec, FormFactory formFactory) {
        this.fileService = fileService;
        this.ec = ec;
        this.formFactory = formFactory;
    }

    public CompletionStage<Result> index() {
        //todo: check why CSRF crashes if upload two files one by one
        return fileService.list()
                .thenApplyAsync(personStream ->
                        ok(views.html.index.render(personStream.collect(Collectors.toList()), formFactory.form(FileRecordDescriptionForm.class))), ec.current()
                );
    }

    public Result appSummary() {
        JsonNode jsonNode = Json.toJson(new AppSummary("Java Play React Seed!"));
        return ok(jsonNode).as("application/json");
    }

    class AppSummary {
        private String content;

        AppSummary(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
