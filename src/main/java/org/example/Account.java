package org.example;

public class Account {

  private String iban;
  private boolean isPremium;
  private final int daysOverdrawn;
  private Money balance;
  private Customer customer;

  public Account(boolean isPremium, int daysOverdrawn) {
    super();
    this.isPremium = isPremium;
    this.daysOverdrawn = daysOverdrawn;
  }

  public double bankcharge() {
    double result = 4.5;
    result += overdraftCharge();
    return result;
  }

  private double overdraftCharge() {
    if (isPremium) {
      double result = 10;
      if (getDaysOverdrawn() > 7) {
        result += (getDaysOverdrawn() - 7) * 1.0;
      }
      return result;
    } else {
      return getDaysOverdrawn() * 1.75;
    }
  }

  public double overdraftFee() {
    if (isPremium) {
      return 0.10;
    } else {
      return 0.20;
    }
  }

  public String getCustomerDaysOverdrawnInfo() {
    return "Account: IBAN: " + getIban() + ", Days Overdrawn: " + getDaysOverdrawn();
  }

  public String getCustomerMoneyInfo() {
    return "Account: IBAN: " + getIban() + ", Money: " + getBalance().getAmount();
  }

  public String getCustomerAccountInfo() {
    return "Account: IBAN: " + getIban() + ", Money: "
        + getBalance().getAmount() + ", Account type: " + (isPremium ? "premium" : "normal");
  }

  public int getDaysOverdrawn() {
    return daysOverdrawn;
  }

  public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public boolean isPremium() {
    return isPremium;
  }

  public void setPremium(boolean premium) {
    isPremium = premium;
  }

  public String printCustomer() {
    return customer.getName() + " " + customer.getEmail();
  }

  public Money getBalance() {
    return balance;
  }

  public void setBalance(Money balance) {
    this.balance = balance;
  }
}
