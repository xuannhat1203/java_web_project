package ra.edu.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.edu.entity.User;

import java.util.List;

@Repository
public class PersonRepositoryImp implements PersonRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean existsByUsername(String username) {
        Session session = sessionFactory.openSession();
        Query<Long> query = session.createQuery("SELECT COUNT(u.id) FROM User u WHERE u.username = :username", Long.class);
        query.setParameter("username", username);
        Long count = query.uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        Session session = sessionFactory.openSession();
        Query<Long> query = session.createQuery("SELECT COUNT(u.id) FROM User u WHERE u.email = :email", Long.class);
        query.setParameter("email", email);
        Long count = query.uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public boolean existsByPhone(String phone) {
        Session session = sessionFactory.openSession();
        Query<Long> query = session.createQuery("SELECT COUNT(u.id) FROM User u WHERE u.phone = :phone", Long.class);
        query.setParameter("phone", phone);
        Long count = query.uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public void register(User user) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
    }

    @Override
    public User login(String username, String password) {
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("FROM User WHERE username =:username AND password = :password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        return query.uniqueResult();
    }
    @Override
    public boolean updatePassword(int id, String newPassword){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User user = session.get(User.class, id);
        if(user == null){
            session.getTransaction().commit();
            return false;
        }
        user.setPassword(newPassword);
        session.update(user);
        session.getTransaction().commit();
        return true;
    }
    @Override
    public boolean updateProfile(User user) {
        Session session = sessionFactory.openSession();
        User user1 = session.get(User.class, user.getId());
        if (user1 == null) {
            return false;
        }
        String hql = "FROM User u WHERE u.id != :id AND u.name = :name AND u.phone = :phone";
        List<User> duplicates = session.createQuery(hql, User.class)
                .setParameter("id", user.getId())
                .setParameter("name", user.getName())
                .setParameter("phone", user.getPhone())
                .getResultList();

        if (!duplicates.isEmpty()) {
            return false;
        }
        user1.setName(user.getName());
        user1.setPhone(user.getPhone());
        session.beginTransaction();
        session.update(user1);
        session.getTransaction().commit();
        session.close();
        return true;
    }
    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        List<User> users = null;
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery("FROM User", User.class);
            users = query.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return users;
    }


}
