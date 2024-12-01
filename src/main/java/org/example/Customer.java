package org.example;

public class Customer {

  private String name;
  private String surname;
  private String email;
  private CustomerType customerType;
  private final Account account;
  private double companyOverdraftDiscount = 1;

  public Customer(String name, String surname, String email, CustomerType customerType,
      Account account) {
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.customerType = customerType;
    this.account = account;
  }

  // use only to create companies
  public Customer(String name, String email, Account account, double companyOverdraftDiscount) {
    this.name = name;
    this.email = email;
    this.customerType = CustomerType.COMPANY;
    this.account = account;
    this.companyOverdraftDiscount = companyOverdraftDiscount;
  }

  public void withdraw(double sum, String currency) {
    validateCurrency(currency);
    processWithdrawal(sum);
  }

  private void validateCurrency(String currency) {
    if (!account.getCurrency().equals(currency)) {
      throw new RuntimeException("Can't withdraw " + currency);
    }
  }

  private void processWithdrawal(double sum) {
    WithdrawalStrategy withdrawalStrategy = createWithdrawalStrategy();
    withdrawalStrategy.withdraw(account, sum);
  }

  private WithdrawalStrategy createWithdrawalStrategy() {
    return account.getType().isPremium()
        ? new PremiumWithdrawalStrategy(customerType)
        : new StandardWithdrawalStrategy(customerType);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public CustomerType getCustomerType() {
    return customerType;
  }

  public void setCustomerType(CustomerType customerType) {
    this.customerType = customerType;
  }

  public String printCustomerDaysOverdrawn() {
    String fullName = name + " " + surname + " ";
    String accountDescription =
        "Account: IBAN: " + account.getIban() + ", Days Overdrawn: " + account.getDaysOverdrawn();
    return fullName + accountDescription;
  }

  public String printCustomerMoney() {
    String fullName = name + " " + surname + " ";
    String accountDescription = "";
    accountDescription += "Account: IBAN: " + account.getIban() + ", Money: " + account.getMoney();
    return fullName + accountDescription;
  }

  public String printCustomerAccount() {
    return "Account: IBAN: " + account.getIban() + ", Money: "
        + account.getMoney() + ", Account type: " + account.getType();
  }

  interface WithdrawalStrategy {
    void withdraw(Account account, double sum);
  }

  // Strategy interface for withdrawal
  class PremiumWithdrawalStrategy implements WithdrawalStrategy {
    private final CustomerType customerType;

    public PremiumWithdrawalStrategy(CustomerType customerType) {
      this.customerType = customerType;
    }

    @Override
    public void withdraw(Account account, double sum) {
      if (account.getMoney() < 0) {
        double overdraftFee = sum * account.overdraftFee();
        double finalAmount = customerType == CustomerType.COMPANY
            ? overdraftFee * companyOverdraftDiscount / 2
            : overdraftFee;

        account.setMoney(account.getMoney() - sum - finalAmount);
      } else {
        account.setMoney(account.getMoney() - sum);
      }
    }
  }

  // Strategy for Standard Accounts
  class StandardWithdrawalStrategy implements WithdrawalStrategy {
    private final CustomerType customerType;

    public StandardWithdrawalStrategy(CustomerType customerType) {
      this.customerType = customerType;
    }

    @Override
    public void withdraw(Account account, double sum) {
      if (account.getMoney() < 0) {
        double overdraftFee = sum * account.overdraftFee();
        double finalAmount = customerType == CustomerType.COMPANY
            ? overdraftFee * companyOverdraftDiscount
            : overdraftFee;

        account.setMoney(account.getMoney() - sum - finalAmount);
      } else {
        account.setMoney(account.getMoney() - sum);
      }
    }
  }
}
