package com.gnirt69.slidingmenuexample;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class talkToDBTask extends AsyncTask<String, Void, String> {
    private String username;
    private final String PREF_FILE = "com.gnirt69.slidingmenuexample.PREFERENCE_FILE";
    private String toUser;
    private String pwd;
    private String email;
    private String share;
    private String groupName;
    private String GID;
    private String userRequest;
    private String requestAnswer;
    private String requestCallback;
    private String sendStatus;
    private String[] sharedVarName;
    private String[] sharedVarID;
    private String[] notSharedName;
    private String[] notSharedID;
    private JSONObject dataObject;
    private String[] values;
    private String[] keys;
    private String[] gnames;
    private String[] gids;
    private String[] gnames_request;
    private String[] gids_request;
    private String[] request_sender;
    private String[] gMembers;
    private String variableType;
    private String[] variablesType;
    private String[] variablesNames;
    private String[] variablesID;
    private String[] GroupVarNames;
    private String[] GroupVarIDs;
    private String[] VarOwner;
    private String URL;
    private int requestType;
    private String variableName;
    private OnTalkToDBFinish listener;
    private Context context;

    public talkToDBTask(OnTalkToDBFinish listener,Context context) {
        this.listener = listener;
        this.context = context;
    }
    @Override
    protected String doInBackground(String... params) {
        String response = "";
        String output = "";
        runCommandOnRequest();
        System.out.println("do In Background");
        //for (String url : params) {
        System.out.println(requestType);
        System.out.println(URL);
        response = getURLResponse(URL);
        //}
        try {
            JSONObject object = new JSONObject(response.toString());
            System.out.println(object.toString());
            if (!checkFail(object)) {
                System.out.println("check fail funkar");
                if (checkType(object).contains("LOGIN")) {
                    if (checkCorrectUser(object)) {
                        output = "TRUE";
                        System.out.println(output);
                        System.out.println("success");
                    }
                } else if (checkType(object).contains("NEW USER")) {
                    if (checkCorrectUser(object)) {
                        output = "TRUE";
                    }
                } else if (checkType(object).contains("VAL ADDED")) {
                    System.out.println("gick in i val added");
                    if (checkCorrectUser(object)) {
                        System.out.println("check correct user funkar");
                        output = "TRUE";
                    }
                } else if (checkType(object).contains("RETR DATA")) {
                    storeValues(object);
                    output = "TRUE";
                } else if (checkType(object).contains("VAR ADDED")) {
                    storeKeysLocal(object);
                    System.out.println("lägg in sharedpref här för nyckel!");
                    output = "TRUE";
                }
                else if (checkType(object).contains("GROUPDATA")) {
                    storeGroupsAndRequests(object);
                    output = "TRUE";
                } else if(checkType(object).contains("VARIABLES")){
                    storeVariables(object);
                    output = "TRUE";
                }
                else if (checkType(object).contains("MEMBERS")) {
                    storeMembers(object);
                    output = "TRUE";
                }
                else if (checkType(object).contains("REQUESTSENT")) {
                    setRequestStatus("REQUESTSENT");
                    output = "TRUE";
                }
                else if (checkType(object).contains("REQUESTEXIST")) {
                    setRequestStatus("REQUESTEXIST");
                    output = "TRUE";
                }
                else if (checkType(object).contains("NOTFOUND")) {
                    setRequestStatus("NOTFOUND");
                    output = "TRUE";
                }
                else if (checkType(object).contains("MEMBEREXIST")) {
                    setRequestStatus("MEMBEREXIST");
                    output = "TRUE";
                }
                else if (checkType(object).contains("ACCEPTREQUEST_SUCCESS")) {
                    setRequestCallback("ACCEPTREQUEST_SUCCESS");
                    output = "TRUE";
                }
                else if (checkType(object).contains("REJECTREQUEST_SUCCESS")) {
                    setRequestCallback("REJECTREQUEST_SUCCESS");
                    output = "TRUE";
                }
                else if (checkType(object).contains("ACCEPTREQUEST_FAIL")) {
                    setRequestCallback("ACCEPTREQUEST_FAIL");
                    output = "TRUE";
                }
                else if (checkType(object).contains("REJECTREQUEST_FAIL")) {
                    setRequestCallback("REJECTREQUEST_FAIL");
                    output = "TRUE";
                }
                else if (checkType(object).contains("REMOVEUSER")) {
                    output = "TRUE";
                }
                else if (checkType(object).contains("SENDEMAIL_SUCCESS")) {
                    sendStatus = "SENDEMAIL_SUCCESS";
                    output = "TRUE";
                }
                else if (checkType(object).contains("EMAIL_NOT_FOUND")) {
                    sendStatus = "EMAIL_NOT_FOUND";
                    output = "TRUE";
                }
                else if (checkType(object).contains("SHAREDVAR")) {
                    storeSharedNotSharedVar(object);
                    output = "TRUE";
                }
                else if (checkType(object).contains("GROUPVAR")) {
                    storeGroupVariables(object);
                    output = "TRUE";
                }else if(checkType(object).contains("ADDVAR_SUCCESS")){
                    output = "TRUE";
                }else if(checkType(object).contains("REMOVEVAR_SUCCESS")){
                    output = "TRUE";
                }
            }


            System.out.println(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return output;
    }
    private void storeVariables(JSONObject object) {
        try {
            JSONArray jsonVID = object.getJSONArray("VariableID");
            JSONArray jsonVName = object.getJSONArray("VariableName");
            JSONArray jsonVType = object.getJSONArray("VariableType");
            variablesID = new String[jsonVID.length()];
            variablesNames = new String[jsonVName.length()];
            variablesType = new String[jsonVType.length()];

            for (int i = 0; i < jsonVID.length(); i++) {
                variablesID[i] = jsonVID.getString(i);
                variablesNames[i] = jsonVName.getString(i);
                variablesType[i] = jsonVType.getString(i);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void storeKeysLocal(JSONObject object) {
       /* SharedPreferences shrdPref = context.getSharedPreferences(PREF_FILE,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shrdPref.edit();

        try {
            JSONArray jsonVID = object.getJSONArray("VariableID");
            JSONArray jsonVName = object.getJSONArray("VariableName");

            variablesID = new String[jsonVID.length()];
            variablesNames = new String[jsonVName.length()];
            for (int i = 0; i < jsonVID.length(); i++) {
                variablesID[i] = jsonVID.getString(i);
                variablesNames[i] = jsonVName.getString(i);
                editor.putString()


            }
            editor.putString()
        }catch (JSONException e) {
        e.printStackTrace();
        }
        */
    }
    @Override
    protected void onPostExecute(String result) {
        System.out.println(result);
        System.out.println(requestType);

        if(result.contains("TRUE")){

            listener.onTaskCompleted(requestType);
            System.out.println("result: "+result);
            System.out.println("values added");
        }
        else{
            listener.onTaskFailed();
            System.out.println("nått gick snett!");
        }

    }
    private void runCommandOnRequest(){
        switch (requestType) {
            case 1:
                login(username, pwd);
                System.out.println("försöker logga in");
                break;
            case 2:
                //email = intent.getStringExtra("email");
                createUser(username, pwd, email);
                break;
            case 3:
                sendValues(username, pwd, values, keys);
                System.out.println(requestType +" request");
                System.out.println("sending values");
                break;
            case 4:
                getValues(username, pwd);
                break;
            case 5:
                addVariable(username,pwd,variableName,variableType);
                break;
            case 6:
                createGroup(groupName, username);
                break;
            case 7:
                addUserToGroup(groupName, username);
                break;
            case 8:
                getGroupsAndRequests(username);
                break;
            case 9:
                setGroupMembers(GID);
                break;
            case 10:
                getUserVariables(username,pwd);
                break;
            case 11:
                sendRequest(username,toUser,GID);
                break;
            case 12:
                answerRequest(username,GID, requestAnswer);
                break;
            case 13:
                leaveGroup(username,GID);
                break;
            case 14:
                sendEmail(email);
                break;
            case 15:
                sharedNotSharedVar(username, GID);
                break;
            case 16:
                groupVariables(GID);
                break;
            case 17:
                setSharedNotShared(GID,variableType,share);
                break;
        }
    }

    private void setSharedNotShared(String gid, String variableType,String add) {
        String program = "editgroupvariables.php?";
        this.URL = setupURLeditGroupVariables(variableType,gid,add,program);
    }

    private void getUserVariables(String username,String pwd) {
        String program = "getuservariables.php?";
        this.URL = setupURLBasic(username,pwd,program);
    }
    private void addVariable(String username, String pwd, String valueName, String variableType) {
        String program = "addvariable.php?";

        this.URL = setupURLAddVariable(program, username,pwd,valueName,variableType);
        System.out.println(URL);

    }
    private void login(String user, String pwd) {
        String program = "LogIn.php?";
        this.URL = setupURLBasic(user, pwd, program);
        System.out.println(URL);
        //setupConnection(new String[]{URL});
    }
    private void storeGroupsAndRequests(JSONObject object) {
        try {
            JSONArray jsonGroupNames = object.getJSONArray("GROUPNAME");
            JSONArray jsonGroupIds = object.getJSONArray("GROUPID");
            JSONArray jsonGroupNamesRequests = object.getJSONArray("GROUPNAMEREQUESTS");
            JSONArray jsonGroupIDRequests = object.getJSONArray("GROUPIDREQUESTS");
            JSONArray jsonRequestSender = object.getJSONArray("REQUESTSENDER");
            gnames = new String[jsonGroupNames.length()];
            gids = new String[jsonGroupIds.length()];
            gnames_request = new String[jsonGroupNamesRequests.length()];
            gids_request = new String[jsonGroupIDRequests.length()];
            request_sender = new String[jsonRequestSender.length()];


            for (int i = 0; i < jsonGroupNames.length(); i++) {
                gnames[i] = jsonGroupNames.getString(i);
                gids[i] = jsonGroupIds.getString(i);
            }

            for (int i = 0; i < jsonGroupIDRequests.length(); i++){
                gnames_request[i] = jsonGroupNamesRequests.getString(i);
                gids_request[i] = jsonGroupIDRequests.getString(i);
                request_sender[i] = jsonRequestSender.getString(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void storeSharedNotSharedVar(JSONObject object) {
        try {
            JSONArray jsonSharedGroupN = object.getJSONArray("NAMESHARED");
            JSONArray jsonSharedGroupID  = object.getJSONArray("IDSHARED");
            JSONArray jsonNotSharedGroupN = object.getJSONArray("NAMENOTSHARED");
            JSONArray jsonNotSharedGroupID = object.getJSONArray("IDNOTSHARED");
            sharedVarName = new String[jsonSharedGroupN.length()];
            sharedVarID = new String[jsonSharedGroupID.length()];
            notSharedName = new String[jsonNotSharedGroupN.length()];
            notSharedID = new String[jsonNotSharedGroupID.length()];


            for (int i = 0; i < jsonSharedGroupN.length(); i++) {
                sharedVarName[i] = jsonSharedGroupN.getString(i);
                sharedVarID[i] = jsonSharedGroupID.getString(i);
            }

            for (int i = 0; i < jsonNotSharedGroupN.length(); i++){
                notSharedName[i] = jsonNotSharedGroupN.getString(i);
                notSharedID[i] = jsonNotSharedGroupID.getString(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void storeMembers(JSONObject object) {
        try {
            JSONArray jsonGroupNames = object.getJSONArray("GROUPMEMBERS");
            System.out.println(jsonGroupNames.getString(0));
            gMembers = new String[jsonGroupNames.length()];

            for (int i = 0; i < jsonGroupNames.length(); i++) {
                gMembers[i] = jsonGroupNames.getString(i);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void storeValues(JSONObject object) {
        try {
            this.dataObject = object.getJSONObject("DICT");
            /*
            JSONArray jsonKeys = object.getJSONArray("KEYS");
            JSONArray jsonValues = object.getJSONArray("VALUES");
            System.out.println(jsonKeys.getString(0));
            keys = new String[jsonKeys.length()];
            values = new String[jsonValues.length()];

            for (int i = 0; i < jsonKeys.length(); i++) {
                keys[i] = jsonKeys.getString(i);
                values[i] = jsonValues.getString(i);
            }
            System.out.println(values[0]);
            */
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void storeGroupVariables(JSONObject object) {
        try {
            JSONArray jsonVarNames = object.getJSONArray("VARNAME");
            JSONArray jsonVarIDs = object.getJSONArray("VARID");
            JSONArray jsonVarOwner = object.getJSONArray("VAROWNER");
            GroupVarNames = new String[jsonVarNames.length()];
            GroupVarIDs = new String[jsonVarIDs.length()];
            VarOwner = new String[jsonVarOwner.length()];


            for (int i = 0; i < jsonVarNames.length(); i++) {
                GroupVarNames[i] = jsonVarNames.getString(i);
                GroupVarIDs[i] = jsonVarIDs.getString(i);
                VarOwner[i] = jsonVarOwner.getString(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void createUser(String user, String pwd, String email) {
        this.URL = setupURLNewUser(user, pwd, email);
        //setupConnection(new String[]{URL});
    }
    private void createGroup(String groupName, String username) {
        this.URL = setupURLNewGroup(groupName,username);
        //setupConnection(new String[]{URL});
    }
    private void getGroupsAndRequests(String username) {
        this.URL = setupURLGetGroupsAndRequests(username);
        //setupConnection(new String[]{URL});
    }
    private void setGroupMembers(String GID) {
        this.URL = setupURLGetGroupMembers(GID);
        //setupConnection(new String[]{URL});
    }
    private void sendRequest(String fromUser, String toUser,String GID) {
        this.URL = setupURLsendRequest(fromUser, toUser, GID);
        //setupConnection(new String[]{URL});
    }
    private void addUserToGroup(String groupName, String username) {
        this.URL = setupURLAddUserToGroup(groupName,username);
        //setupConnection(new String[]{URL});
    }
    public void setVariableName(String variableName){
        this.variableName = variableName;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setToUser(String toUser){
        this.toUser = toUser;
    }
    public void setPwd(String pwd){
        this.pwd = pwd;
    }
    public void setRequestStatus(String status){
        this.userRequest = status;
    }
    public void setRequestCallback(String status){
        this.requestCallback = status;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setGname(String groupName){
        this.groupName = groupName;
    }
    public void setValues(String[] values){
        this.values = values;
    }
    public void setVariableType(String type){this.variableType = type;}
    public void setKeys(String[] keys){
        this.keys = keys;
    }
    public void setVariableID(String varID){this.variableType = varID;}
    public void setSharedStatus(String shared){this.share = shared;}
    public void setRequestType(int requestType){
        this.requestType = requestType;
    }
    public void setRequestAnswer(String requestAnswer){
        this.requestAnswer = requestAnswer;
    }
    public JSONObject getDataObject(){return this.dataObject;}
    public String[] getVariablesNames(){return this.variablesNames;}
    public String[] getVariablesTypes(){return this.variablesType;}
    public String[] getVariablesID(){return this.variablesID;}
    public String[] getSharedVarName(){return this.sharedVarName;}
    public String[] getSharedVarID(){return this.sharedVarID;}
    public String[] getNotSharedVarName(){return this.notSharedName;}
    public String[] getNotSharedVarID(){return this.notSharedID;}
    public void setGroupID(String GID){
        this.GID = GID;
    }
    public String getVariableName(){
        return this.variableName;
    }
    public String getVariableType() {return this.variableType;}
    public int getRequestType(){
        return this.requestType;
    }
    public String getUsername(){
        return this.username;
    }
    public String getSendStatus(){
        return this.sendStatus;
    }
    public String getRequestStatus(){
        return this.userRequest;
    }
    public String [] getGroupNamesRequest(){
        return this.gnames_request;
    }
    public String [] getGroupIDsRequest(){
        return this.gids_request;
    }
    public String [] getRequestSender(){
        return this.request_sender;
    }
    public String [] getGroupVarNames(){
        return this.GroupVarNames;
    }
    public String [] getGroupVarIDs(){
        return this.GroupVarIDs;
    }
    public String [] getVarOwner(){
        return this.VarOwner;
    }
    public String getPwd(){
        return this.pwd;
    }
    public String[] getValues(){
        return this.values;
    }
    public String getRequestCallback(){
        return this.requestCallback;
    }
    public String[] getKeys(){
        return this.keys;
    }
    public String[] getGroupNames(){
        return this.gnames;
    }
    public String[] getGroupIDs(){
        return this.gids;
    }
    public String[] getGroupMembers(){
        return this.gMembers;
    }
    private void sendValues(String user, String pwd, String[] values, String[] keys) {
        this.URL = setupURLValueSend(user, pwd, values, keys);
        //setupConnection(new String[]{URL});
    }
    private void answerRequest(String user, String GID, String requestAnswer) {
        this.URL = setupURLAnswerRequest(user, GID, requestAnswer);
        //setupConnection(new String[]{URL});
    }
    private void leaveGroup(String user, String GID) {
        this.URL = setupURLLeaveGroup(user, GID);
        //setupConnection(new String[]{URL});
    }
    private void sendEmail(String email) {
        this.URL = setupURLSendEmail(email);
        //setupConnection(new String[]{URL});
    }
    private void sharedNotSharedVar(String username, String GID) {
        this.URL = setupURLsharedNotSharedVar(username, GID);
        //setupConnection(new String[]{URL});
    }
    private void groupVariables(String GID) {
        this.URL = setupURLGroupVariables(GID);
        //setupConnection(new String[]{URL});
    }

    private void getValues(String user, String pwd) {
        String program = "getdata.php?";
        this.URL = setupURLBasic(user, pwd, program);
        //setupConnection(new String[]{URL});
    }
    private String checkType(JSONObject object) {
        try {
            System.out.println(object.getString("TYPE"));
            return object.getString("TYPE");

        } catch (JSONException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
    private Boolean checkCorrectUser(JSONObject object) {
        try {
            System.out.println(object.getString("DATA"));
            if (object.getString("DATA").contains("TRUE")) {
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    private Boolean checkFail(JSONObject object) {
        try {
            return object.getString("TYPE").contains("ERROR");
        } catch (JSONException e) {
            return false;
        }
    }
    private String getURLResponse(String url) {
        HttpURLConnection connection = null;
        String response = "";
        try {
            java.net.URL mainURL = new URL(url);
            connection = (HttpURLConnection) mainURL.openConnection();
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            if (connection == null) listener.onTaskFailed();
            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response += line;
                response += '\r';
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;


    }
    private void failureHandler() {

    }
    private String setupURLAddVariable(String program, String username, String password, String valueName, String varType) {
        String ipadress = "http://www.lifebyme.stsvt16.student.it.uu.se/php/";
        String url = "";

        try {
            url = ipadress + program + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            url += "&" + URLEncoder.encode("pwd", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            url += "&" + URLEncoder.encode("varname", "UTF-8") + "=" + URLEncoder.encode(valueName, "UTF-8");
            url += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(varType, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //System.out.println(url);
        return url;

    }
    private String setupURLValueSend(String username, String password, String[] values, String[] keys) {
        String ipadress = "http://www.lifebyme.stsvt16.student.it.uu.se/php/";
        String url = "";
        String program = "addval.php?";


        try {
            url = ipadress + program + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            url += "&" + URLEncoder.encode("pwd", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            for (int i = 0; i < values.length; i++) {
                url += "&data[";
                System.out.println("in to for");
                if (keys.length > 0) {
                    url += URLEncoder.encode(keys[i], "UTF-8");
                    url += "][";
                }
                url += "]=" + URLEncoder.encode(values[i], "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //System.out.println(url);
        return url;
    }
    private String setupURLBasic(String username, String password, String program) {

        String ipadress = "http://www.lifebyme.stsvt16.student.it.uu.se/php/";

        String data = null;
        try {
            data = ipadress + program + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("pwd", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            failureHandler();
        }

        return data;

    }
    private String setupURLNewUser(String username, String password, String email) {
        String ipadress = "http://www.lifebyme.stsvt16.student.it.uu.se/php/";
        String program = "NewUser.php?";
        String data = null;

        try {
            data = ipadress + program + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("pwd", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            failureHandler();
        }

        return data;
    }
    private String setupURLNewGroup(String groupName, String username) {
        String ipadress = "http://www.lifebyme.stsvt16.student.it.uu.se/php/";
        String program = "NewGroup.php?";
        String data = null;

        try {
            data = ipadress + program + URLEncoder.encode("gname", "UTF-8") + "=" + URLEncoder.encode(groupName, "UTF-8");
            data += "&" + URLEncoder.encode("uname", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            failureHandler();
        }

        return data;
    }
    private String setupURLAddUserToGroup(String groupName, String username) {
        String ipadress = "http://www.lifebyme.stsvt16.student.it.uu.se/php/";
        String program = "NewGroup.php?";
        String data = null;

        try {
            data = ipadress + program + URLEncoder.encode("gname", "UTF-8") + "=" + URLEncoder.encode(groupName, "UTF-8");
            data += "&" + URLEncoder.encode("uname", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            failureHandler();
        }

        return data;
    }
    private String setupURLGetGroupsAndRequests(String username) {
        String ipadress = "http://www.lifebyme.stsvt16.student.it.uu.se/php/";
        String program = "grouplist.php?";
        String data = null;

        try {
            data = ipadress + program + URLEncoder.encode("uname", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            failureHandler();
        }

        return data;
    }
    private String setupURLGetGroupMembers(String GID) {
        String ipadress = "http://www.lifebyme.stsvt16.student.it.uu.se/php/";
        String program = "showgroupmembers.php?";
        String data = null;

        try {
            data = ipadress + program + URLEncoder.encode("GID", "UTF-8") + "=" + URLEncoder.encode(GID, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            failureHandler();
        }

        return data;
    }
    private String setupURLsendRequest(String fromUser, String toUser,String GID) {
        String ipadress = "http://www.lifebyme.stsvt16.student.it.uu.se/php/";
        String program = "usersearch.php?";
        String data = null;

        try {
            data = ipadress + program + URLEncoder.encode("fuser", "UTF-8") + "=" + URLEncoder.encode(fromUser, "UTF-8");
            data += "&" + URLEncoder.encode("tuser", "UTF-8") + "=" + URLEncoder.encode(toUser, "UTF-8");
            data += "&" + URLEncoder.encode("GID", "UTF-8") + "=" + URLEncoder.encode(GID, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            failureHandler();
        }

        return data;
    }
    private String setupURLeditGroupVariables(String VID, String GID,String add,String program) {
        String ipadress = "http://www.lifebyme.stsvt16.student.it.uu.se/php/";
        String data = null;

        try {
            data = ipadress + program + URLEncoder.encode("VID", "UTF-8") + "=" + URLEncoder.encode(VID, "UTF-8");
            data += "&" + URLEncoder.encode("GID", "UTF-8") + "=" + URLEncoder.encode(GID, "UTF-8");
            data += "&" + URLEncoder.encode("add", "UTF-8") + "=" + URLEncoder.encode(add, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            failureHandler();
        }

        return data;
    }

    private String setupURLAnswerRequest(String user, String GID, String requestAnswer) {
        String ipadress = "http://www.lifebyme.stsvt16.student.it.uu.se/php/";
        String program = "requestanswer.php?";
        String data = null;

        try {
            data = ipadress + program + URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("GID", "UTF-8") + "=" + URLEncoder.encode(GID, "UTF-8");
            data += "&" + URLEncoder.encode("requestanswer", "UTF-8") + "=" + URLEncoder.encode(requestAnswer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            failureHandler();
        }

        return data;
    }
    private String setupURLLeaveGroup(String user, String GID) {
        String ipadress = "http://www.lifebyme.stsvt16.student.it.uu.se/php/";
        String program = "removeuser.php?";
        String data = null;

        try {
            data = ipadress + program + URLEncoder.encode("uname", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("GID", "UTF-8") + "=" + URLEncoder.encode(GID, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            failureHandler();
        }

        return data;
    }
    private String setupURLSendEmail(String email) {
        String ipadress = "http://www.lifebyme.stsvt16.student.it.uu.se/php/";
        String program = "sendnewpassword.php?";
        String data = null;

        try {
            data = ipadress + program + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            failureHandler();
        }

        return data;
    }
    private String setupURLsharedNotSharedVar(String username, String GID) {
        String ipadress = "http://www.lifebyme.stsvt16.student.it.uu.se/php/";
        String program = "mygroupvariables.php?";
        String data = null;

        try {
            data = ipadress + program + URLEncoder.encode("uname", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("GID", "UTF-8") + "=" + URLEncoder.encode(GID, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            failureHandler();
        }

        return data;
    }
    private String setupURLGroupVariables(String GID) {
        String ipadress = "http://www.lifebyme.stsvt16.student.it.uu.se/php/";
        String program = "groupvariablelist.php?";
        String data = null;

        try {
            data = ipadress + program + URLEncoder.encode("GID", "UTF-8") + "=" + URLEncoder.encode(GID, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            failureHandler();
        }

        return data;
    }





}



