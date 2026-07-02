
package com.group.express.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TokenDTO {
    private String grantType;

    private String accessToken;

    private Long tokenExpiresIn;
}
