package ra.edu.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Course;

import java.util.List;

@Repository
public class DashboardRepositoryImp implements DashboardRepository {
    private final SessionFactory sessionFactory;

    public DashboardRepositoryImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long countTotalCourses() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query<Long> query = session.createQuery("SELECT COUNT(c.id) FROM Course c", Long.class);
            Long result = query.getSingleResult();
            session.getTransaction().commit();
            return result;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Long countTotalStudents() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query<Long> query = session.createQuery("SELECT COUNT(s.id) FROM User s", Long.class);
            Long result = query.getSingleResult();
            session.getTransaction().commit();
            return result;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Long countTotalEnrollments() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query<Long> query = session.createQuery("SELECT COUNT(e.id) FROM Enrollment e", Long.class);
            Long result = query.getSingleResult();
            session.getTransaction().commit();
            return result;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Course> getAllCourses() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query<Course> query = session.createQuery("FROM Course", Course.class);
            List<Course> courses = query.getResultList();
            session.getTransaction().commit();
            return courses;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Course> get5bestCourses() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query<Course> query = session.createQuery("select course from Course course order by course.enrollments.size desc", Course.class);
            query.setMaxResults(5);
            List<Course> courses = query.getResultList();
            session.getTransaction().commit();
            return courses;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
