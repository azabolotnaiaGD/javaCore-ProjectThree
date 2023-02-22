import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.IntStream;

abstract class Contact {
    String number;
    String name;
    LocalDateTime createTime = LocalDateTime.now();
    LocalDateTime lastEdit = LocalDateTime.now();

    Contact(String name, String number) {
        this.name = name;
        if (checkNumber(number))
            this.number = number;
        else {
            System.out.println("Wrong number format!");
            this.number = "[no number]";
        }
    }

    public void setName(String name) {
        this.name = name;
        lastEdit = LocalDateTime.now();
    }

    private boolean checkNumber(String number) {
        String pattern1 = "\\+?[a-zA-Z\\d]+([ -][a-zA-Z\\d]{2,})*";
        String pattern2 = "\\+?\\([a-zA-Z\\d]+\\)([ -][a-zA-Z\\d]{2,})*";
        String pattern3 = "\\+?[a-zA-Z\\d]+[ -]\\([a-zA-Z\\d]{2,}\\)([ -][a-zA-Z\\d]{2,})*";

        return number.matches(pattern1) || number.matches(pattern2) || number.matches(pattern3);
    }

    public void setNumber(String number) {
        if (checkNumber(number))
            this.number = number;
        else {
            System.out.println("Wrong number format!");
        }
        lastEdit = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Contact{" +
                "number='" + number + '\'' +
                ", createTime=" + createTime +
                ", lastEdit=" + lastEdit +
                '}';
    }

    abstract public String fields();

    public String data() {
        return number + " " + name;
    }
}

class Organization extends Contact {
    private String address;

    public Organization(String name, String address, String number) {
        super(name, number);
        this.address = address;
    }

    public void setAddress(String address) {
        this.address = address;
        lastEdit = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Organization{" +
                "address='" + address + '\'' +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", lastEdit=" + lastEdit +
                '}';
    }

    @Override
    public String fields() {
        return "name, address, number";
    }

    @Override
    public String data() {
        return super.data() + " " + address;
    }
}

class Person extends Contact {
    private String surname;
    private String birthDate;
    private String gender;

