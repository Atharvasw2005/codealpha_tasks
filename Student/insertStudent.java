package Student;
import java.util.ArrayList;
import java.util.Scanner;

class InsertMarks {
    public String name, className;
    final String[] subjects = {"English", "Marathi", "Hindi", "Science", "Mathematics"};
    double[] marks = new double[subjects.length];

    public void insertMethod(Scanner sc) {
        System.out.print("Enter Name: ");
        sc.nextLine(); 
        name = sc.nextLine();
        System.out.print("Enter Class Name: ");
        className = sc.nextLine();

        for (int i = 0; i < subjects.length; i++) {
            while (true) {
                System.out.print("Enter Marks For " + subjects[i] + ": ");
                if (sc.hasNextDouble()) {
                    marks[i] = sc.nextDouble();
                    if (marks[i] >= 0 && marks[i] <= 100) break;
                    else System.out.println("Please enter marks between 0 and 100.");
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    sc.next(); 
                }
            }
        }
    }
}

class StudentMarks {
    static ArrayList<InsertMarks> studentList = new ArrayList<>();

    public static void insertStudent(Scanner sc) {
        System.out.print("How Many Students Do You Want to Insert: ");
        int stu = sc.nextInt();
        for (int i = 0; i < stu; i++) {
            InsertMarks student = new InsertMarks();
            student.insertMethod(sc);
            studentList.add(student);
        }
        System.out.println("Student Details Inserted Successfully!");
    }

    public static void showDetails() {
        if (studentList.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }
        for (InsertMarks s : studentList) {
            System.out.println("-----------------------------");
            System.out.println("Name: " + s.name);
            System.out.println("Class: " + s.className);
            System.out.println();
            for (String subject : s.subjects) {
                System.out.print(subject + "\t");
            }
            System.out.println();
            double total = 0, highest = s.marks[0], lowest = s.marks[0];
            for (double mark : s.marks) {
                total += mark;
                if (mark > highest) highest = mark;
                if (mark < lowest) lowest = mark;
                System.out.print(mark + "\t");
            }
            System.out.println();
            double average = total / s.subjects.length;
            System.out.println("Total Marks: " + total);
            System.out.println("Average Marks: " + average);
            System.out.println("Highest Marks: " + highest);
            System.out.println("Lowest Marks: " + lowest);
            System.out.println("-----------------------------\n");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = 0;
        while (num != 3) {
            System.out.println("1. Insert Student");
            System.out.println("2. Show Details");
            System.out.println("3. Exit");
            System.out.print("Select Option: ");
            if (sc.hasNextInt()) {
                num = sc.nextInt();
                switch (num) {
                    case 1:
                        insertStudent(sc);
                        break;
                    case 2:
                        System.out.println("Showing Student Details:");
                        showDetails();
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                sc.next(); 
            }
        }
    }
}