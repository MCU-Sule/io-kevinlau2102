module com.example.pt07_2072030 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.example.pt07_2072030 to javafx.fxml;
    exports com.example.pt07_2072030;
    exports com.example.pt07_2072030.Model;
    opens com.example.pt07_2072030.Model to com.google.gson;
}