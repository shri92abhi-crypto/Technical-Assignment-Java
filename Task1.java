    public List<LoanAccount> getOverdueLoans(List<LoanAccount> accounts) {
        // FIX: Defect 1 - Initialized 'result' list as an ArrayList instead of null to prevent NullPointerException on result.add()
        List<LoanAccount> result = new ArrayList<>();
     
        for (LoanAccount account : accounts) {

            // FIX: Defect 2 - Added null check for account.getDueDate() to prevent NullPointerException on restructured accounts

            if (account.getDueDate() != null && account.getDueDate().before(new Date())) {

                // FIX: Defect 3 - Resolves the incorrect 'null' result for accounts with zero outstanding balance.
                // By initializing the array above, an account with balance <= 0 correctly skips addition, 
                // allowing the method to return a valid empty list ([]) instead of a broken null reference.

                if (account.getOutstandingBalance() > 0) {
                    result.add(account);
                }
            }
        }
        return result;
    }
}