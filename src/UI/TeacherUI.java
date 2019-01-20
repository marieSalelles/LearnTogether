package UI;

import Types.*;
import client.CoreClient;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.List;

/**
 * This UI is for when a teacher wants to use this application.
 * @author Aubin ABADIE
 * @author Marie SALELLES
 * @author Audrey SAMSON
 * @author Yvan SANSON
 * @author Solene SERAFIN
 */
public class TeacherUI extends UI {

	private Scene principalTeacherScene;
	private Tab  tabRecords;
	Tab tabCourse;
	protected ObservableList<PromotionType> promoNames;
	
    /**
     * Default constructor
     */
    public TeacherUI(Stage primaryStage, String login, int id, CoreClient client) {
		this.primaryStage = primaryStage;
		this.login = login;
		this.userID = id;
		this.client = client;
    }

    /**
     * This method creates the principal scene (view) for teachers.
     * It is called by the startUI when a student logs in.
     * @return the principal scene
     */
	public Scene createPrincipalTeacherScene(){
		this.primaryStage.setTitle("LearnTogether for Teachers");
        BorderPane teacherScene = new BorderPane();
        //Create the top bar
        VBox topBar = new VBox();
        HBox titleBar = new HBox();
        Text title = new Text("Learn Together -- Teacher side");
        Text user = new Text("Connected as: " + login);
        title.setFont(Font.font("Cambria", 20));
        titleBar.getChildren().addAll(title, user);

        //Create the Tabs
        TabPane tabPane = new TabPane();
        
        Tab tabProfile = createProfileTab();
        
        Tab tabSchedule = new Tab();
        tabSchedule.setText("Schedule");
        tabSchedule.setClosable(false);

        tabRecords = createRecordTab();
        tabRecords.setText("Record");
        tabRecords.setClosable(false);

        Tab tabDiary = new Tab();
        tabDiary.setText("Diary");
        tabDiary.setClosable(false);

        Tab tabChat = createChatTab();
        
        tabCourse = tabCourse();

        tabPane.getTabs().add(tabProfile);
        tabPane.getTabs().add(tabSchedule);
        tabPane.getTabs().add(tabRecords);
        tabPane.getTabs().add(tabDiary);
        tabPane.getTabs().add(tabChat);
        tabPane.getTabs().add(tabCourse);
        

        topBar.getChildren().addAll(titleBar, tabPane);
        teacherScene.setTop(topBar);
        titleBar.setSpacing(20);
        titleBar.setPadding(new Insets(15, 12, 15, 12));

        principalTeacherScene = new Scene(teacherScene, 900, 700);
        return principalTeacherScene;
	}

    /**
     * This method creates the tab for the courses management.
     * @return the tab.
     */
	protected Tab tabCourse(){
        Tab tabCourse = new Tab();
        tabCourse.setText("Course");
        tabCourse.setClosable(false);
        tabCourse.setContent(courseRead(tabCourse));
        return tabCourse;
	}

