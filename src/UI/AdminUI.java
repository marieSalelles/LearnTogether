package UI;

import Types.*;
import client.CoreClient;
import client.Groups.Department;
import client.Groups.Promotion;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.List;

//StaffAdmin@umontpellier.fr
//AdmStaff

/**
 * 
 */
public class AdminUI extends TeacherUI {

    private Scene principalAdminScene;
    protected TabPane tabPane;
    protected Tab tabProfile;
	protected Tab tabSchedule;
	protected Tab tabRecords;
	protected Tab tabDiary;
	protected Tab tabChat;
	protected Tab tabCourse;
	protected Tab tabRoom;
	protected Tab tabDepartment;
	protected Tab tabUser;
	protected ObservableList<RoomType> roomNames;
    protected ObservableList<DepartmentType> depNames;
    protected ObservableList<PromotionType> promoNames;
    protected ObservableList<ClassType> classNames;
    protected ObservableList<TeacherType> teacherNames;
    protected ObservableList<UserType> userNames;

    /**
     * Default constructor
     */
    public AdminUI(Stage primaryStage, String login, int id, CoreClient client) {
        super(primaryStage, login, id, client);
    }

    public Tab getTabRoom() {
        return tabRoom;
    }

    public Scene getPrincipalAdminScene() {
        return principalAdminScene;
    }

    public void setPrincipalAdminScene(Scene principalAdminScene) {
        this.principalAdminScene = principalAdminScene;
    }

    public void setRooms(List<RoomType> roomList) {
        roomNames.setAll(roomList);
    }
    public void setDepartment(List<DepartmentType> depList) {
        depNames.setAll(depList);
    }
    public void setPromo(List<PromotionType> promoList) {
        promoNames.setAll(promoList);
    }
    public void setClasses(List<ClassType> classes) {
        classNames.setAll(classes);
    }

    public void setTeacher(List<TeacherType> teacherList) {
        teacherNames.setAll(teacherList);
    }
    /**
     * This method create the principal admin scene
     */

    public Scene createPrincipalAdminScene(){
        this.primaryStage.setTitle("LearnTogether for Admins");
        BorderPane adminScene = new BorderPane();
        //Create the top bar
        VBox topBar = new VBox();
        HBox titleBar = new HBox();
        Text title = new Text("Learn Together -- Admin side");
        Text user = new Text("Connected as: " + login);
        title.setFont(Font.font("Cambria", 20));
        titleBar.getChildren().addAll(title, user);

        //Create the Tabs
        tabPane = new TabPane();

        tabProfile = createProfileTab();

        tabSchedule = new Tab();
        tabSchedule.setText("Schedule");
        tabSchedule.setClosable(false);

        tabRecords = createRecordTab();
        tabRecords.setText("Record");
        tabRecords.setClosable(false);

        tabDiary = new Tab();
        tabDiary.setText("Diary");
        tabDiary.setClosable(false);

        tabChat = createChatTab();
        tabRoom= tabRoom();
        tabCourse = tabCourse();
        tabDepartment = tabDepartment();
        tabUser = createTabUser();

        tabPane.getTabs().add(tabProfile);
        tabPane.getTabs().add(tabSchedule);
        tabPane.getTabs().add(tabRecords);
        tabPane.getTabs().add(tabDiary);
        tabPane.getTabs().add(tabChat);
        tabPane.getTabs().add(tabRoom);
        tabPane.getTabs().add(tabCourse);
        tabPane.getTabs().add(tabDepartment);

        HBox hbox = new HBox();
        hbox.getChildren().add(new Label("Tab" ));
        hbox.setAlignment(Pos.CENTER);
        tabProfile.setContent(hbox);

        topBar.getChildren().addAll(titleBar, tabPane);
        adminScene.setTop(topBar);
        titleBar.setSpacing(20);
        titleBar.setPadding(new Insets(15, 12, 15, 12));

        principalAdminScene = new Scene(adminScene, 900, 700);
        return principalAdminScene;
    }

    /**
     * This method create the room tab in the principal admin scene
     */
    
    
    protected Tab tabRoom(){

        Tab tabRoom = new Tab();
        tabRoom.setText("Room");
        tabRoom.setClosable(false);


        tabRoom.setContent(roomRead(tabRoom));
        return tabRoom;
    }

    protected GridPane roomRead(Tab tabRoom){

        /*add list of room*/
        client.getRooms();
        ListView<RoomType> list = new ListView<>();
        roomNames = FXCollections.observableArrayList();
        roomNames.addListener((ListChangeListener<RoomType>) c -> {
            list.setItems(roomNames);
        });

        Image addRoom = new Image(getClass().getResourceAsStream("images/icons8-plus-208.png"));
        ImageView addRoomView = new ImageView(addRoom);
        addRoomView.setFitHeight(15);
        addRoomView.setFitWidth(15);

        //create button add
        Button btnAddRoom = new Button("Add");
        btnAddRoom.setGraphic(addRoomView);//setting icon to button

        //delete button
        Image deleteRoom = new Image(getClass().getResourceAsStream("images/icons8-annuler-208.png"));
        ImageView deleteRoomView = new ImageView(deleteRoom);
        deleteRoomView.setFitHeight(12);
        deleteRoomView.setFitWidth(12);

        //create button delete
        Button btnDeleteRoom = new Button("Delete");
        btnDeleteRoom.setGraphic(deleteRoomView);//setting icon to button

        // add in hbox buttons and title
        HBox hboxButtonRoom = new HBox();

        Text title = new Text("Room : ");
        title.setFont(Font.font(20));
        hboxButtonRoom.getChildren().add(title);
        hboxButtonRoom.getChildren().add(btnAddRoom);
        hboxButtonRoom.getChildren().add(btnDeleteRoom);
        hboxButtonRoom.setSpacing(5);

        list.setItems(roomNames);
        System.out.println(roomNames);
        list.setPrefWidth(350);
        list.setPrefHeight(500);

        // left vbox
        VBox vboxListRoom = new VBox();
        vboxListRoom.getChildren().add(list);

        //grid pane
        GridPane gridRoomVisu = new GridPane();
        gridRoomVisu.setHgap(10);
        gridRoomVisu.setVgap(10);
        gridRoomVisu.setPadding(new Insets(10,10,10,10));

        gridRoomVisu.add(hboxButtonRoom, 1, 0);
        gridRoomVisu.add(vboxListRoom, 1, 2);

        /*creation of the info vbox of one room*/
        VBox vboxInfoRoom = new VBox();

        //title of column
        HBox hboxRoomInfo = new HBox();
        Text titleInfo = new Text("Room information : ");
        titleInfo.setFont(Font.font(20));
        hboxRoomInfo.getChildren().add(titleInfo);
        hboxRoomInfo.setAlignment(Pos.CENTER);

        // initialisation label and input
        HBox hboxnameRoomInfo = new HBox();
        HBox hboxcapacityRoomInfo = new HBox();
        HBox hboxbuildingRoomInfo = new HBox();
        HBox hboxprojectorRoomInfo = new HBox();
        HBox hboxcomputerRoomInfo = new HBox();
        HBox hboxdescRoomInfo = new HBox();
        Label nameLabel = new Label("Name of room : ");
        Label capacityLabel = new Label( "Capacity room : ");
        Label buildingLabel = new Label("Room building number : ");
        Label hasComputerLabel = new Label(" There are computers : ");
        Label hasProjectorLabel = new Label(" There is projector : ");
        Label descriptionLabel = new Label("Room description : ");
        Text name = new Text(" ");
        Text capacity = new Text(" ");
        Text building = new Text(" ");
        Text projector = new Text(" ");
        Text computer = new Text(" ");
        Text description = new Text(" ");


        hboxnameRoomInfo.getChildren().add(nameLabel);
        hboxcapacityRoomInfo.getChildren().add(capacityLabel);
        hboxbuildingRoomInfo.getChildren().add(buildingLabel);
        hboxcomputerRoomInfo.getChildren().add(hasComputerLabel);
        hboxprojectorRoomInfo.getChildren().add(hasProjectorLabel);
        hboxdescRoomInfo.getChildren().add(descriptionLabel);
        hboxnameRoomInfo.getChildren().add(name);
        hboxcapacityRoomInfo.getChildren().add(capacity);
        hboxbuildingRoomInfo.getChildren().add(building);
        hboxprojectorRoomInfo.getChildren().add(projector);
        hboxcomputerRoomInfo.getChildren().add(computer);
        hboxdescRoomInfo.getChildren().add(description);

        hboxnameRoomInfo.setAlignment(Pos.CENTER);
        hboxcapacityRoomInfo.setAlignment(Pos.CENTER);
        hboxbuildingRoomInfo.setAlignment(Pos.CENTER);
        hboxcomputerRoomInfo.setAlignment(Pos.CENTER);
        hboxprojectorRoomInfo.setAlignment(Pos.CENTER);
        hboxdescRoomInfo.setAlignment(Pos.CENTER);

        //create update button
        HBox hboxupdateButton = new HBox();
        Button btnUpdateRoom = new Button("Update");
        hboxupdateButton.getChildren().add(btnUpdateRoom);
        hboxupdateButton.setAlignment(Pos.CENTER);

        vboxInfoRoom.getChildren().addAll(hboxRoomInfo, hboxnameRoomInfo,hboxcapacityRoomInfo,hboxbuildingRoomInfo,hboxprojectorRoomInfo, hboxcomputerRoomInfo, hboxdescRoomInfo, hboxupdateButton);
        vboxInfoRoom.setSpacing(10);
        vboxInfoRoom.setPadding( new Insets(100, 0, 0, 75));


        btnAddRoom.setOnAction(event -> {
            createTabRoom(tabRoom);
        });

        btnUpdateRoom.setOnAction(event ->{
            SelectionModel<RoomType> selectedDeleteRoom = list.getSelectionModel();
            if (selectedDeleteRoom.getSelectedItem() != null) {
                updateTabRoom(tabRoom, selectedDeleteRoom.getSelectedItem().getName(), selectedDeleteRoom.getSelectedItem().getCapacity(),selectedDeleteRoom.getSelectedItem().getBuilding(), selectedDeleteRoom.getSelectedItem().isHasProjector(), selectedDeleteRoom.getSelectedItem().isHasComputer(),selectedDeleteRoom.getSelectedItem().getDescription(), selectedDeleteRoom.getSelectedItem().getId());
            }
        });

        btnDeleteRoom.setOnAction(event -> {
            SelectionModel<RoomType> selectedDeleteRoom = list.getSelectionModel();
            if (selectedDeleteRoom.getSelectedItem() != null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete the room?", ButtonType.YES, ButtonType.NO);
                alert.setHeaderText("Confirmation delete");
                Window win = gridRoomVisu.getScene().getWindow();
                alert.initOwner(win);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.NO) {
                    return;
                }
                if (alert.getResult() == ButtonType.YES) {
                    client.handleDeleteRoom(selectedDeleteRoom.getSelectedItem().getId());
                }
            }

        });

