package programLogic.controllerCode;

import programLogic.model.Account;
import programLogic.model.Status;
import programLogic.model.Ticket;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class TicketDisplay {
    private TicketDisplay() { }

    public static String statusText(Status status) {
        if (status == null) return "";
        return switch (status) {
            case PENDING -> "Pending";
            case UNASSIGNED -> "Unassigned";
            case INPROGRESS -> "In Progress";
            case RESOLVED -> "Resolved";
            case DELETED -> "Deleted";
            case DRAFT -> "Draft";
        };
    }

    public static String number(Ticket ticket) {
        return "#" + Ticket.formatNumber(ticket.getId());
    }

    public static String person(Account account, String ifNone) {
        return account == null ? ifNone : account.getDisplayName();
    }

    public static String date(Date date) {
        return date == null ? "" : new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String dateTime(Date date) {
        return date == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }
}
