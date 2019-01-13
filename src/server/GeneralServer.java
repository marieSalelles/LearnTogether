package server;

import com.lloseng.ocsf.server.ConnectionToClient;
import com.lloseng.ocsf.server.OriginatorMessage;
import common.ChatIF;
import Types.*;
import com.lloseng.ocsf.server.ObservableOriginatorServer;
import server.DAO.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 */
public class GeneralServer implements Observer {

    private ObservableOriginatorServer comm;
    private ChatIF display;
    final private static int DEFAULT_PORT = 5555;
    private Date currentDate;
    private SimpleDateFormat dateFormat;
    private AbstractDAOFactory dao;


    /**
     * The useful constructor
     * @param port: the server address
     * @param display: the server display
     * @throws IOException
     */
    public GeneralServer(int port, ChatIF display) throws IOException {
        comm = new ObservableOriginatorServer(port);
        comm.addObserver(this);
        comm.listen();
        currentDate = new Date();
        dateFormat = new SimpleDateFormat(" '['HH:mm:ss']'");
        this.display=display;
        dao = new SQLServerFactory();
        dao.createDAOUser();
        dao.createDAODepartment();
        dao.createDAORoom();
        display.display("Server is running on port " + port);
    }

    /**
     * This method is called whenever a client sends something to the server.
     * @param msg: the object the client sent.
     * @param client: the original client.
     */
    public void handleMessageFromClient(Object msg, ConnectionToClient client) {
        if (msg instanceof String) {
            if (((String) msg).startsWith("#"))
                handleInstrFromClient(((String) msg).substring(1), client);
        }
    }

    /**
     * This method is used whenever a message sent by a client starts with '#'
     * @param instruction: what the client sent.
     * @param client: the original client.
     */
    public void handleInstrFromClient(String instruction, ConnectionToClient client) {
        if (instruction.startsWith("LOGIN")) {
            String[] ids = instruction.split(" ");
            handleLoginFromClient(ids[1], ids[2], client);
        }
        else if(instruction.startsWith("FIRSTCONN")){
            String[] creds = instruction.split(" ");
            handleFirstConnectionFromClient(creds[1], creds[2], client);
        }
        else if(instruction.startsWith("CREATEDEP")){
            String[] creds = instruction.split(" ");
            handleCreateDepartmentFromClient(creds[1], creds[2], creds[3], client);
        }
        else if(instruction.startsWith("UPDATEDEP")){
            String[] creds = instruction.split(" ");
            handleUpdateDepartmentFromClient(creds[1], creds[2], creds[3],creds[4], client);
        }
        else if(instruction.startsWith("DELETEDEP")){
            String[] creds = instruction.split(" ");
            handleDeleteDepartmentFromClient(creds[1], client);
        }
        else if (instruction.startsWith("CREATEROOM")){
            String[] attributes = instruction.split("-/-");
            handleCreateRoomFromClient(attributes[1], Integer.parseInt(attributes[2]), Integer.parseInt(attributes[3]), Boolean.parseBoolean(attributes[4]), Boolean.parseBoolean(attributes[5]),attributes[6], client);
        }else if (instruction.startsWith("DELETEROOM")){
            String[] attributes = instruction.split("-/-");
            handleDeleteRoomFromClient(Integer.parseInt(attributes[1]), client);
        }
        else if (instruction.startsWith("GETROOMS")){
            handleListRoomsFromClient(client);
        }
        else if(instruction.startsWith("SENDMSGTOCLIENT")){
            String[] attributes = instruction.split("-/-");
            handleSendMessageToClient(Integer.parseInt(attributes[2]), attributes[1],attributes[3], client);
        }
        else if(instruction.startsWith("RETRIEVECONVERSATION")){
            String[] attributes = instruction.split(" ");
            handleReadConversation(Integer.parseInt(attributes[1]), attributes[2], client);
        }
        else if(instruction.startsWith("GETUSER")) {
        	String[] attributes = instruction.split(" ");
        	
        }
    }




