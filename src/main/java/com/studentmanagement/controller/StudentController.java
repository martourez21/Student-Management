package com.studentmanagement.controller;

import com.studentmanagement.entity.Student;
import com.studentmanagement.exception.StudentNotFoundException;
import com.studentmanagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/index")
    public String getHomePage(){
        return "index";
    }

   /* @GetMapping("/students")
    public String get_all_students(Model model){
       //List<Student> students = studentService.getAllStudents();
        model.addAttribute("student", studentService.getAllStudents());
        return "student";
    }*/

    @GetMapping("/students")
    public String get_all_pages(Model model){
        return get_one_page(model, 1);
    }

    @GetMapping("/students/page/{pageNumber}")
    public String get_one_page(Model model, @PathVariable("pageNumber") int currentPage){
        Page<Student> page = studentService.findPage(currentPage);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<Student> student = page.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("student", student);

        return "student";
    }

    @GetMapping("/getone")
    @ResponseBody
    public Student get_student_by_id(long id) throws StudentNotFoundException{
        return studentService.getStudentById(id);
    }


    /*
   @GetMapping("/student/{student_id}") ///replace the s in this endpoint
    public EntityModel<Student> get_student_by_id(@PathVariable long student_id)
            throws StudentNotFoundException {

        EntityModel<Student> resource = EntityModel.of(studentService.getStudentById(student_id));
        /*resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).get_all_students())
                .withRel("get_all_available_students")); */
      //  resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
        //        .get_student_by_department(resource.getContent().getDepartment())).withRel("get_students_by_departments"));

        //return resource;
    //}


    @GetMapping("/students/departments")
    public List<Student> get_student_by_department(@RequestParam("departments") String departments)
            throws StudentNotFoundException {

       return studentService.getAllStudentsByDepartment(departments);
    }

    @PostMapping(value = "/students")
    public String save_student_profile(@ModelAttribute("student") Student student){
       studentService.saveStudent(student);
        return "redirect:/api/v1/students";
    }

    @RequestMapping(value = "/students/update/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String update_student_profile(@RequestBody Student student, @RequestParam("id") long student_id)
            throws StudentNotFoundException {
        ModelAndView mov = new ModelAndView("student");
           mov.addObject("student", studentService.updateStudentInformation(student, student_id));

            return "redirect:/api/v1/students";
    }

    @RequestMapping(value = "/students/update", method = {RequestMethod.PUT, RequestMethod.GET})
    public String update_student(Student student){
        studentService.saveStudent(student);

        return "redirect:/api/v1/students";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String delete_student_profile(long student_id){
        studentService.deleteStudent(student_id);

        return "redirect:/api/v1/students";
    }
}
