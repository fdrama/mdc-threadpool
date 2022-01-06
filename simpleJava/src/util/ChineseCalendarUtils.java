package util;


import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author fdrama
 * @date 2022/01/06
 **/
public class ChineseCalendarUtils {

    private final static String SIMPLE_DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    private final static ZoneOffset DEFAULT_ZONE_OFF_SET = ZoneOffset.of("+8");

    /**
     * 获取工作日集合
     * 遍历日期，去除周六 周日, 去除法定节假日，加上额外工作日
     *
     * @param date             当前日期
     * @param holidayList      法定节假日列表
     * @param extraWorkDayList 额外工作日列表
     */
    public static List<String> getWorkDayList(LocalDate date, List<String> holidayList, List<String> extraWorkDayList) {

        LocalDate start = getFirstDayOfYear(date);
        LocalDate end = getLastDayOfYear(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SIMPLE_DATE_FORMAT_PATTERN);

        Set<String> workDaySet = new LinkedHashSet<>();

        for (LocalDate currentDate = start; currentDate.isBefore(end) || currentDate.isEqual(end); currentDate = currentDate.plusDays(1)) {

            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                continue;
            }
            workDaySet.add(currentDate.format(formatter));
        }
        workDaySet.removeAll(holidayList);
        workDaySet.addAll(extraWorkDayList);

        List<String> workDayList = new ArrayList<>(workDaySet);

        workDayList.sort(new DateComparator());
        return workDayList;
    }

    /**
     * 获取休息日集合
     * 遍历日期，周六 周日 去除额外加班日 加上法定节假日
     *
     * @param date             当前日期
     * @param holidayList      法定节假日列表
     * @param extraWorkDayList 额外工作日列表
     */
    public static List<String> getRestDayList(LocalDate date, List<String> holidayList, List<String> extraWorkDayList) {

        LocalDate start = getFirstDayOfYear(date);
        LocalDate end = getLastDayOfYear(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SIMPLE_DATE_FORMAT_PATTERN);

        Set<String> restDaySet = new LinkedHashSet<>();

        for (LocalDate currentDate = start; currentDate.isBefore(end) || currentDate.isEqual(end); currentDate = currentDate.plusDays(1)) {

            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                restDaySet.add(currentDate.format(formatter));
            }
        }

        restDaySet.addAll(holidayList);
        restDaySet.removeAll(extraWorkDayList);
        List<String> restDayList = new ArrayList<>(restDaySet);
        restDayList.sort(new DateComparator());
        return restDayList;
    }

    /**
     * 判断是否是工作日
     *
     * @param time             当前日期
     * @param holidayList      法定节假日
     * @param extraWorkDayList 额外工作日
     * @return true /false
     */
    public static boolean isWorkingDay(long time, List<String> holidayList, List<String> extraWorkDayList) {

        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), DEFAULT_ZONE_OFF_SET);

        String dateStr = dateTime.format(DateTimeFormatter.ofPattern(SIMPLE_DATE_FORMAT_PATTERN));

        //是否额外工作日
        if (extraWorkDayList.contains(dateStr)) {
            return true;
        }
        //是否节假日
        if (holidayList.contains(dateStr)) {
            return false;
        }
        //是否是周末
        DayOfWeek week = dateTime.getDayOfWeek();
        if (week == DayOfWeek.SATURDAY || week == DayOfWeek.SUNDAY) {
            return false;
        }
        return true;
    }

    private static LocalDate getFirstDayOfYear(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.firstDayOfYear());
    }

    private static LocalDate getLastDayOfYear(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.lastDayOfYear());
    }

    static class DateComparator implements Comparator<String> {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SIMPLE_DATE_FORMAT_PATTERN);

        @Override
        public int compare(String o1, String o2) {
            if (LocalDate.parse(o1, formatter).isBefore(LocalDate.parse(o2, formatter))) {
                return -1;
            } else if (LocalDate.parse(o1, formatter).isAfter(LocalDate.parse(o2, formatter))) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static void main(String[] args) {

        List<String> holidayList = Arrays.asList(
                "2022-01-01", "2022-01-02", "2022-01-03",
                "2022-01-31", "2022-02-01", "2022-02-02",
                "2022-02-03", "2022-02-04", "2022-02-05",
                "2022-02-06", "2022-04-03", "2022-04-04",
                "2022-04-05", "2022-04-30", "2022-05-01",
                "2022-05-02", "2022-05-03", "2022-05-04",
                "2022-06-03", "2022-06-04", "2022-06-05",
                "2022-09-10", "2022-09-11", "2022-09-12",
                "2022-10-01", "2022-10-02", "2022-10-03",
                "2022-10-04", "2022-10-05", "2022-10-06", "2022-10-07");
        List<String> makeUpWorkDayList = Arrays.asList(
                "2022-01-29", "2022-01-30", "2022-04-02",
                "2022-04-24", "2022-05-07", "2022-10-08", "2022-10-09");


        LocalDate now = LocalDate.now();

        List<String> workDay = getWorkDayList(now, holidayList, makeUpWorkDayList);


        List<String> restDayList = getRestDayList(now, holidayList, makeUpWorkDayList);


    }


}