    /**
     * This method is used to send a client a response of a #LOGIN demand.
     * @param isConnected : true if the connection succeeded, false otherwise
     * @param id          : the user ID. value -1 if not connected.
     * @param role        : the user role. Value null if not connected.
     * @param client : the original client.
     */
    public void sendToClientLogin(boolean isConnected, int id, String role, ConnectionToClient client) {
        try {
            if (isConnected) {
                client.sendToClient("#LOGON TRUE " + id + " " + role);
            } else {
                client.sendToClient("#LOGON FALSE -1 null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to send a client a response of a #FIRSTCONN demand.
     * @param hasSucceeded: true id the first connection was a success, false otherwise.
     * @param client: the original client.
     */
    public void sendToClientFirstConn(boolean hasSucceeded, ConnectionToClient client){
        try{
            if(hasSucceeded){
                client.sendToClient("#FIRSTCONN SUCCESS");
            }else{
                client.sendToClient("#FIRSTCONN FAILURE");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This method is used when a client wants to connect.
     * It needs to check in the database to see if credentials matches or not.
     * @param login : the email the client provided.
     * @param password: the password the client provided.
     * @param client: the original client.
     */
    public void handleLoginFromClient(String login, String password, ConnectionToClient client) {
        int userID = dao.getUserDAO().readDAOUserByLogin(login, password);
        if (userID != -1) {
            checkLogin(userID, client);
            client.setInfo("email", login);
            client.setInfo("id", userID);
        } else {
            sendToClientLogin(false, -1, null, client);
        }
    }

    /**
     * This method is used when a user wants to first connect to the application.
     * @param login: the email the client provided.
     * @param password: the (new) password the client provided.
     * @param client: the original client.
     */
    public void handleFirstConnectionFromClient(String login, String password, ConnectionToClient client){
        if(dao.getUserDAO().isPdwNull(login))
            if (dao.getUserDAO().setNewPwd(login, password)) {
                sendToClientFirstConn(true, client);
                return;
            }
        sendToClientFirstConn(false, client);

    }

    /**
     * This method is called when a user has the right credentials and connected.
     * It checks in the DB and grabs the user ID and its role.
     * It always sends true on the login, as it has already been checked.
     * @param id: the user ID
     * @param client: the client from which it originated.
     */
    public void checkLogin(int id, ConnectionToClient client) {
        UserType user = dao.getUserDAO().readDAOUser(id);
        sendToClientLogin(true, user.getId(), user.getRole(), client);
    }

    /**
     * @param name
     * @param refTeacherID
     * @param descriptionDep
     * @param client
     */
    public void handleCreateDepartmentFromClient(String name, String refTeacherID, String descriptionDep, ConnectionToClient client) {
        int result = dao.getDepartmentDAO().createDepartment(name,refTeacherID,descriptionDep);

        String mess;
        if (result == 1){
            mess = "#CREATEDEPARTMENT Success";
        }
        else{
            mess = "#CREATEDEPARTMENT Failure";
        }
        try {
            client.sendToClient(mess);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param idDep
     * @param name
     * @param refTeacherID
     * @param client
     */
    public void handleUpdateDepartmentFromClient(String idDep, String name, String refTeacherID, String descriptionDep, ConnectionToClient client) {
        int idDepart=Integer.parseInt(idDep);
        int result = dao.getDepartmentDAO().updateDepartment(idDepart,name,refTeacherID,descriptionDep);

        String mess;
        if (result == 1){
            mess = "#CREATEDDEPARTMENT Success";
        }
        else{
            mess = "#CREATEDEPARTMENT Failure";
        }
        try {
            client.sendToClient(mess);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param idDep
     * @param client
     */
    public void handleDeleteDepartmentFromClient(String idDep, ConnectionToClient client) {
        int idDepart=Integer.parseInt(idDep);
        int result = dao.getDepartmentDAO().deleteDepartment(idDepart);

        String mess;
        if (result == 1){
            mess = "#DELETEDDEPARTMENT Success";
        }
        else{
            mess = "#DELETEDDEPARTMENT Failure";
        }
        try {
            client.sendToClient(mess);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param idDep
     * @param client
     */
    public void handleReadDepartmentFromClient(String idDep, ConnectionToClient client) {
        int idDepart=Integer.parseInt(idDep);
        int result = dao.getDepartmentDAO().readDepartment(idDepart);

        String mess;
        if (result == 1){
            mess = "#READDEPARTMENT Success";
        }
        else{
            mess = "#READDEPARTMENT Failure";
        }
        try {
            client.sendToClient(mess);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method delegates to the dao the room creation and interprets the result of the insert. At the end, ta message is sending to the client.
     * @param name : room name
     * @param capacity : room capacity
     * @param building : room building number
     * @param hasProjector : if the room has a projector
     * @param hasComputer : if th room has computers
     * @param description : small description of the room
     */
    private void handleCreateRoomFromClient(String name, int capacity, int building, boolean hasProjector, boolean hasComputer, String description, ConnectionToClient client){
        int result = dao.getRoomDAO().createRoom(name, capacity, building, hasProjector, hasComputer, description);

        String mess;
        if (result == 1){
            mess = "#CREATEDROOM Success";
        }
        else{
            mess = "#CREATEDROOM Failure";
        }
        try {
            client.sendToClient(mess);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method delegates to the dao the research of the room
     */

    public void handleListRoomsFromClient(ConnectionToClient client){
       List<RoomType> rooms =  dao.getRoomDAO().searchAllRooms();

        try {
            client.sendToClient(rooms);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method delegates to the dao the deletion of room
     * @param id : room id
     * @param client : client who deletes the room
     */
    public void handleDeleteRoomFromClient(int id, ConnectionToClient client){
        int result = dao.getRoomDAO().deleteRoom(id);

        String mess;
        if (result == 1){
            mess = "#DELETEROOM Success" ;
        } else{
            mess = "#DELETEROOM Failure";
        }

        try{
            client.sendToClient(mess);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method retrieves a message sent by a client to another.
     * It then sends back to the original client, and to the receiver if it is connected.
     * @param senderID: the sender ID from the DB
     * @param receiverEmail: The email of the receiver.
     * @param messageContent: The message content.
     * @param client: The original client.
     */
    private void handleSendMessageToClient(int senderID, String receiverEmail, String messageContent, ConnectionToClient client) {
        int res = dao.getConversationDAO().storeMessage(senderID, receiverEmail, messageContent);
        try{
            if(res == 1){
                client.sendToClient("#MESSAGE SENT");
                Thread[] clients = comm.getClientConnections();
                for(Thread cli : clients){
                    if(((ConnectionToClient)cli).getInfo("email").equals(receiverEmail)) {
                        ((ConnectionToClient) cli).sendToClient("#MSGFORYOU-/-" + client.getInfo("email")+"-/-"+receiverEmail + "-/-" +dateFormat.format(currentDate) + "-/-" + messageContent);
                        continue;
                    }
                }
                client.sendToClient("#MSGFORYOU-/-"+client.getInfo("email")+"-/-"+receiverEmail  + "-/-" + dateFormat.format(currentDate) + "-/-" + messageContent);
            }else {
                client.sendToClient("#MESSAGE ERROR");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This function retrieves all the messages from a specific confirmation and then sends it to the asking client.
     * @param askingId: the ID of the asking client.
     * @param otherEmail: the other participant to the conversation.
     * @param client: the asking client.
     */
    private void handleReadConversation(int askingId, String otherEmail, ConnectionToClient client) {
        List<MessageType> conversationMessages = dao.getConversationDAO().retrieveConversation(askingId, otherEmail);
        try {
            client.sendToClient(conversationMessages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void handleReadUser(int idUser, ConnectionToClient client) {
    	
    }

    /**
     * @param message
     */
    public void checkSuccess(Boolean message) {
        // TODO implement here
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof OriginatorMessage) {
            if (((OriginatorMessage) arg).getOriginator() == null) {
                if (((OriginatorMessage) arg).getMessage().equals(ObservableOriginatorServer.SERVER_STARTED)) {
                    this.serverStarted();
                } else if (((OriginatorMessage) arg).getMessage().equals(ObservableOriginatorServer.SERVER_STOPPED))
                    this.serverStopped();
                else if (((OriginatorMessage) arg).getMessage().equals(ObservableOriginatorServer.SERVER_CLOSED))
                    this.serverClosed();
                else if (((OriginatorMessage) arg).getMessage().toString().contains(ObservableOriginatorServer.LISTENING_EXCEPTION))
                    this.listeningException(((OriginatorMessage) arg).getMessage());
            } else {
                if (((OriginatorMessage) arg).getMessage().equals(ObservableOriginatorServer.CLIENT_CONNECTED))
                    this.clientConnected(((OriginatorMessage) arg).getOriginator());
                else if (((OriginatorMessage) arg).getMessage().equals(ObservableOriginatorServer.CLIENT_DISCONNECTED))
                    this.clientDisconnected(((OriginatorMessage) arg).getOriginator());
                else if (((OriginatorMessage) arg).getMessage().toString().contains(ObservableOriginatorServer.CLIENT_EXCEPTION))
                    this.clientException(((OriginatorMessage) arg).getOriginator(), (Throwable) ((OriginatorMessage) arg).getMessage());
                else
                    this.handleMessageFromClient(((OriginatorMessage) arg).getMessage(), ((OriginatorMessage) arg).getOriginator());

            }

        }

    }

    private void clientException(ConnectionToClient originator, Throwable message) {
    }

    private void clientDisconnected(ConnectionToClient originator) {
    }
    private void clientConnected(ConnectionToClient originator) {
    }
    private void listeningException(Object message) {
    }

    private void serverStopped() {
    }

    private void serverStarted() {
    }
    private void serverClosed() {
    }


    public void handleMessageFromServerUI(String message) {
        display.display("No commands have been implemented yet.");
    }
}


