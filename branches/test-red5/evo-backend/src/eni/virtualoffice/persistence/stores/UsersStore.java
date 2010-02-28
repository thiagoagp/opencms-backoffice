/**
 *
 */
package eni.virtualoffice.persistence.stores;

import javax.persistence.Query;

import org.red5.logging.Red5LoggerFactory;
import org.slf4j.Logger;

import eni.virtualoffice.persistence.entities.Esaminatore;
import eni.virtualoffice.util.Constants;
import eni.virtualoffice.util.Util;

/**
 * This store holds method to
 * query users (esaminatori and esaminandi)
 * entities.
 *
 * @author Giuseppe Miscione
 *
 */
public class UsersStore extends GenericStore{

	private static Logger LOG = Red5LoggerFactory.getLogger(UsersStore.class, Constants.contextName);

	public boolean checkEsaminatoreCredentials(String username, String password) {
		boolean ret = false;
		if(em != null) {
			try {
				Esaminatore.checkAdminExistence(em);

				em.getTransaction().begin();
				String query =
					"SELECT t " +
					"FROM Esaminatore t " +
					"WHERE nickname = :nick";
				Query pQuery = em.createQuery(query);
				pQuery.setParameter("nick", username);
				Esaminatore es = null;
				try {
					es = (Esaminatore)pQuery.getSingleResult();
					if(es.getPassword().equals(Util.encodeUserPassword(password))) {
						ret = true;
					}
				} catch(Exception e) {
					LOG.error(e.getMessage(), e);
				}
				em.getTransaction().commit();

			} catch (Exception e) {
				LOG.error("An error occurred while checking credentials: " + e.getMessage(), e);
				em.getTransaction().rollback();
			} finally {
				closeEntityManager();
			}

		}
		else {
			LOG.warn("The entity manager cannot be initialized.");
		}
		return ret;
	}

}
