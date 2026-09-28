package murach.data;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import murach.business.User;

public class UserRepository {
    public User save(User user) {
        EntityManager entityManager = JpaUtil.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(user);
            transaction.commit();
            return user;
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    public Optional<User> findById(Long id) {
        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            return Optional.ofNullable(entityManager.find(User.class, id));
        } finally {
            entityManager.close();
        }
    }

    public List<User> findAll() {
        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            return entityManager.createQuery(
                    "select user from User user order by user.id",
                    User.class
            ).getResultList();
        } finally {
            entityManager.close();
        }
    }
}
