package com.re.session20.model.dto.request;

import com.re.session20.model.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterRequest {
    @NotBlank(message = "Không được để trống username")
    private String username;
    @NotBlank(message = "Không được để trống password")
    private String password;
    @NotEmpty(message = "Phải có ít nhất 1 role")
    private List<String> roles;
}
