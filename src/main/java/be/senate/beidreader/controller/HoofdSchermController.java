package be.senate.beidreader.controller;

import be.belgium.eid.eidlib.BeID;
import be.belgium.eid.event.CardListener;
import be.belgium.eid.exceptions.EIDException;
import be.senate.beidreader.model.CardHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.smartcardio.CardChannel;
import java.io.File;
import java.util.HashMap;

public class HoofdSchermController implements CardListener {
    private BeID beID;
    private CardHolder currentCardHolder;
    private HashMap<String, CardHolder> cardHolderHashMap;

    @FXML private Stage mainStage;
    @FXML private TextField filePathTextField;
    @FXML private TextField rrnTextField;
    @FXML private TextField naamTextField;
    @FXML private TextField voornamenTextField;
    @FXML private ImageView pasfotoImageView;
    @FXML private Button upButton;
    @FXML private Button downButton;

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

    public void upButtonPushed(ActionEvent actionEvent){
        System.out.println("Up button pushed");
    }

    public void downButtonPushed(ActionEvent actionEvent){
        System.out.println("Down button pushed");
    }
    public void showCardHolderAsCurrent(CardHolder cardHolder) {
        rrnTextField.setText(cardHolder.getRegNr());
        naamTextField.setText(cardHolder.getLastName());
        voornamenTextField.setText(cardHolder.getFirstName());
    }

    public void setBeID(BeID beID) {
        this.beID = beID;
    }

    @Override
    public void cardInserted() {
        System.out.println("Kaart ingebracht.");
        this.currentCardHolder = new CardHolder();
        try {
            this.currentCardHolder.readBeID(this.beID);
            this.rrnTextField.setText(this.currentCardHolder.getRegNr());
            this.naamTextField.setText(this.currentCardHolder.getLastName());
            this.voornamenTextField.setText(this.currentCardHolder.getFirstName() + " " + this.currentCardHolder.getMiddleName());
            Image image = new Image(this.currentCardHolder.getPictureAsInputStream());
            this.pasfotoImageView.setImage(image);
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
}
