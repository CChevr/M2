package fr.uge.jee.hellosession;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TokenManager {
    private final Map<String, Long> clients = new HashMap<>();

    /**
     * Génère un nouveau token non présent dans la liste des clients
     * Appeler cette fonction dans un bloc Synchronized
     * @return Un nouveau token
     */
    private String generateToken() {
        String newToken;

        do {
            newToken = UUID.randomUUID().toString();
        } while (clients.containsKey(newToken));

        return newToken;
    }

    /**
     * Verifie si le token spécifié existe
     * @param token token à vérifier
     * @return renvoie true si le token existe, false sinon
     */
    protected boolean exists(String token) {
        Objects.requireNonNull(token);

        return clients.containsKey(token);
    }

    /**
     * Ajoute un nouveau client dans la liste des clients
     * @return renvoie le nombre de visite du client
     */
    public String addClient() {
        String token;

        synchronized (clients) {
            token = generateToken();
            clients.put(token, 1L);
        }

        return token;
    }

    /**
     * Met à jour le client s'il existe, sinon en créé un nouveau
     * @param token token du client
     * @return le nombre de visite de ce client
     */
    public long visitClient(String token) {
        Long time;

        synchronized(clients) {
            time = clients.computeIfPresent(token, (key, value) -> ((Long) (value + 1L)));
        }

        if (time == null) {
            time = -1L;
        }

        return time;
    }
}
