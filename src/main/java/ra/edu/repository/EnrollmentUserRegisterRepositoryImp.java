package ra.edu.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.entity.Enrollment;

import java.util.List;

@Repository
@Transactional
public class EnrollmentUserRegisterRepositoryImp implements EnrollmentUserRegisterRepository {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.openSession();
    }

    @Override
    public boolean registerCourse(Enrollment enrollment) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(enrollment);

        session.getTransaction().commit();
        return true;
    }

    @Override
    public Enrollment findById(int id) {
        return getSession().get(Enrollment.class, id);
    }

    @Override
    public boolean checkEnrollment(int userId, int courseId) {
        String hql = "FROM Enrollment e WHERE e.user.id = :userId AND e.course.id = :courseId";
        Query<Enrollment> query = getSession().createQuery(hql, Enrollment.class);
        query.setParameter("userId", userId);
        query.setParameter("courseId", courseId);
        return !query.getResultList().isEmpty();
    }

    @Override
    public List<Enrollment> findAllRegister() {
        String hql = "FROM Enrollment e ORDER BY e.registered_at DESC";
        return getSession().createQuery(hql, Enrollment.class).getResultList();
    }
}
