package bojankosta.xml_data.model;

import java.util.ArrayList;

public class Country {

    private String name;
    private ArrayList<User> userList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", userList=" + userList +
                '}';
    }
}
