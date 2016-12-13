package com.example.administrator.facesign.util;


import com.example.administrator.facesign.entity.Course;
import com.example.administrator.facesign.entity.CourseInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/29.
 */

public class TimeUtil {

    //上课时间
    public static final int[] courseSectionStartHour = {8,8,9,10,13,14,15,16,17,19,19,20,21};
    public static final int[] courseSectionStartMinute = {0,50,55,45,40,30,35,25,15,0,50,45,35};
    //下课时间
    public static final int[] courseSectionEndHour = {8,9,10,11,14,15,16,17,18,19,20,21,22};
    public static final int[] courseSectionEndMinute = {45,35,40,30,25,15,20,10,0,45,35,30,20};

    public static final Long M = 60*1000L;
    public static final Long H = 60*M;
    public static final Long D = 24*H;
    public static final Long Week = 7*D;

    public static final int UPLOAD_SUCCESS = 456;           //数据上传成功
    public static final int UPLOAD_FAIL = 457;              //数据上传失败
    public static final int RECOGNIZE_SUCCESS = 458;        //人脸识别成功
    public static final int RECOGNIZE_FAIL = 459;           //人脸识别失败
    public static final int STATUS_NORMAL = 0;            //正常
    public static final int STATUS_LATE = 1;              //迟到
    public static final int STATUS_ABSENT = 3;            //旷课
    public static final int STATUS_EARLY = 2;             //早退
    public static final int FIRST_SIGN_UP = 1;              //上课签到
    public static final int SECOND_SIGN_DOWN = 2;           //下课签到

    public static Course getNearestCourse(CourseInfo courseInfo){
        //开始上课的日期
        Date startDate = courseInfo.getEduTerm().getStartDate();
        //获取现在是星期几
        int currentDay = getDay();
        //获取当前日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date currentDate = new Date();
        String dateStr = dateFormat.format(currentDate);
        //当前时
        int currentHour = Integer.parseInt(dateStr.split(" ")[1].split(":")[0]);
        //当前分钟
        int currentMinute = Integer.parseInt(dateStr.split(" ")[1].split(":")[1]);
        //单双周 1为单周、2为双周
        int singleOrDouble = getCurrentWeek(currentDate)%2;
        if (singleOrDouble == 0) {
            singleOrDouble=2;
        }

        Course nearestCourse = null;
        if (courseInfo.getCourseList().size() != 0) {
            nearestCourse = courseInfo.getCourseList().get(0);
        }
        Long minTime=currentDate.getTime()*100;//毫秒为单位
        for (Course course : courseInfo.getCourseList()) {
            Long tempMin = -1L;
            //当天的课程
            if (currentDay == course.getDay()){

                if (currentHour*H+currentMinute*M<=courseSectionStartHour[course.getStartSection()-1]*H+courseSectionStartMinute[course.getStartSection()-1]*M) {
                    //当前周该课需要上课
                    if (course.getSingleOrDouble() ==0||course.getSingleOrDouble() == singleOrDouble) {
                        tempMin = getClassUpTime(startDate,getCurrentWeek(startDate)-1,course.getDay(),course.getStartSection());
                    }
                }
                else {
                    //下周该课上课
                    if (course.getSingleOrDouble() ==0||course.getSingleOrDouble() != singleOrDouble){
                        tempMin = getClassUpTime(startDate,getCurrentWeek(startDate),course.getDay(),course.getStartSection());
                    }
                }
            }
            //当周的课程
            else if (currentDay < course.getDay()){
                //当前周该课需要上课
                if (course.getSingleOrDouble() ==0||course.getSingleOrDouble() == singleOrDouble) {
                    tempMin = getClassUpTime(startDate,getCurrentWeek(startDate)-1,course.getDay(),course.getStartSection());
                }
            }
            //下周的课程
            else {
                //下周该课上课
                if (course.getSingleOrDouble() ==0||course.getSingleOrDouble() + singleOrDouble == 3){
                    tempMin = getClassUpTime(startDate,getCurrentWeek(startDate),course.getDay(),course.getStartSection());
                }
            }
            if (tempMin != -1L && tempMin < minTime) {
                minTime = tempMin;
                nearestCourse = course;
            }
        }
        return nearestCourse;
    }



