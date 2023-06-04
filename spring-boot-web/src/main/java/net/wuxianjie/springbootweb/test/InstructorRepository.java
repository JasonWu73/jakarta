package net.wuxianjie.springbootweb.test;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InstructorRepository {

  private final EntityManager entityManager;

  @Transactional(rollbackFor = Exception.class)
  public void save(final Instructor instructor) {
    entityManager.persist(instructor);
  }

  public Optional<Instructor> findById(final int instructorId) {
    return Optional.ofNullable(entityManager.find(Instructor.class, instructorId));
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteInstructorById(final int instructorId) {
    final Instructor instructor = entityManager.find(Instructor.class, instructorId);

    entityManager.remove(instructor);
  }
}
