# ActivityMaster Test Suite

This document provides instructions for running the ActivityMaster test suite, which includes tests for the enterprise flow from creation to running internal updates.

## Test Classes

### TestEnterpriseFlow

The `TestEnterpriseFlow` class tests the complete flow from enterprise creation to running internal updates. It includes the following test methods:

1. `testEnterpriseCreation()`: Tests the creation of an enterprise.
2. `testInternalUpdates()`: Tests running internal updates for an existing enterprise.
3. `testCompleteFlow()`: Tests the complete flow from enterprise creation to running internal updates.

## Running the Tests

### Using JUnit in an IDE

1. Open the project in your IDE (e.g., IntelliJ IDEA, Eclipse).
2. Navigate to the test class (`TestEnterpriseFlow.java`).
3. Right-click on the class or a specific test method and select "Run" or "Debug".

### Using Maven

1. Open a terminal or command prompt.
2. Navigate to the project root directory.
3. Run the following command to run all tests:
   ```
   mvn test
   ```
4. To run a specific test class, use:
   ```
   mvn test -Dtest=TestEnterpriseFlow
   ```
5. To run a specific test method, use:
   ```
   mvn test -Dtest=TestEnterpriseFlow#testCompleteFlow
   ```

## Test Configuration

The tests use the `DefaultTestConfig` class as a JUnit extension for setup and teardown. This class:

1. Sets up the Guice context.
2. Configures Hazelcast.
3. Sets up the ActivityMasterConfiguration.
4. Finds or creates the test enterprise.

## Test Data

The tests use the following test data:

- Enterprise Name: `TestEnterprise` (from `DefaultEnterprise.TestEnterprise`)
- Admin Username: `admin`
- Admin Password: `admin`

## Test Flow

The complete test flow (`testCompleteFlow()`) includes the following steps:

1. Create the enterprise using `EnterpriseService.startNewEnterprise`.
2. Configure the ActivityMasterConfiguration with the enterprise name.
3. Get the systems for the enterprise using `ISystemsService.getActivityMaster`.
4. Get the security identity token using `ISystemsService.getSecurityIdentityToken`.
5. Load systems for the enterprise using `ActivityMasterService.loadSystems`.
6. Load updates for the enterprise using `EnterpriseService.loadUpdates`.
7. Get all systems using `ActivityMasterConfiguration.getAllSystems`.
8. Log the names of all systems.

## Additional Resources

For a comprehensive list of services, client components, and entity classes, see the [additional-components-list.md](additional-components-list.md) file.