package ra.edu.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Course;

import java.util.List;

@Repository
public class CourseUserRepositoryImp implements CourseUserRepository {

    private final SessionFactory sessionFactory;

    public CourseUserRepositoryImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Course> searchCoursesByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Course> query = session.createQuery("FROM Course c WHERE c.name LIKE :name", Course.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        }
    }

    @Override
    public List<Course> findAllCourses() {
        try (Session session = sessionFactory.openSession()) {
            Query<Course> query = session.createQuery("FROM Course where name NOT LIKE '%_delete'", Course.class);
            return query.getResultList();
        }
    }
}
