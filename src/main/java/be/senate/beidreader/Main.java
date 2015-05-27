package be.senate.beidreader;

import be.belgium.eid.eidlib.BeID;
import be.senate.beidreader.controller.HoofdSchermController;
import be.senate.beidreader.model.CardHolder;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.annotation.Resource;
import javax.xml.soap.Node;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        // First, we check the arguments given to the application
        List<String> argumentList = getParameters().getUnnamed();
//        Iterator<String> argumentIterator = argumentList.iterator();
//        while (argumentIterator.hasNext()) {
//            System.out.println(argumentIterator.next());
//        }
        String[] args = argumentList.toArray(new String[0]);
        if (args.length != 2) {
            System.out.println("usage: beidreader <lang> <filename>");
            System.out.println("example: beidreader nl c:\\home\\senatoren.csv");
            System.exit(-1);
        }
        String firstFileName = args[1];

        // We load the start-screen
        // As we need the controller afterwards we donot use the static methode of FXMLLoader (as below)...
        //        GridPane root = FXMLLoader.load(getClass().getResource("hoofdScherm.fxml"));
        // But we get an instance of FXMLLoader. We can ask this instance to get the controller
        FXMLLoader hoofdSchermLoader = new FXMLLoader(getClass().getResource("hoofdScherm.fxml"));
        GridPane root = hoofdSchermLoader.load();
        HoofdSchermController hoofdSchermController = (HoofdSchermController)hoofdSchermLoader.getController();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800, 600));
        // We set the filePathTextField to the given argument
        hoofdSchermController.setFilePath(firstFileName);

        // We declare 'hoofdSchermController' as being the CardListener of our system.
        BeID beID = new BeID(true);
        hoofdSchermController.setBeID(beID);
        beID.enableCardListener(hoofdSchermController);



        // Tijdelijk
//        CardHolder cardHolder = new CardHolder();
//        cardHolder.setRegNr("12345");
//        cardHolder.setLastName("Verhaest");
//        cardHolder.setFirstName("Wim");
//        hoofdSchermController.showCardHolderAsCurrent(cardHolder);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
