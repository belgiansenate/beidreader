package be.senate.beidreader.controller;

import be.belgium.eid.eidlib.BeID;
import be.belgium.eid.event.CardListener;
import be.belgium.eid.exceptions.EIDException;
import be.senate.beidreader.model.CardHolder;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class HoofdSchermController implements CardListener {
    final private static short STATE_NEW = 0;
    final private static short STATE_OPENED = 1;
    final private static short STATE_NEWCHANGED = 2;
    final private static short STATE_OPENEDCHANGED = 3;

    private short state = STATE_NEW; // Initial state
    private BeID beID;
    private CardHolder currentCardHolder;
    private HashMap<String, CardHolder> cardHolderHashMap;
    private ObservableList<CardHolder> cardHolderObservableList;
    private ObservableList<String> dummyList;

    @FXML private Stage mainStage;
    @FXML private TextField filePathTextField;
    @FXML private TextField rrnTextField;
    @FXML private TextField naamTextField;
    @FXML private TextField voornamenTextField;
    @FXML private ImageView pasfotoImageView;
    @FXML private ListView<CardHolder> cardHolderListView;
//    @FXML private ListView<String> cardHolderListView;
    @FXML private Button upButton;
    @FXML private Button downButton;
    @FXML private Button deleteButton;

    @FXML private MenuItem newMenuItem;
    @FXML private MenuItem openMenuItem;
    @FXML private MenuItem saveMenuItem;
    @FXML private MenuItem saveAsMenuItem;
    @FXML private MenuItem closeMenuItem;
    @FXML private MenuItem exitMenuItem;




    // This method is automatically called by the 'load'-method of FMXLoader (see Main-class)
    public void initialize() {
        System.out.println("Initialize hoofdschermController");
        init();
    }

    // I have put most initialisation here...
    public void init() {
        this.cardHolderObservableList = FXCollections.observableArrayList();
//        final ListView<CardHolder> listView = new ListView<CardHolder>(this.cardHolderObservableList);
//        listView.setCellFactory(new Callback<ListView<CardHolder>, ListCell<CardHolder>>() {
//            @Override public ListCell<CardHolder> call(ListView<CardHolder> list) {
//                return new CardHolderFormatCell();
//            }
//        });
//        this.cardHolderListView = listView;

        this.cardHolderListView.setItems(this.cardHolderObservableList);
//        this.dummyList = FXCollections.observableArrayList();
//        this.cardHolderListView.setItems(this.dummyList);
        this.state = STATE_NEW;
        reflectState();
    }

    public void reflectState() {
        switch (this.state) {
            case (STATE_NEW) : {
                this.newMenuItem.setDisable(true);
                this.openMenuItem.setDisable(false);
                this.saveMenuItem.setDisable(true);
                this.saveAsMenuItem.setDisable(true);
                this.closeMenuItem.setDisable(true);
                this.exitMenuItem.setDisable(false);
                this.downButton.setDisable(true);
                this.upButton.setDisable(true);
                this.deleteButton.setDisable(true);
                break;
            }
            case (STATE_OPENED) : {
                this.newMenuItem.setDisable(true);
                this.openMenuItem.setDisable(true);
                this.saveMenuItem.setDisable(true);
                this.saveAsMenuItem.setDisable(true);
                this.closeMenuItem.setDisable(false);
                this.exitMenuItem.setDisable(false);
                this.downButton.setDisable(true);
                this.upButton.setDisable(true);
                this.deleteButton.setDisable(true);
                break;
            }
            case (STATE_NEWCHANGED) : {
                this.newMenuItem.setDisable(true);
                this.openMenuItem.setDisable(true);
                this.saveMenuItem.setDisable(false);
                this.saveAsMenuItem.setDisable(false);
                this.closeMenuItem.setDisable(false);
                this.exitMenuItem.setDisable(false);
                this.downButton.setDisable(true);
                this.upButton.setDisable(true);
                this.deleteButton.setDisable(true);
                break;
            }
            case (STATE_OPENEDCHANGED) : {
                this.newMenuItem.setDisable(true);
                this.openMenuItem.setDisable(true);
                this.saveMenuItem.setDisable(false);
                this.saveAsMenuItem.setDisable(false);
                this.closeMenuItem.setDisable(false);
                this.exitMenuItem.setDisable(false);
                this.downButton.setDisable(true);
                this.upButton.setDisable(true);
                this.deleteButton.setDisable(true);
                break;
            }
            default:;

        }
    }
    public void exitApplication(ActionEvent actionEvent) {
        System.exit(0);
        return;
    }

    public void setFilePath(String filePath) {
        this.filePathTextField.setText(filePath);
    }

    public void deleteButtonPushed(ActionEvent actionEvent) {
        this.currentCardHolder = this.cardHolderListView.getFocusModel().getFocusedItem();
        this.cardHolderObservableList.remove(this.currentCardHolder);
        refreshScreenDetail(this.currentCardHolder);
        this.deleteButton.setDisable(true);
        if (this.state == STATE_NEW)
            this.state = STATE_NEWCHANGED;
        else
            this.state = STATE_OPENEDCHANGED;
        reflectState();
    }

    // Methode that opens a filechooser, open the selected file en reads the content into the observable list of cardholders.
    public void startFileChooser(ActionEvent actionEvent) {
        System.out.println("Start fileChooser.");
        FileChooser fileChooser = new FileChooser();
        String currentFileName = filePathTextField.getText();
        File currentFile = new File(currentFileName);
        File directory = currentFile.getParentFile();
        fileChooser.setInitialDirectory(directory);
        fileChooser.setInitialFileName("Voornaam.txt");
        File file = fileChooser.showOpenDialog(mainStage);
        if (file == null) {
            filePathTextField.setText("");
        } else {
            String filename = file.getAbsolutePath();
            filePathTextField.setText(filename);
            openFile(file);
        }
        return;
    }

    // Method that starts a file-save-chooser and saves (using saveToFile) the content of the observable list of cardholders to file.
    public void startFileSaveChooser(ActionEvent actionEvent) {
        System.out.println("Start fileSaveChooser.");
        FileChooser fileChooser = new FileChooser();
        String currentFileName = filePathTextField.getText();
        File currentFile = new File(currentFileName);
        File directory = currentFile.getParentFile();
        fileChooser.setInitialDirectory(directory);
        fileChooser.setInitialFileName("Voornaam.txt");
        File file = fileChooser.showSaveDialog(mainStage);
        if (file == null) {
            filePathTextField.setText("");
        } else {
            String filename = file.getAbsolutePath();
            filePathTextField.setText(filename);
            saveToFile(file);
        }
        return;
    }

    // Try to save on top of the current file
    public void saveCurrentFile(ActionEvent actionEvent) {
        String currentFileName = filePathTextField.getText();
        if ((currentFileName == null) || (currentFileName.equals(""))) { // No current file. We save using the fileChooser.
            startFileSaveChooser(actionEvent);
        } else {  // Here, we execute the 'NORMAL' action (i.e. we save using the current filename
            File file = new File(currentFileName);
            if (file == null) {
                filePathTextField.setText("");
            } else {
                String filename = file.getAbsolutePath();
                filePathTextField.setText(filename);
                saveToFile(file);
            }
        }
        return;
    }

    public void helpMenuItemClicked(ActionEvent actionEvent) {
        Stage helpSchermStage = new Stage();
        FXMLLoader helpSchermLoader = new FXMLLoader(getClass().getResource("helpScherm.fxml"));
        try {
            Pane helpSchermPane = helpSchermLoader.load();
            HelpSchermController helpSchermController = (HelpSchermController)helpSchermLoader.getController();
            Scene helpSchermScene = helpSchermController.getHelpWebScene();
            helpSchermStage.setScene(helpSchermScene);
            helpSchermStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Methode that writes the observable list of cardholders to a given csv-file.
    private void saveToFile(File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            Iterator<CardHolder> cardHolderIterator = this.cardHolderObservableList.iterator();
            while (cardHolderIterator.hasNext()) {
                CardHolder cardHolder = cardHolderIterator.next();
                printWriter.println(cardHolder.toCsv());
            };
            printWriter.close();
            this.state = STATE_OPENED;
            reflectState();
//            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Methode that fills the observable list of cardholders, given a csv-file with entries
    private void openFile(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
            // So far, so good. We can reinitialize the observableList
            this.cardHolderObservableList.clear();
            String currentLine = null;
            do {
                currentLine = lineNumberReader.readLine();
                if (currentLine != null) {
                    CardHolder cardHolder = CardHolder.getInstanceFromCsv(currentLine);
                    this.cardHolderObservableList.add(cardHolder);
                }
            } while (currentLine != null);
            // Now, we set the correct 'state'
            this.state = STATE_OPENED;
            reflectState();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Methode that fills the observable list of cardholders, given the filename of a csv-file with entries
    public void openFile(String fileName) {
        try {
            FileReader fileReader = new FileReader(fileName);
            LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
            // So far, so good. We can reinitialize the observableList
            this.cardHolderObservableList.clear();
            String currentLine = null;
            do {
                currentLine = lineNumberReader.readLine();
                if (currentLine != null) {
                    CardHolder cardHolder = CardHolder.getInstanceFromCsv(currentLine);
                    this.cardHolderObservableList.add(cardHolder);
                }
            } while (currentLine != null);
            // Now, we set the correct 'state'
            this.state = STATE_OPENED;
            reflectState();
        } catch (java.io.FileNotFoundException e) { // If the file does not exist, we make it "new"
            this.state = STATE_NEW;
            reflectState();
            this.setFilePath("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void upButtonPushed(ActionEvent actionEvent){
        System.out.println("Up button pushed");
    }

    public void downButtonPushed(ActionEvent actionEvent){
        System.out.println("Down button pushed");
        this.cardHolderObservableList.add(this.currentCardHolder);
//        this.cardHolderListView.setItems(this.cardHolderObservableList);
//        CardHolder ch = this.currentCardHolder;
//        this.dummyList.add(ch.getRegNr() + " " + ch.getLastName() + " " + ch.getFirstName());
//        this.cardHolderListView.setItems(this.dummyList);
        if (this.state == STATE_NEW)
            this.state = STATE_NEWCHANGED;
        else
            this.state = STATE_OPENEDCHANGED;
        reflectState();

        return;
    }


    public void cardHolderListViewItemClicked(MouseEvent mouseEvent) {
        this.currentCardHolder = this.cardHolderListView.getFocusModel().getFocusedItem();
        if (this.currentCardHolder != null) {
            refreshScreenDetail(this.currentCardHolder);
            this.deleteButton.setDisable(false);
        }
        System.out.println("ListView item clicked; " + this.cardHolderListView.getFocusModel().getFocusedItem());
    }

    public void closeMenuItemClicked(ActionEvent actionEvent) {
        this.currentCardHolder = null;
        this.rrnTextField.setText("");
        this.naamTextField.setText("");
        this.voornamenTextField.setText("");
        this.pasfotoImageView.setImage(null);
        this.downButton.setDisable(true);
        this.cardHolderObservableList = FXCollections.observableArrayList();
        this.cardHolderListView.setItems(this.cardHolderObservableList);
        this.state = STATE_NEW;
        reflectState();
        this.setFilePath("");
    }

    private void showCardHolderAsCurrent(CardHolder cardHolder) {
        rrnTextField.setText(cardHolder.getRegNr());
        naamTextField.setText(cardHolder.getLastName());
        voornamenTextField.setText(cardHolder.getFirstName());
    }

    public void setBeID(BeID beID) {
        this.beID = beID;
    }

    private void refreshScreenDetail(CardHolder cardHolder) {
        this.rrnTextField.setText(cardHolder.getRegNr());
        this.naamTextField.setText(cardHolder.getLastName());
        this.voornamenTextField.setText(cardHolder.getFirstName() + " " + cardHolder.getMiddleName());
        Image image = new Image(cardHolder.getPictureAsInputStream());
        this.pasfotoImageView.setImage(image);
    }

    @Override
    public void cardInserted() {
        System.out.println("Kaart ingebracht.");
        this.currentCardHolder = new CardHolder();
        try {
            this.currentCardHolder.readBeID(this.beID);
            refreshScreenDetail(this.currentCardHolder);
            this.downButton.setDisable(false);
        } catch (EIDException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void cardRemoved() {
        System.out.println("Kaart verwijderd.");
        this.currentCardHolder = null;
        this.rrnTextField.setText("");
        this.naamTextField.setText("");
        this.voornamenTextField.setText("");
        this.pasfotoImageView.setImage(null);
        this.downButton.setDisable(true);

    }

    // This (inner)-class is used to 'present' a CardHolder within the ListView as a string
    // It does not yet function correctly.
    public class CardHolderFormatCell extends ListCell<CardHolder> {
        public CardHolderFormatCell () {};
        @Override protected void updateItem(CardHolder item, boolean empty) {
            super.updateItem(item, empty);
            setText(item == null ? "" : item.getRegNr() + " " + item.getLastName() + " " + item.getFirstName());
        }
    }
}
