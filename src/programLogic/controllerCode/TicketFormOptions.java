package programLogic.controllerCode;

import javafx.util.StringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import programLogic.model.Account;
import programLogic.model.AccountManager;
import programLogic.model.AdminAccount;
import programLogic.model.Ticket;

import java.util.List;

public final class TicketFormOptions {
    private TicketFormOptions() { }

    public static final List<String> CATEGORIES = List.of(
            "Account", "Network / Wi-Fi", "Hardware", "Software", "Other");

    public static final List<Integer> URGENCIES = List.of(1, 2, 3);
    public static final int DEFAULT_URGENCY = 3;

    public static String describe(Account account) {
        return account.getDisplayName() + " (" + account.getUserName() + ")";
    }

    public static StringConverter<Integer> urgencyConverter() {
        return new StringConverter<Integer>() {
            @Override
            public String toString(Integer urgency) {
                if (urgency == null) return "";
                return switch (urgency) {
                    case 1 -> "1 - High";
                    case 2 -> "2 - Medium";
                    case 3 -> "3 - Low";
                    default -> String.valueOf(urgency);
                };
            }

            @Override
            public Integer fromString(String text) {
                return null;
            }
        };
    }

    public static <T extends Account> StringConverter<T> accountConverter() {
        return new StringConverter<T>() {
            @Override
            public String toString(T account) {
                return account == null ? "" : describe(account);
            }

            @Override
            public T fromString(String text) {
                return null;
            }
        };
    }

    public static final AdminAccount NO_ADMIN =
            new AdminAccount(-1, "unassigned", "", "Unassigned", "", "", "");

    public static ObservableList<AdminAccount> adminChoices() {
        ObservableList<AdminAccount> admins =
                FXCollections.observableArrayList(AccountManager.getInstance().getAllAdminAccounts());
        admins.add(0, NO_ADMIN);
        return admins;
    }

    public static StringConverter<AdminAccount> adminConverter() {
        return new StringConverter<AdminAccount>() {
            @Override
            public String toString(AdminAccount admin) {
                if (admin == null) return "";
                return admin == NO_ADMIN ? "Unassigned" : describe(admin);
            }

            @Override
            public AdminAccount fromString(String text) {
                return null;
            }
        };
    }

    public static AdminAccount adminOrNull(AdminAccount choice) {
        return (choice == null || choice == NO_ADMIN) ? null : choice;
    }

    public static String discardDraftMessage(int ticketNumber) {
        return "Discard draft #" + Ticket.formatNumber(ticketNumber) + "?\n\nIt will be deleted, and its ticket number can be reused by a new ticket.";
    }
}
