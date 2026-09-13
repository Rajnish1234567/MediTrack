# MediTrack — Design Decisions

## 1. Purpose
The goal is to keep the application simple enough for a Core Java assessment while still demonstrating clean object-oriented design.

---

# 2. Package Structure

The base package is:
```java
com.airtribe.meditrack
```

Sub-packages are organized by responsibility:
```text
constants   → application-wide constants
docs        → contains all documentation
entity      → domain objects
exception   → custom exceptions
interfaces  → contracts such as Searchable and Payable
service     → business logic
util        → reusable helper classes
validators  → validate person details
test        → manual test runner
```

# 3. Entity Inheritance Design

The entity hierarchy is:
```text
MedicalEntity
      |
    Person
    /    \
 Doctor  Patient
```

### `MedicalEntity`
`MedicalEntity` is an abstract class containing the common entity identifier and the abstract `getEntityType()` behavior.

### `Person`
`Person` contains common personal information:
- Name
- Age
- Phone
- Email

### `Doctor`
`Doctor` extends `Person` and adds:
- Specialization
- Consultation fee

### `Patient`
`Patient` extends `Person` and adds:
- Blood group
- Address

### Reason
This hierarchy demonstrates inheritance, constructor chaining, `super`, abstraction, and reuse of common state and behavior.

---

# 4. Encapsulation
All entity fields are private.

Example:
```java
private String name;
private int age;

```

Fields are accessed through methods such as:
```java
public String getName()
public void setName(String name)
```

Validation is performed before accepting mutable values.

### Reason
Keeping fields private prevents unrestricted access to internal object state and provides a controlled point for validation.

---

# 5. Centralized Validation

Validation logic is located in:

```text
util/Validator.java
```

For example:

```java
Validator.validateAge(age);
Validator.validatePhone(phone);
```

### Reason
Without centralized validation, the same rules would be repeated in `Patient`, `Doctor`, and service classes. A shared validator reduces duplication and keeps validation behavior consistent.

---

# 6. Enum Design
The application uses enums instead of unrestricted strings.

### Specialization
```java
Specialization.CARDIOLOGIST
Specialization.DERMATOLOGIST
```

### AppointmentStatus
```java
AppointmentStatus.PENDING
AppointmentStatus.CONFIRMED
AppointmentStatus.CANCELLED
AppointmentStatus.COMPLETED
```

### Reason
Enums restrict values to a known set and make business rules safer and easier to read.

For example, using a string would allow invalid states such as:
```java
status = "CANCELLED123";
```
An enum prevents that category of error.

---

# 7. Generic DataStore

The application uses:

```java
DataStore<T>
```

instead of creating separate storage classes for every entity.

Examples:

```java
DataStore<Patient> patientStore;
DataStore<Doctor> doctorStore;
DataStore<Appointment> appointmentStore;
```

### Reason
Generics provide compile-time type safety and allow the same reusable storage implementation to work with different entity types.

---

# 8. Service Layer

Business logic is separated from the domain model and command-line UI.

The services are:

```text
PatientService
DoctorService
AppointmentService
```

For example, creating a patient is the responsibility of `PatientService`, not `Main.java`.

### Reason

This provides separation of concerns:

```text
Main
  ↓
Service
  ↓
DataStore / Entity
```

The menu handles user interaction, while services handle application behavior.

---

# 9. Optional Return for Lookup

Patient and doctor lookup methods can return:

```java
Optional<Patient>
Optional<Doctor>
```

instead of returning `null`.

Example:

```java
public Optional<Patient> getPatientById(int id)
```

### Reason

`Optional` makes the absence of a result explicit and avoids unnecessary direct `null` handling.

---

# 10. Immutability of BillSummary

`BillSummary` is designed as an immutable value object.

Design choices:

```java
public final class BillSummary
```

and:

```java
private final int billId;
private final double subtotal;
private final double tax;
private final double total;
```

There are no setters.

### Reason

Once generated, a bill summary should represent a fixed financial result. Immutability also makes the object easier to reason about and safer to share between threads.

Because its fields are primitives, it does not expose mutable internal objects.

---

# 11. Deep Copy of Patient

`Patient` implements `Cloneable`.

The patient contains a mutable nested `Address`, so a real deep-copy example is possible.

During cloning:

```java
cloned.address = this.address.clone();
```

### Reason

A shallow copy would duplicate the outer `Patient` object while keeping the same `Address` reference.

A deep copy creates a separate `Address` object.

Changes to Address B do not modify Address A.

---

