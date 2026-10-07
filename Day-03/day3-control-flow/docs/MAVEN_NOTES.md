# Day 3 — Apache Maven Architecture & Build Lifecycle Notes

Apache Maven is a declarative project management and comprehension tool primarily used for Java applications. It manages dependencies, enforces standardized project directory layouts, and coordinates automated builds.

---

## 1. Maven Standard Directory Layout

Maven enforces convention over configuration. By adhering to standard directory structures, developers do not need to configure where source files, resources, or tests live.

```text
day3-control-flow/
├── pom.xml                                ← Project Object Model configuration
├── README.md                              ← Module documentation
├── .gitignore                             ← Ignores target/ and IDE artifacts
│
├── src/
│   ├── main/
│   │   ├── java/                          ← Application production source code
│   │   │   └── com/auction/
│   │   │       ├── AuctionConsoleApp.java
│   │   │       ├── AuctionConstants.java
│   │   │       ├── Auction.java
│   │   │       ├── Bid.java
│   │   │       └── User.java
│   │   └── resources/                     ← Application runtime configuration files
│   │
│   └── test/
│       ├── java/                          ← Unit and integration test source code
│       │   └── com/auction/
│       │       └── AuctionConsoleAppTest.java
│       └── resources/                     ← Test fixtures and mock data
│
└── docs/                                  ← Training and architecture notes
    ├── CONTROL_FLOW_NOTES.md
    ├── MAVEN_NOTES.md
    └── TEST_CASES.md
```

---

## 2. Maven Coordinates & POM Basics

Every Maven artifact is uniquely identified by three primary coordinates:

1. **`groupId`**: The organization or package domain (e.g. `com.auction`).
2. **`artifactId`**: The unique identifier of this specific project/module (e.g. `day3-control-flow`).
3. **`version`**: The release version of the component (e.g. `1.0-SNAPSHOT`, where SNAPSHOT represents an in-development build).

```xml
<groupId>com.auction</groupId>
<artifactId>day3-control-flow</artifactId>
<version>1.0-SNAPSHOT</version>
<packaging>jar</packaging>
```

---

## 3. Maven Build Lifecycle Phases

Maven executes builds through a strictly sequenced series of **lifecycle phases**:

```text
validate
   ↓
compile
   ↓
test
   ↓
package
   ↓
install
```

### Detailed Phase Explanations

| Phase | Description | Example Command |
| :--- | :--- | :--- |
| **`validate`** | Validates the project structure, checks POM syntax, and verifies all required information is available. | `mvn validate` |
| **`compile`** | Compiles production source code (`src/main/java`) into class files in `target/classes`. | `mvn compile` |
| **`test`** | Compiles test source code (`src/test/java`) and executes automated unit tests (e.g., JUnit 5). | `mvn test` |
| **`package`** | Takes compiled bytecode and packages it into its distributable format (e.g., `.jar`). | `mvn package` |
| **`install`** | Installs the generated package into your local repository (`~/.m2/repository`) for use by local projects. | `mvn install` |
| **`clean`** | Deletes build output (`target/` directory) to guarantee a completely fresh, uncorrupted build. | `mvn clean` |

> **Key Rule of Maven Lifecycles:**
> Calling any phase automatically executes all preceding phases. For example, running `mvn package` automatically runs `validate`, `compile`, and `test` first.

---

## 4. Dependencies & Dependency Scopes

Dependencies are third-party libraries downloaded automatically from Maven Central.

In Day 3, we add JUnit 5 Jupiter for unit testing:

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.11.4</version>
    <scope>test</scope>
</dependency>
```

### Why `<scope>test</scope>`?
The `test` scope indicates that JUnit 5 is only needed when compiling and executing test classes in `src/test/java`. It is **never bundled** into the final production JAR package, keeping the deployable artifact small and clean.

---

## 5. Maven Profiles (`dev` and `prod`)

Maven profiles allow targeting different environments by switching properties or plugin behaviors at build time.

### POM Configuration:

```xml
<profiles>
    <profile>
        <id>dev</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <environment>development</environment>
        </properties>
    </profile>

    <profile>
        <id>prod</id>
        <properties>
            <environment>production</environment>
        </properties>
    </profile>
</profiles>
```

### Profile Commands:
```bash
# Package with development profile (default)
mvn clean package -Pdev

# Package with production profile
mvn clean package -Pprod
```

---

## 6. Future Multi-Module Expansion Architecture

As the Online Auction System grows from a single training module into an enterprise architecture, Maven supports multi-module parent POM structures:

```text
online-auction-training/
│
├── pom.xml (Parent POM with <packaging>pom</packaging> and <modules>)
│
├── auth-service/           ← User authentication, role verification, JWT tokens
│   ├── pom.xml
│   └── src/
│
├── auction-service/        ← Listing creation, seller management, moderation
│   ├── pom.xml
│   └── src/
│
├── bid-service/            ← Real-time bidding engine, anti-sniping, proxy bids
│   ├── pom.xml
│   └── src/
│
└── settlement-service/     ← Winner closing, escrow payouts, delivery tracking
    ├── pom.xml
    └── src/
```

For Day 3, the project is self-contained in `day3-control-flow` while remaining architecturally prepared for future decomposition.
