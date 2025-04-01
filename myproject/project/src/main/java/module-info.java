module com.rabbithop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;  // Make sure this is included
    requires transitive javafx.graphics;
    
    opens com.rabbithop to javafx.fxml;
    exports com.rabbithop;
    exports com.rabbithop.screens;
    exports com.rabbithop.entities;
}