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
      createInstructor();
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
}
