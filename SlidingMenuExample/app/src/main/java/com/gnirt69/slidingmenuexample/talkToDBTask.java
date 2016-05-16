package com.gnirt69.slidingmenuexample;

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
    private String toUser;
    private String pwd;
    private String email;
    private String groupName;
    private String GID;
    private String[] values;
    private String[] keys;
    private String[] gnames;
    private String[] gids;
    private String[] gMembers;
    private String variableType;
    private String URL;
    private int requestType;
    private String variableName;
    private OnTalkToDBFinish listener;

    public talkToDBTask(OnTalkToDBFinish listener) {
        this.listener = listener;
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
                    storeKeys(object);
                    System.out.println("lägg in sharedpref här för nyckel!");
                    output = "TRUE";
                }
                else if (checkType(object).contains("GROUPDATA")) {
                    storeGroups(object);
                    output = "TRUE";
                }
                else if (checkType(object).contains("MEMBERS")) {
                    storeMembers(object);
                    output = "TRUE";
                }
                else if (checkType(object).contains("REQUESTSENT")) {
                    System.out.println("ReQuEsTSeNd");
                    output = "TRUE";
                }
            }


            System.out.println(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return output;
    }

    private void storeKeys(JSONObject object) {
        //skapa shared pref här!
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println(result);
        System.out.println(requestType);

        if(result.contains("TRUE")){
            listener.onTaskCompleted();
        }
        else{
            listener.onTaskFailed();
            System.out.println("nått gick snett!");
        }

    }

    private void runCommandOnRequest(){
        System.out.println("request:");
        System.out.println(username);
        System.out.println(toUser);
        System.out.println(GID);
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
                getGroups(username);
                break;
            case 9:
                setGroupMembers(GID);
                break;
            case 10:
                sendRequest(username,toUser,GID);
                break;
        }
    }

    private void addVariable(String username, String pwd, String valueName, String variableType) {
        String program = "addvariable.php?";

        this.URL = setupURLAddVariable(program, username,pwd,valueName,variableType);

    }

    private void login(String user, String pwd) {
        String program = "LogIn.php?";
        this.URL = setupURLBasic(user, pwd, program);
        System.out.println(URL);
        //setupConnection(new String[]{URL});
    }

    private void storeGroups(JSONObject object) {
        try {
            JSONArray jsonGroupNames = object.getJSONArray("GROUPNAME");
            JSONArray jsonGroupIds = object.getJSONArray("GROUPID");
            System.out.println(jsonGroupNames.getString(0));
            gnames = new String[jsonGroupNames.length()];
            gids = new String[jsonGroupIds.length()];

            for (int i = 0; i < jsonGroupNames.length(); i++) {
                gnames[i] = jsonGroupNames.getString(i);
                gids[i] = jsonGroupIds.getString(i);
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
    private void getGroups(String username) {
        this.URL = setupURLGetGroups(username);
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
    public void setRequestType(int requestType){
        this.requestType = requestType;
    }
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

    public String getPwd(){
        return this.pwd;
    }
    public String[] getValues(){
        return this.values;
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

    private String setupURLGetGroups(String username) {
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

}



