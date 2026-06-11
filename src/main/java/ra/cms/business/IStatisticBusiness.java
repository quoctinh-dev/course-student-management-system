package ra.cms.business;

import ra.cms.dto.CourseStatisticDTO;
import ra.cms.exception.DatabaseException;

import java.util.List;
import java.util.Map;

public interface IStatisticBusiness {
    Map<String, Integer> getGeneralOverview() throws DatabaseException;

    List<CourseStatisticDTO> getStudentCountByCourse() throws DatabaseException;

    List<CourseStatisticDTO> getTop5HotCourses() throws DatabaseException;

    List<CourseStatisticDTO> getCrowdedCourses() throws DatabaseException;

    // PHÂN TRANG NÂNG CAO
    List<CourseStatisticDTO> getStudentCountByCourseWithPagination(int page, int size) throws DatabaseException;
    int countTotalCoursesForStatistic() throws DatabaseException;
    List<CourseStatisticDTO> getCrowdedCoursesWithPagination(int page, int size) throws DatabaseException;
    int countCrowdedCoursesForStatistic() throws DatabaseException;
}