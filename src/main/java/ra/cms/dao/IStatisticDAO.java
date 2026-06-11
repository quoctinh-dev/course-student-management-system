package ra.cms.dao;

import ra.cms.dto.CourseStatisticDTO;
import ra.cms.exception.DatabaseException;

import java.util.List;
import java.util.Map;

public interface IStatisticDAO {
    Map<String, Integer> getGeneralOverview() throws DatabaseException;

    List<CourseStatisticDTO> getStudentCountByCourse() throws DatabaseException;

    List<CourseStatisticDTO> getTop5HotCourses() throws DatabaseException;

    List<CourseStatisticDTO> getCrowdedCourses() throws DatabaseException;
}
