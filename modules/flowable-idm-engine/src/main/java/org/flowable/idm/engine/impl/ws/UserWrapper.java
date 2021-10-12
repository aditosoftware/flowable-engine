package org.flowable.idm.engine.impl.ws;

public class UserWrapper {
    String id;
    String firstName;
    String lastName;
    String fullName;
    String tenantId;
    String[] groups;
    String[] privileges;
    String email;

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String[] getGroups() {
        return groups;
    }

    public String[] getPrivileges() {
        return privileges;
    }
}
