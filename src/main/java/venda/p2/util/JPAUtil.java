package venda.p2.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    
    // Cria a fábrica de conexões lendo o seu persistence.xml (uma única vez)
    private static final EntityManagerFactory FACTORY = 
            Persistence.createEntityManagerFactory("siscom-pu");

    // Método que o seu GenericDAO vai usar para pegar a conexão aberta
    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }
}