package ra.edu.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Enrollment;
import ra.edu.enumData.StatusEnrollment;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EnrollmentAdminRepositoryImp implements EnrollmentAdminRepository {
    public SessionFactory sessionFactory;

    public EnrollmentAdminRepositoryImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Enrollment> listEnrollmentAdmin(String search, String statusFilter, int page, int size) {
        List<Enrollment> result = new ArrayList<>();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            StringBuilder hql = new StringBuilder(
                    "SELECT e FROM Enrollment e " +
                            "JOIN FETCH e.user " +
                            "JOIN FETCH e.course " +
                            "WHERE 1=1"
            );

            if (search != null && !search.isEmpty()) {
                hql.append(" AND e.course.name LIKE :search");
            }

            if (statusFilter != null && !statusFilter.isEmpty() && isValidStatus(statusFilter)) {
                hql.append(" AND e.status = :statusFilter");
            }

            Query<Enrollment> query = session.createQuery(hql.toString(), Enrollment.class);

            if (search != null && !search.isEmpty()) {
                query.setParameter("search", "%" + search + "%");
            }

            if (statusFilter != null && !statusFilter.isEmpty() && isValidStatus(statusFilter)) {
                query.setParameter("statusFilter", StatusEnrollment.valueOf(statusFilter));
            }

            result = query.setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public Long countEnrollments(String search, String statusFilter) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            StringBuilder hql = new StringBuilder("SELECT COUNT(e.id) FROM Enrollment e WHERE 1=1");

            if (search != null && !search.isEmpty()) {
                hql.append(" AND e.course.name LIKE :search");
            }

            if (statusFilter != null && !statusFilter.isEmpty() && isValidStatus(statusFilter)) {
                hql.append(" AND e.status = :statusFilter");
            }

            Query<Long> query = session.createQuery(hql.toString(), Long.class);

            if (search != null && !search.isEmpty()) {
                query.setParameter("search", "%" + search + "%");
            }

            if (statusFilter != null && !statusFilter.isEmpty() && isValidStatus(statusFilter)) {
                query.setParameter("statusFilter", StatusEnrollment.valueOf(statusFilter));
            }

            Long result = query.uniqueResult();
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
    public boolean changeStatus(int idEnrollment, String status) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            Enrollment enrollment = session.get(Enrollment.class, idEnrollment);
            if (enrollment == null) {
                session.getTransaction().commit();
                return false;
            }
            if (enrollment.getStatus() == StatusEnrollment.WAITING) {
                enrollment.setStatus(StatusEnrollment.valueOf(status.toUpperCase()));
            }
            session.update(enrollment);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
    @Override
    public Enrollment findById(int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            Enrollment enrollment = session.get(Enrollment.class, id);
            session.getTransaction().commit();
            return enrollment;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
    private boolean isValidStatus(String status) {
        try {
            StatusEnrollment.valueOf(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
