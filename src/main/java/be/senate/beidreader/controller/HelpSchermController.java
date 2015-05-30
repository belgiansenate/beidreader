package be.senate.beidreader.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Created by wv on 29/05/2015.
 */
public class HelpSchermController {
    @FXML private WebView helpWebView;
    private WebEngine helpWebEngine;

    public void initialize() {
        System.out.println("Initialize helpscherm controller.");
        this.helpWebView = new WebView();
        this.helpWebEngine = this.helpWebView.getEngine();
    }

    public void loadWebPage(String urlString) {
        if (this.helpWebEngine == null)
            System.out.println("Er is geen webengine");
        else
            this.helpWebEngine.load(urlString);
    }
}
