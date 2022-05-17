package app.scrumifiedbackend.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;

import java.util.*;

@Data
@JsonFilter(value = "userFilter")
public class UserDto {
    private Boolean isSuccess;
    private List<String> errorTarget;

    public void addErrorTarget(String s) {
        if (errorTarget == null) {
            errorTarget = new ArrayList<>();
        }
        errorTarget.add(s);
    }

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String description;

    private static final List<String> params = Arrays.asList(
            "id", "firstName", "lastName",
            "email", "password", "description",
            "isSuccess", "errorTarget"
    );

    public static Set<String> getFilter(String... exceptElement) {
        Set<String> result = new HashSet<>(params);
        Arrays.asList(exceptElement).forEach(result::remove);
        return result;
    }
}
