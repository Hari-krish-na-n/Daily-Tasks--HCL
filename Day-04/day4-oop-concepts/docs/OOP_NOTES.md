# Object-Oriented Programming (OOP) Notes - Online Auction System

This document explains the core Object-Oriented Programming (OOP) concepts implemented in Day 4 using the **Online Auction System** domain.

---

## 1. Class
A **class** is a blueprint or template from which individual objects are created. It defines the state (fields) and behavior (methods) common to all objects of that type.
- **Example**: `Auction`, `User`, `Seller`, `Bidder`, `Bid` in the `com.auction.model` package.

```java
public class Auction {
    // Fields and methods defined here
}
```

---

## 2. Object
An **object** is an instance of a class that holds actual state in memory and can perform actions defined by its class.
- **Example**: Creating a specific auction object for a laptop:
```java
Auction laptopAuction = new Auction(1001, "Laptop", seller, 50000.0, 55000.0);
```

---

## 3. Fields (Instance Variables)
**Fields** are variables declared inside a class that represent the state or properties of an object.
- **Example**: In `Auction.java`:
```java
private int auctionId;
private String productName;
private double startingPrice;
private double currentBid;
private boolean active;
```

---

## 4. Methods
**Methods** define the behavior or operations that an object can perform on its data.
- **Example**: Closing an auction or setting a new bid:
```java
public void closeAuction() {
    this.active = false;
}
```

---

## 5. Constructor
A **constructor** is a special block of code called when an object is instantiated (`new`). It has the same name as the class and no return type. Its primary purpose is to initialize object state.
- **Example**:
```java
public User(int userId, String name, String email, String role) {
    this.userId = userId;
    this.name = name;
    this.email = email;
    this.role = role;
}
```

---

## 6. Constructor Overloading
**Constructor overloading** occurs when a class has multiple constructors with the same name but different parameter lists (different number, types, or order of parameters).
- **Example**: `Auction.java` provides three constructors:
  1. `Auction()` (no parameters)
  2. `Auction(int auctionId, String productName)` (2 parameters)
  3. `Auction(int auctionId, String productName, Seller seller, double startingPrice, double reservePrice)` (full parameters)

---

## 7. Constructor Chaining
**Constructor chaining** is the practice of calling one constructor from another constructor within the same class or from a subclass. It eliminates duplicate initialization code.
- In `Auction.java`, the no-arg constructor calls the 2-parameter constructor, which in turn calls the full parameterized constructor.

---

## 8. The `this` Keyword
The `this` keyword refers to the **current object instance**. It is commonly used to:
1. Resolve variable shadowing (distinguishing instance fields from method parameters of the same name).
```java
public void setProductName(String productName) {
    this.productName = productName; // 'this.productName' is field, 'productName' is parameter
}
```

---

## 9. `this()`
`this()` is used inside a constructor to invoke another overloaded constructor in the **same class**.
- **Rule**: `this(...)` must be the very first statement in the constructor.
- **Example** in `Auction.java`:
```java
public Auction(int auctionId, String productName) {
    this(auctionId, productName, null, 0.0, 0.0); // Chains to the full constructor
}
```

---

## 10. The `super` Keyword and `super()`
The `super` keyword refers to the **immediate parent (superclass)** class.
- `super(...)`: Calls the parent class constructor from a subclass constructor (must be the first statement).
- **Example** in `Seller.java`:
```java
public class Seller extends User {
    public Seller(int userId, String name, String email) {
        super(userId, name, email, "SELLER"); // Calls User constructor
    }
}
```

---

## 11. Encapsulation
**Encapsulation** is the bundling of data (fields) and methods that operate on that data into a single unit, while restricting direct access to internal details (data hiding).
- Fields are marked `private`.
- Access is provided through public getters and setters with validation rules.
- **Example** in `Bid.java`:
```java
private double amount;

public void setAmount(double amount) {
    if (amount <= 0) {
        throw new IllegalArgumentException("Bid amount must be greater than zero.");
    }
    this.amount = amount;
}
```

---

## 12. Inheritance
**Inheritance** is an OOP mechanism where a new class (subclass/child) acquires the fields and methods of an existing class (superclass/parent) using the `extends` keyword. It promotes code reusability and establishing "IS-A" relationships.
- `Seller IS-A User`
- `Bidder IS-A User`
- Both inherit `userId`, `name`, `email`, and `role` from `User`.

---

## 13. Static vs Instance Members

| Feature | Static Members | Instance Members |
| :--- | :--- | :--- |
| **Belongs to** | The Class itself | Individual object instance |
| **Memory allocation** | Once when class is loaded | Every time `new` is called |
| **Access syntax** | `ClassName.member` (e.g. `Auction.getAuctionCount()`) | `objectRef.member` (e.g. `auction.getCurrentBid()`) |
| **Auction Example** | `private static int auctionCount;` tracks total auctions across the system. | `private double currentBid;` tracks the bid for that specific auction item. |

---

## 14. Access Modifiers

Java provides four levels of visibility:

1. **`private`**: Accessible only within the declaring class.
   - *Example*: Entity fields (`private double currentBid;`) to enforce encapsulation.
2. **`default` (package-private)**: No keyword. Accessible only by classes within the same package.
   - *Example*: `void updateCurrentBidInternal(double amount)` in `Auction.java` can only be called by classes inside `com.auction.model`.
3. **`protected`**: Accessible within the same package AND by subclasses in different packages.
   - *Example*: `protected String getMaskedEmail()` in `User.java` allows `Seller` and `Bidder` to access or override it.
4. **`public`**: Accessible from any other class in any package.
   - *Example*: `public class AuctionApp`, `public void createAuction(...)`.

---

## 15. `equals()` and `hashCode()`
- By default, `Object.equals()` checks reference equality (`==`).
- Overriding `equals()` allows comparing objects based on their logical identity (state).
- In `Bid.java`, two bids are logically identical if they have the same `bidId`.
- **Contract**: If two objects are equal according to `equals()`, their `hashCode()` must return the identical integer value.
```java
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Bid other = (Bid) obj;
    return this.bidId == other.bidId;
}

@Override
public int hashCode() {
    return Objects.hash(bidId);
}
```

---

## 16. Packages
A **package** is a namespace that groups related classes and interfaces. Packages prevent naming collisions and enforce access boundaries.
- `com.auction.model`: Contains domain entities (`User`, `Seller`, `Bidder`, `Auction`, `Bid`).
- `com.auction.service`: Contains business logic and operations (`AuctionService`).
- `com.auction.app`: Contains application entry point (`AuctionApp`).
