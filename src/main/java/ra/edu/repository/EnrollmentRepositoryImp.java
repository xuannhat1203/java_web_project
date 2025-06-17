package ra.edu.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Enrollment;
import ra.edu.enumData.StatusEnrollment;

import java.util.ArrayList;
import java.util.List;
@Repository
public class EnrollmentRepositoryImp implements EnrollmentRepository {
    public SessionFactory sessionFactory;
    public EnrollmentRepositoryImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public List<Enrollment> listHistoryEnrollment(int userId, String sortStatus, String search, int page, int size) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Enrollment> enrollments = new ArrayList<>();
        try {
            transaction = session.beginTransaction();

            StringBuilder hql = new StringBuilder("FROM Enrollment e WHERE e.user.id = :userId");

            if (search != null && !search.trim().isEmpty()) {
                hql.append(" AND LOWER(e.course.name) LIKE :search");
            }

            if (sortStatus != null && !sortStatus.trim().isEmpty()) {
                hql.append(" AND e.status = :status");
            }

            hql.append(" ORDER BY e.registered_at DESC");

            Query<Enrollment> query = session.createQuery(hql.toString(), Enrollment.class);
            query.setParameter("userId", userId);

            if (search != null && !search.trim().isEmpty()) {
                query.setParameter("search", "%" + search.toLowerCase() + "%");
            }

            if (sortStatus != null && !sortStatus.trim().isEmpty()) {
                query.setParameter("status", StatusEnrollment.valueOf(sortStatus)); //
            }

            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);

            enrollments = query.getResultList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return enrollments;
    }
    @Override
    public Long countHistoryEnrollment(int userId, String search) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Long count = 0L;
        try {
            transaction = session.beginTransaction();

            StringBuilder hql = new StringBuilder("SELECT COUNT(e.id) FROM Enrollment e WHERE e.user.id = :userId");

            if (search != null && !search.trim().isEmpty()) {
                hql.append(" AND LOWER(e.course.name) LIKE :search");
            }

            Query<Long> query = session.createQuery(hql.toString(), Long.class);
            query.setParameter("userId", userId);

            if (search != null && !search.trim().isEmpty()) {
                query.setParameter("search", "%" + search.toLowerCase() + "%");
            }

            count = query.uniqueResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return count;
    }
    @Override
    public boolean cancelEnrollment(int id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Enrollment enrollment = session.get(Enrollment.class, id);

            if (enrollment == null) {
                return false; // Không tìm thấy
            }

            // Chỉ được hủy khi đang chờ xác nhận
            if (enrollment.getStatus() != StatusEnrollment.WAITING) {
                return false;
            }

            enrollment.setStatus(StatusEnrollment.CANCEL);
            session.update(enrollment);

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return false;
    }

    @Override
    public List<Enrollment> findEnrollmentsByUserId(int idUser) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Enrollment> enrollments = new ArrayList<>();
        try {
            transaction = session.beginTransaction();
            Query<Enrollment> query = session.createQuery("FROM Enrollment e WHERE e.user.id = :idUser", Enrollment.class);
            query.setParameter("idUser", idUser);
            enrollments = query.getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return enrollments;
    }
}
