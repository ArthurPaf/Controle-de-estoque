package venda.p2.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    
    
    private static final EntityManagerFactory FACTORY = 
            Persistence.createEntityManagerFactory("siscom-pu");

    
    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }
}