# 12. Deep Copy of Appointment

An `Appointment` contains references to both a `Patient` and a `Doctor`.

The clone operation creates independent copies for the objects that need independent mutable state.

### Reason

This demonstrates that deep cloning becomes more important as object graphs become more complex.

It also allows the project to demonstrate the distinction between copying object references and copying object state.

---

# 13. `equals()` and `hashCode()`

Entity equality is based on an entity's unique identifier.

Example:

```java
return getId() == patient.getId();
```

`hashCode()` uses the same identity basis.

### Reason

Two separate Java objects representing the same database-style entity should be considered equal when they share the same unique identifier.

Overriding both `equals()` and `hashCode()` keeps the objects consistent when used with hash-based collections.

---

# 14. Interfaces

Two interfaces are used.

### `Searchable`

Represents an object or service that supports search behavior.

### `Payable`

Represents something capable of generating a bill summary.

`Payable` also contains a default tax-calculation method.

### Reason

Interfaces model contracts rather than shared object state. They also allow polymorphic behavior.

Example:

```java
Payable payable = bill;
BillSummary summary = payable.generateBill();
```

The reference is the interface type while the actual object is a `Bill`.

---

### Reason

`Bill` represents the calculation operation, while `BillSummary` represents the resulting immutable value.

This separation helps demonstrate abstraction and immutability together.

---

# 15. Constants

Application-wide values are kept in:

```text
constants/Constants.java
```

Examples:

```java
TAX_RATE
PATIENT_FILE
DOCTOR_FILE
APPOINTMENT_FILE
```

### Reason

Centralizing constants prevents magic numbers and repeated file path strings throughout the codebase.

---

# 16. ID Generation

`IdGenerator` is a utility class with static counters.

The project uses `AtomicInteger` for counters:

```java
private static final AtomicInteger PATIENT_ID;
```

### Reason

The counters demonstrate static application-wide state and safe atomic increment operations.

The static initialization block also demonstrates class initialization behavior.

---

# 17. Exception Strategy

The project defines:

```text
InvalidDataException
AppointmentNotFoundException
```

### `InvalidDataException`

Used when supplied data violates business validation rules.

### `AppointmentNotFoundException`

Used when a requested appointment does not exist.

### Reason

Domain-specific exceptions make errors more meaningful than generic exceptions such as `Exception`.

Exception chaining can preserve the original technical cause:

---

# 18. File I/O Design

CSV operations are isolated inside:

```text
CSVUtil.java
```

The implementation uses try-with-resources:

```java
try (BufferedReader reader = ...) {
    // read data
}
```

### Reason

The utility keeps file-handling code away from the entities and services and automatically closes resources.

---

# 19. Streams and Lambdas

The service layer uses Streams for operations such as:

- Searching patients.
- Filtering doctors by specialization.
- Calculating average consultation fees.
- Counting appointments per doctor.
- Sorting doctors by consultation fee.

Example:

```java
double averageFee = doctorStore.getAll()
        .stream()
        .mapToDouble(Doctor::getConsultationFee)
        .average()
        .orElse(0.0);
```

### Reason

Streams make collection-processing operations expressive and demonstrate Java 8+ functional programming concepts.

---


# 20. Manual Testing

The requirement specifies a manual runner, so:

```text
TestRunner.java
```

is used instead of a unit-testing framework.

The runner checks important behaviors such as:

- Patient creation.
- Deep-copy semantics.
- Billing calculations.
- Generic `DataStore<T>` behavior.

### Reason

This directly satisfies the assessment requirement while keeping the project dependency-free.

---


# 21. Why `ArrayList` and `HashMap`

### ArrayList

Used where ordered sequential storage and iteration are required.

Example:

```java
List<T> records = new ArrayList<>();
```

### HashMap

Suitable for key-value lookup and analytics such as:

```java
Map<Integer, Long> appointmentCount;
```

### Reason

These collections directly demonstrate commonly used Java collection types and their appropriate use cases.

---

# 22. No Database used

The base project uses in-memory data structures rather than a relational database.

### Reason

The assignment focuses on Core Java concepts such as OOP, collections, file I/O, generics, exceptions, concurrency, and design patterns.

CSV persistence provides an appropriate lightweight persistence mechanism for the bonus requirement.

---

# 23. Conclusion

The MediTrack design intentionally favors simple, understandable Core Java solutions while making every major learning objective visible in the source code.

The application is not designed to imitate a production hospital-management platform. Its primary purpose is to demonstrate Java fundamentals through a coherent domain model and a runnable console application.
