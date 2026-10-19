package programLogic.model;

public enum Status {
    PENDING, UNASSIGNED, INPROGRESS, RESOLVED, DELETED;

    public static Status parseInt(String number) {
        return Status.values()[Integer.parseInt(number)];
    }
}
