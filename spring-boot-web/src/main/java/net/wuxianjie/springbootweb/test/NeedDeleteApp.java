package net.wuxianjie.springbootweb.test;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NeedDeleteApp {

  private final InstructorRepository instructorRepository;

  @Bean
  public CommandLineRunner commandLineRunner() {
    return args -> {
//      createInstructor();
//      getInstructor();
//      deleteInstructor();
//      getInstructorDetail();
      deleteInstructorDetail();
    };
  }

  private void createInstructor() {
    // create the instructor
    final Instructor instructorToSave = new Instructor("jason", "wu", "jason@gmail.com");

    // create the instructor detail
    final InstructorDetail instructorDetail = new InstructorDetail(
      "http://www.jason73.com/youtube",
      "Hello World!!!"
    );

    // associate the objects
    instructorToSave.setInstructorDetail(instructorDetail);

    // save the instructor
    System.out.println("Saving instructor: " + instructorToSave);
    instructorRepository.save(instructorToSave);
  }

  private void getInstructor() {
    // eager
    final Instructor instructor = instructorRepository.findById(1).orElseThrow();
    System.out.println(instructor.getEmail());
  }

  private void deleteInstructor() {
    instructorRepository.deleteInstructorById(1);
  }

  private void getInstructorDetail() {
    // eager
    final InstructorDetail instructorDetail = instructorRepository.findByInstructorDetailId(2).orElseThrow();

    System.out.println(instructorDetail.getHobby());
    System.out.println(instructorDetail.getInstructor().getFirstName());
  }

  private void deleteInstructorDetail() {
    instructorRepository.deleteInstructorById(2);
  }
}
