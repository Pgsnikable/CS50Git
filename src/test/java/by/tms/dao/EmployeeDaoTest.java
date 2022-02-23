package by.tms.dao;

import by.tms.entity.Employee;
import by.tms.entity.Payment;
import by.tms.util.EmployeeTestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class EmployeeDaoTest {

    private EmployeeDao employeeDao = EmployeeDao.getInstance();
    private SessionFactory sessionFactory;

    @Before
    public void initDb() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
        EmployeeTestDataImporter.getInstance().importTestData(sessionFactory);
    }

    @After
    public void finish() {
        sessionFactory.close();
    }

    @Test
    public void testFindAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Employee> results = employeeDao.findAll(session);
            List<String> fullNames = results.stream().map(Employee::fullName).collect(toList());
            assertThat(results, hasSize(5));
            assertThat(fullNames, containsInAnyOrder("Bill Gates", "Steve Jobs", "Sergey Brin", "Tim Cook", "Diane Greene"));

            session.getTransaction().commit();
        }
    }

    @Test
    public void testFindAllByFirstName() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Employee> results = employeeDao.findAllByFirstName(session, "Bill");
            assertThat(results, hasSize(1));
            assertThat(results.get(0).getLastName(), equalTo("Gates"));

            session.getTransaction().commit();
        }
    }

    @Test
    public void testFindFirstThreeEmployeesOrderedByBirthday() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Employee> results = employeeDao.findLimitedEmployeesOrderedByBirthday(session, 3);
            assertThat(results, hasSize(3));
            List<String> fullNames = results.stream().map(Employee::fullName).collect(toList());
            assertThat(fullNames, contains("Diane Greene", "Steve Jobs", "Bill Gates"));

            session.getTransaction().commit();
        }
    }

    @Test
    public void testAllFindByOrganizationName() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Employee> results = employeeDao.findAllByOrganizationName(session, "Google");
            assertThat(results, hasSize(2));
            List<String> fullNames = results.stream().map(Employee::fullName).collect(toList());
            assertThat(fullNames, containsInAnyOrder("Sergey Brin", "Diane Greene"));

            session.getTransaction().commit();
        }
    }

    @Test
    public void testFindAllPaymentsByOrganizationName() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Payment> applePayments = employeeDao.findAllPaymentsByOrganizationName(session, "Apple");
            assertThat(applePayments, hasSize(5));
            List<Integer> amounts = applePayments.stream().map(p -> p.getAmount().intValue()).collect(toList());
            assertThat(amounts, contains(250, 500, 600, 300, 400));

            session.getTransaction().commit();
        }
    }

    @Test
    public void testFindAveragePaymentAmountByFirstAndLastNames() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Double averagePaymentAmount = employeeDao.findAveragePaymentAmountByFirstAndLastNames(session, "Bill", "Gates");
            assertThat(averagePaymentAmount, equalTo(300.0d));

            session.getTransaction().commit();
        }
    }

    @Test
    public void testFindOrganizationNamesWithAvgEmployeePaymentsOrderedByOrgName() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Object[]> results = employeeDao.findOrganizationNamesWithAvgEmployeePaymentsOrderedByOrgName(session);
            assertThat(results, hasSize(3));
            List<String> orgNames = results.stream().map(a -> (String) a[0]).collect(toList());
            assertThat(orgNames, contains("Apple", "Google", "Microsoft"));

            List<Double> orgAvgPayments = results.stream().map(a -> (Double) a[1]).collect(toList());
            assertThat(orgAvgPayments, contains(410.0d, 400.0d, 300.0d));

            session.getTransaction().commit();
        }
    }

    @Test
    public void testCanYouDoIt() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Object[]> results = employeeDao.canYouDoIt(session);
            assertThat(results, hasSize(2));

            List<String> names = results.stream().map(r -> ((Employee) r[0]).fullName()).collect(toList());
            assertThat(names, contains("Sergey Brin", "Steve Jobs"));

            List<Double> averagePayments = results.stream().map(r -> (Double) r[1]).collect(toList());
            assertThat(averagePayments, contains(500.0d, 450.0d));

            session.getTransaction().commit();
        }
    }
}
