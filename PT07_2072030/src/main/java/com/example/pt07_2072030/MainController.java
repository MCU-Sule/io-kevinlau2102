package com.example.pt07_2072030;

import com.example.pt07_2072030.Model.Comment;
import com.google.gson.JsonSyntaxException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.google.gson.Gson;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class MainController {
    @FXML
    private ListView<Comment> lvComment;
    @FXML
    private TextField namaUser;
    @FXML
    private TextArea isiKomentar;
    private ObservableList<Comment> oList;

    public void initialize() {
        oList = FXCollections.observableArrayList();
        lvComment.setItems(oList);
    }

    public void addComment() {
        if (namaUser.getText().isEmpty() || isiKomentar.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill the field properly", ButtonType.OK);
            alert.showAndWait();
        } else {
            oList.add(new Comment(namaUser.getText(), isiKomentar.getText()));
            namaUser.clear();
            isiKomentar.clear();
        }
    }

    public void saveComment() {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("data/comment.txt"));
            Gson g = new Gson();
            String jsonTemp = g.toJson(lvComment.getItems());
            writer.write(jsonTemp);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadComment() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/comment.txt"));
            String jsonString = "";
            String s;
            while ((s = reader.readLine()) != null) {
                jsonString += s;
            }
            Gson g = new Gson();
            Comment[] c = g.fromJson(jsonString, Comment[].class);
            oList = FXCollections.observableArrayList(c);
            lvComment.setItems(oList);
            reader.close();

        } catch (FileNotFoundException e) {
            oList = FXCollections.observableArrayList();
            lvComment.setItems(oList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveCommentNio() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text Documents", "*.txt");
        chooser.getExtensionFilters().add(extensionFilter);
        chooser.setSelectedExtensionFilter(extensionFilter);
        File f = chooser.showSaveDialog(namaUser.getScene().getWindow());
        if (f != null) {
            Path p = Paths.get(f.toURI());
            Gson g = new Gson();
            String jsonTemp = g.toJson(lvComment.getItems());
            try {
                Files.write(p, jsonTemp.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void loadCommentNio() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text Documents", "*.txt");
        chooser.getExtensionFilters().add(extensionFilter);
        chooser.setSelectedExtensionFilter(extensionFilter);
        File f = chooser.showOpenDialog(namaUser.getScene().getWindow());
        if (f != null) {
            Path p = Paths.get(f.toURI());
            try {
                String jsonString = Files.readString(p);
                Gson g = new Gson();
                try {
                    Comment[] c = g.fromJson(jsonString, Comment[].class);
                    oList = FXCollections.observableArrayList(c);
                    lvComment.setItems(oList);
                } catch (JsonSyntaxException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Cannot read other than JSON Format", ButtonType.OK);
                    alert.showAndWait();
                    loadCommentNio();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void moveFile() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text Documents", "*.txt");
        chooser.getExtensionFilters().add(extensionFilter);
        chooser.setSelectedExtensionFilter(extensionFilter);
        File f = chooser.showOpenDialog(namaUser.getScene().getWindow());
        Path movefrom = Paths.get(f.toURI());
        Path targetDir = Paths.get("newPath/" + f.getName());

        // File and Directory check test if exists
//        File fileTest = new File("newPath/" + f.getName());
//        if (fileTest.exists() && fileTest.isFile()) {
//            System.out.println("file exists");
//        } else {
//            System.out.println("file not exists");
//        }
//
//        File dirTest = new File("src/main");
//        if (dirTest.exists() && dirTest.isDirectory()) {
//            System.out.println("directory exists");
//        } else {
//            System.out.println("directory not exists");
//        }

        try {
            Files.move(movefrom, targetDir, REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteComment(ActionEvent actionEvent) {

    }
}