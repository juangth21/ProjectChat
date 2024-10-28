module com.github.juan {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml.bind;
    requires java.desktop;

    opens com.github.juan to javafx.fxml; // Esto permite el acceso a todas las clases en com.github.alvader01
    opens com.github.juan.Utils to java.xml.bind;
    opens com.github.juan.Model.entity to java.xml.bind;
    opens com.github.juan.view to javafx.fxml; // Asegúrate de incluir esta línea

    exports com.github.juan;
    exports com.github.juan.Model.entity;
    exports com.github.juan.Test;
    opens com.github.juan.Model.XML to java.xml.bind;
}