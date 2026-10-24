package programLogic.model;

/**
 * Defines the lifecycle states available for a support ticket.
 */
public enum Status {
    PENDING, UNASSIGNED, INPROGRESS, RESOLVED, DELETED;

    /**
     * Converts a persisted ordinal value to a ticket status.
     *
     * @param number string containing the status ordinal
     * @return the corresponding status
     */
    public static Status parseInt(String number) {
        return Status.values()[Integer.parseInt(number)];
    }
}
