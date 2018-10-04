package pl.wojtektrzos.filmkrecimy.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@NoArgsConstructor
@Setter
@Getter
public class PostDto {
    private String postText;
    private LocalDateTime created;
    private String authorsImageURI;
    private String authorsName;
}