    public Person(String name, String surname, String birthDate, String gender, String number) {
        super(name, number);
        this.surname = surname;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public void setSurname(String surname) {
        this.surname = surname;
        lastEdit = LocalDateTime.now();
    }

    public void setBirthDate(String birthDate) {
        System.out.println("Bad birth date!");
        this.birthDate = "[no data]";
        lastEdit = LocalDateTime.now();
    }

    public void setGender(String gender) {
        if (gender.equals("")) {
            System.out.println("Bad gender!");
            this.gender = "[no data]";
        } else
            this.gender = gender;
        lastEdit = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Person{" +
                "surname='" + surname + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", gender='" + gender + '\'' +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", lastEdit=" + lastEdit +
                '}';
    }

    @Override
    public String fields() {
        return "name, surname, birth, gender, number";
    }

    @Override
    public String data() {
        return super.data() + " " + surname;
    }
}

class ContactBook {
    private static ContactBook instance;

    static ContactBook getInstance() {
        if (instance == null)
            instance = new ContactBook();
        return instance;
    }

    private ContactBook() {
    }

    private ArrayList<Contact> data = new ArrayList<>();


    public String countStr() {
        return "The Phone Book has " + data.size() + " records.";
    }

    void add(Contact contact) {
        data.add(contact);
        System.out.println("The record added.");
    }

    public void remove(int n) {
        data.remove(n);
        System.out.println("The record removed!");
    }

    public void edit(int n) {
        System.out.println("Select a field (" + data.get(n).fields() + "): ");
    }

    public void editName(int n, String name) {
        data.get(n).setName(name);
        System.out.println("The record updated!");
    }

    public void editSurname(int n, String surname) {
        ((Person) data.get(n)).setSurname(surname);
        System.out.println("The record updated!");
    }

    public void editBirth(int n, String birth) {
        ((Person) data.get(n)).setBirthDate(birth);
        System.out.println("The record updated!");
    }

    public void editGender(int n, String gender) {
        ((Person) data.get(n)).setGender(gender);
        System.out.println("The record updated!");
    }

    public void editAddress(int n, String address) {
        ((Organization) data.get(n)).setAddress(address);
        System.out.println("The record updated!");
    }

    public void editNumber(int n, String number) {
        data.get(n).setNumber(number);
        System.out.println("The record updated!");
    }

    public void info(int n) {
        System.out.println(data.get(n).toString());
    }

    public ArrayList<Integer> search(String part) {
        ArrayList<Integer> res = new ArrayList<>();
        String out = "";
        for (int i = 0; i < data.size(); ++i) {
            if (data.get(i).data().toLowerCase().contains(part.toLowerCase())) {
                res.add(i);
                out += "\n" + res.size() + ". " + data.get(i).data();
            }
        }
        System.out.println("Found " + res.size() + " results:" + out);
        return res;
    }

    @Override
    public String toString() {
        int len = data.size();
        if (len == 0) return "Empty";
        StringBuilder sb = new StringBuilder("1. " + data.get(0).toString());
        IntStream.range(1, len).forEach(i -> {
            sb.append("\n");
            sb.append(i + 1 + ". " + data.get(i).toString());
        });
        return sb.toString();
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Scanner in = new Scanner(System.in);
        ContactBook contacts = ContactBook.getInstance();
        String mode, num;
        int numbers;
        while (true) {
            System.out.print("Enter action (add, list, search, count, exit): ");
            switch (in.nextLine()) {
                case "add":
                    System.out.print("Enter the type (person, organization): ");
                    switch (in.nextLine()) {
                        case "organization":
                            System.out.print("Enter the organization name: ");
                            String orgname = in.nextLine();
                            System.out.print("Enter the address: ");
                            String address = in.nextLine();
                            System.out.print("Enter the number: ");
                            String orgnumber = in.nextLine();
                            contacts.add(new Organization(orgname, address, orgnumber));
                            break;
                        case "person":
                            System.out.print("Enter the name: ");
                            String name = in.nextLine();
                            System.out.print("Enter the surname: ");
                            String surname = in.nextLine();
                            System.out.print("Enter the birth date: ");
                            String birthdate = in.nextLine();
                            if (birthdate.equals("")) {
                                birthdate = "[no data]";
                                System.out.println("Bad birth date!");
                            }
                            System.out.print("Enter the gender (M, F): ");
                            String gender = in.nextLine();
                            if (gender.equals("")) {
                                gender = "[no data]";
                                System.out.println("Bad gender!");
                            }
                            System.out.print("Enter the number: ");
                            String number = in.nextLine();
                            contacts.add(new Person(name, surname, birthdate, gender, number));
                            break;
                    }
                    System.out.println();
                    break;
                case "list":
                    System.out.println(contacts);
                    System.out.println();
                    System.out.println("Enter action ([number], back): ");
                    num = in.nextLine();
                    if (num.equals("back"))
                        break;
                    numbers = Integer.parseInt(num) - 1;
                    contacts.info(numbers);
                    do {
                        System.out.println();
                        System.out.print("Enter action (edit, delete, menu): ");
                        mode = in.nextLine();
                        switch (mode) {
                            case "edit":
                                contacts.edit(numbers);
                                switch (in.nextLine()) {
                                    case "name":
                                        System.out.print("Enter name: ");
                                        contacts.editName(numbers, in.nextLine());
                                        break;
                                    case "surname":
                                        System.out.print("Enter surname: ");
                                        contacts.editSurname(numbers, in.nextLine());
                                        break;
                                    case "birth":
                                        System.out.print("Enter birth date: ");
                                        contacts.editBirth(numbers, in.nextLine());
                                        break;
                                    case "gender":
                                        System.out.print("Enter gender: ");
                                        contacts.editGender(numbers, in.nextLine());
                                        break;
                                    case "address":
                                        System.out.print("Enter address: ");
                                        contacts.editAddress(numbers, in.nextLine());
                                        break;
                                    case "number":
                                        System.out.print("Enter number: ");
                                        contacts.editNumber(numbers, in.nextLine());
                                        break;
                                }
                                contacts.info(numbers);
                                break;
                            case "delete":
                                contacts.remove(numbers);
                                break;
                        }
                    } while (!mode.equals("menu"));
                    System.out.println();
                    break;
                case "search":
                    loop1:
                    while (true) {
                        System.out.print("Enter search query: ");
                        ArrayList<Integer> res = contacts.search(in.nextLine());

                        System.out.println("Enter action ([number], back, again): ");
                        num = in.nextLine();
                        if (num.equals("back"))
                            break;
                        if (num.equals("again"))
                            continue;
                        numbers = res.get(Integer.parseInt(num) - 1);
                        contacts.info(numbers);
                        while (true) {
                            System.out.print("Enter action (edit, delete, menu): ");
                            mode = in.nextLine();
                            switch (mode) {
                                case "edit":
                                    contacts.edit(numbers);
                                    switch (in.nextLine()) {
                                        case "name":
                                            System.out.print("Enter name: ");
                                            contacts.editName(numbers, in.nextLine());
                                            break;
                                        case "surname":
                                            System.out.print("Enter surname: ");
                                            contacts.editSurname(numbers, in.nextLine());
                                            break;
                                        case "birth":
                                            System.out.print("Enter birth date: ");
                                            contacts.editBirth(numbers, in.nextLine());
                                            break;
                                        case "gender":
                                            System.out.print("Enter gender: ");
                                            contacts.editGender(numbers, in.nextLine());
                                            break;
                                        case "address":
                                            System.out.print("Enter address: ");
                                            contacts.editAddress(numbers, in.nextLine());
                                            break;
                                        case "number":
                                            System.out.print("Enter number: ");
                                            contacts.editNumber(numbers, in.nextLine());
                                            break;
                                    }
                                    contacts.info(numbers);
                                    break;
                                case "delete":
                                    contacts.remove(numbers);
                                    break;
                                case "menu":
                                    break loop1;
                            }
                        }
                    }
                    System.out.println();
                    break;
                case "count":
                    System.out.println(contacts.countStr());
                    System.out.println();
                    break;
                default:
                    return;
            }
        }
    }
}