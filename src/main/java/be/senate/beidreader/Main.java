package be.senate.beidreader;

import be.belgium.eid.eidlib.BeID;
import be.senate.beidreader.controller.MainScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        // First, we check the arguments given to the application (methode 'getParameters' from class 'Application')
        List<String> argumentList = getParameters().getUnnamed();
//        Iterator<String> argumentIterator = argumentList.iterator();
//        while (argumentIterator.hasNext()) {
//            System.out.println(argumentIterator.next());
//        }
        String[] args = argumentList.toArray(new String[0]);
        if (args.length != 1) {
            System.out.println("usage: java -jar BeIDReader-jar-with-dependencies.jar <filename>");
            System.out.println("example: java -jar BeIDReader-jar-with-dependencies.jar c:\\home\\senatoren.csv");
            System.exit(-1);
        }
        String firstFileName = args[0];

        // We load the start-screen
        // As we need the controller afterwards we donot use the static methode of FXMLLoader (as below)...
        //        GridPane root = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        // But we get an instance of FXMLLoader. We can ask this instance to get the controller
        FXMLLoader hoofdSchermLoader = new FXMLLoader(getClass().getResource("controller/mainScreen.fxml"));
        GridPane root = hoofdSchermLoader.load();
        MainScreenController mainScreenController = (MainScreenController)hoofdSchermLoader.getController();
        primaryStage.setTitle("BeIDReader");
        primaryStage.setScene(new Scene(root, 800, 600));
        // We set the filePathTextField to the given argument
        mainScreenController.setFilePath(firstFileName);
        mainScreenController.openFile(firstFileName);

        // We declare 'mainScreenController' as being the CardListener of our system.
        BeID beID = new BeID(true);
        mainScreenController.setBeID(beID);
        beID.enableCardListener(mainScreenController);



        // Tijdelijk
//        CardHolder cardHolder = new CardHolder();
//        cardHolder.setRegNr("12345");
//        cardHolder.setLastName("Verhaest");
//        cardHolder.setFirstName("Wim");
//        mainScreenController.showCardHolderAsCurrent(cardHolder);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
