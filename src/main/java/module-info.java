module com.example.midiainterfacefx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.example.midiainterfacefx to javafx.fxml;
    exports com.example.midiainterfacefx;
}