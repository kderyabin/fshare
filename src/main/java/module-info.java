module com.kderyabin {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.kderyabin to javafx.fxml;
    opens com.kderyabin.controllers to javafx.fxml;
    exports com.kderyabin;
}
