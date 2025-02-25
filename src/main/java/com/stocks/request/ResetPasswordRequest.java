package com.stocks.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ResetPasswordRequest {
 
    private String email;
    private String currentPassword;
    private String newPassword;
    private String confNewPassword;
    
}
