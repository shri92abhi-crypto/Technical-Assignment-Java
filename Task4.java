public class ReportDAO {

    private DataSource dataSource;

    public List<ReportEntry> fetchMonthlyReport(String accountId, int month, int year) throws SQLException {
        List<ReportEntry> entries = new ArrayList<>();
        
        // FIX: Implemented Java 8 try-with-resources. Connection and PreparedStatement are declared here.
        // They will be closed automatically in the reverse order of their creation.
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM report_entries " +
                 "WHERE account_id = ? AND MONTH(entry_date) = ? " +
                 "AND YEAR(entry_date) = ?"
             )) {
             
            ps.setString(1, accountId);
            ps.setInt(2, month);
            ps.setInt(3, year);

            // FIX: Nested try-with-resources for ResultSet ensures it closes first (ResultSet -> PreparedStatement -> Connection)
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    entries.add(mapRow(rs));
                }
            }
        } // FIX: All resources (rs, ps, conn) are implicitly and safely closed here, resolving the connection pool exhaustion.

        return entries; 
    }
}