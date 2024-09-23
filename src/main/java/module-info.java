module com.example.pingpongstage2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pingpongstage2 to javafx.fxml;
    exports com.example.pingpongstage2;
}