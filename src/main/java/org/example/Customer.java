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

  public void withdraw(double sum, Currency currency) {
    validateCurrency(currency);
    processWithdrawal(sum);
  }

  private void validateCurrency(Currency currency) {
    if (!account.getBalance().getCurrency().equals(currency)) {
      throw new RuntimeException("Can't withdraw " + currency);
    }
  }

  private void processWithdrawal(double sum) {
    WithdrawalStrategy withdrawalStrategy = createWithdrawalStrategy();
    withdrawalStrategy.withdraw(account, sum);
  }

  private WithdrawalStrategy createWithdrawalStrategy() {
    return account.isPremium()
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
    String accountDescription = account.getCustomerDaysOverdrawnInfo();
    return fullName + accountDescription;
  }

  public String printCustomerMoney() {
    String fullName = name + " " + surname + " ";
    String accountDescription = account.getCustomerMoneyInfo();
    return fullName + accountDescription;
  }

  public String printCustomerAccount() {
    return account.getCustomerAccountInfo();
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
      if (account.getBalance().getAmount() < 0) {
        double overdraftFee = sum * account.overdraftFee();
        double finalAmount = customerType == CustomerType.COMPANY
            ? overdraftFee * companyOverdraftDiscount / 2
            : overdraftFee;

        account.getBalance().setAmount(account.getBalance().getAmount() - sum - finalAmount);
      } else {
        account.getBalance().setAmount(account.getBalance().getAmount() - sum);
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
      if (account.getBalance().getAmount() < 0) {
        double overdraftFee = sum * account.overdraftFee();
        double finalAmount = customerType == CustomerType.COMPANY
            ? overdraftFee * companyOverdraftDiscount
            : overdraftFee;

        account.getBalance().setAmount(account.getBalance().getAmount() - sum - finalAmount);
      } else {
        account.getBalance().setAmount(account.getBalance().getAmount() - sum);
      }
    }
  }
}
