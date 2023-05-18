## Project domain

The idea of the User Profile API is to accept commands from various sources and update the user
profile according to them. The user profile
is a collection of properties associated with the `userId`. User profiles can be created on the fly
when user is first time seen by the
service.

User Profile API was born to live to decouple operations on the user profile and its storage from
the logic responsible for extracting the
data from the outside world.

## UserProfile Commands API

### API PATH : /commands/execute 
  This one api can perform following operation based on the given `type` in the request.

* `replace` to replace the value of a certain property of the user profile.
  ```json
  {
    "userId": "de4310e5-b139-441a-99db-77c9c4a5fada",
    "type": "replace",
    "properties": {
      "currentGold": 500,
      "currentGems": 800
    }
  }
  ```
* `increment` increments the current value in the profile. This command can also take negative
  numbers to decrement the value.
  * API : /commands/execute
  ```json
    {
      "userId": "de4310e5-b139-441a-99db-77c9c4a5fada",
      "type": "increment",
      "properties": {
        "battleFought": 10,
        "questsNotCompleted": -1
      }
    }
    ```

* `collect` adds values to a list of the values in the user profile property:
  ```json
  {
    "userId": "de4310e5-b139-441a-99db-77c9c4a5fada",
    "type": "collect",
    "properties": {
      "inventory": ["sword1", "sword2", "shield1"],
      "tools": ["tool1", "tool2"]
    }
  }
  ```
### BULK API : /commands/execute-bulk
This one api perform bulk operation of above commands based on the given `type` in the request.
```json
  [{
    "userId": "de4310e5-b139-441a-99db-77c9c4a5fada",
    "type": "replace",
    "properties": {
      "currentGold": 500,
      "currentGems": 800
    }
  },
  {
    "userId": "b12b12-b139-441a-99db-77c9c4a5fada",
    "type": "increment",
    "properties": {
      "battleFought": 500
    }
  }
]
  ```

### Add new commands
Changes to be done in following resources:
* CommandEnum.java : Constant of command type
* UserProfileService.java : Add method to process command on user profile. Call your method in `executeCommand`

## Project Specs

There is a project which is set up in the following way:

* Java 17 LTS (with support for [jenv](https://www.jenv.be), if it's your thing)
* [Gradle](https://gradle.org/)
  with [Shadow](https://imperceptiblethoughts.com/shadow/introduction/),
  [JaCoCo](https://docs.gradle.org/current/userguide/jacoco_plugin.html),
  and [test-logger](https://plugins.gradle.org/plugin/com.adarshr.test-logger)
  plugins
* [Dropwizard](https://www.dropwizard.io/en/latest/)
* [dropwizard-guicey](https://github.com/xvik/dropwizard-guicey) which
  brings [Guice](https://github.com/google/guice) power to Dropwizard
* [JUnit 5](https://junit.org/junit5/), [Mockito](https://site.mockito.org/), [AssertJ](https://assertj.github.io/doc/)
  and [JsonUnit](https://github.com/lukas-krecan/JsonUnit#assertj-integration)
* [GitHub action](https://docs.github.com/en/actions) to build, run checks and tests

These parts of the application are already implemented:

* General project setup is done
* `UserResource` is able to return the profile of the user.
* `UserProfileDaoInMemory` implements storage of the user profile in memory.
* There are examples of fixture usage, integration tests, and mocking.

## How to?

### Run tests

```shell
./gradlew test
```

### Run checks and tests

```shell
./gradlew check
```

### Run application

```shell
./gradlew run --args='server'
```

or without Gradle:

```shell
./gradlew build
java -jar ./build/libs/userprofile-api-1.0.0-SNAPSHOT.jar server
```

### How to make a request to a running application

```shell
curl http://localhost:8080/users/some-user-id/profile
```