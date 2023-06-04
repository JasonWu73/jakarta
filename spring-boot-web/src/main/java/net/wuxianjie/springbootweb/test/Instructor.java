package net.wuxianjie.springbootweb.test;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "instructor")
@Data
@NoArgsConstructor
public class Instructor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "email")
  private String email;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "instructor_detail_id")
  private InstructorDetail instructorDetail;

  public Instructor(final String firstName, final String lastName, final String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }
}
