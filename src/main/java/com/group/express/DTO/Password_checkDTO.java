package com.group.express.DTO;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Password_checkDTO {
    private String oldPass;
    private String newPass;
    private String newPass_confirm;

}
