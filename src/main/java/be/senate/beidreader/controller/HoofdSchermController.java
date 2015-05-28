package be.senate.beidreader.controller;

import be.belgium.eid.eidlib.BeID;
import be.belgium.eid.event.CardListener;
import be.belgium.eid.exceptions.EIDException;
import be.senate.beidreader.model.CardHolder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.control.ListView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

public class HoofdSchermController implements CardListener {
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
    }
    public void exitApplication(ActionEvent actionEvent) {
        System.exit(0);
        return;
    }

    public void setFilePath(String filePath) {
        this.filePathTextField.setText(filePath);
    }

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
            filePathTextField.setText("<filename>");
        } else {
            String filename = file.getAbsolutePath();
            filePathTextField.setText(filename);
        }
        return;
    }

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
            filePathTextField.setText("<filename>");
        } else {
            String filename = file.getAbsolutePath();
            filePathTextField.setText(filename);
            saveToFile(file);
        }
        return;
    }

    public void saveToFile(File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            Iterator<CardHolder> cardHolderIterator = this.cardHolderObservableList.iterator();
            while (cardHolderIterator.hasNext()) {
                CardHolder cardHolder = cardHolderIterator.next();
                printWriter.println(cardHolder.toCsv());
            };
            printWriter.close();
//            fileOutputStream.close();
        } catch (FileNotFoundException e) {
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
        return;
    }

    public void cardHolderListViewItemClicked(MouseEvent mouseEvent) {
        this.currentCardHolder = this.cardHolderListView.getFocusModel().getFocusedItem();
        refreshScreenDetail(this.currentCardHolder);
        System.out.println("ListView item clicked; " + this.cardHolderListView.getFocusModel().getFocusedItem());
    }

    public void showCardHolderAsCurrent(CardHolder cardHolder) {
        rrnTextField.setText(cardHolder.getRegNr());
        naamTextField.setText(cardHolder.getLastName());
        voornamenTextField.setText(cardHolder.getFirstName());
    }

    public void setBeID(BeID beID) {
        this.beID = beID;
    }

    public void refreshScreenDetail(CardHolder cardHolder) {
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
    public class CardHolderFormatCell extends ListCell<CardHolder> {
        public CardHolderFormatCell () {};
        @Override protected void updateItem(CardHolder item, boolean empty) {
            super.updateItem(item, empty);
            setText(item == null ? "" : item.getRegNr() + " " + item.getLastName() + " " + item.getFirstName());
        }
    }
}
