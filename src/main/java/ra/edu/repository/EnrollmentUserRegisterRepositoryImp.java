package ra.edu.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Enrollment;

import java.util.List;
import java.util.Optional;

@Repository
public class EnrollmentUserRegisterRepositoryImp implements EnrollmentUserRegisterRepository {
    @Autowired
    private SessionFactory sessionFactory;
    public boolean registerCourse(Enrollment enrollment) {
        Session session = sessionFactory.getCurrentSession();
        session.save(enrollment);
        return true;
    }
    public Enrollment findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Enrollment enrollment = session.get(Enrollment.class, id);
        return enrollment;
    }
    public boolean checkEnrollment(int userId, int courseId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Enrollment e WHERE e.user.id = :userId AND e.course.id = :courseId";
        Query<Enrollment> query = session.createQuery(hql, Enrollment.class);
        query.setParameter("userId", userId);
        query.setParameter("courseId", courseId);
        return true;
    }

    public List<Enrollment> findAllRegister() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Enrollment e ORDER BY e.registered_at DESC";
        Query<Enrollment> query = session.createQuery(hql, Enrollment.class);
        return query.getResultList();
    }
}
