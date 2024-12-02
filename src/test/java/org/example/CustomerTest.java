package org.example;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomerTest {

  @Test
  public void testWithdrawPersonWithNormalAccount() {
    Account account = getAccountByTypeAndMoney(false, 34.0);
    Customer customer = getPersonCustomer(account);
    customer.withdraw(10, Currency.EUR);
    assertEquals(24.0, account.getBalance().getAmount(), 0.001);
  }

  @Test
  public void testWithdrawPersonWithNormalAccountAndOverdraft() {
    Account account = getAccountByTypeAndMoney(false, -10.0);
    Customer customer = getPersonCustomer(account);
    customer.withdraw(10, Currency.EUR);
    assertEquals(-22.0, account.getBalance().getAmount(), 0.001);
  }

  @Test
  public void testWithdrawPersonWithPremiumAccount() {
    Account account = getAccountByTypeAndMoney(true, 34.0);
    Customer customer = getPersonCustomer(account);
    customer.withdraw(10, Currency.EUR);
    assertEquals(24.0, account.getBalance().getAmount(), 0.001);
  }

  @Test
  public void testWithdrawPersonWithPremiumAccountAndOverdraft() {
    Account account = getAccountByTypeAndMoney(true, -10.0);
    Customer customer = getPersonCustomer(account);
    customer.withdraw(10, Currency.EUR);
    assertEquals(-21.0, account.getBalance().getAmount(), 0.001);
  }

  @Test
  public void testWithdrawCompanyWithNormalAccount() {
    Account account = getAccountByTypeAndMoney(false, 34);
    Customer customer = getCompanyCustomer(account);
    customer.withdraw(10, Currency.EUR);
    assertEquals(24.0, account.getBalance().getAmount(), 0.001);
  }

  @Test
  public void testWithdrawCompanyWithNormalAccountAndOverdraft() {
    Account account = getAccountByTypeAndMoney(false, -10);
    Customer customer = getCompanyCustomer(account);
    customer.withdraw(10, Currency.EUR);
    assertEquals(-21.0, account.getBalance().getAmount(), 0.001);
  }

  @Test
  public void testWithdrawCompanyWithPremiumAccount() {
    Account account = getAccountByTypeAndMoney(true, 34);
    Customer customer = getCompanyCustomer(account);
    customer.withdraw(10, Currency.EUR);
    assertEquals(24.0, account.getBalance().getAmount(), 0.001);
  }

  @Test
  public void testWithdrawCompanyWithPremiumAccountAndOverdraft() {
    Account account = getAccountByTypeAndMoney(true, -10);
    Customer customer = getCompanyCustomer(account);
    customer.withdraw(10, Currency.EUR);
    assertEquals(-20.25, account.getBalance().getAmount(), 0.001);
  }

  @Test
  public void testPrintCustomerDaysOverdrawn() {
    Customer customer = getPersonWithAccount(false);
    assertEquals("danix dan Account: IBAN: RO023INGB434321431241, Days Overdrawn: 9",
        customer.printCustomerDaysOverdrawn()
    );
  }

  @Test
  public void testPrintCustomerMoney() {
    Customer customer = getPersonWithAccount(false);
    assertEquals("danix dan Account: IBAN: RO023INGB434321431241, Money: 34.0",
        customer.printCustomerMoney()
    );
  }

  @Test
  public void testPrintCustomerAccountNormal() {
    Customer customer = getPersonWithAccount(false);
    assertEquals("Account: IBAN: RO023INGB434321431241, Money: 34.0, Account type: normal",
        customer.printCustomerAccount()
    );
  }

  @Test
  public void testPrintCustomerAccountPremium() {
    Customer customer = getPersonWithAccount(true);
    assertEquals("Account: IBAN: RO023INGB434321431241, Money: 34.0, Account type: premium",
        customer.printCustomerAccount()
    );
  }

  private Customer getPersonWithAccount(boolean premium) {
    Account account = new Account(premium, 9);
    Customer customer = getPersonCustomer(account);
    account.setIban("RO023INGB434321431241");
    account.setBalance(new Money(34.0, Currency.EUR));
    return customer;
  }

  private Account getAccountByTypeAndMoney(boolean premium, double money) {
    Account account = new Account(premium, 9);
    account.setIban("RO023INGB434321431241");
    account.setBalance(new Money(money, Currency.EUR));
    return account;
  }

  private Customer getPersonCustomer(Account account) {
    Customer customer = new Customer("danix", "dan", "dan@mail.com", CustomerType.PERSON, account);
    account.setCustomer(customer);
    return customer;
  }

  private Customer getCompanyCustomer(Account account) {
    Customer customer = new Customer("company", "company@mail.com", account, 0.50);
    account.setCustomer(customer);
    return customer;
  }
}
