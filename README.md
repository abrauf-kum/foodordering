## OOP Semester Project

## Student Information
- Name: Abdul Rauf Kumbhar
- CMS_ID: 023-25-0149
- Department: BSCS [AI] – II
- Section: C
- Course: Object Oriented Program
- Instructor: Dr. Saif Hassan
- Institute: Sukkur IBA University

## Project Overview
The Food Ordering System is a desktop application built in Java that connects customers with restaurant menus through a clean and simple interface. Customers can register an account, log in, browse food items from multiple restaurants, filter by category, add items to a cart, and place orders with a single click. Every order is saved to a MySQL database instantly, and customers can revisit their order history at any time. On the other side, an administrator can log in to a dedicated panel that shows all incoming orders from all customers, with the ability to update each order's status from Pending through Processing to Delivered or Cancelled.

The system is built following Object-Oriented Programming principles throughout. It uses interfaces such as Persistable and Priceable to define contracts, abstract classes such as Model, BaseDAO, and BaseView to eliminate repeated code through inheritance, and encapsulation to keep all data fields private with controlled access. The database layer follows the DAO pattern, meaning all SQL queries are isolated inside dedicated classes and never mixed with UI code. The application is organized into six packages — model, dao, session, util, view, and the root — each with a single clear responsibility, making the codebase easy to read, maintain, and extend.

## Purpose
The purpose of the Food Ordering System is to provide a fast, organized, and user-friendly platform for managing food orders digitally. The system simplifies the process of browsing restaurant menus, placing orders, and tracking order history for customers, while also allowing administrators to efficiently monitor and manage incoming orders through a centralized control panel. By integrating Java with a MySQL database and applying Object-Oriented Programming principles and the DAO architecture, the project aims to create a maintainable, scalable, and efficient desktop application that demonstrates real-world software development practices.

## Directory Layout

```text
src/
└── foodordering/
    │
    ├── FoodOrderingSystem.java        ← Entry point (main method)
    │
    ├── model/                         ← Domain model classes
    │   ├── Persistable.java           (interface)
    │   ├── Priceable.java             (interface)
    │   ├── Model.java                 (abstract base)
    │   ├── User.java
    │   ├── MenuItem.java
    │   ├── CartItem.java
    │   └── Order.java
    │
    ├── dao/                           ← Database access layer
    │   ├── BaseDAO.java               (abstract — manages DB connection)
    │   ├── UserDAO.java
    │   ├── MenuDAO.java
    │   └── OrderDAO.java
    │
    ├── session/                       ← Session state
    │   └── Session.java
    │
    ├── util/                          ← Shared UI utilities
    │   ├── Theme.java                 (colours, fonts, component factories)
    │   └── BaseView.java              (abstract — shared banner + logout)
    │
    └── view/                          ← UI screens
        ├── LoginView.java
        ├── RegisterView.java
        ├── MainView.java
        └── AdminView.java
```

## How to Compile

Firstly, 
cd D:\foodordering\src
then
javac -cp .;mysql-connector-j-9.6.0.jar foodordering\FoodOrderingSystem.java foodordering\*.java

## How to Run
java -cp .;mysql-connector-j-9.6.0.jar foodordering.FoodOrderingSystem


## Presentation Video Link

https://drive.google.com/file/d/15okjuob-jU0Zqt35MM4RPOsfBNb4AAm9/view?usp=sharing
