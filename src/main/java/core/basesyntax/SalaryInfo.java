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
            StringBuilder headerOnly = new StringBuilder();
            headerOnly.append("Report for period ")
                    .append(df)   // use trimmed
                    .append(" - ")
                    .append(dt)   // use trimmed
                    .append(LINE_SEP);
            return headerOnly.toString();
        }

        if (names == null) {
            names = new String[0];
        }
        if (data == null) {
            data = new String[0];
        }

        StringBuilder report = new StringBuilder();
        // use trimmed dates in header to avoid stray spaces
        report.append("Report for period ")
                .append(df)
                .append(" - ")
                .append(dt)
                .append(LINE_SEP);

        for (String name : names) {
            long total = 0L;
            String targetName = name == null ? "" : name.trim();

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
                if (!entryName.equals(targetName)) {
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

            report.append(targetName).append(" - ").append(total).append(LINE_SEP);
        }

        return report.toString();
    }
}
