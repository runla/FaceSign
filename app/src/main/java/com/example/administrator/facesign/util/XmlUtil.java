package com.example.administrator.facesign.util;


import com.example.administrator.facesign.entity.Course;
import com.example.administrator.facesign.entity.CourseInfo;
import com.example.administrator.facesign.entity.Student;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Created by Administrator on 2016/10/12.
 */
public class XmlUtil {

    public static CourseInfo xmlToObject_CourseInfo(String xml){
        CourseInfo courseInfo = null;
        Student student = null;
        List<Course> courseList = new ArrayList<>();

        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        //创建DOM解析器工厂
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();



        try {
            //创建DOM解析器
            DocumentBuilder builder = factory.newDocumentBuilder();
            //获取到了document对象
            //Document document = builder.parse(inputStream);

            Document document = builder.parse(inputStream);
            //获得文档的根元素
            Element root = document.getDocumentElement();


            //获得名称为student的元素节点（student只有一个节点）
            Element studentElement = (Element) root.getElementsByTagName("student").item(0);
            student = getStudentObject(studentElement);

            //得到文档中名称为courseInfo的元素的节点列表
            NodeList courseNodeList = root.getElementsByTagName("courseList");
            courseList =getCourseListObject(courseNodeList);

            courseInfo = new CourseInfo(student,courseList);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return courseInfo;
    }

    private static Student getStudentObject(Element element){
        String name = element.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
        String studentid = element.getElementsByTagName("studentid").item(0).getFirstChild().getNodeValue();
        String major = element.getElementsByTagName("major").item(0).getFirstChild().getNodeValue();
        String schools =element.getElementsByTagName("schools").item(0).getFirstChild().getNodeValue();
        String grade = element.getElementsByTagName("grade").item(0).getFirstChild().getNodeValue();

        Student student = new Student(name,studentid,major,schools,grade);
        return student;
    }

    private static List<Course> getCourseListObject(NodeList nodeList){
        List<Course> courseList = new ArrayList<>();
        Course course = null;

        for (int i = 0; i < nodeList.getLength(); i++) {
            //获得一个子节点
            Element element = (Element) nodeList.item(i);

             String courseName = element.getElementsByTagName("courseName").item(0).getFirstChild().getNodeValue();
             String courseId = element.getElementsByTagName("courseId").item(0).getFirstChild().getNodeValue();
             String teacherName = element.getElementsByTagName("teacherName").item(0).getFirstChild().getNodeValue();
             String teacherId = element.getElementsByTagName("teacherId").item(0).getFirstChild().getNodeValue();
             String room = element.getElementsByTagName("room").item(0).getFirstChild().getNodeValue();

             int day = Integer.parseInt(element.getElementsByTagName("day").item(0).getFirstChild().getNodeValue());
             int startWeek = Integer.parseInt(element.getElementsByTagName("startWeek").item(0).getFirstChild().getNodeValue());
             int totalWeeks = Integer.parseInt(element.getElementsByTagName("totalWeeks").item(0).getFirstChild().getNodeValue());
             int startSection = Integer.parseInt(element.getElementsByTagName("startSection").item(0).getFirstChild().getNodeValue());
             int totalSection = Integer.parseInt(element.getElementsByTagName("totalSection").item(0).getFirstChild().getNodeValue());
             int singleOrDouble = Integer.parseInt(element.getElementsByTagName("singleOrDouble").item(0).getFirstChild().getNodeValue());

            course = new Course(courseName,courseId,teacherName,teacherId,room,day,startWeek,totalWeeks,startSection,totalSection,singleOrDouble);
            courseList.add(course);
        }

        return courseList;
    }
}
