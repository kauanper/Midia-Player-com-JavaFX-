package com.example.midiainterfacefx;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
    private double eixoY, eixoX;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 322, 336);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                eixoX = mouseEvent.getSceneX();
                eixoY = mouseEvent.getSceneY();
            }
        });

        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setX(mouseEvent.getScreenX() - eixoX);
                stage.setY(mouseEvent.getScreenY() - eixoY);
            }
        });

        System.out.println(eixoX + " " + eixoY);
    }

    public static void main(String[] args) {
        // Definindo o caminho para as bibliotecas do JavaFX
        System.setProperty("java.library.path", "C:/Users/kauan/OneDrive/Área de Trabalho/JavaSetup/javafx-sdk-23.0.1/bin");

        launch();
    }
}