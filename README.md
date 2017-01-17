**step 0: init workshop**
```
git checkout step0/init-workshop
```

**step 1: simple commands**
```
git merge origin/step1/exercice/simple-command
```

1. implement account registration command
 * write BankAccount_registerBankAccountTest.should_register_bank_account_with_success test and make it pass
 * write BankAccount_registerBankAccountTest.should_fail_registering_bank_account_with_already_used_id and make it pass
2. implement provisioning command
 * write BankAccount_provisionCreditTest.should_provision_credit_with_success test and make it pass
3. implement withdraw command
 * make the tests defined in BankAccount_withdrawCreditTest pass

```
git add .
git commit -m "step 1 solution"
```


**step 2: long running process**

* if you managed to make all the test pass:
```
git merge origin/step2/exercice/long-running-process
```

* if you want to continue from a clean solution
```
git checkout step2/solution/long-running-process
```

1. implement transfer initialization command
 * write BankAccount_transferInitializedTest.should_fail_initializing_transfer_when_destination_is_same_than_initializer test and make it pass
 * write BankAccount_transferInitializedTest.should_fail_initializing_transfer_when_credit_to_transfer_greater_than_available_credit and make it pass
 * write BankAccount_transferInitializedTest.should_initialize_transfer and make it pass
2. implement transfer reception command
 * make the tests defined in BankAccount_transferReceivedTest pass
3. implement transfer finalization command
 * make the tests defined in BankAccount_transferFinalizedTest pass
4. implement transfer cancellation command
 * make the tests defined in BankAccount_transferCanceledTest pass
5. implement the transfer process manager
 * write TransferProcessManagerTest.should_finalize_transfer test and make it pass
 * write TransferProcessManagerTest.should_cancel_transfer test and make it pass

```
git add .
git commit -m "step 2 solution"
```

**step 3: projection**
* if you managed to make all the test pass:
```
git merge origin/step3/exercice/projection
```

* if you want to continue from a clean solution
```
git checkout step3/solution/projection
```

1. implement the credit balance projection manager
 * make the tests defined in CreditBalanceProjectionManagerTest pass

```
git add .
git commit -m "step 3 solution"
```

**step 4: integration**
* if you managed to make all the test pass:
```
git merge origin/step4/exercice/integration
```

* if you want to continue from a clean solution
```
git checkout step4/solution/integration
```

1. follow the instruction defined in Server constructor
2. execute Server main method
3. execute SimulateOperation main method
4. open a terminal, go into `~/workspace/workshop-event-sourcing/application/target/classes` and execute `java com.zenika.ylegat.workshop.application.SimulateRead`
