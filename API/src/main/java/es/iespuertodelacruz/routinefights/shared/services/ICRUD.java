package es.iespuertodelacruz.routinefights.shared.services;

import java.util.List;

/**
 * IGeneric
 */
public interface ICRUD<T> {
    /**
     * Method to find all T
     * 
     * @return List<T> list of T
     */
    public List<T> findAll();

    /**
     * Method to find T by id
     * 
     * @param id
     * @return T
     */
    public T findById(String id);

    /**
     * Method to delete T
     * 
     * @param id id
     * @return boolean
     */
    public boolean delete(String id);
}
