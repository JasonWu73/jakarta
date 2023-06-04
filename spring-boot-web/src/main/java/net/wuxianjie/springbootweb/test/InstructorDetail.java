package net.wuxianjie.springbootweb.test;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "instructor_detail")
@Data
@NoArgsConstructor
public class InstructorDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "youtube_channel")
  private String youtubeChannel;

  @Column(name = "hobby")
  private String hobby;

  public InstructorDetail(final String youtubeChannel, final String hobby) {
    this.youtubeChannel = youtubeChannel;
    this.hobby = hobby;
  }
}
