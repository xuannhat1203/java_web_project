package ra.edu.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ra.edu.entity.User;
import ra.edu.enumData.StatusAccount;

import java.util.List;
@Repository
public class StudentRepositoryImp implements StudentRepository {
    public final SessionFactory sessionFactory;
    public StudentRepositoryImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public List<User> listStudentPagination(String search, String sort, int page, int size) {
        Session session = sessionFactory.openSession();
        try {
            StringBuilder hql = new StringBuilder("FROM User c WHERE 1=1");

            if (search != null && !search.trim().isEmpty()) {
                hql.append(" AND LOWER(c.name) LIKE :keyword");
            }
            if ("desc".equalsIgnoreCase(sort)) {
                hql.append(" ORDER BY c.name DESC");
            } else {
                hql.append(" ORDER BY c.name ASC");
            }
            Query<User> query = session.createQuery(hql.toString(), User.class);
            if (search != null && !search.trim().isEmpty()) {
                query.setParameter("keyword", "%" + search.toLowerCase() + "%");
            }
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);

            return query.list();
        } finally {
            session.close();
        }
    }



    @Override
    public boolean updateStatus(int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = session.get(User.class, id);
        if (user == null) {
            session.getTransaction().rollback();
            session.close();
            return false;
        }

        if (user.getStatus() == StatusAccount.ACTIVE) {
            user.setStatus(StatusAccount.INACTIVE);
        } else {
            user.setStatus(StatusAccount.ACTIVE);
        }

        session.update(user);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public long countWithFilter(String keyword){
        Session session = sessionFactory.openSession();
        StringBuilder hql = new StringBuilder("SELECT COUNT(u.id) FROM User u WHERE 1=1");
        if (keyword != null && !keyword.trim().isEmpty()) hql.append(" AND u.name LIKE :keyword");
        Query<Long> query = session.createQuery(hql.toString(), Long.class);
        if (keyword != null && !keyword.trim().isEmpty()) query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
        return query.uniqueResult();
    }
}
