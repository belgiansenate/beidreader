package be.senate.beidreader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class oudHoofdSchermController {
    @FXML private TextField boodschapTextField;
    @FXML private Label boodschapLabel;
    @FXML private Label tellerLabel;

    public void visueleKnopIngedrukt(ActionEvent actionEvent) {
        System.out.println("Mijn visuele knop is ingedrukt.");
        String boodschap = boodschapTextField.getText();
        boodschapLabel.setText(boodschap);
        String currentTellerString = tellerLabel.getText();
        int currentTeller = Integer.parseInt(currentTellerString);
        currentTeller++;
        currentTellerString = Integer.toString(currentTeller);
        tellerLabel.setText(currentTellerString);
    }
}
