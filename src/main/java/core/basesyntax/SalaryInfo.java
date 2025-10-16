package core.basesyntax;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class SalaryInfo {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String LINE_SEP = System.lineSeparator();

    public String getSalaryInfo(String[] names, String[] data, String dateFrom, String dateTo) {
        if (dateFrom == null) {
            dateFrom = "";
        }
        if (dateTo == null) {
            dateTo = "";
        }
        String df = dateFrom.trim();
        String dt = dateTo.trim();

        LocalDate from;
        LocalDate to;
        try {
            from = LocalDate.parse(df, FORMATTER);
            to = LocalDate.parse(dt, FORMATTER);
        } catch (DateTimeParseException e) {
            return "Report for period " + df + " - " + dt + LINE_SEP;
        }

        if (names == null) {
            names = new String[0];
        }
        if (data == null) {
            data = new String[0];
        }

        StringBuilder report = new StringBuilder();
        report.append("Report for period ")
                .append(df)
                .append(" - ")
                .append(dt)
                .append(LINE_SEP);

        for (int i = 0; i < names.length; i++) {
            String name = names[i] == null ? "" : names[i].trim();
            long total = 0L;

            for (String entry : data) {
                if (entry == null || entry.trim().isEmpty()) {
                    continue;
                }
                String[] parts = entry.trim().split("\\s+");
                if (parts.length != 4) {
                    continue;
                }

                LocalDate entryDate;
                try {
                    entryDate = LocalDate.parse(parts[0].trim(), FORMATTER);
                } catch (DateTimeParseException e) {
                    continue;
                }

                if (entryDate.isBefore(from) || entryDate.isAfter(to)) {
                    continue;
                }

                String entryName = parts[1].trim();
                if (!entryName.equals(name)) {
                    continue;
                }

                int hours;
                int rate;
                try {
                    hours = Integer.parseInt(parts[2].trim());
                    rate = Integer.parseInt(parts[3].trim());
                } catch (NumberFormatException e) {
                    continue;
                }

                total += (long) hours * rate;
            }

            report.append(name).append(" - ").append(total);
            if (i < names.length - 1) {
                report.append(LINE_SEP);
            }
        }

        return report.toString();
    }
}