        list.setOnMouseClicked(event -> {
            gridRoomVisu.getChildren().remove(vboxInfoRoom);
            gridRoomVisu.add(vboxInfoRoom, 2, 2);
            System.out.println("clicked on " + list.getSelectionModel().getSelectedItem());
            SelectionModel<RoomType> selectedRoom = list.getSelectionModel();
            name.setText(selectedRoom.getSelectedItem().getName());
            capacity.setText(Integer.toString(selectedRoom.getSelectedItem().getCapacity()));
            building.setText(Integer.toString(selectedRoom.getSelectedItem().getBuilding()));
            if ( selectedRoom.getSelectedItem().isHasProjector()){
                projector.setText("Yes");
            } else {
                projector.setText("No");
            }
            if ( selectedRoom.getSelectedItem().isHasComputer()){
                computer.setText("Yes");
            } else {
                computer.setText("No");
            }
            description.setText(selectedRoom.getSelectedItem().getDescription());

        });



        return gridRoomVisu;
    }

    protected GridPane createTabRoom(Tab tabRoom){
        //return button
        Image returnRoom = new Image(getClass().getResourceAsStream("images/icons8-return.png"));
        ImageView returnRoomView = new ImageView(returnRoom);
        returnRoomView.setFitHeight(15);
        returnRoomView.setFitWidth(15);

        //create return button
        Button btnReturnRoom = new Button();
        btnReturnRoom.setGraphic(returnRoomView);//setting icon to button

        HBox returnBox = new HBox();

        returnBox.getChildren().add(btnReturnRoom);
        // labels
        Label nameLabel = new Label("Name of room : ");
        Label capacityLabel = new Label("Capacity : ");
        Label buildingLabel = new Label("Building : ");
        Label projLabel = new Label("There is a projector : ");
        Label compLabel = new Label("There are computers : ");
        Label descLabel = new Label("Room description : ");

        // Add text Field
        TextField nameField = new TextField();
        TextField capacityField = new TextField();
        capacityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                capacityField.setText(newValue.replaceAll("[^\\d]", ""));
        });
        TextField buildingField = new TextField();
        buildingField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                buildingField.setText(newValue.replaceAll("[^\\d]", ""));
        });

        RadioButton py = new RadioButton();
        py.setText("Yes");
        RadioButton pn = new RadioButton();
        pn.setText("No");

        ToggleGroup tp = new ToggleGroup();

        py.setToggleGroup(tp);
        pn.setToggleGroup(tp);
        tp.selectToggle(py);

        RadioButton cy = new RadioButton();
        cy.setText("Yes");
        RadioButton cn = new RadioButton();
        cn.setText("No");

        ToggleGroup tp2 = new ToggleGroup();

        cy.setToggleGroup(tp2);
        cn.setToggleGroup(tp2);
        tp2.selectToggle(cn);

        TextArea descriptionField = new TextArea();

        //grid pane
        GridPane gridRoom = new GridPane();
        gridRoom.setHgap(10);
        gridRoom.setVgap(10);
        gridRoom.setPadding(new Insets(10,10,10,10));

        //Hbox
        HBox nameRoom = new HBox();
        HBox capacityRoom = new HBox();
        HBox buildingRoom = new HBox();
        HBox projectorRoom = new HBox();
        HBox computerRoom = new HBox();
        HBox descriptionRoom = new HBox();

        // add form in hbox
        nameRoom.getChildren().addAll(nameLabel, nameField);
        capacityRoom.getChildren().addAll(capacityLabel, capacityField) ;
        buildingRoom.getChildren().addAll(buildingLabel, buildingField) ;
        projectorRoom.getChildren().addAll(projLabel, py, pn) ;
        computerRoom.getChildren().addAll(compLabel, cy, cn) ;
        descriptionRoom.getChildren().addAll(descLabel, descriptionField) ;

        //add hbox in gridpane
        gridRoom.add(returnBox,2, 0);

        gridRoom.add(nameRoom, 1, 0);
        gridRoom.add(capacityRoom, 1, 2);
        gridRoom.add(buildingRoom, 1, 5);
        gridRoom.add(projectorRoom, 1, 7);
        gridRoom.add(computerRoom, 1, 9);
        gridRoom.add(descriptionRoom, 1, 11);

        //add gridpane in tab
        tabRoom.setContent(gridRoom);

        //add button

        Button okCreate = new Button("Create");
        okCreate.setPrefHeight(40);
        okCreate.setDefaultButton(true);
        okCreate.setPrefWidth(100);
        gridRoom.add(okCreate, 0, 13, 1, 1);
        gridRoom.setHalignment(okCreate, HPos.RIGHT);
        gridRoom.setMargin(okCreate, new Insets(20, 0,20,0));

        Button cancelCreate = new Button("Cancel");
        cancelCreate.setPrefHeight(40);
        cancelCreate.setDefaultButton(false);
        cancelCreate.setPrefWidth(100);
        gridRoom.add(cancelCreate, 2, 13, 1, 1);
        gridRoom.setHalignment(cancelCreate, HPos.RIGHT);
        gridRoom.setMargin(cancelCreate, new Insets(20, 0,20,0));

        btnReturnRoom.setOnAction(event -> {
            tabRoom.setContent(roomRead(tabRoom));
        });

        okCreate.setOnAction(event -> {
            if (nameField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridRoom.getScene().getWindow(), "Form Error!", "Please enter room name");
                return;
            }
            if (capacityField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridRoom.getScene().getWindow(), "Form Error!", "Please enter room capacity");
                return;
            }
            if (buildingField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridRoom.getScene().getWindow(), "Form Error!", "Please enter room building");
                return;
            }
            boolean hasProjector;
            boolean hasComputer;
            if(((RadioButton) tp.getSelectedToggle()).getText().equals("Yes"))
                hasProjector = true;
            else
                hasProjector = false;

            if(((RadioButton) tp2.getSelectedToggle()).getText().equals("Yes"))
                hasComputer = true;
            else
                hasComputer = false;

            client.handleCreateRoom(nameField.getText(), Integer.parseInt(capacityField.getText()),Integer.parseInt(buildingField.getText()), hasProjector, hasComputer, descriptionField.getText());
            nameField.setText("");
            capacityField.setText("");
            buildingField.setText("");
            descriptionField.setText("");

        });

        cancelCreate.setOnAction(event -> {
            tabRoom.setContent(roomRead(tabRoom));
        });


        return gridRoom;
    }

    protected GridPane updateTabRoom(Tab tabRoom, String name, int capacity, int building, boolean hasProjector, boolean hasComputer, String description, int id){

        //return button
        Image returnRoom = new Image(getClass().getResourceAsStream("images/icons8-return.png"));
        ImageView returnRoomView = new ImageView(returnRoom);
        returnRoomView.setFitHeight(15);
        returnRoomView.setFitWidth(15);

        //create return button
        Button btnReturnRoom = new Button();
        btnReturnRoom.setGraphic(returnRoomView);//setting icon to button

        HBox returnBox = new HBox();

        returnBox.getChildren().add(btnReturnRoom);

        // labels
        Label nameLabel = new Label("Name of room: ");
        Label capacityLabel = new Label("Capacity: ");
        Label buildingLabel = new Label("Building: ");
        Label projLabel = new Label("There is a projector: ");
        Label compLabel = new Label("There are computers: ");
        Label descLabel = new Label("Room description: ");

        // Add text Field
        TextField nameField = new TextField();
        nameField.setText(name);
        TextField capacityField = new TextField();
        capacityField.setText(Integer.toString(capacity));
        capacityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                capacityField.setText(newValue.replaceAll("[^\\d]", ""));
        });
        TextField buildingField = new TextField();
        buildingField.setText(Integer.toString(building));
        buildingField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                buildingField.setText(newValue.replaceAll("[^\\d]", ""));
        });

        RadioButton py = new RadioButton();
        py.setText("Yes");
        RadioButton pn = new RadioButton();
        pn.setText("No");

        ToggleGroup tp = new ToggleGroup();

        py.setToggleGroup(tp);
        pn.setToggleGroup(tp);
        if (hasProjector){
            tp.selectToggle(py);
        } else {
            tp.selectToggle(pn);
        }


        RadioButton cy = new RadioButton();
        cy.setText("Yes");
        RadioButton cn = new RadioButton();
        cn.setText("No");

        ToggleGroup tp2 = new ToggleGroup();

        cy.setToggleGroup(tp2);
        cn.setToggleGroup(tp2);

        if (hasComputer){
            tp2.selectToggle(cy);
        } else {
            tp2.selectToggle(cn);
        }


        TextArea descriptionField = new TextArea();
        descriptionField.setText(description);

        //grid pane
        GridPane gridUpadateRoom = new GridPane();
        gridUpadateRoom.setHgap(10);
        gridUpadateRoom.setVgap(10);
        gridUpadateRoom.setPadding(new Insets(10,10,10,10));

        //Hbox
        HBox nameRoom = new HBox();
        HBox capacityRoom = new HBox();
        HBox buildingRoom = new HBox();
        HBox projectorRoom = new HBox();
        HBox computerRoom = new HBox();
        HBox descriptionRoom = new HBox();

        // add form in hbox
        nameRoom.getChildren().addAll(nameLabel, nameField);
        capacityRoom.getChildren().addAll(capacityLabel, capacityField) ;
        buildingRoom.getChildren().addAll(buildingLabel, buildingField) ;
        projectorRoom.getChildren().addAll(projLabel, py, pn) ;
        computerRoom.getChildren().addAll(compLabel, cy, cn) ;
        descriptionRoom.getChildren().addAll(descLabel, descriptionField) ;

        //add hbox in gridpane
        gridUpadateRoom.add(returnBox,2, 0);

        gridUpadateRoom.add(nameRoom, 1, 1);
        gridUpadateRoom.add(capacityRoom, 1, 2);
        gridUpadateRoom.add(buildingRoom, 1, 5);
        gridUpadateRoom.add(projectorRoom, 1, 7);
        gridUpadateRoom.add(computerRoom, 1, 9);
        gridUpadateRoom.add(descriptionRoom, 1, 11);

        //add gridpane in tab
        tabRoom.setContent(gridUpadateRoom);

        //add button

        Button okUpdate = new Button("Update");
        okUpdate.setPrefHeight(40);
        okUpdate.setDefaultButton(true);
        okUpdate.setPrefWidth(100);
        gridUpadateRoom.add(okUpdate, 0, 13, 1, 1);
        gridUpadateRoom.setHalignment(okUpdate, HPos.RIGHT);
        gridUpadateRoom.setMargin(okUpdate, new Insets(20, 0,20,0));

        Button cancelUpdate = new Button("Cancel");
        cancelUpdate.setPrefHeight(40);
        cancelUpdate.setDefaultButton(false);
        cancelUpdate.setPrefWidth(100);
        gridUpadateRoom.add(cancelUpdate, 2, 13, 1, 1);
        gridUpadateRoom.setHalignment(cancelUpdate, HPos.RIGHT);
        gridUpadateRoom.setMargin(cancelUpdate, new Insets(20, 0,20,0));

        btnReturnRoom.setOnAction(event -> {
            tabRoom.setContent(roomRead(tabRoom));
        });

        okUpdate.setOnAction(event -> {
            if (nameField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridUpadateRoom.getScene().getWindow(), "Form Error!", "Please enter room name");
                return;
            }
            if (capacityField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridUpadateRoom.getScene().getWindow(), "Form Error!", "Please enter room capacity");
                return;
            }
            if (buildingField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridUpadateRoom.getScene().getWindow(), "Form Error!", "Please enter room building");
                return;
            }
            boolean hasProjectorUp;
            boolean hasComputerUp;
            if(((RadioButton) tp.getSelectedToggle()).getText().equals("Yes"))
                hasProjectorUp = true;
            else
                hasProjectorUp = false;

            if(((RadioButton) tp2.getSelectedToggle()).getText().equals("Yes"))
                hasComputerUp = true;
            else
                hasComputerUp = false;

            client.handleUpdateRoom(id, nameField.getText(), Integer.parseInt(capacityField.getText()),Integer.parseInt(buildingField.getText()), hasProjectorUp, hasComputerUp, descriptionField.getText());
            nameField.setText("");
            capacityField.setText("");
            buildingField.setText("");
            descriptionField.setText("");

        });

        cancelUpdate.setOnAction(event -> {
            tabRoom.setContent(roomRead(tabRoom));
        });

        return gridUpadateRoom;
    }

    public GridPane setRoomTab(){
        return roomRead(tabRoom);
    }


    public void addUIControls(BorderPane borderPane){

    }


    /**
     * This method create the department tab in the principal admin scene
     */

    protected Tab tabDepartment(){

        Tab tabDepartment = new Tab();
        tabDepartment.setText("Students Groups");
        tabDepartment.setClosable(false);


        tabDepartment.setContent(departmentRead(tabDepartment));
        return tabDepartment;
    }

    protected GridPane departmentRead(Tab tabDepartment) {

        /*add list of Department*/
        client.getDepartment();
        ListView<DepartmentType> list = new ListView<>();
        depNames = FXCollections.observableArrayList();
        depNames.addListener((ListChangeListener<DepartmentType>) c -> {
            list.setItems(depNames);
        });

        Image addDepartment = new Image(getClass().getResourceAsStream("images/icons8-plus-208.png"));
        ImageView addDepView = new ImageView(addDepartment);
        addDepView.setFitHeight(15);
        addDepView.setFitWidth(15);

        //create button add
        Button btnAddDep = new Button("Add-Dep");
        btnAddDep.setGraphic(addDepView);//setting icon to button
        
        Image deleteDep = new Image(getClass().getResourceAsStream("images/icons8-annuler-208.png"));
        ImageView deleteDepView = new ImageView(deleteDep);
        deleteDepView.setFitHeight(12);
        deleteDepView.setFitWidth(12);

        //create button delete
        Button btnDeleteDep = new Button("Delete-Dep");
        btnDeleteDep.setGraphic(deleteDepView);//setting icon to button

        //create update button
        HBox hboxupdateButtonDep = new HBox();
        Button btnUpdateDep = new Button("Update-Dep");
        hboxupdateButtonDep.getChildren().add(btnUpdateDep);


        // add in hbox buttons and title
        HBox hboxButtonDep = new HBox();

        Text title = new Text("Department : ");
        hboxButtonDep.getChildren().addAll(title,btnAddDep,btnDeleteDep,btnUpdateDep);
        hboxButtonDep.setSpacing(5);

        list.setItems(depNames);
        System.out.println(depNames);
        list.setPrefWidth(300);
        list.setPrefHeight(500);

        // left vbox
        VBox vboxListDep = new VBox();
        vboxListDep.getChildren().add(list);

        //title of column
        HBox hboxDepInfo = new HBox();
        Text titleInfo = new Text("Department information : ");
        titleInfo.setFont(Font.font(20));
        hboxDepInfo.getChildren().add(titleInfo);
        hboxDepInfo.setAlignment(Pos.CENTER);

        // initialisation label and input
        HBox hboxnameDepInfo = new HBox();
        HBox hboxteacherDepInfo = new HBox();
        HBox hboxdescDepInfo = new HBox();
        Label nameLabel = new Label("Name of Departement : ");
        Label teacherLabel = new Label( "Referent teacher: ");
        Label descriptionLabel = new Label("Department description : ");
        Text name = new Text(" ");
        Text teacher = new Text(" ");
        Text description = new Text(" ");


        hboxnameDepInfo.getChildren().addAll(nameLabel,name);
        hboxteacherDepInfo.getChildren().addAll(teacherLabel,teacher);
        hboxdescDepInfo.getChildren().addAll(descriptionLabel,description);

        hboxnameDepInfo.setAlignment(Pos.CENTER);
        hboxteacherDepInfo.setAlignment(Pos.CENTER);
        hboxdescDepInfo.setAlignment(Pos.CENTER);

        VBox VBoxAffichageDep = new VBox();
        VBoxAffichageDep.getChildren().addAll(hboxnameDepInfo,hboxteacherDepInfo,hboxdescDepInfo);
        VBoxAffichageDep.setAlignment(Pos.CENTER_RIGHT);
        //

        //PROMOTION
        /*add list of Promotion*/
        client.getPromo();
        ListView<PromotionType> listPromo = new ListView<>();
        promoNames = FXCollections.observableArrayList();
        promoNames.addListener((ListChangeListener<PromotionType>) c -> {
            listPromo.setItems(promoNames);
        });


        Image addPromo = new Image(getClass().getResourceAsStream("images/icons8-plus-208.png"));
        ImageView addPromoView = new ImageView(addPromo);
        addPromoView.setFitHeight(15);
        addPromoView.setFitWidth(15);

        //create button add
        Button btnAddPromo= new Button("Add-Promo");
        btnAddPromo.setGraphic(addPromoView);//setting icon to button

        //delete button
        Image deletePromo = new Image(getClass().getResourceAsStream("images/icons8-annuler-208.png"));
        ImageView deletePromoView = new ImageView(deletePromo);
        deletePromoView.setFitHeight(12);
        deletePromoView.setFitWidth(12);

        //create button delete
        Button btnDeletePromo = new Button("Delete-Promo");
        btnDeletePromo.setGraphic(deletePromoView);//setting icon to button


        // add in hbox buttons and title
        HBox hboxButtonPromo = new HBox();
        Text titlePromo = new Text("Promotion : ");
        hboxButtonPromo.getChildren().addAll(titlePromo,btnAddPromo,btnDeletePromo);
        hboxButtonPromo.setSpacing(5);


        listPromo.setItems(promoNames);
        System.out.println(promoNames);
        listPromo.setPrefWidth(300);
        listPromo.setPrefHeight(500);

        // left vbox
        VBox vboxListPromo = new VBox();
        vboxListPromo.getChildren().add(listPromo);


        //title of column
        HBox hboxPromoInfo = new HBox();
        Text titleInfoPromo = new Text("Promotion information : ");
        titleInfoPromo.setFont(Font.font(20));
        hboxPromoInfo.getChildren().add(titleInfoPromo);
        hboxPromoInfo.setAlignment(Pos.CENTER);

        // initialisation label and input
        HBox hboxdepPromoInfo = new HBox();
        HBox hboxnamePromoInfo = new HBox();
        HBox hboxgraduationPromoInfo = new HBox();
        HBox hboxdescPromoInfo = new HBox();
        Label depLabelPromo = new Label("Referent Department : ");
        Label nameLabelPromo = new Label("Name of Promotion : ");
        Label graduationLabelPromo = new Label( "Graduation: ");
        Label descriptionLabelPromo = new Label("Promotion description : ");
        Text depPromo = new Text(" ");
        Text namePromo = new Text(" ");
        Text graduationPromo = new Text(" ");
        Text descriptionPromo = new Text(" ");

        hboxdepPromoInfo.getChildren().add(depLabelPromo);
        hboxnamePromoInfo.getChildren().add(nameLabelPromo);
        hboxgraduationPromoInfo.getChildren().add(graduationLabelPromo);
        hboxdescPromoInfo.getChildren().add(descriptionLabelPromo);

        hboxdepPromoInfo.setAlignment(Pos.CENTER);
        hboxnamePromoInfo.setAlignment(Pos.CENTER);
        hboxgraduationPromoInfo.setAlignment(Pos.CENTER);
        hboxdescPromoInfo.setAlignment(Pos.CENTER);
        //

        /*add list of Classes*/
        client.getClasses();
        ListView<ClassType> listClass = new ListView<>();
        classNames = FXCollections.observableArrayList();
        classNames.addListener((ListChangeListener<ClassType>) c -> {
            listClass.setItems(classNames);
        });

        Image addClass = new Image(getClass().getResourceAsStream("images/icons8-plus-208.png"));
        ImageView addClassView = new ImageView(addClass);
        addClassView.setFitHeight(15);
        addClassView.setFitWidth(15);

        //create button add
        Button btnAddClass= new Button("Add-Class");
        btnAddClass.setGraphic(addClassView);//setting icon to button

        //delete button
        Image deleteClass = new Image(getClass().getResourceAsStream("images/icons8-annuler-208.png"));
        ImageView deleteClassView = new ImageView(deleteClass);
        deleteClassView.setFitHeight(12);
        deleteClassView.setFitWidth(12);

        //create button delete
        Button btnDeleteClass = new Button("Delete-Class");
        btnDeleteClass.setGraphic(deleteClassView);//setting icon to button


        listClass.setItems(classNames);
        System.out.println(classNames);
        listClass.setPrefWidth(300);
        listClass.setPrefHeight(500);

        // left vbox
        VBox vboxListClass = new VBox();
        vboxListClass.getChildren().add(listClass);


        // add in hbox buttons and title
        HBox hboxButtonClass = new HBox();
        Text titleClass = new Text("Class : ");
        hboxButtonClass.getChildren().addAll(titleClass,btnAddClass,btnDeleteClass);
        hboxButtonClass.setSpacing(5);

        // add in hbox list
        HBox hboxListDep = new HBox();
        hboxListDep.getChildren().addAll(vboxListDep,vboxListPromo,vboxListClass);

        //title of column
        HBox hboxClassInfo = new HBox();
        Text titleInfoClass = new Text("Class information : ");
        titleInfoClass.setFont(Font.font(20));
        hboxClassInfo.getChildren().add(titleInfoClass);
        hboxClassInfo.setAlignment(Pos.CENTER);

        // initialisation label and input
        HBox hboxdepClassInfo = new HBox();
        HBox hboxnameClassInfo = new HBox();
        HBox hboxgraduationClassInfo = new HBox();
        HBox hboxdescClassInfo = new HBox();
        Label depLabelClass = new Label("Referent Promotion : ");
        Label nameLabelClass = new Label("Name of Class : ");
        Label descriptionLabelClass = new Label("Class description : ");
        Text DepClass = new Text(" ");
        Text nameClass = new Text(" ");
        Text graduationClass = new Text(" ");
        Text descriptionClass = new Text(" ");

        hboxdepClassInfo.getChildren().add(depLabelClass);
        hboxnameClassInfo.getChildren().add(nameLabelClass);
        hboxdescClassInfo.getChildren().add(descriptionLabelClass);

        hboxdepClassInfo.setAlignment(Pos.CENTER);
        hboxnameClassInfo.setAlignment(Pos.CENTER);
        hboxgraduationClassInfo.setAlignment(Pos.CENTER);
        hboxdescClassInfo.setAlignment(Pos.CENTER);
        //

        //grid pane
        GridPane gridDepV = new GridPane();
        gridDepV.setHgap(10);
        gridDepV.setVgap(10);
        gridDepV.setPadding(new Insets(10,10,10,10));

        //gridDepV.add(vboxListPromo,1,0);
        gridDepV.add(hboxButtonDep, 1, 2);
        gridDepV.add(hboxButtonPromo, 1, 3);
        gridDepV.add(hboxButtonClass, 1, 4);
        gridDepV.add(hboxListDep, 1, 5);



        /*creation of the info vbox of one department*/
        VBox vboxInfoDep = new VBox();
        vboxInfoDep.getChildren().addAll(hboxDepInfo, hboxnameDepInfo,hboxteacherDepInfo,hboxdescDepInfo);
        vboxInfoDep.setSpacing(10);
        vboxInfoDep.setPadding( new Insets(100, 0, 0, 75));

        /*creation of the info vbox of one promotion*/
        VBox vboxInfoPromo = new VBox();
        vboxInfoPromo.getChildren().addAll(hboxPromoInfo, hboxnamePromoInfo,hboxgraduationPromoInfo,hboxdescPromoInfo);
        vboxInfoPromo.setSpacing(10);
        vboxInfoPromo.setPadding( new Insets(100, 0, 0, 75));

        //Dep
        btnAddDep.setOnAction(event -> {
            createTabDepartment(tabDepartment);
        });


        btnDeleteDep.setOnAction(event -> {
            SelectionModel<DepartmentType> selectedDeleteDepartment = list.getSelectionModel();
            if (selectedDeleteDepartment.getSelectedItem() != null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"You are sure to delete this department", ButtonType.YES, ButtonType.NO);
                alert.setHeaderText("Confirmation delete");
                Window win = gridDepV.getScene().getWindow();
                alert.initOwner(win);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.NO) {
                    return;
                }
                if (alert.getResult() == ButtonType.YES) {
                    client.handleDeleteDepartment(selectedDeleteDepartment.getSelectedItem().getIdDepartment());;
                }
            }

        });

        btnUpdateDep.setOnAction(event ->{
            SelectionModel<DepartmentType> selectedDeleteDep = list.getSelectionModel();
            if (selectedDeleteDep.getSelectedItem() != null) {
                updateTabDep(tabDepartment, selectedDeleteDep.getSelectedItem().getNameDep(), selectedDeleteDep.getSelectedItem().getRefTeacher(),selectedDeleteDep.getSelectedItem().getDescriptionDep(), selectedDeleteDep.getSelectedItem().getIdDepartment());
            }
        });

        list.setOnMouseClicked(event -> {
            gridDepV.getChildren().remove(vboxInfoDep);
            gridDepV.add(vboxInfoDep, 2, 2);
            System.out.println("clicked on " + list.getSelectionModel().getSelectedItem());
            SelectionModel<DepartmentType> selectedDep = list.getSelectionModel();
            name.setText(selectedDep.getSelectedItem().getNameDep());
            teacher.setText(Integer.toString(selectedDep.getSelectedItem().getRefTeacher()));
            description.setText(selectedDep.getSelectedItem().getDescriptionDep());

        });

        //Promo
        btnAddPromo.setOnAction(event -> {
            createTabPromotion(tabDepartment);
        });


        btnDeletePromo.setOnAction(event -> {
            SelectionModel<PromotionType> selectedDeletePromo = listPromo.getSelectionModel();
            if (selectedDeletePromo.getSelectedItem() != null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"You are sure to delete a promo", ButtonType.YES, ButtonType.NO);
                alert.setHeaderText("Confirmation delete");
                Window win = gridDepV.getScene().getWindow();
                alert.initOwner(win);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.NO) {
                    return;
                }
                if (alert.getResult() == ButtonType.YES) {
                    return;
                }
            }

        });



        listPromo.setOnMouseClicked(event -> {
            gridDepV.getChildren().remove(vboxInfoPromo);
            gridDepV.add(vboxInfoPromo, 2, 2);
            System.out.println("clicked on " + listPromo.getSelectionModel().getSelectedItem());
            SelectionModel<PromotionType> selectedClass = listPromo.getSelectionModel();
            namePromo.setText(selectedClass.getSelectedItem().getNamePromo());
            graduationPromo.setText(Integer.toString(selectedClass.getSelectedItem().getGraduationPromo()));
            descriptionPromo.setText(selectedClass.getSelectedItem().getDescriptionPromo());
            depPromo.setText(Integer.toString(selectedClass.getSelectedItem().getRefDep()));

        });

        //Class
        btnAddClass.setOnAction(event -> {
            createTabDepartment(tabDepartment);
        });


        btnDeleteClass.setOnAction(event -> {
            SelectionModel<ClassType> selectedDeleteDepartment = listClass.getSelectionModel();
            if (selectedDeleteDepartment.getSelectedItem() != null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"You are sure to delete a department", ButtonType.YES, ButtonType.NO);
                alert.setHeaderText("Confirmation delete");
                Window win = gridDepV.getScene().getWindow();
                alert.initOwner(win);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.NO) {
                    return;
                }
                if (alert.getResult() == ButtonType.YES) {
                    return;
                }
            }

        });

        listClass.setOnMouseClicked(event -> {
            gridDepV.getChildren().remove(vboxInfoDep);
            gridDepV.add(vboxInfoDep, 2, 2);
            System.out.println("clicked on " + listClass.getSelectionModel().getSelectedItem());
            SelectionModel<ClassType> selectedClass = listClass.getSelectionModel();
            nameClass.setText(selectedClass.getSelectedItem().getClassName());
            descriptionClass.setText(selectedClass.getSelectedItem().getClassDescription());
        });

        return gridDepV;
    }

    protected GridPane createTabDepartment(Tab tabDepartment) {
        // labels
        Label nameLabel = new Label("Name of departement : ");
        Label teacherLabel = new Label("Referent teacher : ");
        Label descLabel = new Label("Description : ");

        // Add text Field
        TextField nameField = new TextField();
        TextArea descriptionField = new TextArea();

        client.getTeacher();
        ListView<TeacherType> listT = new ListView<>();
        teacherNames = FXCollections.observableArrayList();
        teacherNames.addListener((ListChangeListener<TeacherType>) c -> {
            listT.setItems(teacherNames);
        });

        ComboBox teacherComboBox = new ComboBox();
        teacherComboBox.setItems(teacherNames);
        teacherComboBox.getSelectionModel().select(1);


        //grid pane
        GridPane gridDep = new GridPane();
        gridDep.setHgap(10);
        gridDep.setVgap(10);
        gridDep.setPadding(new Insets(10, 10, 10, 10));

        //Hbox
        HBox nameDep = new HBox();
        HBox teacherDep = new HBox();
        HBox descriptionDep = new HBox();

        // add form in hbox
        nameDep.getChildren().addAll(nameLabel, nameField);
        teacherDep.getChildren().addAll(teacherLabel, teacherComboBox);
        descriptionDep.getChildren().addAll(descLabel, descriptionField);

        //add hbox in gridpane
        gridDep.add(nameDep, 1, 0);
        gridDep.add(teacherDep, 1, 2);
        gridDep.add(descriptionDep, 1, 5);

        //add gridpane in tab
        tabDepartment.setContent(gridDep);

        //add button

        Button okCreate = new Button("Create");
        okCreate.setPrefHeight(40);
        okCreate.setDefaultButton(true);
        okCreate.setPrefWidth(100);
        gridDep.add(okCreate, 0, 13, 1, 1);
        gridDep.setHalignment(okCreate, HPos.RIGHT);
        gridDep.setMargin(okCreate, new Insets(20, 0, 20, 0));

        Button cancelCreate = new Button("Cancel");
        cancelCreate.setPrefHeight(40);
        cancelCreate.setDefaultButton(false);
        cancelCreate.setPrefWidth(100);
        gridDep.add(cancelCreate, 2, 13, 1, 1);
        gridDep.setHalignment(cancelCreate, HPos.RIGHT);
        gridDep.setMargin(cancelCreate, new Insets(20, 0, 20, 0));

        okCreate.setOnAction(event -> {
            if (nameField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridDep.getScene().getWindow(), "Form Error!", "Please enter department name");
                return;
            }
            if (teacherComboBox.getSelectionModel().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridDep.getScene().getWindow(), "Form Error!", "Please enter referent teacher");
                return;
            }

            TeacherType teach= (TeacherType) teacherComboBox.getSelectionModel().getSelectedItem();
            client.handleCreateDepartment(nameField.getText(), teach.getId(), descriptionField.getText());
            nameField.setText("");
            descriptionField.setText("");

        });

        cancelCreate.setOnAction(event -> {
            tabDepartment.setContent(departmentRead(tabDepartment));
        });


        return gridDep;
    }

    protected GridPane updateTabDep(Tab tabDepartment,String nameDep, int refTeacher, String descDep, int idDep) {
        // labels
        Label nameLabel = new Label("Name of departement : ");
        Label teacherLabel = new Label("Referent teacher : ");
        Label descLabel = new Label("Description : ");

        // Add text Field
        TextField nameField = new TextField();
        TextArea descriptionField = new TextArea();
        nameField.setText(nameDep);
        descriptionField.setText(descDep);

        client.getTeacher();
        ListView<TeacherType> listT = new ListView<>();
        teacherNames = FXCollections.observableArrayList();
        teacherNames.addListener((ListChangeListener<TeacherType>) c -> {
            listT.setItems(teacherNames);
        });

        ComboBox teacherComboBox = new ComboBox();
        teacherComboBox.setItems(teacherNames);
        teacherComboBox.getSelectionModel().select(1);

        //return button
        Image returnDep = new Image(getClass().getResourceAsStream("images/icons8-return.png"));
        ImageView returnDepView = new ImageView(returnDep);
        returnDepView.setFitHeight(15);
        returnDepView.setFitWidth(15);

        //create return button
        Button btnReturnDep = new Button();
        btnReturnDep.setGraphic(returnDepView);//setting icon to button

        HBox returnBox = new HBox();
        returnBox.getChildren().add(btnReturnDep);

        //grid pane
        GridPane gridDep = new GridPane();
        gridDep.setHgap(10);
        gridDep.setVgap(10);
        gridDep.setPadding(new Insets(10, 10, 10, 10));

        //Hbox
        HBox nameDepUp = new HBox();
        HBox teacherDepUp = new HBox();
        HBox descriptionDepUp = new HBox();

        // add form in hbox
        nameDepUp.getChildren().addAll(nameLabel, nameField);
        teacherDepUp.getChildren().addAll(teacherLabel, teacherComboBox);
        descriptionDepUp.getChildren().addAll(descLabel, descriptionField);

        //add hbox in gridpane
        gridDep.add(nameDepUp, 1, 0);
        gridDep.add(teacherDepUp, 1, 2);
        gridDep.add(descriptionDepUp, 1, 5);
        gridDep.add(returnBox,2, 0);

        //add gridpane in tab
        tabDepartment.setContent(gridDep);

        //add button
        Button okUpdate = new Button("Update");
        okUpdate.setPrefHeight(40);
        okUpdate.setDefaultButton(true);
        okUpdate.setPrefWidth(100);
        gridDep.add(okUpdate, 0, 13, 1, 1);
        gridDep.setHalignment(okUpdate, HPos.RIGHT);
        gridDep.setMargin(okUpdate, new Insets(20, 0,20,0));

        Button cancelUpdate = new Button("Cancel");
        cancelUpdate.setPrefHeight(40);
        cancelUpdate.setDefaultButton(false);
        cancelUpdate.setPrefWidth(100);
        gridDep.add(cancelUpdate, 2, 13, 1, 1);
        gridDep.setHalignment(cancelUpdate, HPos.RIGHT);
        gridDep.setMargin(cancelUpdate, new Insets(20, 0,20,0));

        btnReturnDep.setOnAction(event -> {
            tabDepartment.setContent(departmentRead(tabDepartment));
        });

        okUpdate.setOnAction(event -> {
            if (nameField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridDep.getScene().getWindow(), "Form Error!", "Please enter department name");
                return;
            }if (teacherComboBox.getSelectionModel().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridDep.getScene().getWindow(), "Form Error!", "Please enter referent teacher");
                return;
            }if (descriptionField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridDep.getScene().getWindow(), "Form Error!", "Please enter department description");
                return;
            }
            TeacherType teach= (TeacherType) teacherComboBox.getSelectionModel().getSelectedItem();
            client.handleUpdateDepartment(idDep, nameField.getText(), teach.getId(), descriptionField.getText());
            nameField.setText("");
            descriptionField.setText("");
            tabDepartment.setContent(departmentRead(tabDepartment));

        });

        cancelUpdate.setOnAction(event -> {
            tabDepartment.setContent(departmentRead(tabDepartment));
        });


        return gridDep;
    }

    protected GridPane createTabPromotion(Tab tabDepartment) {

        // labels
        Label nameLabel = new Label("Name of promotion : ");
        Label depLabel = new Label("Referent Department : ");
        Label gradLabel = new Label("Promo's graduation : ");
        Label descLabel = new Label("Description : ");

        // Add text Field
        TextField nameField = new TextField();
        TextField gradField = new TextField();
        TextArea descriptionField = new TextArea();

        client.getDepartment();
        ListView<DepartmentType> listT = new ListView<>();
        depNames = FXCollections.observableArrayList();
        depNames.addListener((ListChangeListener<DepartmentType>) c -> {
            listT.setItems(depNames);
        });

        ComboBox depComboBox = new ComboBox();
        depComboBox.setItems(depNames);
        depComboBox.getSelectionModel().select(1);


        //grid pane
        GridPane gridPromo = new GridPane();
        gridPromo.setHgap(10);
        gridPromo.setVgap(10);
        gridPromo.setPadding(new Insets(10, 10, 10, 10));

        //Hbox
        HBox namePromo = new HBox();
        HBox refDep = new HBox();
        HBox gradPromo = new HBox();
        HBox descriptionPromo = new HBox();

        // add form in hbox
        namePromo.getChildren().addAll(nameLabel, nameField);
        refDep.getChildren().addAll(depLabel,depComboBox);
        gradPromo.getChildren().addAll(gradLabel,gradField);
        descriptionPromo.getChildren().addAll(descLabel, descriptionField);

        //add hbox in gridpane
        gridPromo.add(namePromo, 1, 0);
        gridPromo.add(refDep, 1, 2);
        gridPromo.add(gradPromo, 1, 5);
        gridPromo.add(descriptionPromo, 1, 7);

        //add gridpane in tab
        tabDepartment.setContent(gridPromo);

        //add button

        Button okCreate = new Button("Create");
        okCreate.setPrefHeight(40);
        okCreate.setDefaultButton(true);
        okCreate.setPrefWidth(100);
        gridPromo.add(okCreate, 0, 13, 1, 1);
        gridPromo.setHalignment(okCreate, HPos.RIGHT);
        gridPromo.setMargin(okCreate, new Insets(20, 0, 20, 0));

        Button cancelCreate = new Button("Cancel");
        cancelCreate.setPrefHeight(40);
        cancelCreate.setDefaultButton(false);
        cancelCreate.setPrefWidth(100);
        gridPromo.add(cancelCreate, 2, 13, 1, 1);
        gridPromo.setHalignment(cancelCreate, HPos.RIGHT);
        gridPromo.setMargin(cancelCreate, new Insets(20, 0, 20, 0));

        okCreate.setOnAction(event -> {
            if (nameField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPromo.getScene().getWindow(), "Form Error!", "Please enter promotion name");
                return;
            }
            if (gradField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPromo.getScene().getWindow(), "Form Error!", "Please enter graduation");
                return;
            }
            if (descriptionField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPromo.getScene().getWindow(), "Form Error!", "Please enter descritpion");
                return;
            }
            if (depComboBox.getSelectionModel().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPromo.getScene().getWindow(), "Form Error!", "Please enter referent department");
                return;
            }

            DepartmentType dep= (DepartmentType) depComboBox.getSelectionModel().getSelectedItem();
            client.handleCreatePromotion(nameField.getText(),descriptionField.getText(),Integer.parseInt(gradField.getText()),dep.getIdDepartment());
            nameField.setText("");
            gradField.setText("");
            descriptionField.setText("");

        });

        cancelCreate.setOnAction(event -> {
            tabDepartment.setContent(departmentRead(tabDepartment));
        });


        return gridPromo;
    }

    protected Tab tabPromo(){

        Tab tabPromo = new Tab();
        tabPromo.setClosable(false);
        tabPromo.setContent(showTabPromotion(tabPromo));
        return tabPromo;
    }

    protected GridPane showTabPromotion(Tab tabDepartment) {

        /*add list of Promotion*/
        client.getPromo();
        ListView<PromotionType> listPromo = new ListView<>();
        promoNames = FXCollections.observableArrayList();
        promoNames.addListener((ListChangeListener<PromotionType>) c -> {
            listPromo.setItems(promoNames);
        });

        Image addPromo = new Image(getClass().getResourceAsStream("images/icons8-plus-208.png"));
        ImageView addPromoView = new ImageView(addPromo);
        addPromoView.setFitHeight(15);
        addPromoView.setFitWidth(15);

        //create button add
        Button btnAddPromo = new Button("Add-Promo");
        btnAddPromo.setGraphic(addPromoView);//setting icon to button

        //delete button
        Image deletePromo = new Image(getClass().getResourceAsStream("images/icons8-annuler-208.png"));
        ImageView deletePromoView = new ImageView(deletePromo);
        deletePromoView.setFitHeight(12);
        deletePromoView.setFitWidth(12);

        //create button delete
        Button btnDeletePromo = new Button("Delete-Promo");
        btnDeletePromo.setGraphic(deletePromoView);//setting icon to button

        //create button add
        Button btnShowPromo = new Button("Show-Promo");

        //create button add
        Button btnReturnPromo = new Button("Cancel");

        // add in hbox buttons and title
        HBox hboxButtonPromo = new HBox();
        Text titlePromo = new Text("Promotion : ");
        hboxButtonPromo.getChildren().addAll(titlePromo, btnAddPromo, btnDeletePromo);
        hboxButtonPromo.setSpacing(5);


        listPromo.setItems(promoNames);
        System.out.println(promoNames);
        listPromo.setPrefWidth(300);
        listPromo.setPrefHeight(500);

        // left vbox
        VBox vboxListPromo = new VBox();
        vboxListPromo.getChildren().add(listPromo);


        //title of column
        HBox hboxPromoInfo = new HBox();
        Text titleInfoPromo = new Text("Promotion information : ");
        titleInfoPromo.setFont(Font.font(20));
        hboxPromoInfo.getChildren().add(titleInfoPromo);
        hboxPromoInfo.setAlignment(Pos.CENTER);

        // initialisation label and input
        HBox hboxdepPromoInfo = new HBox();
        HBox hboxnamePromoInfo = new HBox();
        HBox hboxgraduationPromoInfo = new HBox();
        HBox hboxdescPromoInfo = new HBox();
        Label depLabelPromo = new Label("Referent Department : ");
        Label nameLabelPromo = new Label("Name of Promotion : ");
        Label graduationLabelPromo = new Label("Graduation: ");
        Label descriptionLabelPromo = new Label("Promotion description : ");
        Text depPromo = new Text(" ");
        Text namePromo = new Text(" ");
        Text graduationPromo = new Text(" ");
        Text descriptionPromo = new Text(" ");

        hboxdepPromoInfo.getChildren().add(depLabelPromo);
        hboxnamePromoInfo.getChildren().add(nameLabelPromo);
        hboxgraduationPromoInfo.getChildren().add(graduationLabelPromo);
        hboxdescPromoInfo.getChildren().add(descriptionLabelPromo);

        hboxdepPromoInfo.setAlignment(Pos.CENTER);
        hboxnamePromoInfo.setAlignment(Pos.CENTER);
        hboxgraduationPromoInfo.setAlignment(Pos.CENTER);
        hboxdescPromoInfo.setAlignment(Pos.CENTER);

        /*creation of the info vbox of one promotion*/
        VBox vboxInfoPromo = new VBox();
        vboxInfoPromo.getChildren().addAll(hboxPromoInfo, hboxnamePromoInfo, hboxgraduationPromoInfo, hboxdescPromoInfo);
        vboxInfoPromo.setSpacing(10);
        vboxInfoPromo.setPadding(new Insets(100, 0, 0, 75));


        //grid pane
        GridPane gridPromoV = new GridPane();
        gridPromoV.setHgap(10);
        gridPromoV.setVgap(10);
        gridPromoV.setPadding(new Insets(10, 10, 10, 10));

        gridPromoV.add(hboxButtonPromo, 1, 2);
        gridPromoV.add(vboxListPromo, 1, 4);

        listPromo.setOnMouseClicked(event -> {
        gridPromoV.getChildren().remove(vboxInfoPromo);
        gridPromoV.add(vboxInfoPromo, 2, 2);
        System.out.println("clicked on " + listPromo.getSelectionModel().getSelectedItem());
        SelectionModel<PromotionType> selectedClass = listPromo.getSelectionModel();
        namePromo.setText(selectedClass.getSelectedItem().getNamePromo());
        graduationPromo.setText(Integer.toString(selectedClass.getSelectedItem().getGraduationPromo()));
        descriptionPromo.setText(selectedClass.getSelectedItem().getDescriptionPromo());
        depPromo.setText(Integer.toString(selectedClass.getSelectedItem().getRefDep()));});

        //Promo
        btnReturnPromo.setOnAction(event -> {
            createTabDepartment(tabDepartment);
        });

        return gridPromoV;
    }
    
    /**
     * 
     * @return
     */
    protected Tab createTabUser(){

        Tab tabUser = new Tab();
        tabUser.setText("Room");
        tabUser.setClosable(false);


        tabUser.setContent(roomUser(tabUser));
        return tabUser;
    }

    /**
     * 
     * @param tabUser
     * @return
     */
    protected GridPane roomUser(Tab tabUser){

        /*add list of users*/
        //client.getRooms();
        ListView<UserType> list = new ListView<>();
        userNames = FXCollections.observableArrayList();
        userNames.addListener((ListChangeListener<UserType>) c -> {
            list.setItems(userNames);
        });

        Image addUser = new Image(getClass().getResourceAsStream("images/icons8-plus-208.png"));
        ImageView addUserView = new ImageView(addUser);
        addUserView.setFitHeight(15);
        addUserView.setFitWidth(15);

        //create button add
        Button btnAddUser = new Button("Add");
        btnAddUser.setGraphic(addUserView);//setting icon to button

        //delete button
        Image deleteUser = new Image(getClass().getResourceAsStream("images/icons8-annuler-208.png"));
        ImageView deleteUserView = new ImageView(deleteUser);
        deleteUserView.setFitHeight(12);
        deleteUserView.setFitWidth(12);

        //create button delete
        Button btnDeleteUser = new Button("Delete");
        btnDeleteUser.setGraphic(deleteUserView);//setting icon to button

        // add in hbox buttons and title
        HBox hboxButtonUser = new HBox();

        Text title = new Text("Room : ");
        title.setFont(Font.font(20));
        hboxButtonUser.getChildren().add(title);
        hboxButtonUser.getChildren().add(btnAddUser);
        hboxButtonUser.getChildren().add(btnDeleteUser);
        hboxButtonUser.setSpacing(5);

        list.setItems(userNames);
        System.out.println(userNames);
        list.setPrefWidth(350);
        list.setPrefHeight(500);

        // left vbox
        VBox vboxListUser = new VBox();
        vboxListUser.getChildren().add(list);

        //grid pane
        GridPane gridUserVisu = new GridPane();
        gridUserVisu.setHgap(10);
        gridUserVisu.setVgap(10);
        gridUserVisu.setPadding(new Insets(10,10,10,10));

        gridUserVisu.add(hboxButtonUser, 1, 0);
        gridUserVisu.add(vboxListUser, 1, 2);

        /*creation of the info vbox of one user*/
        VBox vboxInfoUser = new VBox();

        //title of column
        HBox hboxUserInfo = new HBox();
        Text titleInfo = new Text("User information : ");
        titleInfo.setFont(Font.font(20));
        hboxUserInfo.getChildren().add(titleInfo);
        hboxUserInfo.setAlignment(Pos.CENTER);

        // initialisation label and input
        HBox hboxnameUserInfo = new HBox();
        HBox hboxbirthdateUserInfo = new HBox();
        HBox hboxemailUserInfo = new HBox();
        HBox hboxidUserInfo = new HBox();
        HBox hboxroleUserInfo = new HBox();
        Label nameLabel = new Label("Name: ");
        Label birthdateLabel = new Label( "Birthdate: ");
        Label emailLabel = new Label("Email: ");
        Label idLabel = new Label("ID: ");
        Label roleLabel = new Label("Role: ");
        Text name = new Text(" ");
        Text birthdate = new Text(" ");
        Text email = new Text(" ");
        Text id = new Text(" ");
        Text role = new Text(" ");


        hboxnameUserInfo.getChildren().add(nameLabel);
        hboxnameUserInfo.getChildren().add(name);
        hboxbirthdateUserInfo.getChildren().add(birthdateLabel);
        hboxbirthdateUserInfo.getChildren().add(birthdate);
        hboxemailUserInfo.getChildren().add(emailLabel);
        hboxemailUserInfo.getChildren().add(email);
        hboxidUserInfo.getChildren().add(idLabel);
        hboxidUserInfo.getChildren().add(id);
        hboxroleUserInfo.getChildren().add(roleLabel);
        hboxroleUserInfo.getChildren().add(role);

        hboxnameUserInfo.setAlignment(Pos.CENTER);
        hboxbirthdateUserInfo.setAlignment(Pos.CENTER);
        hboxemailUserInfo.setAlignment(Pos.CENTER);
        hboxidUserInfo.setAlignment(Pos.CENTER);
        hboxroleUserInfo.setAlignment(Pos.CENTER);

        //create update button
        HBox hboxupdateButton = new HBox();
        Button btnUpdateUser = new Button("Update");
        hboxupdateButton.getChildren().add(btnUpdateUser);
        hboxupdateButton.setAlignment(Pos.CENTER);

        vboxInfoUser.getChildren().addAll(hboxUserInfo, hboxnameUserInfo,hboxnameUserInfo,hboxbirthdateUserInfo,hboxemailUserInfo, hboxidUserInfo, hboxroleUserInfo, hboxupdateButton);
        vboxInfoUser.setSpacing(10);
        vboxInfoUser.setPadding( new Insets(100, 0, 0, 75));


        btnAddUser.setOnAction(event -> {
            createUser(tabUser);
        });

        btnUpdateUser.setOnAction(event ->{
            SelectionModel<UserType> selectedDeleteUser = list.getSelectionModel();
            if (selectedDeleteUser.getSelectedItem() != null) {
                updateUser(tabUser, selectedDeleteUser.getSelectedItem().getName(), selectedDeleteUser.getSelectedItem().getBirthDate(),selectedDeleteUser.getSelectedItem().getEmail(), selectedDeleteUser.getSelectedItem().getId(), selectedDeleteUser.getSelectedItem().getRole());
            }
        });

        btnDeleteUser.setOnAction(event -> {
            SelectionModel<UserType> selectedDeleteUser = list.getSelectionModel();
            if (selectedDeleteUser.getSelectedItem() != null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete the user?", ButtonType.YES, ButtonType.NO);
                alert.setHeaderText("Confirmation delete");
                Window win = gridUserVisu.getScene().getWindow();
                alert.initOwner(win);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.NO) {
                    return;
                }
                if (alert.getResult() == ButtonType.YES) {
                    client.handleDeleteRoom(selectedDeleteUser.getSelectedItem().getId());
                }
            }

        });

        list.setOnMouseClicked(event -> {
            gridUserVisu.getChildren().remove(vboxInfoUser);
            gridUserVisu.add(vboxInfoUser, 2, 2);
            System.out.println("clicked on " + list.getSelectionModel().getSelectedItem());
            SelectionModel<UserType> selectedUser = list.getSelectionModel();
            name.setText(selectedUser.getSelectedItem().getName());
            birthdate.setText(selectedUser.getSelectedItem().getName());
            email.setText(selectedUser.getSelectedItem().getEmail());
            id.setText(Integer.toString(selectedUser.getSelectedItem().getId()));
            role.setText(selectedUser.getSelectedItem().getRole());

        });

        return gridUserVisu;
    }

	private void createUser(Tab tabUser) {
		// TODO Auto-generated method stub
		
	}

	private void updateUser(Tab tabUser, String name, String birthDate, String email, int id, String role) {
		// TODO Auto-generated method stub
		
	}

 }
