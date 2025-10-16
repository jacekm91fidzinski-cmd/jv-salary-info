package core.basesyntax;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SalaryInfo {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String LINE_SEP = System.lineSeparator();

    public String getSalaryInfo(String[] names, String[] data, String dateFrom, String dateTo) {
        LocalDate from = LocalDate.parse(dateFrom, FORMATTER);
        LocalDate to = LocalDate.parse(dateTo, FORMATTER);

        StringBuilder report = new StringBuilder();
        report.append("Report for period ")
                .append(dateFrom)
                .append(" - ")
                .append(dateTo)
                .append(LINE_SEP);

        for (String name : names) {
            long total = 0L;
            for (String entry : data) {
                if (entry == null || entry.trim().isEmpty()) {
                    continue;
                }
                String[] parts = entry.trim().split("\\s+");
                if (parts.length != 4) {
                    continue;
                }
                LocalDate entryDate = LocalDate.parse(parts[0], FORMATTER);
                if (entryDate.isBefore(from) || entryDate.isAfter(to)) {
                    continue;
                }
                String entryName = parts[1];
                if (!entryName.equals(name)) {
                    continue;
                }
                int hours;
                int rate;
                try {
                    hours = Integer.parseInt(parts[2]);
                    rate = Integer.parseInt(parts[3]);
                } catch (NumberFormatException e) {
                    continue;
                }
                total += (long) hours * rate;
            }
            report.append(name).append(" - ").append(total).append(LINE_SEP);
        }
        return report.toString();
    }
}
