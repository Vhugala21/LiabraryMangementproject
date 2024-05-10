import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
class Book implements Serializable {
   private static final long serialVersionUID = 1L;
   String title;
   String author;
   LocalDate dueDate;
   double overdueFine;
   public Book(String title, String author) {
       this.title = title;
       this.author = author;
   }
}
class Member implements Serializable {
   private static final long serialVersionUID = 1L;
   String name;
   List<Book> borrowedBooks;
   public Member(String name) {
       this.name = name;
       this.borrowedBooks = new ArrayList<>();
   }
   public void borrowBook(Book book, int loanPeriod) {
       book.dueDate = LocalDate.now().plusDays(loanPeriod);
       borrowedBooks.add(book);
   }
}
class OverdueFineCalculator {
   private static final double DAILY_FINE_RATE = 0.5;
   public static double calculateOverdueFine(Book book) {
       if (book.dueDate == null) {
           return 0.0;
       }
       LocalDate today = LocalDate.now();
       if (!today.isAfter(book.dueDate)) {
           return 0.0;
       }
       long overdueDays = ChronoUnit.DAYS.between(book.dueDate, today);
       return overdueDays * DAILY_FINE_RATE;
   }
}
class NotificationService {
   public static void sendNotification(Member member, Book book) {
       System.out.println("Notification sent to " + member.name + " for book '" + book.title + "' due on " + book.dueDate);
   }
}
public class LibraryManagementSystem {
   private static List<Member> members = new ArrayList<>();
   private static final String DATA_FILE_PATH = "library_data.dat";
   public static void main(String[] args) {
       loadData();
       Thread fineProcessingThread = new Thread(LibraryManagementSystem::processOverdueFines);
       Thread notificationThread = new Thread(LibraryManagementSystem::sendNotifications);
       fineProcessingThread.start();
       notificationThread.start();
       while (true) {
           System.out.println("Enter 'add' to add a new member, 'borrow' to borrow a book, 'check' to check due dates and fines, 'notifications' to manage notifications, or 'exit' to quit.");
           String input = System.console().readLine();
           switch (input.toLowerCase()) {
               case "add":
                   addMember();
                   break;
               case "borrow":
                   borrowBook();
                   break;
               case "check":
                   checkDueDatesAndFines();
                   break;
               case "notifications":
                   manageNotifications();
                   break;
               case "exit":
                   saveData();
                   System.exit(0);
               default:
                   System.out.println("Invalid input. Please try again.");
                   break;
           }
       }
   }
   private static void loadData() {
       try {
           FileInputStream fileIn = new FileInputStream(DATA_FILE_PATH);
           ObjectInputStream in = new ObjectInputStream(fileIn);
           members = (List<Member>) in.readObject();
           in.close();
           fileIn.close();
       } catch (IOException | ClassNotFoundException e) {
           System.out.println("Error loading data: " + e.getMessage());
       }
   }
   private static void saveData() {
       try {
           FileOutputStream fileOut = new FileOutputStream(DATA_FILE_PATH);
           ObjectOutputStream out = new ObjectOutputStream(fileOut);
           out.writeObject(members);
           out.close();
           fileOut.close();
       } catch (IOException e) {
           System.out.println("Error saving data: " + e.getMessage());
       }
   }
   private static void addMember() {
       System.out.print("Enter member name: ");
       String name = System.console().readLine();
       Member member = new Member(name);
       members.add(member);
       System.out.println("Member '" + name + "' added successfully.");
   }
   private static void borrowBook() {
       System.out.print("Enter member name: ");
       String name = System.console().readLine();
       Member member = members.stream()
               .filter(m -> m.name.equals(name))
               .findFirst()
               .orElse(null);
       if (member == null) {
           System.out.println("Member not found.");
           return;
       }
       System.out.print("Enter book title: ");
       String title = System.console().readLine();
       System.out.print("Enter book author: ");
       String author = System.console().readLine();
       System.out.print("Enter loan period (in days): ");
       int loanPeriod = Integer.parseInt(System.console().readLine());
       Book book = new Book(title, author);
       member.borrowBook(book, loanPeriod);
       System.out.println("Book '" + title + "' by '" + author + "' borrowed successfully.");
   }
   private static void checkDueDatesAndFines() {
       System.out.println("Due dates and fines:");
       for (Member member : members) {
           System.out.println("Member: " + member.name);
           for (Book book : member.borrowedBooks) {
               System.out.println("    Book: " + book.title + " by " + book.author);
               System.out.println("    Due Date: " + book.dueDate);
               System.out.println("    Overdue Fine: $" + book.overdueFine);
           }
       }
   }
   private static void manageNotifications() {
       System.out.println("Manage notifications:");
       System.out.println("1. Enable notifications");
       System.out.println("2. Disable notifications");
       System.out.print("Enter your choice: ");
       String choice = System.console().readLine();
       switch (choice) {
           case "1":
               System.out.println("Notifications enabled.");
               break;
           case "2":
               System.out.println("Notifications disabled.");
               break;
           default:
               System.out.println("Invalid choice.");
               break;
       }
   }
   private static void processOverdueFines() {
       while (true) {
           for (Member member : members) {
               for (Book book : member.borrowedBooks) {
                   book.overdueFine = OverdueFineCalculator.calculateOverdueFine(book);
               }
           }
           System.out.println("Overdue fines updated.");
           try {
               Thread.sleep(60000); // Wait for 1 minute
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
   }
   private static void sendNotifications() {
       while (true) {
           for (Member member : members) {
               for (Book book : member.borrowedBooks) {
                   if (book.dueDate != null && book.dueDate.isEqual(LocalDate.now())) {
                       NotificationService.sendNotification(member, book);
                   }
               }
           }
           System.out.println("Notifications sent.");
           try {
               Thread.sleep(86400000); // Wait for 1 day
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
   }
}