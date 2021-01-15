package org.DS_assignment2;

import java.io.Serializable;

public class Client implements Serializable {
    private String name;
    private String password;

    public Client(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
