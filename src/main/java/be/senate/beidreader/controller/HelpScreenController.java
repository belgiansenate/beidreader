package be.senate.beidreader.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.*;

/**
 * Created by wv on 29/05/2015.
 */
public class HelpScreenController {
    @FXML private WebView helpWebView;
    private WebEngine helpWebEngine;
    private Scene helpWebScene;

    public void initialize() {
        System.out.println("Initialize helpscherm controller.");
        this.helpWebView = new WebView();
        this.helpWebEngine = this.helpWebView.getEngine();
        this.helpWebScene = new Scene(this.helpWebView, 800, 800);
        getHelp();
    }

    private void getHelp() {
        String helpContent = "";
        try {
            // Be carefull !
            // When we specify the helpfile als a resource relative to a class, we cannot use '.' or '..'.
            // Although this construction works if the help-file is on the filesystem, it fails when the help-file
            // is contained in a jar-file (which is the case when the software is packaged).
            // Therefore, the file has to be "downstream" our entry-point in the class-tree.
            // This is the reason I use the 'ClassLoader' to be at the top of the pyramid.
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("be/senate/beidreader/help/help.html");;
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            LineNumberReader lineNumberReader = new LineNumberReader(inputStreamReader);
            String currentLine = null;
            do {
                currentLine = lineNumberReader.readLine();
                if (currentLine != null) {
                    helpContent = helpContent + currentLine;
                }
            } while (currentLine != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.helpWebEngine.loadContent(helpContent);
    }


    public Scene getHelpWebScene() {
        return helpWebScene;
    }
}
