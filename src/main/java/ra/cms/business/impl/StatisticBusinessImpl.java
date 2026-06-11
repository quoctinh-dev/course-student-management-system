package ra.cms.business.impl;

import ra.cms.business.IStatisticBusiness;
import ra.cms.dao.IStatisticDAO;
import ra.cms.dao.impl.StatisticDAOImpl;
import ra.cms.dto.CourseStatisticDTO;
import ra.cms.exception.DatabaseException;

import java.util.List;
import java.util.Map;

public class StatisticBusinessImpl implements IStatisticBusiness {

    private final IStatisticDAO statisticDAO = new StatisticDAOImpl();

    @Override
    public Map<String, Integer> getGeneralOverview() throws DatabaseException {
        return statisticDAO.getGeneralOverview();
    }

    @Override
    public List<CourseStatisticDTO> getStudentCountByCourse() throws DatabaseException {
        return statisticDAO.getStudentCountByCourse();
    }

    @Override
    public List<CourseStatisticDTO> getTop5HotCourses() throws DatabaseException {
        return statisticDAO.getTop5HotCourses();
    }

    @Override
    public List<CourseStatisticDTO> getCrowdedCourses() throws DatabaseException {
        return statisticDAO.getCrowdedCourses();
    }

    // PHÂN TRANG NÂNG CAO
    @Override
    public List<CourseStatisticDTO> getStudentCountByCourseWithPagination(int page, int size) throws DatabaseException {
        return statisticDAO.getStudentCountByCourseWithPagination(page, size);
    }

    @Override
    public int countTotalCoursesForStatistic() throws DatabaseException {
        return statisticDAO.countTotalCoursesForStatistic();
    }

    @Override
    public List<CourseStatisticDTO> getCrowdedCoursesWithPagination(int page, int size) throws DatabaseException {
        return statisticDAO.getCrowdedCoursesWithPagination(page, size);
    }

    @Override
    public int countCrowdedCoursesForStatistic() throws DatabaseException {
        return statisticDAO.countCrowdedCoursesForStatistic();
    }
}