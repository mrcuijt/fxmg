package fxmg;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.logging.LogManager;

public class FxGenerate extends Application {

    static String basePath = "D:/workspace/fxmg/resource/";

    @Override
    public void start(Stage stage) throws Exception {
        stage.setWidth(800);
        stage.setHeight(600);
        Scene scene = new Scene(new Group());

        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(browser);

        com.sun.javafx.webkit.WebConsoleListener.setDefaultListener(new com.sun.javafx.webkit.WebConsoleListener() {
            @Override
            public void messageAdded(WebView webView, String message, int lineNumber, String sourceId) {
                System.out.println("From webview: " + message + " [" + sourceId + " - " + lineNumber + "]");
            }
        });

        JavaConnector javaConnector = new JavaConnector(webEngine);

        webEngine.getLoadWorker().stateProperty()
                .addListener(new ChangeListener<State>() {
                    @Override
                    public void changed(ObservableValue ov, State oldState, State newState) {
                        //System.out.println(newState);
                        if (newState == Worker.State.SUCCEEDED) {
                            stage.setTitle(webEngine.getLocation());
                            JSObject window = (JSObject) webEngine.executeScript("window");
                            window.setMember("javaConnector", javaConnector);
                        }
                    }
                });

        try {
            String index = "index.html";
            String content = FileUtil.getContent(basePath + index);
            webEngine.loadContent(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        scene.setRoot(scrollPane);

        stage.setScene(scene);
        stage.show();
    }
}
