package ra.cms.dao;

import ra.cms.exception.DatabaseException;
import ra.cms.model.Course;
import java.util.List;
import java.util.Optional;

public interface ICourseDAO {
    List<Course> findAll() throws DatabaseException;
    void save(Course course) throws DatabaseException;
    boolean existsByName(String name) throws DatabaseException;
    Optional<Course> findById(Long id) throws DatabaseException;
    boolean existsByNameAndIdNot(String name, Long id) throws DatabaseException;
    void update(Course course) throws DatabaseException;
    boolean hasEnrollments(Long courseId) throws DatabaseException;
    void deleteById(Long id) throws DatabaseException;
    List<Course> findByNameContaining(String keyword) throws DatabaseException;
    List<Course> findAllSorted(String sortField, String direction) throws DatabaseException;
}