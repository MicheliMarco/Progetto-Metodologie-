
module it.unicam.cs.mpgc.rpg129040 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.almasb.fxgl.all;

    opens it.unicam.cs.mpgc.rpg129040 to javafx.fxml, com.almasb.fxgl.all;
    opens it.unicam.cs.mpgc.rpg129040.ui to com.almasb.fxgl.all, javafx.fxml;

    exports it.unicam.cs.mpgc.rpg129040;
    exports it.unicam.cs.mpgc.rpg129040.model;
    exports it.unicam.cs.mpgc.rpg129040.service;
    exports it.unicam.cs.mpgc.rpg129040.repository;
}