    /**
     * 获取下节课上课的时间从1970.1.1 算起，毫秒为单位
     * @param courseInfo
     * @return
     */

    public static Long getNearestTime(CourseInfo courseInfo){
        //开始上课的日期
        Date startDate = courseInfo.getEduTerm().getStartDate();
        //获取现在是星期几
        int currentDay = getDay();
        //获取当前日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        String dateStr = dateFormat.format(currentDate);
        //当前时
        int currentHour = Integer.parseInt(dateStr.split(" ")[1].split(":")[0]);
        //当前分钟
        int currentMinute = Integer.parseInt(dateStr.split(" ")[1].split(":")[1]);
        //单双周 1为单周、2为双周
        int singleOrDouble = getCurrentWeek(currentDate)%2;
        if (singleOrDouble == 0) {
            singleOrDouble=2;
        }

        Long minTime=currentDate.getTime()*100;//毫秒为单位
        for (Course course : courseInfo.getCourseList()) {
            Long tempMin = -1L;
            //当天的课程
            if (currentDay == course.getDay()){

                if (currentHour*H+currentMinute*M<=courseSectionStartHour[course.getStartSection()-1]*H+courseSectionStartMinute[course.getStartSection()-1]*M) {
                    //当前周该课需要上课
                    if (course.getSingleOrDouble() ==0||course.getSingleOrDouble() == singleOrDouble) {
                        tempMin = getClassUpTime(startDate,getCurrentWeek(startDate)-1,course.getDay(),course.getStartSection());
                    }
                }
                else {
                    //下周该课上课
                    if (course.getSingleOrDouble() ==0||course.getSingleOrDouble() != singleOrDouble){
                        tempMin = getClassUpTime(startDate,getCurrentWeek(startDate),course.getDay(),course.getStartSection());
                    }
                }
            }
            //当周的课程
            else if (currentDay < course.getDay()){
                //当前周该课需要上课
                if (course.getSingleOrDouble() ==0||course.getSingleOrDouble() == singleOrDouble) {
                    tempMin = getClassUpTime(startDate,getCurrentWeek(startDate)-1,course.getDay(),course.getStartSection());
                }
            }
            //下周的课程
            else {
                //下周该课上课
                if (course.getSingleOrDouble() ==0||course.getSingleOrDouble() + singleOrDouble == 3){
                    tempMin = getClassUpTime(startDate,getCurrentWeek(startDate),course.getDay(),course.getStartSection());
                }
            }
            if (tempMin != -1L && tempMin < minTime) {
                minTime = tempMin;
            }
        }
        return minTime;
    }


    /**
     * 获取上课的时间
     * @param startSection
     * @return
     */
    public static String getClassStartTime(int startSection)
    {
        String hour = courseSectionStartHour[startSection-1] > 9 ?""+courseSectionStartHour[startSection-1]:"0"+courseSectionStartHour[startSection-1];
        String minute = courseSectionStartMinute[startSection-1] > 9 ?""+courseSectionStartMinute[startSection-1]:"0"+courseSectionStartMinute[startSection-1];
        return new String(hour+":"+minute);
    }

    /**
     * 获取下课时间
     * @param endSection
     * @return
     */
    public static String getClassEndTime(int endSection){
        String hour = courseSectionEndHour[endSection-1] > 9 ?""+courseSectionEndHour[endSection-1]:"0"+courseSectionEndHour[endSection-1];
        String minute = courseSectionEndMinute[endSection-1] > 9 ?""+courseSectionEndMinute[endSection-1]:"0"+courseSectionEndMinute[endSection-1];
        return new String(hour+":"+minute);
    }

    //获取当前的时间（格式为：12:11:01）
    public static String getCurrentTime(){
        Date date = new Date();
        return String.format("%tH:%tM:%tS",date,date,date);
    }

    /**
     * 获取距离现在下一节课的上课时间描述
     * @param time
     * @return
     */
    public static String timeDescribe(Long time){
        //Date date = new Date(time);
        Date now = new Date();
        Long between = time - now.getTime();
        Long day = between / D;
        Long hour = between % D / H;
        Long min = (between % D % H)/M;
        return new String(day+"天"+hour+"时"+min+"分");
    }



