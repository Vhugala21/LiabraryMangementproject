Overview
This Java program simulates a library management system that allows you to add members, borrow books, check due dates and fines, and manage notifications. It includes classes for Book, Member, OverdueFineCalculator, and NotificationService.

Instructions
Make sure you have Java installed on your system.
Compile the LibraryManagementSystem.java file using the Java compiler.
Run the compiled program by executing the LibraryManagementSystem class.
Classes
Book: Represents a book with properties such as title, author, dueDate, and overdueFine.
Member: Represents a library member with properties like name and a list of borrowedBooks.
OverdueFineCalculator: Utility class to calculate overdue fines for a book.
NotificationService: Sends notifications to members for overdue books.
Functionality
Add Member: Adds a new member to the library system.
Borrow Book: Allows a member to borrow a book with a specified loan period.
Check Due Dates and Fines: Displays due dates and fines for borrowed books.
Manage Notifications: Enables or disables notifications for members.
Automatic Processes: The program automatically calculates overdue fines and sends notifications on separate threads.
Data Persistence
Member and book data is serialized and stored in a file ("library_data.dat") for persistence across program executions.
Usage
Follow the on-screen instructions to perform various actions.
Type exit to save data and exit the program.
