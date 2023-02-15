package org.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * ServerSession - is a class which to work with sessions in Hibernate.
 * The doInSessionWithReturn() method creates an EntityManager object and executes a consumer in transaction,
 *  then returns the result of execution.
 * The doInSession() method calls doInSessionWithReturn() but misses the return result.
 * EntityManagerFactory is only one for the server, and EntityManager is created at every user request to DB.
 * */
public class ServerSession {

    private final EntityManagerFactory entityManagerFactory;

    public ServerSession(){
        entityManagerFactory = Persistence.createEntityManagerFactory("catfish_mysql");

    }

    public ServerSession(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    public void doInSession(Consumer<EntityManager> consumer){
        doInSessionWithReturn(entityManager -> {
            consumer.accept(entityManager);
            return null;
        });
    }

    public <T>T doInSessionWithReturn(Function<EntityManager, T> function){
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            T variable = function.apply(entityManager);
            entityManager.getTransaction().commit();
            return variable;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e.getMessage());
        } finally {
            entityManager.close();
        }
    }

}