    /**
     * This method populates the content for the courses information display
     * @param tabCourse the tab to populate
     * @return the content.
     */
	protected GridPane courseRead(Tab tabCourse){
        client.getCourses(userID);
        ListView<CourseType> list = new ListView<>();
        CourseType ct = new CourseType(1,"course1","communication course",1,2, 5);
        courseNames = FXCollections.observableArrayList();
        courseNames.add(ct);
        courseNames.addListener((ListChangeListener<CourseType>) c -> list.setItems(courseNames));

        Image addCourse = new Image(getClass().getResourceAsStream("images/icons8-plus-208.png"));
        ImageView addCourseView = new ImageView(addCourse);
        addCourseView.setFitHeight(15);
        addCourseView.setFitWidth(15);

        //create button add
        Button btnAddCourse = new Button("Add");
        btnAddCourse.setGraphic(addCourseView);//setting icon to button

        //delete button
        Image deleteCourse = new Image(getClass().getResourceAsStream("images/icons8-annuler-208.png"));
        ImageView deleteCourseView = new ImageView(deleteCourse);
        deleteCourseView.setFitHeight(12);
        deleteCourseView.setFitWidth(12);

        //create button delete
        Button btnDeleteCourse = new Button("Delete");
        btnDeleteCourse.setGraphic(deleteCourseView);//setting icon to button

        // add in hbox buttons and title
        HBox hboxButtonCourse = new HBox();

        Text title = new Text("Course : ");
        title.setFont(Font.font(20));
        hboxButtonCourse.getChildren().add(title);
        hboxButtonCourse.getChildren().add(btnAddCourse);
        hboxButtonCourse.getChildren().add(btnDeleteCourse);
        hboxButtonCourse.setSpacing(5);

        list.setItems(courseNames);
        System.out.println(courseNames);
        list.setPrefWidth(350);
        list.setPrefHeight(500);

        // left vbox
        VBox vboxListCourse = new VBox();
        vboxListCourse.getChildren().add(list);

        //grid pane
        GridPane gridCourseVisu = new GridPane();
        gridCourseVisu.setHgap(10);
        gridCourseVisu.setVgap(10);
        gridCourseVisu.setPadding(new Insets(10,10,10,10));

        gridCourseVisu.add(hboxButtonCourse, 1, 0);
        gridCourseVisu.add(vboxListCourse, 1, 2);

        /*creation of the info vbox of one course*/
        VBox vboxInfoCourse = new VBox();

        //title of column
        HBox hboxCourseInfo = new HBox();
        Text titleInfo = new Text("Course information : ");
        titleInfo.setFont(Font.font(20));
        hboxCourseInfo.getChildren().add(titleInfo);
        hboxCourseInfo.setAlignment(Pos.CENTER);

        // initialisation label and input
        HBox hboxnameCourseInfo = new HBox();
        HBox hboxdescriptionCourseInfo = new HBox();
        HBox hboxnbHourTotalCourseInfo = new HBox();
        HBox hbPromo = new HBox();

        Label nameLabel = new Label("Name of course : ");
        Label descriptionCourseLabel = new Label("Course description : ");
        Label nbHourTotalLabel = new Label( "Number Total Hour course : ");
        Label promoLabel = new Label(" Promotion identifiant : ");

        Text name = new Text(" ");
        Text descriptionCourse = new Text(" ");
        Text nbHourTotal = new Text(" ");
        Text idTeacher = new Text(" ");
        Text idPromo = new Text ("");


        hboxnameCourseInfo.getChildren().add(nameLabel);
        hboxdescriptionCourseInfo.getChildren().add(descriptionCourseLabel);
        hboxnbHourTotalCourseInfo.getChildren().add(nbHourTotalLabel);
        hbPromo.getChildren().add(promoLabel);

        hboxnameCourseInfo.getChildren().add(name);
        hboxdescriptionCourseInfo.getChildren().add(descriptionCourse);
        hboxnbHourTotalCourseInfo.getChildren().add(nbHourTotal);
        hbPromo.getChildren().add(idPromo);

        hboxnameCourseInfo.setAlignment(Pos.CENTER);
        hboxdescriptionCourseInfo.setAlignment(Pos.CENTER);
        hboxnbHourTotalCourseInfo.setAlignment(Pos.CENTER);
        hbPromo.setAlignment(Pos.CENTER);

        //create update button
        HBox hboxupdateButton = new HBox();
        Button btnUpdateCourse = new Button("Update");
        hboxupdateButton.getChildren().add(btnUpdateCourse);
        hboxupdateButton.setAlignment(Pos.CENTER);

        vboxInfoCourse.getChildren().addAll(hboxCourseInfo, hboxnameCourseInfo,hboxdescriptionCourseInfo,hboxnbHourTotalCourseInfo, hbPromo, hboxupdateButton);
        vboxInfoCourse.setSpacing(10);
        vboxInfoCourse.setPadding( new Insets(100, 0, 0, 75));

        btnAddCourse.setOnAction(event -> createTabCourse(tabCourse));

        btnUpdateCourse.setOnAction(event ->{
            SelectionModel<CourseType> selectedDeleteCourse = list.getSelectionModel();
            if (selectedDeleteCourse.getSelectedItem() != null) {
                updateTabCourse(tabCourse, selectedDeleteCourse.getSelectedItem().getName(),selectedDeleteCourse.getSelectedItem().getDescription(),selectedDeleteCourse.getSelectedItem().getNbTotalHour(),selectedDeleteCourse.getSelectedItem().getIdTeacher(), selectedDeleteCourse.getSelectedItem().getId());
            }
        });

        btnDeleteCourse.setOnAction(event -> {
            SelectionModel<CourseType> selectedDeleteCourse = list.getSelectionModel();
            if (selectedDeleteCourse.getSelectedItem() != null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"You are sure to delete a course", ButtonType.YES, ButtonType.NO);
                alert.setHeaderText("Confirmation delete");
                Window win = gridCourseVisu.getScene().getWindow();
                alert.initOwner(win);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.NO) {
                    return;
                }
                if (alert.getResult() == ButtonType.YES) {
                    client.handleDeleteCourse(selectedDeleteCourse.getSelectedItem().getId());
                }
            }

        });

        list.setOnMouseClicked(event -> {
            gridCourseVisu.getChildren().remove(vboxInfoCourse);
            gridCourseVisu.add(vboxInfoCourse, 2, 2);
            System.out.println("clicked on " + list.getSelectionModel().getSelectedItem());
            SelectionModel<CourseType> selectedCourse = list.getSelectionModel();
            name.setText(selectedCourse.getSelectedItem().getName());
            descriptionCourse.setText(selectedCourse.getSelectedItem().getDescription());
            nbHourTotal.setText(Integer.toString(selectedCourse.getSelectedItem().getNbTotalHour()));
            idPromo.setText(Integer.toString(selectedCourse.getSelectedItem().getIdPromo()));
        });

        return gridCourseVisu;
	}

    /**
     * This method populates the content for the different courses management.
     * @param tabCourse the tab to populate
     * @return the content.
     */
	private GridPane createTabCourse(Tab tabCourse){
	      
	       // labels
	        Label nameCourseLabel = new Label("Name of course : ");
	        Label descriptionCourseLabel = new Label("Description : ");
	        Label nbTotalHourLabel = new Label("Total hours : ");
	        Label promoLabel = new Label("Promotion : ");

	        // Add text Field
	        TextField nameCourseField = new TextField();
	        TextArea descriptionCourseField = new TextArea();
	        TextField nbTotalHourField = new TextField();
	        nbTotalHourField.textProperty().addListener((observable, oldValue, newValue) -> {
	            if(!newValue.matches("\\d*"))
	                nbTotalHourField.setText(newValue.replaceAll("[^\\d]", ""));
	        });
	        TextField promoField = new TextField();
	        
	        //grid pane
	        GridPane gridCourse = new GridPane();
	        gridCourse.setHgap(10);
	        gridCourse.setVgap(10);
	        gridCourse.setPadding(new Insets(10,10,10,10));
	        
	        ComboBox<PromotionType> promoComboBox = new ComboBox<PromotionType>();
	        promoComboBox.setItems(promoNames);
	        promoComboBox.getSelectionModel().select(1);

	        //Hbox
	        HBox nameCourse = new HBox();
	        HBox descriptionCourse = new HBox();
	        HBox nbTotalHourCourse = new HBox();
	        HBox idReferentTeacherCourse = new HBox();
	        HBox promoCourse = new HBox();

	        // add form in hbox
	        nameCourse.getChildren().addAll(nameCourseLabel, nameCourseField);
	        descriptionCourse.getChildren().addAll(descriptionCourseLabel, descriptionCourseField) ;
	        nbTotalHourCourse.getChildren().addAll(nbTotalHourLabel, nbTotalHourField) ;
	        promoCourse.getChildren().addAll(promoLabel, promoComboBox);
	        

	        //add hbox in gridpane
	        gridCourse.add(nameCourse, 1, 1);
	        gridCourse.add(descriptionCourse, 1, 2);
	        gridCourse.add(nbTotalHourCourse, 1, 3);
	        gridCourse.add(idReferentTeacherCourse, 1, 4);
	        gridCourse.add(promoCourse, 1, 5);

	        //add gridpane in tab
	        tabCourse.setContent(gridCourse);

	        //add button

	        Button okCreate = new Button("Create");
	        okCreate.setPrefHeight(40);
	        okCreate.setDefaultButton(true);
	        okCreate.setPrefWidth(100);
	        gridCourse.add(okCreate, 0, 13, 1, 1);
	        GridPane.setHalignment(okCreate, HPos.RIGHT);
	        gridCourse.setMargin(okCreate, new Insets(20, 0, 20, 0));

	        Button cancelCreate = new Button("Cancel");
	        cancelCreate.setPrefHeight(40);
	        cancelCreate.setDefaultButton(false);
	        cancelCreate.setPrefWidth(100);
	        gridCourse.add(cancelCreate, 2, 13, 1, 1);
	        gridCourse.setHalignment(cancelCreate, HPos.RIGHT);
	        gridCourse.setMargin(cancelCreate, new Insets(20, 0, 20, 0));

	        okCreate.setOnAction(event -> {
	            if (nameCourseField.getText().isEmpty()) {
	                showAlert(Alert.AlertType.ERROR, gridCourse.getScene().getWindow(), "Form Error!", "Please enter course name");
	                return;
	            }
	            if (descriptionCourseField.getText().isEmpty()) {
	                showAlert(Alert.AlertType.ERROR, gridCourse.getScene().getWindow(), "Form Error!", "Please enter course description");
	                return;
	            }
	            if (nbTotalHourField.getText().isEmpty()) {
	                showAlert(Alert.AlertType.ERROR, gridCourse.getScene().getWindow(), "Form Error!", "Please enter course total number of hour");
	                return;
	            }if (promoComboBox.getSelectionModel().isEmpty()) {
	                showAlert(Alert.AlertType.ERROR, gridCourse.getScene().getWindow(), "Form Error!", "Please enter course Promo");
	                return;
	            }
	           
	            PromotionType promo= (PromotionType) promoComboBox.getSelectionModel().getSelectedItem();
	            client.handleCreateCourse(nameCourseField.getText(), descriptionCourseField.getText(), Integer.parseInt(nbTotalHourField.getText()),userID, promo.getIdPromo());
	            nameCourseField.setText("");
	            descriptionCourseField.setText("");
	            nbTotalHourField.setText("");
	            });
	        
	        cancelCreate.setOnAction(event -> {
	        	tabCourse.setContent(courseRead(tabCourse));
	        });
	        
	        return gridCourse;
	        }

    /**
     * This method sets the current tab to the courseTab.
     * @return the courses information display.
     */
	protected GridPane setCourseTab(){
        return courseRead(tabCourse);
    }

    /**
     * This method creates the update course view.
     * @param tabCourse the tab course.
     * @param nameCourse the current name of the course.
     * @param descriptionCourse the current description of the course.
     * @param nbTotalHourCourse the current number of hours of the course.
     * @param referentTeacherCourse the current referent teacher of the course.
     * @param idCourse the current id of the course.
     * @return the grid pane populated with the different information.
     */
	protected GridPane updateTabCourse(Tab tabCourse, String nameCourse, String descriptionCourse, int nbTotalHourCourse, int referentTeacherCourse, int idCourse){
        // labels
        Label nameCourseLabel = new Label("Name of course : ");
        Label descriptionCourseLabel = new Label("Description : ");
        Label nbTotalHourLabel = new Label("Total hours : ");
        Label promoLabel = new Label("Promotion : ");

        // Add text Field
        TextField nameCourseField = new TextField();
        nameCourseField.setText(nameCourse);
        TextArea descriptionCourseField = new TextArea();
        descriptionCourseField.setText(descriptionCourse);
        TextField capacityField = new TextField();
        TextField nbTotalHourField = new TextField();
        nbTotalHourField.setText(Integer.toString(nbTotalHourCourse));
        nbTotalHourField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                nbTotalHourField.setText(newValue.replaceAll("[^\\d]", ""));
        });
        
        ComboBox<PromotionType> promoComboBox = new ComboBox<PromotionType>();
        promoComboBox.setItems(promoNames);
        promoComboBox.getSelectionModel().select(1);
        
        //grid pane
        GridPane gridUpdateCourse = new GridPane();
        gridUpdateCourse.setHgap(10);
        gridUpdateCourse.setVgap(10);
        gridUpdateCourse.setPadding(new Insets(10,10,10,10));

        //Hbox
        HBox nameCourseHb = new HBox();
        HBox descriptionCourseHb = new HBox();
        HBox nbTotalHourCourseHb = new HBox();
        HBox referentTeacherCourseHb = new HBox();
        HBox promoHb = new HBox();

        // add form in hbox
        nameCourseHb.getChildren().addAll(nameCourseLabel, nameCourseField);
        descriptionCourseHb.getChildren().addAll(descriptionCourseLabel, descriptionCourseField);
        nbTotalHourCourseHb.getChildren().addAll(nbTotalHourLabel, nbTotalHourField);
        promoHb.getChildren().addAll(promoLabel, promoComboBox);

         //add hbox in gridpane
        gridUpdateCourse.add(nameCourseHb, 1, 1);
        gridUpdateCourse.add(descriptionCourseHb, 1, 2);
        gridUpdateCourse.add(nbTotalHourCourseHb, 1, 3);
        gridUpdateCourse.add(promoHb, 1, 4);

        //add gridpane in tab
        tabCourse.setContent(gridUpdateCourse);

        //add button

        Button okUpdate = new Button("Update");
        okUpdate.setPrefHeight(40);
        okUpdate.setDefaultButton(true);
        okUpdate.setPrefWidth(100);
        gridUpdateCourse.add(okUpdate, 0, 13, 1, 1);
        gridUpdateCourse.setHalignment(okUpdate, HPos.RIGHT);
        gridUpdateCourse.setMargin(okUpdate, new Insets(20, 0,20,0));

        Button cancelUpdate = new Button("Cancel");
        cancelUpdate.setPrefHeight(40);
        cancelUpdate.setDefaultButton(false);
        cancelUpdate.setPrefWidth(100);
        gridUpdateCourse.add(cancelUpdate, 2, 13, 1, 1);
        gridUpdateCourse.setHalignment(cancelUpdate, HPos.RIGHT);
        gridUpdateCourse.setMargin(cancelUpdate, new Insets(20, 0,20,0));

        okUpdate.setOnAction(event -> {
            if (nameCourseField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridUpdateCourse.getScene().getWindow(), "Form Error!", "Please enter a name ");
                return;
            }
            if (descriptionCourseField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridUpdateCourse.getScene().getWindow(), "Form Error!", "Please enter a description");
                return;
            }
            if (nbTotalHourField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridUpdateCourse.getScene().getWindow(), "Form Error!", "Please enter a total number of hour");
                return;
            }if (promoComboBox.getSelectionModel().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridUpdateCourse.getScene().getWindow(), "Form Error!", "Please enter a pormotion");
                return;
            }
            PromotionType promo= (PromotionType) promoComboBox.getSelectionModel().getSelectedItem();
            client.handleUpdateCourse(idCourse, nameCourseField.getText(), descriptionCourseField.getText(), Integer.parseInt(nbTotalHourField.getText()),userID, promo.getIdPromo());
            nameCourseField.setText("");
            descriptionCourseField.setText("");
            nbTotalHourField.setText("");
        });
        cancelUpdate.setOnAction(event -> {
            tabCourse.setContent(courseRead(tabCourse));
        });

        return gridUpdateCourse;
    }
}