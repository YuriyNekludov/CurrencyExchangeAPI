package edu.project.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {

    T addElement(T entity) throws SQLException;

    List<T> getAllElements() throws SQLException;
}
