package by.tms.util;

import by.tms.entity.Employee;
import by.tms.entity.Gender;
import by.tms.entity.Organization;
import by.tms.entity.Payment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmployeeTestDataImporter {

    private static EmployeeTestDataImporter INSTANCE = new EmployeeTestDataImporter();

    public static EmployeeTestDataImporter getInstance() {
        return INSTANCE;
    }

    public void importTestData(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Organization microsoft = saveOrganization(session, "Microsoft");
            Organization apple = saveOrganization(session, "Apple");
            Organization google = saveOrganization(session, "Google");

            Employee billGates = saveEmployee(session, "Bill", "Gates",
                    LocalDate.of(1955, Month.OCTOBER, 28), Gender.MALE, microsoft);
            Employee steveJobs = saveEmployee(session, "Steve", "Jobs",
                    LocalDate.of(1955, Month.FEBRUARY, 24), Gender.MALE, apple);
            Employee sergeyBrin = saveEmployee(session, "Sergey", "Brin",
                    LocalDate.of(1973, Month.AUGUST, 21), Gender.MALE, google);
            Employee timCook = saveEmployee(session, "Tim", "Cook",
                    LocalDate.of(1960, Month.NOVEMBER, 1), Gender.MALE, apple);
            Employee dianeGreene = saveEmployee(session, "Diane", "Greene",
                    LocalDate.of(1955, Month.JANUARY, 1), Gender.FEMALE, google);

            savePayment(session, billGates, BigDecimal.valueOf(100));
            savePayment(session, billGates, BigDecimal.valueOf(300));
            savePayment(session, billGates, BigDecimal.valueOf(500));

            savePayment(session, steveJobs, BigDecimal.valueOf(250));
            savePayment(session, steveJobs, BigDecimal.valueOf(600));
            savePayment(session, steveJobs, BigDecimal.valueOf(500));

            savePayment(session, timCook, BigDecimal.valueOf(400));
            savePayment(session, timCook, BigDecimal.valueOf(300));

            savePayment(session, sergeyBrin, BigDecimal.valueOf(500));
            savePayment(session, sergeyBrin, BigDecimal.valueOf(500));
            savePayment(session, sergeyBrin, BigDecimal.valueOf(500));

            savePayment(session, dianeGreene, BigDecimal.valueOf(300));
            savePayment(session, dianeGreene, BigDecimal.valueOf(300));
            savePayment(session, dianeGreene, BigDecimal.valueOf(300));
        }
    }

    private Organization saveOrganization(Session session, String name) {
        Organization organization = new Organization(name);
        session.save(organization);

        return organization;
    }

    private Employee saveEmployee(Session session, String firstName, String lastName,
                                  LocalDate birthday, Gender gender, Organization organization) {
        Employee employee = new Employee(firstName, lastName, birthday, gender, organization);
        session.save(employee);

        return employee;
    }

    private void savePayment(Session session, Employee employee, BigDecimal amount) {
        session.save(new Payment(amount, employee));
    }
}
