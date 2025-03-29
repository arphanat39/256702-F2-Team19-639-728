module com.rabbithop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires transitive javafx.graphics; // Add "transitive" here
    
    opens com.rabbithop to javafx.fxml;
    exports com.rabbithop;
    exports com.rabbithop.screens;
    exports com.rabbithop.entities;
}