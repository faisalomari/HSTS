package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.Student;
import il.cshaifasweng.OCSFMediatorExample.client.StudentDetails;
import il.cshaifasweng.OCSFMediatorExample.client.StudentListSubscriber;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentsListTable {
    private List<Student> studentList = new ArrayList<>();

    private StudentListSubscriber subscriber;

    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableColumn<Student, Object> idNumColumn;

    @FXML
    private TableColumn<Student, Object> idColumn;

    @FXML
    private TableColumn<Student, Object> firstNameColumn;

    @FXML
    private TableColumn<Student, Object> lastNameColumn;

    public void initialize() {
//        idNumColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getNumID()));
        idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getID()));
        firstNameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getLastName()));

        TableColumn<Student, Void> showColumn = new TableColumn<>("Show");
        showColumn.setCellFactory(param -> new TableCell<>() {
            private final Button showButton = new Button("Show");

            {
                showButton.setOnAction(event -> {
                    Student selectedStudent = getTableRow().getItem();
                    if (selectedStudent != null) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentDetails.fxml"));
                            Parent root = loader.load();
                            // Get the controller of the next FXML file
                            StudentDetails nextController = loader.getController();

                            // Pass the student to the next controller
                            nextController.setStudent(selectedStudent);
                            // Show the new FXML file in a new window
                            Scene scene = new Scene(root);
                            Stage stage = new Stage();
                            stage.setTitle(selectedStudent.getStudentName() + " Grades");
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(showButton);
                }
            }
        });

        studentTable.getColumns().add(showColumn);
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
        if (studentTable != null) {
            studentTable.getItems().clear();
            studentTable.getItems().addAll(studentList);
        }
    }

    public void setSubscriber(StudentListSubscriber subscriber) {
        this.subscriber = subscriber;
        this.studentList = this.subscriber.getStudentList();
    }

    public void handleStudentList(List<Student> studentList) {
        this.studentList = studentList;
        studentTable.getItems().clear();
        studentTable.getItems().addAll(studentList);
    }
}