    /**
     * 获取当天是星期几1-7
     */

    public static int getDay(){
        String str[]={"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};//字符串数组
        Calendar rightNow= Calendar.getInstance();
        int day=rightNow.get(rightNow.DAY_OF_WEEK);//获取时间
        if (day == 1) {
            day = 7;
        }
        else{
            day -= 1;
        }
        System.out.println("今天是"+str[day-1]);//通过数组把周几输出
        return day;
    }



    /**
     * 判断当前周是为单？双周,1代表单周，0代表双周
     *
     */
    public static int singleOrDoubleWeek(int currentWeek){
        return currentWeek%2;
    }




    /**
     * 获取当前是第几周
     * @param startDate
     * @return
     */
    public static int getCurrentWeek(Date startDate){
        Date currentDate =new Date();
        return (int)((currentDate.getTime() - startDate.getTime())/Week)+1;
    }


    /**
     * 获取某节课的上课时间，时间从1970.1.1开始算起，以毫秒为单位
     * @param startDate         开始上课的日期（该日期应该是从00:00:00开始的）
     * @param currentWeek       当前是第几周
     * @param day                 当天是星期几
     * @param startSec          将要下节课是哪一个开始
     * @return
     */
    public static Long getClassUpTime(Date startDate, int currentWeek, int day, int startSec){
        Long time = startDate.getTime()
                + Week*(currentWeek)
                + D*(day-1)
                + courseSectionStartHour[startSec-1]*H
                + courseSectionStartMinute[startSec-1]*M;
       // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return time;
    }



    /**
     * 获取某节课的下课时间,时间从1970.1.1开始算起，以毫秒为单位
     * @param startDate
     * @param currentWeek
     * @param day
     * @param endSec
     * @return
     */
    public static Long getClassDownTime(Date startDate, int currentWeek, int day, int endSec){
        Long time = startDate.getTime()
                + Week*(currentWeek)
                + D*(day-1)
                + courseSectionEndHour[endSec-1]*H
                + courseSectionEndMinute[endSec-1]*M;
       // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return time;
    }




    /**
     * 判断学生签到是否有迟到、旷课或早退的现象
     * @param startDate
     * @param currentWeek
     * @param day
     * @param startSec
     * @return
     */
    public static int getSignUpStatus(Date startDate, int currentWeek, int day, int startSec,int endSec){

        //上课的时间
        final long classUpTime = TimeUtil.getClassUpTime(startDate,currentWeek-1,day,startSec);
        //下课时间
        final long classDownTime = TimeUtil.getClassDownTime(startDate,currentWeek-1,day,endSec);
        //当前时间
        long currentTime = System.currentTimeMillis();

        //上课前 10 分钟为正常
        if (classUpTime - 10 * TimeUtil.M <= currentTime && currentTime <= classUpTime){
            return STATUS_NORMAL;//正常
        }
        //上课后 10 分钟为迟到
        else if (classUpTime  < currentTime && currentTime <= classUpTime + 10*TimeUtil.M){
            return STATUS_LATE;//迟到
        }
        //上课后 10 分钟  ---  下课前 10 分钟）
        else if (classUpTime + 10* TimeUtil.M < currentTime && currentTime <= classDownTime - 10* TimeUtil.M){
            return STATUS_ABSENT;//旷课
        }
        //超过上课的时间了
        else {
            return -1;//不是上课时间
        }

       /* //当前时间
        Long currentTime = new Date().getTime();
        //上课时间
        Long classUpTime = getClassUpTime(startDate,currentWeek,day,startSec);
        Long between = (classUpTime - currentTime)/M;//分钟
        if (between >= 0 && between <= 20){
            return STATUS_NORMAL;//正常
        }
        else if (between < 0 && between >= -10){
            return STATUS_LATE;//迟到
        }
        else if (between <= -10 && between >= -45){
            return STATUS_ABSENT;//旷课
        }
        else {
            return -1;//不是上课时间
        }*/
    }


    /**
     * 判断学生下课签到是否存在早退的现象
     * @param startDate
     * @param currentWeek
     * @param day
     * @param endSec
     * @return
     */
    public static int getSignDownStatus(Date startDate, int currentWeek, int day, int startSec,int endSec){
        //上课的时间
        final long classUpTime = TimeUtil.getClassUpTime(startDate,currentWeek-1,day,startSec);
        //下课时间
        final long classDownTime = TimeUtil.getClassDownTime(startDate,currentWeek-1,day,endSec);
        //当前时间
        long currentTime = System.currentTimeMillis();

        //下课前10分钟 -- 下课后10分钟为正常
        if (classDownTime-10*M <= currentTime && currentTime <= classDownTime+10*M) {
            return STATUS_NORMAL;
        }
        //早退
        else if (classUpTime + 10*M < currentTime && currentTime <= classDownTime-10*M){
            return STATUS_EARLY;
        }
        else {
            return -1;
        }

        /*//当前时间
        Long currentTime = new Date().getTime();
        //下课时间
        Long classDownTime = getClassDownTime(startDate,currentWeek,day,endSec);
        Long between = (currentTime - classDownTime)/M;//分钟

        //只有在下课前后10分钟内进行敲到下课才表示下课签到正常，否则都是早退
       if (between >= 10 || between < -10){
            return STATUS_EARLY;
        }
        else if (between < 10 && between >= -10){
           return STATUS_NORMAL;
       }
        else {
           return -1;
       }*/
    }


    /**
     * 判断当前是否上课签到的时间
     * @param startDate
     * @param currentWeek
     * @param day
     * @param startSec
     * @return
     */
  /*  public static boolean isClassUpTime(Date startDate, int currentWeek, int day, int startSec,int endSec){
        //当前时间
        Long currentTime = new Date().getTime();
        //上课时间
        Long classUpTime = getClassUpTime(startDate,currentWeek,day,startSec);
        //下课时间
        final long classDownTime = TimeUtil.getClassDownTime(startDate,currentWeek-1,day,endSec);

        //显示上课签到按钮（上课前10分钟  ---  下课前10分钟）
        if (classUpTime - 10 * TimeUtil.M <= currentTime && currentTime < classDownTime - 10* TimeUtil.M) {

        }
        //显示下课签到按钮（下课前10分钟  ---  下课后10分钟）
        else if (classDownTime - 10* TimeUtil.M <= currentTime && currentTime <= classDownTime + 10* TimeUtil.M){

        }
        //超过上课签到的时间了
        else if (currentTime > classDownTime + 10* TimeUtil.M){

        }

            Long between = (currentTime - classUpTime)/M;//分钟

        if (between >= -10 && between <= 45){//签到时间
            return true;
        }
        else {//不是签到时间
            return false;
        }

    }
*/
    /**
     * 判断当前下课签到是否为早退
     * @param startDate
     * @param currentWeek
     * @param day
     * @param endSec
     * @return
     */
 /*   public static boolean isClassDownEarly(Date startDate, int currentWeek, int day, int startSec,int endSec){
        //当前时间
        Long currentTime = new Date().getTime();
        //上课时间
        Long classUpTime = getClassUpTime(startDate,currentWeek,day,startSec);
        //下课时间
        final long classDownTime = TimeUtil.getClassDownTime(startDate,currentWeek-1,day,endSec);

        //显示上课签到按钮（上课前10分钟  ---  下课前10分钟）
        if (classUpTime - 10 * TimeUtil.M <= currentTime && currentTime < classDownTime - 10* TimeUtil.M) {
            return true;
        }
        //显示下课签到按钮（下课前10分钟  ---  下课后10分钟）
        else if (classDownTime - 10* TimeUtil.M <= currentTime && currentTime <= classDownTime + 10* TimeUtil.M){
            return false;
        }
        //超过上课签到的时间了
        else if (currentTime > classDownTime + 10* TimeUtil.M){
            return true;
        }
        return false;

        //当前时间
        Long currentTime = new Date().getTime();
        //上课时间
        Long classDownTime = getClassDownTime(startDate,currentWeek,day,endSec);
        Long between = (currentTime - classDownTime)/M;//分钟
        //只有在下课前后10分钟内进行敲到下课才表示下课签到正常，否则都是早退
        if (between < -10){
            return true;
        }
        return false;
    }
*/

    }
