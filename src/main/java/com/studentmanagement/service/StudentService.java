package com.studentmanagement.service;

import com.studentmanagement.entity.Student;
import com.studentmanagement.exception.StudentNotFoundException;
import com.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student>  getAllStudents(){
        return studentRepository.findAll();
    }

    public Page<Student> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber-1, 5);
        return studentRepository.findAll(pageable);
    }
    public Student getStudentById(long student_id)
            throws StudentNotFoundException{
        Optional<Student> student = studentRepository.findById(student_id);

        if(!student.isPresent()){
            throw new StudentNotFoundException("Student with this Id: "+student_id+ " is not found!");
        }
      return  student.get();
    }

    public List<Student> getAllStudentsByDepartment(String department)
            throws StudentNotFoundException {
        Optional<List<Student>> students = Optional.ofNullable(studentRepository.findByDepartment(department));

        if(!students.isPresent()){
            throw new StudentNotFoundException("Student department: "+department+ " is not found!");
        }
        return students.get();
    }

    public void saveStudent(Student student){
        studentRepository.save(student);
    }

    public Student updateStudentInformation(Student student, long student_id)
            throws StudentNotFoundException{
        Student studDB = studentRepository.findById(student_id).get();

        if(Objects.nonNull(student.getFirstName())
            && !"".equalsIgnoreCase(student.getFirstName())){
            studDB.setFirstName(student.getFirstName());
        }

        if(Objects.nonNull(student.getLastName())
            &&!"".equalsIgnoreCase(student.getLastName())){
            studDB.setLastName(student.getLastName());
        }

        if(Objects.nonNull(student.getUpdatedBy())
            && !"".equalsIgnoreCase(student.getUpdatedBy())){
            studDB.setUpdatedBy(student.getUpdatedBy());
        }
        if(Objects.nonNull(student.getDepartment())
            && !"".equalsIgnoreCase(student.getDepartment())){
            studDB.setDepartment(student.getDepartment());
        }

        return studentRepository.save(studDB);
    }

    public void deleteStudent(long student_id){
        studentRepository.deleteById(student_id);
    }


}
